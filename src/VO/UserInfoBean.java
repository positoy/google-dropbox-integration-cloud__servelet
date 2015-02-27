package VO;

public class UserInfoBean {
	private String id = null;
	private String password = null;
	private String email = null;
	private int google;
	private int dropbox;

	public UserInfoBean() {
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
	
	public void setGoogle(int google) {
		this.google = google;
	}
	
	public int getGoogle() {
		return google;
	}
	
	public void setDropbox(int dropbox) {
		this.dropbox = dropbox;
	}
	
	public int getDropbox() {
		return dropbox;
	}
}
