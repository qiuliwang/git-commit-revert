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
 * subsystem touched
 * directories touched
 * number of added lines
 * number of deleted lines
 * file name list
 * 
 */
public class JgitDiff {
	
	public JgitDiff(Git ogit) {
		URL="/Users/WangQL/Documents/git/";
		NS = 0;
		ND = new ArrayList<Integer>();
		addNum = new ArrayList<Integer>();
		delNum = new ArrayList<Integer>();
		ND = new ArrayList<Integer>();
		fileList = new ArrayList<String>();
		git = ogit;
		SubSystem = new ArrayList<String>();
	}
	static String URL="/Users/WangQL/Documents/git/";
	static Git git;
	public static List<Integer> addNum;
	public static List<Integer> delNum;
	public static Integer NS;	//number of subsystems touched by the current change
	public static List<Integer> ND;	//number of directories touched by the current change //one commit has only one NS but many ND, addNum and delNum.
	public static List<String> fileList;
	public static Repository repository;
	public static List<String> SubSystem;

	
	public static void main(String[] args) throws Exception {

	}

	public void getInfo(String logS, String logF)
	{
		ND.clear();
		NS = 0;
		addNum.clear();
		delNum.clear();
		fileList.clear();
		repository=git.getRepository();
		SubSystem.clear();
		ObjectReader reader = repository.newObjectReader();
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();	
		try {
			ObjectId old = repository.resolve(logS + "^{tree}");
			ObjectId head = repository.resolve(logF+"^{tree}");
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
            //每一个diffEntry都是第个文件版本之间的变动差异
            for (DiffEntry diffEntry : diffs) { 
            	//NS ++;
            	//打印文件差异具体内容
                df.format(diffEntry);  
                String diffText = out.toString("UTF-8"); 
                String diffFile = diffText.substring(0, diffText.indexOf('\n'));
                String firstFile = diffFile.substring(diffFile.indexOf("a/"), diffFile.indexOf("b/") - 1);
                String secondFile = diffFile.substring(diffFile.indexOf("b/"), diffFile.length());
                
                String subString = firstFile.substring(firstFile.indexOf("a/") + 2, firstFile.length());
                try{
                	subString = subString.substring(0, subString.indexOf("/"));
                }
                catch(Exception e)
                {
                	;
                }
                if(!SubSystem.contains(subString))
                {
                	SubSystem.add(subString);
                }
                fileList.add(firstFile.substring(firstFile.lastIndexOf('/') + 1));
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
                	}
                }
                addNum.add(addSize);
                delNum.add(subSize);
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
	
	public List<String> getSubSystem()
	{
		return SubSystem;
	}
	
	public Integer getNF()
	{
		return fileList.size();
	}
	
	public Integer getSubSystemNum()
	{
		return SubSystem.size();
	}
	
	public List<String> getSubSysList()
	{
		return SubSystem;
	}
	
	public Integer getAddLines()
	{
		return addNum.get(0);
	}
	
	public Integer getDelLines()
	{
		return delNum.get(0);
	}
	
	//计算路径长度
	private static Integer getNumOfOneChar(String str)
	{
		Integer count = 0;
		String localStr = str;
		for(int i = 0; i < str.length(); i ++)
		{
			if(localStr.indexOf('/') == -1) break;
			count ++;
			localStr = localStr.substring(localStr.indexOf('/') + 1);
		}
		return count;
	}
	
	public List<String> getFileList()
	{
		return fileList;
	}

	private AbstractTreeIterator prepareTreeParser(RevCommit revCommit) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

