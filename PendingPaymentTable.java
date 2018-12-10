/**
 * BookTable.java
 * Creates a model table to be used by the bookController
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/
public class PendingPaymentTable {

	String userName;
	float balance;
	float amount;
	String status;




	public PendingPaymentTable(String userName, float balance, float amount, String status){
		this.userName = userName;
		this.balance = balance;
		this.amount = amount;
		this.status = status;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
