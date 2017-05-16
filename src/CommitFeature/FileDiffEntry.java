package CommitFeature;

public class FileDiffEntry {
	// each file change,  one commit contains several files change.
	
	private String oldPath;
	private String newPath;
	private String type;
	private String diffText;		
	
	public String getOldPath() {
		return oldPath;
	}
	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}
	public String getNewPath() {
		return newPath;
	}
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDiffText() {
		return diffText;
	}
	public void setDiffText(String diffText) {
		this.diffText = diffText;
	}

	
	

}
