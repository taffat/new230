/**
 * Creates a model table to be used by the borrowHistoryController
 * @author Jack Long (965615)
 * Copyright: No copyright
 * @version 1.0
*/

public class BorrowHistoryTable {

	int resCopyID;
	String username;
	String firstName;
	String lastName;
	String dateBorrowed;
	String dateReturned;
	
	public BorrowHistoryTable (int resCopyID, String username, String firstName, String lastName, String dateBorrowed, String dateReturned) {
		this.resCopyID = resCopyID;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateBorrowed = dateBorrowed;
		this.dateReturned = dateReturned;
	}
	
	public void setResCopyID (int resCopyID) {
		this.resCopyID = resCopyID;
	}
	
	public void setUsername (String username) {
		this.username = username;
	}
	
	public void setFirstName (String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName (String lastName) {
		this.lastName = lastName;
	}
	
	public void setDateBorrowed (String dateBorrowed) {
		this.dateBorrowed = dateBorrowed;
	}
	
	public void setDateReturned (String dateReturned) {
		this.dateReturned = dateReturned;
	}
	
	public int getResCopyID () {
		return this.resCopyID;
	}
	
	public String getUsername () {
		return this.username;
	}
	
	public String getFirstName () {
		return this.firstName;
	}
	
	public String getLastName () {
		return this.lastName;
	}
	
	public String getDateBorrowed () {
		return this.dateBorrowed;
	}
	
	public String getDateReturned () {
		return this.dateReturned;
	}
}
