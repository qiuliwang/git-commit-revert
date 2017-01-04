package CommitFeature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
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
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.NullOutputStream;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.SignificanceLevel;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class DiffCommit {

	private Git git;
	public DiffCommit(Git git) {
		this.git = git;
	}

	public static void main(String[] args) {
		
	}

	// child commit, parent commit
	public List<DiffEntry> diffMethod(String Child, String Parent)
			throws GitAPIException, IncorrectObjectTypeException, IOException {

		Repository repository = git.getRepository();

		ObjectReader reader = repository.newObjectReader();
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();

		ObjectId old = repository.resolve(Child + "^{tree}");
		ObjectId head = repository.resolve(Parent + "^{tree}");
		oldTreeIter.reset(reader, old);
		CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
		newTreeIter.reset(reader, head);

		OutputStream outputStream = NullOutputStream.INSTANCE;
		DiffFormatter formatter = new DiffFormatter(outputStream);
		formatter.setRepository(git.getRepository());
		formatter.setDetectRenames(true);

		List<DiffEntry> diffs = formatter.scan(oldTreeIter, newTreeIter);

		return diffs;
	}

	public FileDiffEntry getFileDiffEntry(DiffEntry diffEntry)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter df = new DiffFormatter(out); 
        df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
        df.setRepository(git.getRepository()); 
        df.format(diffEntry); 
        
		FileDiffEntry thisFileDiffEntry = new FileDiffEntry();
		String oldPath = diffEntry.getOldPath();
		String newPath = diffEntry.getNewPath();
		ChangeType cType = diffEntry.getChangeType();
		String type = cType.name();
		String diffText = out.toString("UTF-8");

		thisFileDiffEntry.setNewPath(newPath);
		thisFileDiffEntry.setOldPath(oldPath);
		thisFileDiffEntry.setDiffText(diffText);
		thisFileDiffEntry.setType(type);

		//System.out.println(diffText);
		out.reset();
		df.close();
		return thisFileDiffEntry;
	}

	public List<Commit> getFeaturedAllCommits(List<Commit> allCommits)
			throws IncorrectObjectTypeException, GitAPIException, IOException {
		List<String> fileList = new ArrayList<String>();
		List<String> subSysList = new ArrayList<String>();
		List<Integer> addNum = new ArrayList<Integer>();
		List<Integer> delNum = new ArrayList<Integer>();
		List<String> dirs = new ArrayList<String>();
		HashMap<String, Integer> uniqueChange = new HashMap<String, Integer>();
		
		for (int i = allCommits.size() - 1; i > 0; i--) {
			//System.out.println("==================");
			subSysList.clear();
			fileList.clear();
			addNum.clear();
			delNum.clear();
			dirs.clear();
			
			Commit oldCommit = allCommits.get(i);
			Commit newCommit = allCommits.get(i - 1);
			
			int numberOfAddFiles = 0;
			int numberOfDeleteFiles = 0;
			int numberOfModifyFiles = 0;
			int numberOfRenameFiles = 0;
			int numberOfLineChanges = 0;
			int numberOfLow = 0;
			int numberOfMedium = 0;
			int numberOfHigh = 0;
			int numberOfCrucial = 0;

			String newCommitId = newCommit.getCommitid();
			String oldCommitId = oldCommit.getCommitid();
			
			List<DiffEntry> thisDiffs = diffMethod(oldCommitId, newCommitId);

			for (DiffEntry diffEntry : thisDiffs) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();  
				DiffFormatter df = new DiffFormatter(out); 
				df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
	            df.setRepository(git.getRepository()); 
	            
				FileDiffEntry thisFileDiffEntry = getFileDiffEntry(diffEntry);
				String thisType = thisFileDiffEntry.getType();
				
				if (thisType.equals("ADD")) {
					numberOfAddFiles = numberOfAddFiles + 1;
				} else if (thisType.equals("MODIFY")) {
					numberOfModifyFiles = numberOfModifyFiles + 1;
				} else if (thisType.equals("RENAME")) {
					numberOfRenameFiles = numberOfRenameFiles + 1;
				} else if (thisType.equals("DELETE")) {
					numberOfDeleteFiles = numberOfDeleteFiles + 1;
				} else {
				}

				String oldFilePath = thisFileDiffEntry.getOldPath();
				String newFilePath = thisFileDiffEntry.getNewPath();

				if (oldFilePath.indexOf(".java") > -1
						&& newFilePath.indexOf(".java") > -1) {

					CheckOutFile checker = new CheckOutFile(git);

					checker.CheckOutFileByCommitId(oldCommitId, oldFilePath);
					String oldCopyPath = gitLogAnalysis.outputHome + "oldcopy"
							+ oldFilePath.replaceAll("/", "_");
					checker.Copy(gitLogAnalysis.projectHome + oldFilePath,
							oldCopyPath);

					checker.CheckOutFileByCommitId(newCommitId, newFilePath);
					String newCopyPath = gitLogAnalysis.outputHome + "newcopy"
							+ newFilePath.replaceAll("/", "_");
					;
					checker.Copy(gitLogAnalysis.projectHome + newFilePath,
							newCopyPath);

					// Fine-grained features
					ChangeDistillerClass changeDistiller = new ChangeDistillerClass();

					List<SourceCodeChange> changes = changeDistiller
							.getFineGrainedChange(oldCopyPath, newCopyPath);

					if (changes != null) {

						for (SourceCodeChange change : changes) {
							numberOfLineChanges = numberOfLineChanges + 1;
							SignificanceLevel thisLevel = change
									.getSignificanceLevel();
							String thisLevelString = thisLevel.toString();
							if (thisLevelString.equals("LOW")) {
								numberOfLow = numberOfLow + 1;
							} else if (thisLevelString.equals("MEDIUM")) {
								numberOfMedium = numberOfMedium + 1;
							} else if (thisLevelString.equals("HIGH")) {
								numberOfHigh = numberOfHigh + 1;
							} else if (thisLevelString.equals("CRUCIAL")) {
								numberOfCrucial = numberOfCrucial + 1;
							} else {
							}

						}
					}
					 checker.deleteFile(oldCopyPath);
					 checker.deleteFile(newCopyPath);
				}
				
				//some thing about diff infomation
				String diffText = thisFileDiffEntry.getDiffText();
				String diffFile = diffText.substring(0, diffText.indexOf('\n'));
	            String firstFile = diffFile.substring(diffFile.indexOf("a/") + 1, diffFile.indexOf("b/") - 1);
	            String subString = firstFile.substring(firstFile.indexOf("a/") + 2, firstFile.length());
	            try{
	            	subString = subString.substring(0, subString.indexOf("/"));
	            }
	            catch(Exception e)
	            {
	            	;
	            }
	            if(!subSysList.contains(subString))
	            {
	            	subSysList.add(subString);
	            }
	            
	            String file = firstFile.substring(firstFile.lastIndexOf("/") + 1);
	            
	            String dir = firstFile.substring(0, firstFile.lastIndexOf("/"));
	            //System.out.println(dir);
	            //ND
	            if(!dirs.contains(dir))
	            {
	            	dirs.add(dir);
	            }

	            if(!fileList.contains(file))
	            {
	            	fileList.add(file);
	            }
	            
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
			}
			//newCommit.setf
			newCommit.setAddFiles(numberOfAddFiles);
			newCommit.setDeleteFiles(numberOfDeleteFiles);
			newCommit.setRenameFiles(numberOfRenameFiles);
			newCommit.setModifyFiles(numberOfModifyFiles);

			newCommit.setNumberOfLow(numberOfLow);
			newCommit.setNumberOfMedium(numberOfMedium);
			newCommit.setNumberOfHigh(numberOfHigh);
			newCommit.setNumberOfCrucial(numberOfCrucial);
			newCommit.setSubSystemNum(subSysList.size());
			newCommit.setAddlines(sum(addNum));
			newCommit.setDellines(sum(delNum));
			newCommit.setNF(fileList.size());
			newCommit.setND(dirs.size());
			
			if(fileList.size() == 1)
			{
				//System.out.println(fileList.get(0) + "ddd");
				if(!uniqueChange.containsKey(fileList.get(0)))
				{
					uniqueChange.put(fileList.get(0), 1);
				}
				else
				{
					uniqueChange.put(fileList.get(0), uniqueChange.get(fileList.get(0)) + 1);
				}
				newCommit.setNUC(uniqueChange.get(fileList.get(0)));
			}
			else
			{
				newCommit.setNUC(0);
			}
		}
		return allCommits;
	}

	private Integer sum(List<Integer> arr)
	{
		Integer ans = 0;
		for(int i = 0; i < arr.size(); i ++)
		{
			ans += arr.get(i);
		}
		
		return ans;
	}
}
