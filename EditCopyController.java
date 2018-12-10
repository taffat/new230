/**
 * To give librarian's the ability to edit copies.
 * @author Jack Long (965615)
 * Copyright: No copyright
 * @version 1.0
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditCopyController implements Initializable {
	
	@FXML private Label currentLoanDuration;
	@FXML private TextField search;
	@FXML private ChoiceBox loanDurationChoice;
	private String userName;
	private int copyID;
	
	ObservableList<String> loanDurationList = FXCollections.observableArrayList("1","7","14","28");
	
	/**
     * Gets the username from the dash controller.
     * @param usernameUse the username of the logged in user.
     */
    public void getUsername(String usernameUse) throws IOException {
        userName = usernameUse;
    }
      
	
	public void displayCopy() {
		DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        copyID = Integer.parseInt(search.getText());
        System.out.println(copyID);
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT rcp_loanDuration FROM rcp_resourcecopy WHERE rcp_Copyid = "+copyID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                currentLoanDuration.setText("Loan duration: " + rs.getString("rcp_loanDuration")  + " days");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public void updateInfoCopy(String field, String value, Connection connection){
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE rcp_resourcecopy SET "+ field + "= '" + value + "' " +
                    "WHERE rcp_copyid =" + copyID + "");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public void editCopy (ActionEvent event) {
		DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
		if (loanDurationChoice.getValue() == "1") {
			updateInfoCopy("rcp_loanduration", "1", connection);
		} else if (loanDurationChoice.getValue() == "7") {
			updateInfoCopy("rcp_loanduration", "7", connection);
		} else if (loanDurationChoice.getValue() == "14") {
			updateInfoCopy("rcp_loanduration", "14", connection);
		} else if (loanDurationChoice.getValue() == "28") {
			updateInfoCopy("rcp_loanduration", "28", connection);
		}
		JOptionPane.showMessageDialog(null, "Information updated successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        changeScene("/javafx/ManageResources.fxml", "ManageResourcesController", event);
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		loanDurationChoice.setItems(loanDurationList);
		//loanDurationChoice.setValue();
	}
	
	/**
     * Changes scene to the user dashboard scene.
     * @param event An action (button click) has taken place.
     */
    public void goToManageResources(ActionEvent event){
        changeScene("/javafx/ManageResources.fxml", "LibrarianController", event);
    }


    /**
     * Changes scene to the login scene.
     * @param event An action (button click) has taken place.
     */
    public void goToLogin(ActionEvent event){
        changeScene("/javafx/Login.fxml", "LoginController", event);
    }
    
    private void changeScene(String scene, String type, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(scene));

            Parent homeWindow = loader.load();
            Scene login = new Scene(homeWindow);

            if (type == "DashboardController") {
                DashboardController controller = loader.getController();
                controller.getUsername(userName);
            } else if (type == "ProfileController"){
                EditResourceController profileController=loader.getController();
                profileController.getUsername(userName);
            } if (type == "LibrarianController") {
            	LibrarianController controller = loader.getController();
            	controller.getUsername(userName);
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
