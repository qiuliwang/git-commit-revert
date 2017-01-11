package CommitFeature;

import java.util.Date;

public class Commit {
	
    // coarse-grained features
	private int id;  // index id by time
	private String commitid; // hash commitid length:40
	private String committer; // name
	private Date time;    
	private int label;  // default 0, reverted 1, reverting 2
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
	private int subSystemNum = 0;
	private int EXP = 0;
	private int NDEV = 0;
	private int NF = 0;
	private int NUC = 0;	
	private int ND = 0;	
	private String date = "";	
	private double entropy = 0;
	private Integer conf = 0;
	
	
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
