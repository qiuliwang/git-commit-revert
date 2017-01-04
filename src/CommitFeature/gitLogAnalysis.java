package CommitFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	public static String project;
	public static String home;
	static String projectHome;
	static String outputHome;
	static String tempCopyFileHome;
	
	//count number of developers
	//NDEV
	static Integer numOfDevelopers = 0;
	
	static List<String> nameOfDevelopers;
	
	static HashMap<String, Integer> hmp = new HashMap<String, Integer>();
	
	public gitLogAnalysis(String pro) throws NoHeadException, IOException, GitAPIException
	{
		nameOfDevelopers = new ArrayList<String>();
		project = pro;
		home = "/Users/WangQL/Documents/git/";
		projectHome = home+project+"/";
		outputHome = home+project+"Output/";
		tempCopyFileHome =outputHome+"tempFiles"; 
		//this.analysis();
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, NoHeadException,
			GitAPIException {
		gitLogAnalysis gas = new gitLogAnalysis("Java");
		gas.analysis();
	}
	
	public void analysis() throws IOException, NoHeadException, GitAPIException
	{
		
		String repGit = "/.git";
		String gitDir = home.concat(project + repGit);

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
			if(!hmp.containsKey(committerName))
			{
				hmp.put(committerName, 1);
			}
			else
			{
				hmp.put(committerName, hmp.get(committerName) + 1);				
			}
			
			thisCommit.setEXP(hmp.get(committerName));
			thisCommit.setNDEV(hmp.size());
			
			Date commitDate = committer.getWhen();
			String msg = thisLog.getFullMessage();
			String regulizedMsg = csvHandlerStr(msg);
			ObjectId thisID = thisLog.getId();

			String commitId = getCommitId(thisID.toString()); // get commit ID hashcode																 			thisCommit.setId(numberOfAllCommit); // index
			thisCommit.setCommitid(commitId); // commitId
			//System.out.println(commitId);
			thisCommit.setCommitter(committerName); // committer
			thisCommit.setTime(commitDate); // date
			thisCommit.setLabel(0); // label, 0, default ,  1, reverted ,  2, reverting
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
		
		List<Commit> allLabeledCommits = getLabeledCommits(allCommits,revertedCommitIDs);
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
				}
			}
			labeledAllCommits.add(thisCommit);
		}
		return labeledAllCommits;
	}
}
