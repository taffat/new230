

/**
 * BookTable.java
 * Creates a model table to be used by the bookController
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/
public class ReturnResourceTable {

	String userName;
	String resourceTitle;
	String overdue;
	int fine;
	int id;

	public ReturnResourceTable(String userName, String resourceTitle, String overdue, int fine,int id){
		this.userName = userName;
		this.resourceTitle = resourceTitle;
		this.overdue = overdue;
		this.fine = fine;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getResourceTitle() {
		return resourceTitle;
	}

	public void setResourceTitle(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}

	public String getOverdue() {
		return overdue;
	}

	public void setOverdue(String overdue) {
		this.overdue = overdue;
	}

	public int getFine() {
		return fine;
	}

	public void setFine(int fine) {
		this.fine = fine;
	}





}
