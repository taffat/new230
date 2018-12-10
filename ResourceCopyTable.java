/**
 * ResourceTable.java
 * Creates a model table to be used by all resources
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/
public class ResourceCopyTable {

	private int copyId;
	private int days;
	private String copyStatus;
	
	
	public ResourceCopyTable(int copyId, int days,  String copyStatus) {
		this.copyId = copyId;
		this.days = days;
		this.copyStatus = copyStatus;
		
	}

	public int getCopyId() {
		return copyId;
	}


	public int getDays() {
		return days;
	}


	public String getCopyStatus() {
		return copyStatus;
	}


	public void setCopyId(int copyId) {
		this.copyId = copyId;
	}


	public void setDays(int days) {
		this.days = days;
	}


	public void setCopyStatus(String copyStatus) {
		this.copyStatus = copyStatus;
	}
}
