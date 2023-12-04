package huy.java.server;

public class User {
	private String userID;
	private String firstName;
	private String lastName;
	private String email;
	

	public User() {
	}

	public User(String id, String firstName , String lastName, String email) {
		super();
		this.userID = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getemail() {
		return email;
	}

	public void setemail(String email) {
		this.email = email;
	}


}