package cn.duke.common;

public class UserInfo {

	private String loginName;
	private String password;
	private int role;
	private String mainPage;

	public UserInfo(String loginName, String password, int role, String mainPage) {
		super();
		this.loginName = loginName;
		this.password = password;
		this.role = role;
		this.mainPage = mainPage;
	}
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

}
