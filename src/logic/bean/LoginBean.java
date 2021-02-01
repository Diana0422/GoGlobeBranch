package logic.bean;

public class LoginBean {
	
	private String username;
	private String password;
	protected String nome;
	protected String cognome;
	private int points;
	
	public LoginBean() {
		//empty constructor
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public boolean validate(){
		return  !(username == null || username.equals("") || password==null || password.equals(""));
		
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}
	

}