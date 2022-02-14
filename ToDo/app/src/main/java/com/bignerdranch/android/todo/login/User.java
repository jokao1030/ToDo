package com.bignerdranch.android.todo.login;

/**
 * A single data item
 * Adapt the code from
 * Joern Kreutel
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
	private String email;
	private String password;


	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public User(long id, String email, String password) {
		this.setId(id == -1 ? ID++ : id);
		this.setEmail(email);
		this.setPassword(password);
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
