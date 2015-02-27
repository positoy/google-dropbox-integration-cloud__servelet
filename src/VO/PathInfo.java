package VO;

public class PathInfo {
	private String name;
	private int src;
	private String id;
	private int depth;

	public PathInfo() {

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getSrc() {
		return src;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}
}
