package VO;

public class ActionForward {
	private boolean isRedirect = false;
	private String path = null;
	private String Url = null;

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setUrl(String url) {
		this.Url = url;
	}
	
	public String getUrl(){
		return Url;
	}

}
