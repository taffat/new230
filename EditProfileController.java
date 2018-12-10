/**
 * To give librarian's the ability to create new Users.
 * @author Samuel Dobbie (966537), Matt Ashman (912639)
 * Copyright: No copyright
 * @version 1.0
 */

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EditProfileController {
    
    @FXML private Label userID;
    @FXML private Label username;
    @FXML private Label firstname;
    @FXML private Label surname;
    @FXML private Label phonenum;
    @FXML private Label address;
    @FXML private Label postcode;
    @FXML private Label balance;
    @FXML private ImageView profilePIC;

    @FXML private Label statusLabel;

    @FXML private TextField usernameNEW;
    @FXML private TextField firstnameNEW;
    @FXML private TextField lastnameNEW;
    @FXML private TextField phonenumNEW;
    @FXML private TextField addressNEW;
    @FXML private TextField postcodeNEW;

    private String userName;
    
    /**
     * Gets the username from the dash controller.
     * @param usernameUse the username of the logged in user.
     */
    public void getUsername(String usernameUse) throws IOException {
        userName = usernameUse;
        setLabels();
    }

    /**
     * sets the label on the scene to match the user's info.
     */
    public void setLabels(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement("select use_UserId,use_Username,use_FirstName,use_LastName,use_PhoneNumber,use_Address,use_Postcode,use_Balance from use_user where use_Username = '" + StaticUserInfo.getUsername() + "'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userID.setText(rs.getString("use_UserId"));
                username.setText(rs.getString("use_Username"));
                firstname.setText(rs.getString("use_FirstName"));
                surname.setText(rs.getString("use_LastName"));
                phonenum.setText(rs.getString("use_PhoneNumber"));
                address.setText(rs.getString("use_Address"));
                postcode.setText(rs.getString("use_Postcode"));
                balance.setText(rs.getString("use_Balance"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Checks the data to see if the newly entered data is valid.
     * @param event An action (button click) has taken place.
     */
    public void checkData(ActionEvent event){
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        if (!firstnameNEW.getText().isEmpty()){
            updateInfo("use_FirstName", firstnameNEW.getText(),connection);
        }
        if (!lastnameNEW.getText().isEmpty()){
            updateInfo("use_LastName", lastnameNEW.getText(),connection);
        }
        if (!phonenumNEW.getText().isEmpty()){
            updateInfo("use_PhoneNumber", phonenumNEW.getText(),connection);
        }
        if (!addressNEW.getText().isEmpty()){
            updateInfo("use_Address", addressNEW.getText(),connection);
        }
        if (!postcodeNEW.getText().isEmpty()){
            updateInfo("use_Postcode", postcodeNEW.getText(),connection);
        }
        if (!usernameNEW.getText().isEmpty()){
            PreparedStatement usernameCheck;
            try {
                usernameCheck = connection.prepareStatement("SELECT * FROM use_user WHERE LOWER(use_Username) = LOWER('" + usernameNEW.getText() + "')");
                usernameCheck.executeQuery().next() ;
                if (usernameCheck.executeQuery().next()) {
                    statusLabel.setText("That username is taken. Please enter a unique one.");
                    statusLabel.setTextFill(Color.RED);
                }else{
                    updateInfo("use_Username", usernameNEW.getText(), connection);
                    JOptionPane.showMessageDialog(null, "Information updated successfully! Please re-login.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    changeScene("/javafx/Login.fxml", "LoginController", event);                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(null, "Information updated successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
            changeScene("/javafx/UserProfile.fxml", "ProfileController", event);
        }
    }

    /**
     * Updates the user's information.
     * @param field name of the field to be changed.
     * @param value the new data to be entered.
     * @param connection ??
     */
    public void updateInfo(String field, String value, Connection connection){
        String useUserID = getUserID();
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE use_user SET "+ field + "= '" + value + "' " +
                    "WHERE use_UserId = '" + useUserID + "'");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get's the ID of the user.
     * @return Id of the user.
     */
    public String getUserID(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        String userID = "";
        try {
            PreparedStatement stmt = conn.prepareStatement("select use_UserId from use_user where use_Username = '" + StaticUserInfo.getUsername() + "'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userID = rs.getString("use_UserId");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userID;
    }

    /**
     * Changes scene to the user dashboard scene.
     * @param event An action (button click) has taken place.
     */
    public void goToUserDash(ActionEvent event){
    	if (StaticUserInfo.getUserType() == 1) {
        changeScene("/javafx/LibrarianDashboard.fxml", "LibrarianController", event);
    	} else {
    		changeScene("/javafx/UserDashboard.fxml", "DashboardController", event);
    	}
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
                EditProfileController profileController=loader.getController();
                profileController.getUsername(userName);
            } else if (type == "LibrarianController") {
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
