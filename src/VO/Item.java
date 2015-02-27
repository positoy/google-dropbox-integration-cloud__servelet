package VO;

public class Item {
	
	public final static int googleDrive = 0;
	public final static int dropbox = 1;
	
	public boolean isFolder;
	
	int src;
	String name;
	String extension;
	String id;

	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public void setSrc(int src) {
		this.src = src;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	public boolean getIsFolder() {
		return isFolder;
	}
	
	public int getSrc() {
		return src;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public String getId() {
		return id;
	}
	
}