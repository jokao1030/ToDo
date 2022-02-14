package de.thb.fbi.msr.maus.einkaufsliste.modelToDo;

import java.io.Serializable;

/**
 * A single data item
 * 
 * @author Joern Kreutel
 * 
 */
public class ToDo implements Serializable {

	/**
	 * some static id assignment
	 */
	private static int ID = 0;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7481912314472891511L;

	/**
	 * the fields
	 */
	private int id;
	private String item;
	private String description;
	private String date;
	private String time;
	private boolean completed;
	private boolean favorite;


	public void setItem(String item) {
		this.item = item;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(String date){this.date = date;}

	public void setTime(String time){this.time = time;}

	public void setCompleted(boolean completed){this.completed = completed;}

	public void setFavorite(boolean favorite){this.favorite = favorite;}

	public ToDo(int id, String item, String description,
				String date, String time, boolean completed, boolean favorite) {
		this.setId(id == -1 ? ID++ : id);
		this.setItem(item);
		this.setDescription(description);
		this.setDate(date);
		this.setTime(time);
		this.setCompleted(completed);
		this.setFavorite(favorite);
	}

	public ToDo() {
		// TODO Auto-generated constructor stub
	}

	public String getItem() {
		return this.item;
	}

	public String getDescription() {
		return this.description;
	}

	public String getDate(){return this.date; }

	public String getTime(){return this.time; }

	public boolean isCompleted(){return this.completed; }

	public boolean isFavorite(){return this.favorite; }


	/**
	 * update an item given the content of anothr one
	 * 
	 * @param todo
	 */
	public ToDo updateFrom(ToDo todo) {
		this.setItem(todo.getItem());
		this.setDescription(todo.getDescription());
		this.setDate(todo.getDate());
		this.setTime(todo.getTime());
		this.setCompleted(todo.isCompleted());
		this.setFavorite(todo.isFavorite());

		return this;
	}

	public String toString() {
		return "{ToDoItem " + this.getId() + " " + this.getItem() + "}";
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * compare two elements
	 */
	public boolean equals(Object other) {

		// we cannot compare getClass() because classes do not coincide in case
		// of delete, where we create an anonymous inner class that extends
		// DataItem
		if (other == null || !(other instanceof ToDo)) {
			return false;
		} else {
			return ((ToDo) other).getId() == this.getId();
		}

	}

}
