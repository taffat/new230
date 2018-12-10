/**
 * ResourceTable.java
 * Creates a model table to be used by all resources
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/
public class FineTable {
	private String dateTime;
	private int amount;
	private int days;
		
	public FineTable(String dateTime, int amount, int days) {
		this.dateTime = dateTime;
		this.amount = amount;
		this.days = days;
	}

	public String getDateTime() {
		return dateTime;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getDays() {
		return days;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setDays(int days) {
		this.days = days;
	}
}
