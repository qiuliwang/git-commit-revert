package CommitFeature;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.eclipse.jgit.transport.RequestNotYetReadException;

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
	
	public gitLogAnalysis(String homeUrl, String pro) throws IOException
	{
		//nameOfDevelopers = new ArrayList<String>();
		project = pro;
		home = homeUrl;
		projectHome = home+project+"/";
		outputHome = home+project+"Output/";
		tempCopyFileHome =outputHome+"tempFiles"; 
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, NoHeadException, GitAPIException
			 {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		String time1 = df.format(new Date()).toString();//
		//C:\Users\WangQL\Documents\Code\Java
		gitLogAnalysis gas = new gitLogAnalysis("C:/Users/WangQL/Documents/GitCode/", "testgit");
		//gitLogAnalysis gas = new gitLogAnalysis("/Users/WangQL/Documents/git/", "Java");

		//myFirstRep  camel
		gas.analysis();
		
		String time2 = df.format(new Date()).toString();
		System.out.println(time1);
		System.out.println(time2);

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
		//HashMap<String id1, String id2> revertInfo = new ()
		
		HashMap<String,String> revertInfo = new HashMap<String, String>();

		
		File allCommitCsv = new File(home + "/" + project + "Output/AllCommits.csv");
		File revertingCommitCsv = new File(home + "/" + project + "Output/RevertingCommits.csv");
		File revertedCommitCsv = new File(home + "/" + project + "Output/RevertedCommits.csv");
		File revertCommitCsv = new File(home + "/" + project + "Output/RevertCommits.csv");
		File revertTest = new File(home + "/" + project + "Output/RevertTest.csv");
		File allCommitCsvWithId = new File(home + "/" + project + "Output/AllCommitsWithID.csv");

		File allCommitMessageCsv = new File(home + "/" + project + "Output/AllCommitsMessage.csv");
		File revertingCommitMessageCsv = new File(home + "/" + project + "Output/RevertingCommitsMessage.csv");
		File revertedCommitMessageCsv = new File(home + "/" + project + "Output/RevertedCommitsMessage.csv");
		
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
		//RevCommit commit = walk.parseCommit(head);
		Iterable<RevCommit> gitLog = git.log().call();
		Iterator<RevCommit> it = gitLog.iterator();

		int numberOfAllCommit = 1;
		int numberOfRevertingCommit = 0;
		int count = 0;
		walk.close();
		while (it.hasNext()) {
			count ++;

			if(count == 1500)
			{
				break;
			}
			Commit thisCommit = new Commit();
			
			RevCommit thisLog = it.next();
			PersonIdent committer = thisLog.getCommitterIdent();
			
			String committerName = committer.getName();
			
			Date commitDate = committer.getWhen();
			String msg = thisLog.getFullMessage();
			String regulizedMsg = csvHandlerStr(msg);
			ObjectId thisID = thisLog.getId();
			
			//cannot add date info, because the date is reversed			
			String commitId = getCommitId(thisID.toString()); // get commit ID hashcode																 			thisCommit.setId(numberOfAllCommit); // index
			thisCommit.setCommitid(commitId); // commitId
			
			//process message here			
			
			if(msg.contains("bug"))
			{
				thisCommit.setTextHasBug(1);
			}
			if(msg.contains("feature"))
			{
				thisCommit.setTestHasFeature(1);
			}
			if(msg.contains("impoove"))
			{
				thisCommit.setTextHasImprove(1);
			}
			if(msg.contains("document"))
			{
				thisCommit.setTextHasDocument(1);
			}
			if(msg.contains("refactor"))
				thisCommit.setTextHasRefactor(1);

			//count number of space set length
			int spaceNum = 0;
			for(int i = 0; i < msg.length(); i ++)
			{
				if(msg.charAt(i) == ' ')
				{
					spaceNum ++;
				}
			}
			
			
			
			thisCommit.setMsg_length(spaceNum+1);

			thisCommit.setCommitter(committerName); // committer
			thisCommit.setTime(commitDate); // date
			thisCommit.setLabel(0); // label, 0, default ,  1, reverted ,  2, reverting
			thisCommit.setRevertedId(""); // revertedCommitId default null
			thisCommit.setDate(commitDate.toString());
			thisCommit.setMsg(regulizedMsg); // full message
			
			// reverting label, if it is a reverting commit, label as 1
			
			if (msg.indexOf("This reverts commit") > 0) {
				thisCommit.setLabel(0);
				thisCommit.setRevertingLabel(1);
				numberOfRevertingCommit = numberOfRevertingCommit + 1;
				String revertedCommitId = getRevertedCommitId(regulizedMsg);
				thisCommit.setRevertedId(revertedCommitId);
				revertingCommitIDs.add(thisCommit.getCommitid());
				revertedCommitIDs.add(thisCommit.getRevertedId());
				if(revertingCommitIDs.size() == 10)
					{
						System.out.println("different");
						
					}
				if(revertedCommitId.length() != 0 && thisCommit.getCommitid().length() != 0)
					revertInfo.put(revertedCommitId, thisCommit.getCommitid());
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
		
		List<String> revertedIDs = new ArrayList<>();
		for(String tmp: revertInfo.keySet())
		{
			revertedIDs.add(tmp);
		}
		List<Commit> revertedCommits2 = getSubCommits(featuredAllCommits, revertedIDs);
		List<Commit> revertingCommits2;
		List<String> tempRevertingIds = new ArrayList<>();
		for(String tmp : revertedIDs)
		{
			String reverting = "";
			if(revertInfo.keySet().contains(tmp))
			{
				reverting = revertInfo.get(tmp);
				tempRevertingIds.add(reverting);
			}
		}
		revertingCommits2 = getSubCommits(featuredAllCommits, tempRevertingIds);
		
		//use revertedCommits2 and revertingCommits2 time to set time before reverted
		for(int i = 0; i < revertedCommits2.size(); i ++)
		{
			Commit temp = revertedCommits2.get(i);
			Date datereverted = temp.getTime();
			Date datereverting = revertingCommits2.get(i).getTime();
			double hours = countDate(datereverted, datereverting);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println(datereverted.toString());
			System.out.println(datereverting.toString());

			System.out.println(hours);
			for(int j=0; j<allCommits.size();j++)
			{
				Commit tmp = allCommits.get(j);
				if(tmp.getCommitid().equals(temp.getCommitid()))
				{
					tmp.setTime_before_reverted(hours);
					allCommits.set(j, tmp);
				}
			}
		}
				
		operateCsv.writeCommitsToCsvWithoutMsg(allCommitCsv, allCommits);
		operateCsv.writeCommitsToCsvWithoutMsg(revertingCommitCsv, revertingCommits);
		operateCsv.writeCommitsToCsvWithoutMsg(revertedCommitCsv, revertedCommits);

		operateCsv.writeCommitsMsgsToCsv(allCommitMessageCsv, allCommits);
		operateCsv.writeCommitsMsgsToCsv(revertingCommitMessageCsv, revertingCommits);
		operateCsv.writeCommitsMsgsToCsv(revertedCommitMessageCsv, revertedCommits);
		operateCsv.writeCommitsMsgsToCsv2(revertCommitCsv, revertedCommits2, revertingCommits2);
		//operateCsv.writeCommitsMsgsToCsv3(revertTest, revertedCommitIDs, revertingCommitIDs);
		operateCsv.writeCommitsToCsvWithoutMsg(allCommitCsvWithId, allCommits, "id");
		
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
//		for (int i=0; i < allCommits.size();i++){
//			Commit thisCommit = allCommits.get(i);
//			String thisCommitId = thisCommit.getCommitid();
//			for (int j=0; j < subCommitIDs.size();j++){			
//				String thisSubId = subCommitIDs.get(j);
//				if (thisCommitId.equals(thisSubId)){
//					subCommits.add(thisCommit);
//				}
//			}
//		}
		for(int i = 0; i < subCommitIDs.size(); i ++)
		{
			String commitid = subCommitIDs.get(i);
			for(int j = 0; j < allCommits.size(); j ++)
			{
				Commit thisCom = allCommits.get(j);
				String thisComId = thisCom.getCommitid();
				if(commitid.equals(thisComId))
					subCommits.add(thisCom);
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
	
	//count days given to dates
	@SuppressWarnings({ "deprecation", "unused" })
	private double countDate(Date date1, Date date2) {
		double res = 0;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long temp = time2 - time1;
		res = ((double)temp / (60 * 60 * 1000));
		return res;
	}
}
