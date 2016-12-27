package CommitFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class gitLogAnalysis {

	/**
	 * @param args
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws NoHeadException
	 */
	static String[] projects = new String[] { "hello-world","Cpp-Primer","primer-4th","Java" };

	static int projectIndex = 3;
	static String project = projects[projectIndex];
	static String home = "/Users/WangQL/Documents/git/";
	static String projectHome = home+project+"/";
	static String outputHome = home+project+"Output/";
	static String tempCopyFileHome =outputHome+"tempFiles"; 
	
	//count number of developers
	//NDEV
	
	static Integer numOfDevelopers = 0;
	static List<String> nameOfDevelopers;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, NoHeadException,
			GitAPIException {
		// TODO Auto-generated method stub
		gitLogAnalysis gas = new gitLogAnalysis();
		gas.analysis(project);
	}

	
	public gitLogAnalysis()
	{
		nameOfDevelopers = new ArrayList<String>();
	}
	
	public void analysis(String proj) throws IOException, NoHeadException, GitAPIException
	{
		
		String repGit = "/.git";
		String gitDir = home.concat(proj + repGit);

		System.out.println(gitDir);

		
		List<Commit> allCommits = new ArrayList<Commit>();
		List<String> revertingCommitIDs = new ArrayList<String>();
		List<Commit> revertingCommits = new ArrayList<Commit>();	
		List<String> revertedCommitIDs = new ArrayList<String>();
		List<Commit> revertedCommits = new ArrayList<Commit>();		
		
		File allCommitCsv = new File(home + "/" + project + "Output/AllLog.csv");
		File revertingCommitCsv = new File(home + "/" + project + "Output/RevertingLog.csv");
		File revertedCommitCsv = new File(home + "/" + project + "Output/RevertedLog.csv");
		CSV_handler operateCsv = new CSV_handler();

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(gitDir))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		//Respository 仓库
		// RevWalk walk = new RevWalk(repository);
		// RevCommit commit = walk.parseCommit(objectIdOfCommit);

		//Git: API to interact with a git repository
		Git git = new Git(repository);

		ObjectId head = repository.resolve(Constants.HEAD);
		//RevWalk: Walks a commit graph and produces the matching commits in order.
		RevWalk walk = new RevWalk(repository);
		//RevCommit: A commit reference to a commit in the DAG.
		RevCommit commit = walk.parseCommit(head);
		Iterable<RevCommit> gitLog = git.log().call();
		Iterator<RevCommit> it = gitLog.iterator();

		int numberOfAllCommit = 1;
		int numberOfRevertingCommit = 0;
		
		//#################
		FileWriter fwriter1 = null;
		fwriter1 = new FileWriter("/Users/WangQL/Desktop/mess.txt");
		while (it.hasNext()) {
			Commit thisCommit = new Commit();
			
			RevCommit thisLog = it.next();
			PersonIdent committer = thisLog.getCommitterIdent();
			String committerName = committer.getName();
			//add name
			if(!nameOfDevelopers.contains(committerName))
			{
				nameOfDevelopers.add(committerName);
			}
			
			//System.out.println(committerName);
			//String committerEmail = committer.getEmailAddress();
			Date commitDate = committer.getWhen();
			//String date = commitDate.toString();
			String msg = thisLog.getFullMessage();
			String regulizedMsg = csvHandlerStr(msg);
			ObjectId thisID = thisLog.getId();

			String commitId = getCommitId(thisID.toString()); // get commit ID hashcode																 

			//System.out.println(msg);
		    fwriter1.write("\n===============================\n"+msg);
			thisCommit.setId(numberOfAllCommit); // index
			thisCommit.setCommitid(commitId); // commitId
			thisCommit.setCommitter(committerName); // committer
			thisCommit.setTime(commitDate); // date
			thisCommit.setLabel(0); // label, 
																// 0, default 
																// 1, reverted 
																// 2, reverting
			thisCommit.setRevertedId(""); // revertedCommitId default null
			thisCommit.setMsg(regulizedMsg); // full msg

			// reverting label, if it is a reverting commit, label as 1
			if (msg.indexOf("This reverts commit") > 0) {
				thisCommit.setLabel(2);
				numberOfRevertingCommit = numberOfRevertingCommit + 1;
				String revertedCommitId = getRevertedCommitId(regulizedMsg);
				thisCommit.setRevertedId(revertedCommitId);
				// System.out.println(revertedCommitId);
				revertingCommitIDs.add(thisCommit.getCommitid());
				revertedCommitIDs.add(thisCommit.getRevertedId());
			}

			// remove the commit log from svn before migrate to git
			if (msg.indexOf("git-svn-id:") < 0) {
				numberOfAllCommit = numberOfAllCommit + 1;
				allCommits.add(thisCommit);
			}
		}
		//####################
		fwriter1.close();
		
		List<Commit> allLabeledCommits = getLabeledCommits(allCommits,revertedCommitIDs);
		
		// git.log().add(head).addPath("F:/hadooplog.txt").call();
		DiffCommit jgitDiff = new DiffCommit(git);
		List<Commit> featuredAllCommits = jgitDiff.getFeaturedAllCommits(allLabeledCommits);
		
		revertedCommits = getSubCommits(featuredAllCommits, revertedCommitIDs);
		revertingCommits  = getSubCommits(featuredAllCommits, revertingCommitIDs);
		
		operateCsv.writeCommitsToCsvWithoutMsg(allCommitCsv, allCommits);
		operateCsv.writeCommitsToCsvWithoutMsg(revertingCommitCsv, revertingCommits);
		operateCsv.writeCommitsToCsvWithoutMsg(revertedCommitCsv, revertedCommits);

		System.out.println("Total commits:"+Integer.toString(numberOfAllCommit-1));
		System.out.println("Reverting commits:"+numberOfRevertingCommit);
		System.out.println("Reverted commits:"+revertedCommits.size());				
		
		//jgitDiff.diffMethod(Child, Parent)
		//count number of developers by nameOfDevelpoers
		numOfDevelopers = nameOfDevelopers.size();
		System.out.println(numOfDevelopers);
		for(int i = 0; i < nameOfDevelopers.size(); i ++)
		{
			System.out.println(nameOfDevelopers.get(i) + " ");
		}
	}
	
	// get regulizedMsg 
	private static String csvHandlerStr(String str) {

		String tempDescription = str;
		if (str.indexOf(",") > 0) {
			tempDescription = str.replaceAll(",", ";");
		}

		if (tempDescription.indexOf("\"") > 0) {
			tempDescription = tempDescription.replaceAll("\"", " ");
		}
		return tempDescription;
	}

	// get commitID hashcode from objectId
	private static String getCommitId(String str) {

		String tempDescription = str;
		tempDescription = str.substring(7, 47);
		return tempDescription;
	}

	// get reverted commitID hashcode from msg
	private static String getRevertedCommitId(String msg) {
		String tempDescription = msg;
		int startIndex = msg.indexOf("This reverts commit ");
		tempDescription = msg.substring(startIndex + 20, startIndex + 60);
		return tempDescription;
	}
	
	// get sub commit list
	private static List<Commit> getSubCommits(List<Commit> allCommits, List<String> subCommitIDs) {
		
		List<Commit> subCommits = new ArrayList<Commit>();
		
		for (int i=0; i < allCommits.size();i++){
			Commit thisCommit = allCommits.get(i);
			String thisCommitId = thisCommit.getCommitid();
			for (int j=0; j < subCommitIDs.size();j++){			
				String thisSubId = subCommitIDs.get(j);
				if (thisCommitId.equals(thisSubId)){
					//thisCommit.setLabel(1);
					subCommits.add(thisCommit);
				}
			}
		}
		return subCommits;
	}
	
	// label all commits 
	private static List<Commit> getLabeledCommits(List<Commit> allCommits, List<String> revertedCommitIDs) {
		
		List<Commit> labeledAllCommits = new ArrayList<Commit>();
		
		for (int i=0; i < allCommits.size();i++){
			Commit thisCommit = allCommits.get(i);
			String thisCommitId = thisCommit.getCommitid();
			for (int j=0; j < revertedCommitIDs.size();j++){			
				String thisrevertedId = revertedCommitIDs.get(j);
				if (thisCommitId.equals(thisrevertedId)){
					thisCommit.setLabel(1);
					//subCommits.add(thisCommit);
				}
			}
			labeledAllCommits.add(thisCommit);
		}
		return labeledAllCommits;
	}
}
