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
	public static Git git;

	public static void main(String args[]) throws NoHeadException, IOException, GitAPIException
	{
		
//		analysis te = new analysis("/Users/WangQL/Documents/git", "Java");
//		System.out.println(gitDir);
//		te.init(gitDir);

		//te.getAllInfo();
	}
	
	public analysis(String hom, String pro, Git ogit, List<String> Logs)
	{
		logList = Logs;
		fileList = new ArrayList<String>();
		diffList = new ArrayList<String>();
		ages = new ArrayList<Integer>();
		git = ogit;
	}
	
	public void getAllInfo()
	{
		JgitDiff dif = new JgitDiff(git);
		dif.getInfo(logList.get(0), logList.get(1));
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
				dif.getInfo(logList.get(i), logList.get(i + 1));
				localFiles = dif.getFileList();
//				for(int k = 0; k < localFiles.size(); k ++)
//				{
//					System.out.print(localFiles.get(k));
//				}
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
}