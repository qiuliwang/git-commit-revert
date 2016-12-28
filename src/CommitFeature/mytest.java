package CommitFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;


public class mytest
{
	public static String home;
	public static String project;
	public static String URL;
	public static List<String> logList;
	public static Repository repository;
	public static Git git;
	
	public mytest(String pro) throws IOException
	{
		logList = new ArrayList<String>();
		home = "/Users/WangQL/Documents/git/";
		URL = home + pro + "/.git";
		System.out.println(URL);
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		repository = builder.setGitDir(new File(URL))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		//Respository 仓库
		// RevWalk walk = new RevWalk(repository);
		// RevCommit commit = walk.parseCommit(objectIdOfCommit);
		
		//Git: API to interact with a git repository
		git = new Git(repository);
		
	}
	
	public static void main(String args[]) throws NoHeadException, IOException, GitAPIException
	{
		mytest mt = new mytest("Java");
		mt.getAllLogs();
		JgitDiff dif = new JgitDiff(git);
		dif.getInfo(logList.get(0), logList.get(1));
		analysis ana = new analysis(home, project, git, logList);
		ana.getAllInfo();
	}
	
	private void getAllLogs() throws IOException, NoHeadException, GitAPIException
	{
		Iterable<RevCommit> gitLog = git.log().call();
		Iterator<RevCommit> it = gitLog.iterator();
		while (it.hasNext()) {			
			RevCommit thisLog = it.next();
			ObjectId thisID = thisLog.getId();
			String commitId = getCommitId(thisID.toString()); // get commit ID hashcode	
			//System.out.println(commitId);
			logList.add(commitId);
		}
	}
	
	private static String getCommitId(String str) {

		String tempDescription = str;
		tempDescription = str.substring(7, 47);
		return tempDescription;
	}
}