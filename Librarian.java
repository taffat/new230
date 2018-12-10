/**
 * To store information about a Librarian
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version: 1.0
 */

import java.util.Date;

public class Librarian extends User {
	// The librarian's staff number
	private int staffNumber;
	// The librarian's date of employment
	private Date employmentDate;
	// The default librarian's balance
	private final static int balance = 0;
	
	/**
	 * A constructor to model a librarian
	 * @param username the unique identifier of the librarian
	 * @param firstName the first name of the librarian
	 * @param lastName the last name of the librarian
	 * @param phoneNumber the phone number of the librarian
	 * @param address the address of the librarian
	 * @param postCode the post code of the librarian
	 * @param avatarFilePath the file path to the librarians' avatar
	 * @param staffNumber the librarian's staff number
	 * @param employmentDate the date the librarian was employed
	 */
	public Librarian(String usertype,String username, String firstName, String lastName, String phoneNumber, String address,
				     String postCode, String avatarFilePath, int staffNumber, Date employmentDate) {
		super(usertype,username, firstName, lastName, phoneNumber, address, postCode, avatarFilePath, balance);
		this.staffNumber = staffNumber;
		this.employmentDate = employmentDate;
	}
	
	/**
	 * Get the librarian's staff number
	 * @return the librarian's staff number
	 */
	public int getStaffNumber() {
		return this.staffNumber;
	}
	
	/**
	 * Get the librarian's employment date
	 * @return the librarian's employment date
	 */
	public Date getEmploymentDate() {
		return this.employmentDate;
	}
	
	/**
	 * Set the librarian's staff number
	 * @param staffNumber the staff number of the librarian
	 */
	public void setStaffNumber(int staffNumber) {
		this.staffNumber = staffNumber;
	}
	
	/**
	 * Set the employment date of the librarian
	 * @param employmentDate the librarian's date of employment
	 */
	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}
	
}
