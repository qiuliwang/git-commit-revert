package CommitFeature;

import java.util.Date;

public class Commit {
	
    // coarse-grained features
	private int id;  // index id by time
	private String commitid; // hash commitid length:40
	private String committer; // name
	private Date time;    
	private int label;  // default 0, reverted 1, reverting 2
	private int revertingLabel;  //reverting 1
	private String revertedId; // default null, 
	private String msg; 
	private int modifyFiles;  
	private int addFiles;
	private int deleteFiles;
	private int renameFiles;
	private int copyFiles;
	
	// fine-grained features
	private int numberOfLow;
	private int numberOfMedium = 0;
	private int numberOfHigh = 0;
	private int numberOfCrucial = 0;
	

	private int addlines = 0;
	private int dellines = 0;
	private int subSystemNum = 0; //The number of subsystems touched in a change
	private int EXP = 0;      //Developers experience, i.e., the number of submitted changes currently.
	private int NDEV = 0;       //The number of developers that changed the files in a change
	private int NF = 0;         //The number of files touched in a change
	private int NUC = 0;	   //The number of unique last changes to the files
	private int ND = 0;	        //The number of directories touched in a change

	private String date = "";	
	private double entropy = 0;  //Distribution across the touched files.

	private Integer conf = 0;    // The number of configure files touched in a change

	//WangQL 2017/5/18
	private int textHasBug = 0;  //description of this code change contains word "bug"
	private int testHasFeature = 0; //description of this code change contains word "feature"
	private int textHasImprove = 0; //description of this code change contains word "improve"
	private int textHasDocument = 0;//description of this code change contains word "document"
	private int textHasRefactor = 0;//description of this code change contains word "refactor"
	private int msg_length = 0;   //length of description
	
	//Owner Experience
	private int change_num = 0;;  //number of prior code changes submitted by the owner of this code change
	private int recent_change_num = 0; //number of prior code changes submitted by the owner of this code change
										//that is counted according to our weighting scheme in recent 120 days
	private int subsystem_change_num = 0;  //number of prior code changes submitted by the owner of this code change, 
										//that contain at least ont subsystem affected by this code chage
	
	//WangQL 2017/5/19
	private int file_type_num = 0; //number of file types in this code change
	
	//WangQL 2017/5/22
	private int segs_added_num = 0; //number of added_code_segs in this code change
	private int segs_deleted_num = 0; //number of deleted_code_segs in this code change
	private int segs_update_num = 0;  //number of update_code_segs in this code change
	
	//WangQL 2017/5/23
	private int changes_files_modified = 0; //number of files in this code change were modified before

	//WangQL 2017/5/30
	private int file_developer_num = 0; //number of developers who changed files in this code change
	
	//WangQL 2017/6/8
	private int time_before_reverted = 0;
	
	
	
	public int getTime_before_reverted() {
		return time_before_reverted;
	}

	public void setTime_before_reverted(int time_before_reverted) {
		this.time_before_reverted = time_before_reverted;
	}

	public int getRevertingLabel() {
		return revertingLabel;
	}

	public void setRevertingLabel(int revertingLabel) {
		this.revertingLabel = revertingLabel;
	}

	public int getChanges_files_modified() {
		return changes_files_modified;
	}

	public void setChanges_files_modified(int changes_files_modified) {
		this.changes_files_modified = changes_files_modified;
	}

	public int getSegs_added_num() {
		return segs_added_num;
	}

	public void setSegs_added_num(int segs_added_num) {
		this.segs_added_num = segs_added_num;
	}

	public int getSegs_deleted_num() {
		return segs_deleted_num;
	}

	public void setSegs_deleted_num(int segs_deleted_num) {
		this.segs_deleted_num = segs_deleted_num;
	}

	public int getSegs_update_num() {
		return segs_update_num;
	}

	public void setSegs_update_num(int segs_update_num) {
		this.segs_update_num = segs_update_num;
	}

	public int getChange_num() {
		return change_num;
	}

	public void setChange_num(int change_num) {
		this.change_num = change_num;
	}

	public int getRecent_change_num() {
		return recent_change_num;
	}

	public void setRecent_change_num(int recent_change_num) {
		this.recent_change_num = recent_change_num;
	}

	public int getSubsystem_change_num() {
		return subsystem_change_num;
	}

	public void setSubsystem_change_num(int subsystem_change_num) {
		this.subsystem_change_num = subsystem_change_num;
	}
	private int language_num = 0;  //number of programming languageused in this code change
	public int getFile_type_num() {
		return file_type_num;
	}

	public int getLanguage_num() {
		return language_num;
	}

	public void setLanguage_num(int language_num) {
		this.language_num = language_num;
	}

	public void setFile_type_num(int file_type_num) {
		this.file_type_num = file_type_num;
	}

	public int getTextHasRefactor() {
		return textHasRefactor;
	}

	public void setTextHasRefactor(int textHasRefactor) {
		this.textHasRefactor = textHasRefactor;
	}

	public int getMsg_length() {
		return msg_length;
	}

	public void setMsg_length(int msg_length) {
		this.msg_length = msg_length;
	}

	public int getTextHasBug() {
		return textHasBug;
	}

	public void setTextHasBug(int textHasBug) {
		this.textHasBug = textHasBug;
	}

	public int getTestHasFeature() {
		return testHasFeature;
	}

	public void setTestHasFeature(int testHasFeature) {
		this.testHasFeature = testHasFeature;
	}

	public int getTextHasImprove() {
		return textHasImprove;
	}

	public void setTextHasImprove(int textHasImprove) {
		this.textHasImprove = textHasImprove;
	}

	public int getTextHasDocument() {
		return textHasDocument;
	}

	public void setTextHasDocument(int textHasDocument) {
		this.textHasDocument = textHasDocument;
	}

	public int getCopyFiles() {
		return copyFiles;
	}

	public void setCopyFiles(int copyFiles) {
		this.copyFiles = copyFiles;
	}

	public Integer getConf() {
		return conf;
	}

	public void setConf(Integer conf) {
		this.conf = conf;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getND() {
		return ND;
	}

	public void setND(int nD) {
		ND = nD;
	}

	public int getNUC() {
		return NUC;
	}

	public void setNUC(int nUC) {
		NUC = nUC;
	}

	public int getNF() {
		return NF;
	}

	public void setNF(int nF) {
		NF = nF;
	}

	public int getNDEV() {
		return NDEV;
	}

	public void setNDEV(int nDEV) {
		NDEV = nDEV;
	}
	
	public int getEXP()
	{
		return EXP;
	}
	
	public void setEXP(int ages)
	{
		this.EXP = ages;
	}
	
	public int getSubSystemNum() {
		return subSystemNum;
	}
	public void setSubSystemNum(int subSystem) {
		this.subSystemNum = subSystem;
	}
	public int getAddlines() {
		return addlines;
	}
	public void setAddlines(int addlines) {
		this.addlines = addlines;
	}
	public int getDellines() {
		return dellines;
	}
	public void setDellines(int dellines) {
		this.dellines = dellines;
	}
	
	public int getNumberOfLow() {
		return numberOfLow;
	}
	public void setNumberOfLow(int numberOfLow) {
		this.numberOfLow = numberOfLow;
	}
	public int getNumberOfMedium() {
		return numberOfMedium;
	}
	public void setNumberOfMedium(int numberOfMedium) {
		this.numberOfMedium = numberOfMedium;
	}
	public int getNumberOfHigh() {
		return numberOfHigh;
	}
	public void setNumberOfHigh(int numberOfHigh) {
		this.numberOfHigh = numberOfHigh;
	}
	public int getNumberOfCrucial() {
		return numberOfCrucial;
	}
	public void setNumberOfCrucial(int numberOfCrucial) {
		this.numberOfCrucial = numberOfCrucial;
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCommitid() {
		return commitid;
	}
	public void setCommitid(String commitid) {
		this.commitid = commitid;
	}
	public String getCommitter() {
		return committer;
	}
	public void setCommitter(String committer) {
		this.committer = committer;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public String getRevertedId() {
		return revertedId;
	}
	public void setRevertedId(String revertedId) {
		this.revertedId = revertedId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public int getModifyFiles() {
		return modifyFiles;
	}
	public void setModifyFiles(int modifyFiles) {
		this.modifyFiles = modifyFiles;
	}
	public int getAddFiles() {
		return addFiles;
	}
	public void setAddFiles(int addFiles) {
		this.addFiles = addFiles;
	}
	public int getDeleteFiles() {
		return deleteFiles;
	}
	public void setDeleteFiles(int deleteFiles) {
		this.deleteFiles = deleteFiles;
	}
	public int getRenameFiles() {
		return renameFiles;
	}
	public void setRenameFiles(int renameFiles) {
		this.renameFiles = renameFiles;
	}	
}
