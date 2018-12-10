/**
<<<<<<< HEAD
 * @author Samuel Dobbie (966537)
=======
 * Implements the User dashboard.
 * @author Matt Ashman
>>>>>>> 1495250670d6d14f165cc0071dfaa3021220505f
 * Copyright: No copyright
 * @version 1.0
 */

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
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardController {


    @FXML public Label firstName;
    private String username;

    /**
     * Gets the username from the login controller.
     * @param userName the username of the logged in user.
     */
    public void getUsername(String userName){
        this.username = userName;
        setFirstname();
    }
    
    public void setUsername(String username){
        this.username = username;
        setFirstname();
    }
    /**
     * Sets the firstname of the user to a label and displays welcome message.
     */
    public void setFirstname(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("select use_Firstname from use_user where use_Username = '" + StaticUserInfo.getUsername() + "'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String userFirstname = rs.getString("use_Firstname");
                firstName.setText("Welcome, " + userFirstname);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    /**
     * Changes scene to User dashboard
     * @param event An action (button click) has taken place.
     */
    public void goToUserDash(ActionEvent event){
        changeScene("/javafx/UserDashboard.fxml", "DashboardController", event);
    }

    /**
     * Changes scene to Requests and Reserved
     * @param event An action (button click) has taken place.
     */
    public void goToReqAndRes(ActionEvent event){
        changeScene("/javafx/ReqAndRes.fxml", "ReqAndResController", event);
    }
    
    /**
     * Changes scene to the book scene.
     * @param event An action (button click) has taken place.
     */
    public void goToBooks(ActionEvent event){
        changeScene("/javafx/resourceViewsTemplate.fxml", "ResourceController", event);
    }
    
    @FXML
    /**
     * 
     * @param event An action (button click) has taken place.
     */
    public void goToLogin(ActionEvent event){
    	 changeScene("/javafx/Login.fxml", "LoginController", event);
    }

    @FXML
    /**
     * Changes scene to User's items borrowed Scene.
     * @param event An action (button click) has taken place.
     */
    public void goToItemsBorrowed(ActionEvent event){
        changeScene("/javafx/BorrowedItems.fxml", "BorrowingController", event);
    }
	
	/**
	 * Changes scene to the user profile scene.
	 * @param event An action (button click) has taken place.
	 */
    public void goToUserProfile(ActionEvent event){
        changeScene("/javafx/UserProfile.fxml", "ProfileController", event);
    }


    @FXML
    /**
     * Changes scene to a user's transaction history
     * @param event An action (button click) has taken place.
     */
    public void goToTransHistory(ActionEvent event){
    	changeScene("/javafx/TransHistory.fxml", "TransactionController", event);
    }
    
    @FXML
    /**
     * Changes scene to the issue desk scene.
     * @param event An action (button click) has taken place.
     */
	public void goToIssueDesk(ActionEvent event){
		changeScene("/javafx/IssueDeskUser.fxml", "IssueDeskController", event);
	}
    
    private void changeScene(String scene, String type, ActionEvent event) {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource(scene));
	
	        Parent homeWindow = loader.load();
	        Scene login = new Scene(homeWindow);
	        
	        if (type == "ResourceCreationController") {
	        	ResourceCreationController controller=loader.getController();
	        	controller.getUsername(username);
	        } else if (type == "TransactionController") {
	        	TransactionController controller = loader.getController();
	        	controller.getUsername(username);
	        } else if (type == "AccountCreationController") {
	        	AccountCreationController controller = loader.getController();
	        	controller.getUsername(username);
	        } else if (type == "BorrowingController") {
	        	BorrowingController controller = loader.getController();
	        	controller.getUsername(username);
	        } else if (type == "IssueDeskController") {
	        	IssueDeskController controller = loader.getController();
	        	controller.setUsername(username);
	        } else if (type == "ResourceController") {
	        	ResourceController controller = loader.getController();
	        	controller.getUsername(username);
	        } else if (type == "DashboardController") {
	        	DashboardController dashboardController=loader.getController();
	            dashboardController.getUsername(username);
	        } else if (type == "ProfileController"){
	            ProfileController profileController=loader.getController();
	            profileController.getUsername(username);
	        }else if (type == "ReqAndResController"){
                ReqAndResController controller = loader.getController();
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

}
