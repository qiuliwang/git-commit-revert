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


public class mytest
{
	public static List<String> logList;
	public static void main(String args[]) throws NoHeadException, IOException, GitAPIException
	{
		String home = "/Users/WangQL/Documents/git/";
		String project = "Java";
		String repGit = "/.git";
		String gitDir = home.concat(project + repGit);

		System.out.println(gitDir);
		mytest te = new mytest();
		te.init(gitDir);
		for(int i = 0; i < logList.size(); i ++)
		{
			System.out.println(logList.get(i));
		}
	}
	
	public mytest()
	{
		logList = new ArrayList<String>();
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