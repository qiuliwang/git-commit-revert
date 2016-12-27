package CommitFeature;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
/*
 * 
 * Child 子版本号
 * Parent 父版本号
 */
public class JgitDiff {
	
	public JgitDiff() {
		NS = 0;
		ND = new ArrayList<Integer>();
	}
	static String URL="/Users/WangQL/Documents/git/hello-world/.git";
	static Git git;
	
	public static List<Integer> addNum;
	public static List<Integer> delNum;
	public static Integer NS;	//number of subsystems touched by the current change
	public static List<Integer> ND;	//number of directories touched by the current change
	//one commit has only one NS but many ND, addNum and delNum.
	
	public static Repository repository;
	
	public static void main(String[] args) throws Exception {
		JgitDiff jgitDiff = new JgitDiff();
		
		//jgitDiff.diffMethod("75ae5a6240747e1a062de855f43100c655aacb11",
			//	"576515208ce27251f22e0c571f7fd64608d83a5b");
		jgitDiff.cal("32162f598a2d1a176b1ea2891d0f1d1ce36aac49",
						"5b167221ab8bcd81d54df53d842fd2813d2cafc3");
		//a/EXE1/exe1_3.cpp
		//System.out.println(getNumOfOneChar("a/EXE1/exe1_3.cpp"));
	}
	/*
	 * 
	 */
	
	public void diffMethod(String Child, String Parent){
		try {
			git=Git.open(new File("/Users/WangQL/Documents/git/hello-world/.git"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		repository=git.getRepository();
		ObjectReader reader = repository.newObjectReader();
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
	
		try {
			ObjectId old = repository.resolve(Child + "^{tree}");
			ObjectId head = repository.resolve(Parent+"^{tree}");
					oldTreeIter.reset(reader, old);
			CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
			newTreeIter.reset(reader, head);
			List<DiffEntry> diffs= git.diff()
                    .setNewTree(newTreeIter)
                    .setOldTree(oldTreeIter)
                    .call();
			
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			    DiffFormatter df = new DiffFormatter(out);
			    df.setRepository(git.getRepository());
			
			for (DiffEntry diffEntry : diffs) {
		         df.format(diffEntry);
		         String diffText = out.toString();
		         System.out.println(diffText);
		         //analyseDiff(diffText);
		       //  out.reset();
			}
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void cal(String Child, String Parent)
	{
		try {
			git=Git.open(new File("/Users/WangQL/Documents/git/hello-world/.git"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		repository=git.getRepository();
		ObjectReader reader = repository.newObjectReader();
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
	
		try {
			ObjectId old = repository.resolve(Child + "^{tree}");
			ObjectId head = repository.resolve(Parent+"^{tree}");
					oldTreeIter.reset(reader, old);
			CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
			newTreeIter.reset(reader, head);
			List<DiffEntry> diffs= git.diff()
                    .setNewTree(newTreeIter)
                    .setOldTree(oldTreeIter)
                    .call();
			ByteArrayOutputStream out = new ByteArrayOutputStream();  
            DiffFormatter df = new DiffFormatter(out); 
            //设置比较器为忽略空白字符对比（Ignores all whitespace）
            df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            df.setRepository(git.getRepository()); 
            //System.out.println("------------------------------start-----------------------------");
            //每一个diffEntry都是第个文件版本之间的变动差异
            for (DiffEntry diffEntry : diffs) { 
            	NS ++;
            	//打印文件差异具体内容
                df.format(diffEntry);  

                String diffText = out.toString("UTF-8"); 
                String diffFile = diffText.substring(0, diffText.indexOf('\n'));
                System.out.println(diffFile);  
                String firstFile = diffFile.substring(diffFile.indexOf("a/"), diffFile.indexOf("b/") - 1);
                String secondFile = diffFile.substring(diffFile.indexOf("b/"), diffFile.length());
                System.out.println(firstFile);  
                System.out.println(secondFile); 
                Integer count1 = 0, count2 = 0;
                

                //获取文件差异位置，从而统计差异的行数，如增加行数，减少行数

                FileHeader fileHeader = df.toFileHeader(diffEntry);

                List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();
                int addSize = 0;
                int subSize = 0;
                for(HunkHeader hunkHeader:hunks){
                	EditList editList = hunkHeader.toEditList();
                	for(Edit edit : editList){
                		subSize += edit.getEndA()-edit.getBeginA();
                		addSize += edit.getEndB()-edit.getBeginB();
                		//System.out.println(edit.getEndA());
                		//System.out.println(edit.getBeginA());
                		//System.out.println(edit.getEndB());
                		//System.out.println(edit.getBeginA());
                	}
                }
                
                //System.out.println("addSize="+addSize);
                //System.out.println("subSize="+subSize);
                //System.out.println("------------------------------end-----------------------------");
                out.reset();  
            }
            
		}catch (IncorrectObjectTypeException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		} catch (GitAPIException e) {
    			e.printStackTrace();
    		}
            
	}
	
	private static Integer getNumOfOneChar(String str)
	{
		Integer count = 0;
		String localStr = str;
		for(int i = 0; i < str.length(); i ++)
		{
			if(localStr.indexOf('/') == -1) break;
			//System.out.println(localStr.indexOf('/'));
			count ++;
			localStr = localStr.substring(localStr.indexOf('/') + 1);
		}
		return count;
	}

	private AbstractTreeIterator prepareTreeParser(RevCommit revCommit) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

