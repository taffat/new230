import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * To store information about a general user
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version: 1.0
 */

public class StaticUserInfo {
	// The unique identifier of the user
	private static int userId;
	
	private static int userType;
	// The unique identifier of the user
	private static String username;
	// The users' first name
	private static String firstName;
	// The users' last name
	private static String lastName;
	// The users' phone number
	private static String phoneNumber;
	// The users' address
	private static String address;
	// The users' post code
	private static String postCode;
	// The users balance (ADDITIONAL FEATURE: allowing users to add extra money)
	private static float balance = 0;
	// The file path to the users' avatar
	private static String avatarFilePath;
	
	public static void setUserValues(String currentUser) {
		DatabaseManager db = new DatabaseManager();
   		Connection connection = db.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("Select use_UserId, use_Username, use_FirstName, use_LastName, use_PhoneNumber, use_Address, use_Postcode, use_Balance, use_ProfileImage, use_UserType from use_user WHERE use_Username = '" + currentUser + "'");
            ResultSet rs = stmt.executeQuery();
            
			while (rs.next()) {
				userId = rs.getInt(1);
				username = rs.getString(2);
				firstName = rs.getString(3);
				lastName = rs.getString(4);
				phoneNumber = rs.getString(5);
				address = rs.getString(6);
				postCode = rs.getString(7);
				balance = rs.getInt(8);
				avatarFilePath = rs.getString(9);
				userType = rs.getInt(10);
			}
        } catch (SQLException ex) {
        	  System.out.println("There's an error in your SQL");
        }
	}
	
	/**
	 * Gets the userId of the user
	 * @return the users' username
	 */
	public static int getUserId() {
		return userId;
	}
	
	public static int getUserType() {
		return userType;
	}
	
	/**
	 * Gets the username of the user
	 * @return the users' username
	 */
	public static String getUsername() {
		return username;
	}
	
	/**
	 * Gets the first name of the user
	 * @return the users' first name
	 */
	public static String getFirstName() {
		return firstName;
	}
	
	/**
	 * Gets the last name of the user
	 * @return the users' last name
	 */
	public static String getLastName() {
		return lastName;
	}
	
	/**
	 * Gets the phone number of the user
	 * @return the users' phone number
	 */
	public static String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * Gets the address of the user
	 * @return the users' address
	 */
	public static String getAddress() {
		return address;
	}
	
	/**
	 * Gets the post code of the user
	 * @return the users' post code
	 */
	public static String getPostCode() {
		return postCode;
	}
	
	/**
	 * Gets the file path of the users' avatar
	 * @return the file path of the users' avatar
	 */
	public static String getAvatarFilePath() {
		return avatarFilePath;
	}
	
	/**
	 * Gets the current balance of the user
	 * @return the users' balance
	 */
	public static float getBalance() {
		return balance;
	}
	
	/**
	 * Enables users to change their username (ADDITIONAL FEATURE)
	 * @param username the users' new username
	 */
	public static void setUsername(String newUsername) {
		username = newUsername;
	}
	
	/**
	 * Enables users to update their first name
	 * @param firstname the updated first name
	 */
	public static void setFirstName(String newFirstName) {
		firstName = newFirstName;
	}
	
	/**
	 * Enables users to update their last name
	 * @param lastname the updated last name
	 */
	public static void setLastName(String newLastName) {
		lastName = newLastName;
	}
	
	/**
	 * Enables users to update their phone number
	 * @param phoneNumber the users' new phone number
	 */
	public static void setPhoneNumber(String newPhoneNumber) {
		phoneNumber = newPhoneNumber;
	}
	
	/**
	 * Enables users to update their address
	 * @param address the users' new address
	 */
	public static void setAddress(String newAddress) {
		address = newAddress;
	}
	
	/**
	 * Enables users to update their post code
	 * @param postCode the users' new post code
	 */
	public static void setPostCode(String newPostCode) {
		postCode = newPostCode;
	}
	
	/**
	 * Enables users to update their avatar
	 * @param avatarFilePath the file path to the users' new avatar
	 */
	public static void setAvatarFilePath(String newAvatarFilePath) {
		avatarFilePath = newAvatarFilePath;
	}
}
