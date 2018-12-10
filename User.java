/**
 * To store information about a general user
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version: 1.0
 */

public class User {
	private String usertype;
	// The unique identifier of the user
	private String username;
	// The users' first name
	private String firstname;
	// The users' last name
	private String lastname;
	// The users' phone number
	private String phoneNumber;
	// The users' address
	private String address;
	// The users' post code
	private String postCode;
	// The file path to the users' avatar
	private String avatarFilePath;
	// The users balance (ADDITIONAL FEATURE: allowing users to add extra money)
	private float balance = 0;
	
	/**
	 * A constructor to model a general user
	 * @param username the unique identifier of the user
	 * @param firstname the first name of the user
	 * @param lastname the last name of the user
	 * @param phoneNumber the phone number of the user
	 * @param address the address of the user
	 * @param postCode the post code of the user
	 * @param avatarFilePath the file path to the users' avatar
	 */
	public User(String usertype,String username, String firstname, String lastname, String phoneNumber, String address,
			    String postCode, String avatarFilePath, float balance) {
		// TODO: Need to check to ensure username is unique before user creation
		this.usertype = usertype;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.postCode = postCode;
		this.avatarFilePath = avatarFilePath;
		this.balance = balance;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	/**
	 * Provides ability to increase a users' balance
	 * @param amount the amount to be added to the users' balance
	 */
	public void increaseBalance(int amount) {
		this.balance += balance;
	}
	
	/**
	 * Provides ability to decrease a users' balance
	 * @param amount the amount to be reduced from the users' balance
	 */
	public void decreaseBalance(int amount) {
		this.balance -= balance;
	}
	
	/**
	 * Gets the username of the user
	 * @return the users' username
	 */
	public String getUserName() {
		return this.username;
	}
	
	/**
	 * Gets the first name of the user
	 * @return the users' first name
	 */
	public String getFirstName() {
		return this.firstname;
	}
	
	/**
	 * Gets the last name of the user
	 * @return the users' last name
	 */
	public String getLastName() {
		return this.lastname;
	}
	
	/**
	 * Gets the phone number of the user
	 * @return the users' phone number
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	/**
	 * Gets the address of the user
	 * @return the users' address
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * Gets the post code of the user
	 * @return the users' post code
	 */
	public String getPostCode() {
		return this.postCode;
	}
	
	/**
	 * Gets the file path of the users' avatar
	 * @return the file path of the users' avatar
	 */
	public String getAvatarFilePath() {
		return this.avatarFilePath;
	}
	
	/**
	 * Gets the current balance of the user
	 * @return the users' balance
	 */
	public float getBalance() {
		return this.balance;
	}
	
	/**
	 * Enables users to change their username (ADDITIONAL FEATURE)
	 * @param username the users' new username
	 */
	public void setUserName(String username) {
		this.username = username;
	}
	
	/**
	 * Enables users to update their first name
	 * @param firstname the updated first name
	 */
	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Enables users to update their last name
	 * @param lastname the updated last name
	 */
	public void setLastName(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * Enables users to update their phone number
	 * @param phoneNumber the users' new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * Enables users to update their address
	 * @param address the users' new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Enables users to update their post code
	 * @param postCode the users' new post code
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	/**
	 * Enables users to update their avatar
	 * @param avatarFilePath the file path to the users' new avatar
	 */
	public void setAvatarFilePath(String avatarFilePath) {
		this.avatarFilePath = avatarFilePath;
	}
}
