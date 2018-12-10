/**
 * Creates a model table to be used by the BorrowRequestController
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version 1.0
*/

public class BorrowRequestTable {
	private int borrowId;
	private String title;
	private String username;
	private int numberAvailable;
	private String type;
	
	public BorrowRequestTable(int borrowId, String title, String username, int numberAvailable, String type) {
		this.borrowId = borrowId;
		this.title = title;
		this.username = username;
		this.numberAvailable = numberAvailable;
		this.type = type;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setNumberAvailable(int numberAvailable) {
		this.numberAvailable = numberAvailable;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getBorrowId() {
		return this.borrowId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getNumberAvailable() {
		return this.numberAvailable;
	}
	
	public String getType() {
		return this.type;
	}
}
