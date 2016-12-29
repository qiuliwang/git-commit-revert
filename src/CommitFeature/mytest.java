package CommitFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
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
	public static HashMap<String, Integer> map;
	
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
		map = new HashMap<String, Integer>();
	}
	
	public static void main(String args[]) throws NoHeadException, IOException, GitAPIException
	{
		mytest mt = new mytest("Java");
		mt.getAllLogs();
		JgitDiff dif = new JgitDiff(git);
		dif.getInfo(logList.get(0), logList.get(1));
		//analysis ana = new analysis(git, logList);
		//ana.getAllInfo();
		mt.showAllDevelopers();
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
			//use map to store develpoers
			PersonIdent committer = thisLog.getCommitterIdent();
			String committerName = committer.getName();
			if(map.containsKey(committerName)){//判断是否已经有该数值，如有，则将次数加1
				map.put(committerName, map.get(committerName).intValue() + 1);
	        }else{
	            map.put(committerName, 1);
	        }
		}
	}
	
	private void showAllDevelopers()
	{
		//遍历map中的键

		for (String key : map.keySet()) {

		    System.out.print("Name : " + key + "|| ");
		    System.out.println("commit count = " + map.get(key));
		}

	}
	
	public HashMap<String, Integer> getMap()
	{
		return map;
	}
	
	private static String getCommitId(String str) {

		String tempDescription = str;
		tempDescription = str.substring(7, 47);
		return tempDescription;
	}
}