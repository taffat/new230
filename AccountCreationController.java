/**
 * To give librarian's the ability to create new User and Librarian accounts
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version 1.0
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;

public class AccountCreationController {

	// Username field
	@FXML private TextField userName;
	
	// Phone number field
	@FXML private TextField phoneNumber;
	
	// First name field
	@FXML private TextField firstName;
	
	// Last name field
	@FXML private TextField lastName;
	
	// Address field
	@FXML private TextField address;
	
	// Post code field
	@FXML private TextField postCode;
	
	// Staff number field
	@FXML private TextField staffNumber;
	
	// Employment date field
	@FXML private DatePicker employmentDate;
	
	// Label to display status
	@FXML private Label statusLabel;

	@FXML private  Image image;
	@FXML private  ImageView profileImage;
	private File file;

	private final int DEFAULT_BALANCE = 0;
	private final int ACC_TYPE_USER = 0;
	private final int ACC_TYPE_LIB = 1;
	private String username;

	public void getUsername(String usernameLib){
		username = usernameLib;
	}

	public void setImage(){
		Avatar a = new Avatar();
		Stage stage = new Stage();
		a.start(stage);
    }
    public void loadImage(){
        String url = Avatar.testUser.getAvatarFilePath();
        System.out.println(url);
        file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\230\\src\\" + url);
        image = new Image(file.toURI().toString());
        profileImage.setImage(image);
    }
    public FileInputStream getFis() throws FileNotFoundException {
        System.out.println(file.getAbsolutePath());
        FileInputStream fiss = new FileInputStream(file);
        return fiss;
    }
	
	@FXML
	public void createUserAccount(ActionEvent event) {
		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		boolean hasPassed = passedDataValidation("User", connection);
		
		try {
			if (hasPassed) {
				statusLabel.setText("");
                PreparedStatement ps = connection.prepareStatement("insert into use_user (use_UserType, use_Username,use_FirstName,use_LastName,use_PhoneNumber,use_Address,use_Postcode,use_Balance,use_ProfileImage) values (?,?,?,?,?,?,?,?,?)");
                ps.setInt(1,ACC_TYPE_USER);
                ps.setString(2,userName.getText());
                ps.setString(3,firstName.getText());
                ps.setString(4,lastName.getText());
                ps.setString(5,phoneNumber.getText());
                ps.setString(6,address.getText());
                ps.setString(7,postCode.getText());
                ps.setInt(8,DEFAULT_BALANCE);
                FileInputStream fis = getFis();
                ps.setBinaryStream(9,fis);

                ps.executeUpdate();
				JOptionPane.showMessageDialog(null, "User account created successfully.", "Success!", JOptionPane.INFORMATION_MESSAGE);
				changeScene("/javafx/AccountCreationUser.fxml");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	public void createLibrarianAccount(ActionEvent event) {
		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		
		boolean hasPassed = passedDataValidation("Librarian", connection);
		
		try {
			if (hasPassed) {
				statusLabel.setText("");
				// Insert general librarian data
				PreparedStatement stmtOne = connection.prepareStatement("INSERT INTO use_user VALUES(NULL, " + ACC_TYPE_LIB + ", '" + userName.getText() + "', '" + firstName.getText() + "', '" + lastName.getText() + "', '" + phoneNumber.getText() + "', '" + address.getText() + "', '" + postCode.getText() + "', " + DEFAULT_BALANCE + ", 'images\\\\avatars\\\\default\\\\default-avatar.png')");
				stmtOne.executeUpdate();
				// Get userId of newly added librarian
				int userId = getUserId(connection);
				// Insert librarian specific data if error free
				if (userId != -1) {
					PreparedStatement stmtThree = connection.prepareStatement("INSERT INTO lib_librarian VALUES(" + userId + ",'" + staffNumber.getText() + "', '" + employmentDate.getValue() + "')");
					stmtThree.executeUpdate();
					JOptionPane.showMessageDialog(null, "Librarian account created successfully.", "Success!", JOptionPane.INFORMATION_MESSAGE);
					changeScene("/javafx/AccountCreationLibrarian.fxml");
				}
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
	 * Changes scene to User dashboard
	 * @param event ??
	 */
	public void goToLibDash(ActionEvent event){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/LibrarianDashboard.fxml"));

			Parent homeWindow = loader.load();
			Scene borrowedItems = new Scene(homeWindow);

			LibrarianController librarianController=loader.getController();
			librarianController.getUsername(username);
			// Get the stage
			Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
			window.setScene(borrowedItems);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean passedDataValidation(String userType, Connection connection) {
		PreparedStatement usernameCheck;
		try {
			usernameCheck = connection.prepareStatement("SELECT * FROM use_user WHERE LOWER(use_Username) = LOWER('" + userName.getText() + "')");
			
			// Check if any fields are empty
			if (userName.getText().isEmpty() || phoneNumber.getText().isEmpty() ||
					firstName.getText().isEmpty() || lastName.getText().isEmpty() ||
					address.getText().isEmpty() || postCode.getText().isEmpty() ||
					(userType == "Librarian" && staffNumber.getText().isEmpty()) ||
					(userType == "Librarian" && employmentDate.getValue() == null)) {
				statusLabel.setText("Please complete all fields.");
				statusLabel.setTextFill(Color.RED);
				return false;
			// Check if the entered username already exists
			} else if (usernameCheck.executeQuery().next()) {
				statusLabel.setText("That username is taken. Please enter a unique one.");
				statusLabel.setTextFill(Color.RED);
				return false;
			}
			
			// Check if staffNumber is a valid integer
			if (userType == "Librarian") {
				try {
					Integer.parseInt(staffNumber.getText());  
				} catch (NumberFormatException e) { 
					statusLabel.setText("Please enter a valid staff number.");
					statusLabel.setTextFill(Color.RED);
					return false;
				} 
			}
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	private int getUserId(Connection connection) {
		PreparedStatement stmtTwo;
		try {
			stmtTwo = connection.prepareStatement("SELECT use_UserId FROM use_user WHERE use_Username = '" + userName.getText() + "'");
			
			ResultSet result = stmtTwo.executeQuery();
			int userId = 0;
			while (result.next()) {
			   userId = result.getInt(1);
			}
			return userId;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

	private void changeScene(String nextScene) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(nextScene));
			
			Parent homeWindow = loader.load();
			Scene dashboard = new Scene(homeWindow);
			
			// Get the stage
			Stage window = (Stage) userName.getScene().getWindow();
			window.setScene(dashboard);
			window.show();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	public void goToUsersOverview(ActionEvent event){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/UsersOverview.fxml"));

			Parent homeWindow = loader.load();
			Scene borrowedItems = new Scene(homeWindow);

			// Get the stage
			Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
			window.setScene(borrowedItems);
			window.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}