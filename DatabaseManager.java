/**
b * Handles the connectivity between the Java program and the database. Provides alternative connections depending on the user.
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

	/**
	 * 
	 * @return DriverManager this returns a connection with the database, handled by the driver manager.
	 */
	public Connection getConnection() {
		
		String databaseName = null;
		String portNumber = null;
		String userName = null;
		String password = null;
		
		/* ADD YOUR CONNECTION DETAILS BELOW AND UNCOMMENT YOUR NAME WHEN USING */
				


		 //String USER = "sam";
		// String USER = "chaye";
		// String USER = "sam";
		// String USER = "chaye";
		// String USER = "chris";
		 String USER = "matt";
		// String USER = "jackM";
		// String USER = "jackL";
		// String USER = "dan";
		
		
		if (USER == "sam") {
			databaseName = "cdb";
			portNumber = "3312";
			userName = "root";
			password = "";
		} else if (USER == "chaye") {
			databaseName = "cdb";
			portNumber = "3312";
			userName = "root";
			password = "password";
		} else if (USER == "chris") {
			databaseName = "";
			portNumber = "";
			userName = "";
			password = "";
		} else if (USER == "matt") {
			databaseName = "library";
			portNumber = "3306";
			userName = "root";
			password = "password";
		} else if (USER == "jackM") {
			databaseName = "library";
			portNumber = "3306";
			userName = "root";
			password = "";
		} else if (USER == "jackL") {
			databaseName = "lib";
			portNumber = "3306";
			userName = "root";
			password = "";
		} else if (USER == "dan") {
			databaseName = "";
			portNumber = "";
			userName = "";
			password = "";
		}
			
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://localhost:" + portNumber + "/" + databaseName, userName, password);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// TODO Deal with failed connection
		return null;
	}
	
}
