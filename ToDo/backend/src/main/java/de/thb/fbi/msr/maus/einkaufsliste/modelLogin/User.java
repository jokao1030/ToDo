package de.thb.fbi.msr.maus.einkaufsliste.modelLogin;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import javax.validation.constraints.*;

/**
 * A single data item
 * 
 * @author Joern Kreutel
 * 
 */
public class User{

	/**
	 * some static id assignment
	 */
	private static int ID = 0;

	
	/**
	 * the fields
	 */
	private long id;
	
	@NotEmpty(message = "Empty Email is not allowed !")
	@Email(message = "Invalid Email address !")
	private String email;
	@NotEmpty(message = "Empty Password is not allowed !")
	@Size(min=6,max=6, message = "Password should be 6 numbers !")
	@Digits(integer=6, fraction=0,message = "Password should be 6 numbers !")
	private String password;

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	private boolean registered;
	

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	public User(long id, String email, String password, boolean registered) {
		this.setId(id == -1 ? ID++ : id);
		this.setEmail(email);
		this.setPassword(password);
		this.setRegistered(registered);
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	
	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}

	
	/**
	 * update an item given the content of another one
	 * 
	 * @param user
	 */
	public User updateFrom(User user) {
		this.setEmail(user.getEmail());
		this.setPassword(user.getPassword());
		return this;
	}

	public String toString() {
		return "{User " + this.getId() + " " + this.getEmail() + "}";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * compare two elements
	 */
	public boolean equals(Object other) {

		// we cannot compare getClass() because classes do not coincide in case
		// of delete, where we create an anonymous inner class that extends
		// DataItem
		if (other == null || !(other instanceof User)) {
			return false;
		} else {
			return ((User) other).getId() == this.getId();
		}

	}

}
