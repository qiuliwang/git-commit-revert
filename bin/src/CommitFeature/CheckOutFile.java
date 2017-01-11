package CommitFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CheckoutResult;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

public class CheckOutFile {
	
	private Git git;

	public CheckOutFile(Git git) {
		this.git=git;
	}
	
	public void CheckOutFileByCommitId (String commitId, String path) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException{		
		
		CheckoutCommand check = git.checkout().setStartPoint(commitId).addPath(path);
		check.call();
		//CheckoutResult cr =check.getResult();
		//List<String> crResult = cr.getConflictList();
		//List<String> crResult2 = cr.getModifiedList();	
	}

	public void CheckOutFileMaster (String path) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException{				
		
		CheckoutCommand check = git.checkout().setStartPoint("HEAD^").addPath(path);
		check.call();
		//CheckoutResult cr =check.getResult();
		//List<String> crResult = cr.getConflictList();
		//List<String> crResult2 = cr.getModifiedList();				
	}
	
	public void Copy(String oldPath, String newPath) {
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
	
	
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
