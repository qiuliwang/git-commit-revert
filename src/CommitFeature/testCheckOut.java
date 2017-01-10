package CommitFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CheckoutResult;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class testCheckOut {

	/**
	 * @param args
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws CheckoutConflictException
	 * @throws InvalidRefNameException
	 * @throws RefNotFoundException
	 * @throws RefAlreadyExistsException
	 */
	public static void main(String[] args) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		// TODO Auto-generated method stub
        String home="F:/commitdata/testgit/";
        String tempDir = "F:/commitdata/testgitOutput/tempFiles/";
		String gitUrl = "F:/commitdata/testgit/.git";
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(gitUrl))
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();
		
		Git git = new Git(repository);
		String oldPath = "README.md";
		CheckoutCommand check = git.checkout().setStartPoint("48d45e7dead749f44be55dd88d714545eedb4dbc").addPath(oldPath);
		//CheckoutResult cr =check.getResult();
		//List<String> crResult = cr.getConflictList();
		//List<String> crResult2 = cr.getModifiedList();
		check.call();
		String oldPre = "old";
		String newPre = "new";
		String copyPath = oldPath.replaceAll("/","_");
		File tempFile = new File(tempDir+"/"+copyPath); 
        Copy(home+oldPath,tempDir+copyPath); 
        git.close();
		//System.out.println(crResult.size());
	}

	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

}
