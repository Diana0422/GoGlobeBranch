package logic.bean;
import java.util.logging.Logger;

public class RegistrationBean {
	private String regBeanEmail;
	private String password;
	private String regBeanName;
	private String regBeanSurname;
	private String birthday;
	private String passwordconf;
	private SessionBean session;
	
	//RegistrationBean Constructor
	public RegistrationBean() {
		// empty constructor
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public boolean validate() {
		String infoStr = regBeanEmail+" "+regBeanName+" "+regBeanSurname+" "+password+" "+passwordconf+" "+birthday+" ";
		Logger.getGlobal().info(infoStr);
		
		// registration values control
		return !(regBeanName == null || regBeanSurname == null || regBeanEmail == null || password == null || birthday == null);
	}

	public String getRegBeanEmail() {
		return regBeanEmail;
	}

	public void setRegBeanEmail(String regBeanEmail) {
		this.regBeanEmail = regBeanEmail;
	}

	public String getRegBeanSurname() {
		return regBeanSurname;
	}

	public void setRegBeanSurname(String regBeanSurname) {
		this.regBeanSurname = regBeanSurname;
	}

	public String getRegBeanName() {
		return regBeanName;
	}

	public void setRegBeanName(String regBeanName) {
		this.regBeanName = regBeanName;
	}

	public String getPasswordconf() {
		return passwordconf;
	}

	public void setPasswordconf(String passwordconf) {
		this.passwordconf = passwordconf;
	}

	public SessionBean getSession() {
		return session;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}
	
}
