/**
 * Creates a model table to be used by the OverdueCopiesController
 * @author Jack Long (965615)
 * Copyright: No copyright
 * @version 1.0
*/

public class OverdueCopiesTable {

	String username;
	String bookTitle;
	String daysOverdue;
	
	public OverdueCopiesTable (String username, String bookTitle, String daysOverdue) {
		this.username = username;
		this.bookTitle = bookTitle;
		this.daysOverdue = daysOverdue;
	}
	
	public void setUsername (String username) {
		this.username = username;
	}
	
	public void setBookTitle (String bookTitle) {
		this.bookTitle = bookTitle;
	}
	
	public void setDaysOverdue (String daysOverdue) {
		this.daysOverdue = daysOverdue;
	}
	
	public String getUsername () {
		return this.username;
	}
	
	public String getBookTitle () {
		return this.bookTitle;
	}
	
	public String getDaysOverdue () {
		return this.daysOverdue;
	}
	
}