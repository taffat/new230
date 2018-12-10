/**
 * ResourceTable.java
 * Creates a model table to be used by all resources
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/
public class PaymentTable {


	private String dateTime;
	private int amount;
	
	public PaymentTable(String dateTime, int amount) {
		this.dateTime = dateTime;
		this.amount = amount;	
	}

	public String getDateTime() {
		return dateTime;
	}
	
	public int getAmount() {
		return amount;
	}
		
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

}
