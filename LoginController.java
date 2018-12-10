/**
 * LoginController.java
 * Implements the login functionality of the library system, queries the database, performs verification and loads the appropriate dashboard.
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.9
*/

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {
	
		// Username text field
		@FXML
		private TextField txtUsername;

		// Label to display status
		@FXML private Label lblStatus;
		
		/**
		 * Logs the user in if the username they entered exists in the database, and checks whether it is a standard user or a librarian.
		 * @param event An action (button click) has taken place.
		 */
		@FXML
		public void Login(ActionEvent event) {
			DatabaseManager db = new DatabaseManager();
			Connection connection = db.getConnection();
			
			try {
				//implemented prepared statements to prevent SQL injection attacks, as an extensive feature.
				PreparedStatement libCheck = connection.prepareStatement("SELECT use_username FROM use_user WHERE use_UserType = 1 and use_username = '" + txtUsername.getText() + "';");
				PreparedStatement userCheck = connection.prepareStatement("SELECT use_username FROM use_user WHERE use_UserType = 0 and use_username = '" + txtUsername.getText() + "';");
				// old, unsecure version. String user = "SELECT * FROM userinfo WHERE username = '" + textUsername.getText() + "';";
				ResultSet user = userCheck.executeQuery();
				ResultSet librarian = libCheck.executeQuery();
				
				if (user.next()) {
					StaticUserInfo.setUserValues(txtUsername.getText());
					changeSceneOnLogin(event,txtUsername.getText());
				} else if (librarian.next() ){
					StaticUserInfo.setUserValues(txtUsername.getText());
					changeSceneOnLoginLibrarian(event,txtUsername.getText());
				}
				 else {
					lblStatus.setText("Login has failed. Please enter a valid username");
					lblStatus.setTextFill(Color.RED);
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		
		}
		
		/**
		 * Changes scene to a User's Dashboard on successful login
		 * @param event An action (button click) has taken place.
		 * @throws Exception ??
		 */
		private void changeSceneOnLogin(ActionEvent event,String username) throws Exception {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/javafx/UserDashboard.fxml"));
				
				Parent homeWindow = loader.load();
				Scene dashboard = new Scene(homeWindow);
				
				//Connects to the second controller
				DashboardController secController=loader.getController();
                		secController.getUsername(username);
				
				// Get the stage
				Stage window = (Stage) txtUsername.getScene().getWindow();
				window.setScene(dashboard);
				window.show();
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		/*
		 * Changes scene to a Librarian's Dashboard on successful login
		 * @param event An action (button click) has taken place.
		 * @throws Exception ??
		 */
		private void changeSceneOnLoginLibrarian(ActionEvent libLogin,String username) throws Exception {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/javafx/LibrarianDashboard.fxml"));
				
				Parent homeWindow = loader.load();
				Scene dashboard = new Scene(homeWindow);

				//Connects to the second controller
				LibrarianController secController=loader.getController();
				secController.getUsername(username);
				
				// Get the stage
				Stage window = (Stage) txtUsername.getScene().getWindow();
				window.setScene(dashboard);
				window.show();
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
}
