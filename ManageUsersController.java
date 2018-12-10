/**
* DvdController.java
* Implements the filter: DVD function, allowing users to filter resources by type DVD, contact database and display results in a table
* @Director Chaye Novak (902037), Jack Mcleay
* Copyright: No copyright
* @version 1.2
*/
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;

public class ManageUsersController implements Initializable {

	@FXML
	private TableView<User> tblUsers;
	@FXML
	private TableColumn<User, String> col_UserName;
	@FXML
	private TableColumn<User, String> col_FirstName;
	@FXML
	private TableColumn<User, String> col_LastName;
	@FXML
	private TableColumn<User, String> col_Balance;
	@FXML
	private TableColumn<User, String> col_Usertype;

	private String userType;

	@FXML
	private TextField searchBox;

	 ObservableList<User> userList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();

		try {

			PreparedStatement getUsers = connection.prepareStatement("SELECT * FROM use_user where use_UserType = 1 or use_UserType = 0 Order by use_Usertype DESC");
			ResultSet userInfo = getUsers.executeQuery();
			//table doesn't need an address, postcode, phonenumber or profile picture so there's no need to use the real one
			//I'll use an empty string instead
			// store all user details in the observable list "users"

			while (userInfo.next()) {

				if (userInfo.getInt("use_Usertype") == 0){
					userType = "User";
				}else{
					userType = "Librarian";
				}

				userList.add(new User(userType,userInfo.getString("use_Username"),
						userInfo.getString("use_FirstName"), userInfo.getString("use_LastName"), userInfo.getString("use_PhoneNumber"),
						userInfo.getString("use_Address"), userInfo.getString("use_Postcode"), userInfo.getString("use_ProfileImage"),userInfo.getInt("use_Balance")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		}
		System.out.println(userType);
		col_Usertype.setCellValueFactory(new PropertyValueFactory<>("usertype"));
		col_UserName.setCellValueFactory(new PropertyValueFactory<>("UserName"));
		col_FirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
		col_LastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
		col_Balance.setCellValueFactory(new PropertyValueFactory<>("balance"));

		tblUsers.setItems(userList);

		tblUsers.setOnMousePressed(event -> {
			if (event.getClickCount() == 2 && event.isPrimaryButtonDown()) {
				String username = tblUsers.getSelectionModel().getSelectedItem().getUserName();
				Object[] options = {"Edit User", "Delete User"};
				int n = JOptionPane.showOptionDialog(null,
						"What would you like to do?",
						"Please Choose",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
				if (n ==0){
					System.out.println("edit");
				}else if (n ==1){
					System.out.println("delete");
					int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
						System.out.println("yes");
						deleteUser(username);
						JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
						goToManageUsers(event);

					}else{
						System.out.println("no");
					}
				}else{
					System.out.println("nothing");
				}

			}
		});


	}

	public void deleteUser(String username){
		DatabaseManager db = new DatabaseManager();
		Connection conn = db.getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement("update use_user set use_Usertype = 2 where use_Username = '"+ username+"'");
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void goToManageUsers(MouseEvent event){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/ManageUsers.fxml"));

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

	/**
     * Changes scene to User dashboard
     * @param event ??
     */
    public void goToLibrarianDashboard(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/javafx/LibrarianDashboard.fxml"));

            Parent dashboard = loader.load();
            Scene userDash = new Scene(dashboard);

            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(userDash);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void searchResources(KeyEvent event) {
    	//
    	FilteredList <User> filterResources = new FilteredList<>(userList, p -> true);
    	searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
    		filterResources.setPredicate(items -> {
    			if (newValue == null || newValue.isEmpty()) {
    				return true;
    			}
    			String typedText = newValue.toLowerCase();
    			
    			if (items.getUserName().toLowerCase().indexOf(typedText) != -1) {
    				return true;
    			}
    			
    			if (items.getFirstName().toLowerCase().indexOf(typedText) != -1) {
    				return true;
    			}
				if (items.getLastName().toLowerCase().indexOf(typedText) != -1) {			
				    return true;
				}
    			return false;
    		});
    		SortedList <User> sortedList = new SortedList <> (filterResources);
    		sortedList.comparatorProperty().bind(tblUsers.comparatorProperty());;
    		tblUsers.setItems(sortedList);
    	});
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
