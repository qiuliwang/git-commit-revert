package CommitFeature;

import java.io.File;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

public class gitStatus {
	public static void main(String args[])
	{
		//gitCheckout("/Users/WangQL/Documents/git/primer-4th/", "test");
		gitShowStatus("/Users/WangQL/Documents/git/primer-4th/");
	}
	
	public static void gitCheckout(String path, String version) {
	    File RepoGitDir = new File(path + "/.git");
	    if (!RepoGitDir.exists()) {
	    	System.out.println("Error! Not Exists : " + RepoGitDir.getAbsolutePath());
	    	///Users/WangQL/Documents/git/primer-4th/.git
	    } else {
	        Repository repo = null;
	        try {
	            repo = new FileRepository(RepoGitDir.getAbsolutePath());
	            System.out.println("Checkout to 1" + version);

	            Git git = new Git(repo);
	            System.out.println("Checkout to 2" + version);

	            CheckoutCommand checkout = git.checkout();
	            checkout.setName(version);
	            System.out.println("Checkout to 3" + version);

	            checkout.call();
	            System.out.println("Checkout to 4" + version);

	            PullCommand pullCmd = git.pull();
	            pullCmd.call();
	            git.close();
	            System.out.println("Pulled from remote repository to local repository at " + repo.getDirectory());
	        } catch (Exception e) {
	        	System.out.println(e.getMessage() + " :: " + RepoGitDir.getAbsolutePath());
	        } finally {
	            if (repo != null) {
	                repo.close();
	            }
	        }
	    }
	}
	
	public static void gitShowStatus(String repoDir) {
	    File RepoGitDir = new File(repoDir + "/.git");
	    if (!RepoGitDir.exists()) {
	    	System.out.println("Error! Not Exists : " + RepoGitDir.getAbsolutePath());
	    } else {
	        Repository repo = null;
	        try {
	            repo = new FileRepository(RepoGitDir.getAbsolutePath());
	            Git    git    = new Git(repo);
	            Status status = git.status().call();
	            System.out.println("Git Change: " + status.getChanged());
	            System.out.println("Git Modified: " + status.getModified());
	            System.out.println("Git UncommittedChanges: " + status.getUncommittedChanges());
	            System.out.println("Git Untracked: " + status.getUntracked());
	            git.close();
	        } catch (Exception e) {
	        	System.out.println(e.getMessage() + " : " + repoDir);
	        } finally {
	            if (repo != null) {
	                repo.close();
	            }
	        }
	    }
	}
}
