/**
 * Implements the Librarian dashboard.
 * @author Matt Ashman, Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version 1.0
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LibrarianController implements Initializable {

    @FXML public Label firstName;
    public String username;

    /**
     * Gets the username from the login controller.
     * @param userName the username of the logged in user.
     */
    public void getUsername(String userName){
        username = userName;
        setFirstname();
    }

    /**
     * sets the firstname of the user to a label and displays welcome message.
     */ 
    public void setFirstname(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("select use_Firstname from use_user where use_Username = '" + StaticUserInfo.getUsername() + "'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String userFirstname = rs.getString("use_Firstname");
                firstName.setText("Welcome, " + userFirstname + " (Librarian)");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Changes the scene to the Login scene.
     * @param event An action (button click) has taken place.
     */
    public void goToLogin(ActionEvent event){
    	changeScene("/javafx/Login.fxml", event);
    }
    
    /**
     * Changes the scene to the Login scene.
     * @param event An action (button click) has taken place.
     */
    public void goToIssueDesk(ActionEvent event){
    	changeScene("/javafx/IssueDeskLibrarian.fxml", event);
    }

    /**
     * Changes the scene to the User Creation scene.
     * @param event An action (button click) has taken place.
     */
    public void goToCreateUser(ActionEvent event){
    	changeSceneAccountController("/javafx/AccountCreationUser.fxml", event);
    }

    /**
     * Changes the scene to the Librarian Creation scene.
     * @param event An action (button click) has taken place.
     */
    public void goToCreateLibrarian(ActionEvent event){
    	changeSceneAccountController("/javafx/AccountCreationLibrarian.fxml", event);
    }

    /**
     * Changes the scene to the Create Book scene.
     * @param event An action (button click) has taken place.
     */
    public void goToCreateBook(ActionEvent event){
    	changeSceneResourceController("/javafx/ResourceCreationBook.fxml", event);
    }

    /**
     * Changes the scene to the Create DVD scene.
     * @param event An action (button click) has taken place.
     */
    public void goToCreateDVD(ActionEvent event){
    	changeSceneResourceController("/javafx/ResourceCreationDVD.fxml", event);
    }
    
    /**
     * Changes the scene to the Create Laptop scene.
     * @param event An action (button click) has taken place.
     */
    public void goToCreateLaptop(ActionEvent event){
    	changeSceneResourceController("/javafx/ResourceCreationLaptop.fxml", event);
    }
    
    /**
     * Changes the scene to the Manage Users scene.
     * @param event An action (button click) has taken place.
     */
    public void goToManageUsers(ActionEvent event){
    	changeSceneManageUsersController("/javafx/ManageUsers.fxml", event);
    }
	    
    /**
     * Changes the scene to the overdue scene.
     * @param event An action (button click) has taken place.
     */
    public void goToOverdueCopies(ActionEvent event){
    	changeScene("/javafx/Overdue.fxml", event);
    }
    
    /**
     * Changes the scene to the borrow history scene.
     * @param event An action (button click) has taken place.
     */
    public void goToBorrowHistory(ActionEvent event){
    	changeScene("/javafx/BorrowHistory.fxml", event);
    }

	/**
	 * Changes scene to the user profile scene.
	 * @param event An action (button click) has taken place.
	 */
    public void goToUserProfile(ActionEvent event){
    	changeScene("/javafx/EditUser.fxml", event);
    }
    
    /**
     * Changes the scene to the borrow history scene.
     * @param event An action (button click) has taken place.
     */
    public void goToCopyAdder(ActionEvent event){
        changeSceneCopy("/javafx/ResourceCopyAddition.fxml", event);
    }
    
    /**
     * Changes the scene to the borrow history scene.
     * @param event An action (button click) has taken place.
     */
    public void goToEditCopy(ActionEvent event){
        changeScene("/javafx/EditCopy.fxml", event);
    }


    /**
     * Changes the scene to the Login scene.
     * @param event An action (button click) has taken place.
     */
    public void changeScene(String scene, ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(scene));

            Parent homeWindow = loader.load();
            Scene login = new Scene(homeWindow);
            
            if (scene == "/javafx/EditUser.fxml") {
            	EditProfileController controller = loader.getController();
                controller.getUsername(username);
            }

            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(login);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Changes the scene to the Login scene.
     * @param event An action (button click) has taken place.
     */
    public void changeSceneCopy(String scene, ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(scene));

            Parent homeWindow = loader.load();
            Scene login = new Scene(homeWindow);

            ResourceAdditionController controller=loader.getController();
            controller.getUsername(username);

            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(login);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    
    private void changeSceneAccountController(String scene, ActionEvent event) {
    	 try {
             FXMLLoader loader = new FXMLLoader();
             loader.setLocation(getClass().getResource(scene));

             Parent homeWindow = loader.load();
             Scene borrowedItems = new Scene(homeWindow);

             AccountCreationController secController=loader.getController();
             secController.getUsername(username);

             // Get the stage
             Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
             window.setScene(borrowedItems);
             window.show();

         } catch (IOException e) {
             e.printStackTrace();
         }
	}
    
    private void changeSceneManageUsersController(String scene, ActionEvent event) {
   	 try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(scene));

            Parent homeWindow = loader.load();
            Scene manageUsers = new Scene(homeWindow);
            
            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(manageUsers);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    private void changeSceneResourceController(String scene, ActionEvent event) {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource(scene));
	
	        Parent homeWindow = loader.load();
	        Scene login = new Scene(homeWindow);
	
	        ResourceCreationController secController=loader.getController();
	        secController.getUsername(username);
	
	        // Get the stage
	        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
	        window.setScene(login);
	        window.show();
	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

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

    public void goToEditBook(ActionEvent event){
        changeScene("/javafx/EditResourceBook.fxml", event);
    }

    public void goToEditLaptop(ActionEvent event){
        changeScene("/javafx/EditResourceLaptop.fxml", event);
    }

    public void goToEditDVD(ActionEvent event){
        changeScene("/javafx/EditResourceDVD.fxml", event);
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

    public void goToManageResources(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/javafx/ManageResources.fxml"));

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

}
