package CommitFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;


public class analysis
{
	public static List<String> logList;
	public static List<String> fileList;
	public static List<String> diffList;
	public static List<Integer> ages;
	public static String home = "/Users/WangQL/Documents/git/";
	public static String project = "git-commit-revert";
	public static String repGit = "/.git";
	public static String gitDir = home.concat(project + repGit);

	public static void main(String args[]) throws NoHeadException, IOException, GitAPIException
	{
		
		analysis te = new analysis();
		te.init(gitDir);
		te.getAllInfo();
	}
	
	public analysis()
	{
		logList = new ArrayList<String>();
		fileList = new ArrayList<String>();
		diffList = new ArrayList<String>();
		ages = new ArrayList<Integer>();
		home = "/Users/WangQL/Documents/git/";
		project = "git-commit-revert";
		repGit = "/.git";
		gitDir = home.concat(project + repGit);
	}
	
	private void getAllInfo()
	{

		JgitDiff dif = new JgitDiff(logList.get(0), logList.get(1), project);
		dif.getInfo();
		fileList = dif.getFileList();
		
		System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		for(int t = 0; t < fileList.size(); t ++)
		{
			List<String> cpFile = new ArrayList<String>();
			cpFile.add(fileList.get(t));
			System.out.println(cpFile.get(0));

			for(int i = 1; i < logList.size() - 1; i ++)	
			{
				System.out.println(i);
				List<String> localFiles = new ArrayList<String>();
				JgitDiff Diff = new JgitDiff(logList.get(i), logList.get(i + 1), project);
				Diff.getInfo();
				localFiles = Diff.getFileList();
				for(int k = 0; k < localFiles.size(); k ++)
				{
					System.out.print(localFiles.get(k));
				}
				System.out.print("\n");
				if(localFiles.retainAll(cpFile))
				{
					System.out.println("have the same file log");
					ages.add(i);
					System.out.println("distance between two changes:  "+i);
					System.out.println("@@@@@@@@@@@@@@@@@@@@@");

					break;
				}
			}
		}
	}
	
	private void init(String path) throws IOException, NoHeadException, GitAPIException
	{
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(path))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		//Respository 仓库
		// RevWalk walk = new RevWalk(repository);
		// RevCommit commit = walk.parseCommit(objectIdOfCommit);

		//Git: API to interact with a git repository
		Git git = new Git(repository);
		Iterable<RevCommit> gitLog = git.log().call();
		Iterator<RevCommit> it = gitLog.iterator();

		while (it.hasNext()) {			
			RevCommit thisLog = it.next();
			ObjectId thisID = thisLog.getId();
			String commitId = getCommitId(thisID.toString()); // get commit ID hashcode																 
			logList.add(commitId);
		}
	}
	
	private static String getCommitId(String str) {

		String tempDescription = str;
		tempDescription = str.substring(7, 47);
		return tempDescription;
	}

}