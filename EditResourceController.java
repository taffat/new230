/**
 * To give librarian's the ability to create new Users.
 * @author Samuel Dobbie (966537), Matt Ashman (912639)
 * Copyright: No copyright
 * @version 1.0
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EditResourceController {

    @FXML private TextField search;
    public String username;
    @FXML private Label title;
    @FXML private Label id;
    @FXML private Label year;
    @FXML private Label desc;
    @FXML private Label author;
    @FXML private Label publisher;
    @FXML private Label genre;
    @FXML private Label isbn;
    @FXML private Label language;
    @FXML private Label director;
    @FXML private Label runtime;
    @FXML private Label man;
    @FXML private Label model;
    @FXML private Label os;


    @FXML private TextField titleNew;
    @FXML private TextField publisherNew;
    @FXML private TextField authorNew;
    @FXML private TextField descNew;
    @FXML private TextField yearNew;
    @FXML private TextField genreNew;
    @FXML private TextField isbnNew;
    @FXML private TextField languageNew;
    @FXML private TextField directorNew;
    @FXML private TextField runtimeNew;
    @FXML private TextField manNew;
    @FXML private TextField modelNew;
    @FXML private TextField osNew;

    private int resourceID;



    private String userName;
    
    /**
     * Gets the username from the dash controller.
     * @param usernameUse the username of the logged in user.
     */
    public void getUsername(String usernameUse) throws IOException {
        userName = usernameUse;
        //displayResource();
    }

    public void displayResourceBook() {
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        resourceID = Integer.parseInt(search.getText());
        System.out.println(resourceID);
        id.setText(Integer.toString(resourceID));
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * from res_resource,bok_book where res_ResourceId = bok_ResourceId and res_ResourceId = "+resourceID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                title.setText(rs.getString("res_Title"));
                year.setText(rs.getString("res_Year"));
                desc.setText(rs.getString("res_Description"));
                author.setText(rs.getString("bok_Author"));
                publisher.setText(rs.getString("bok_Publisher"));
                genre.setText(rs.getString("bok_Genre"));
                isbn.setText(rs.getString("bok_ISBN"));
                language.setText(rs.getString("bok_Language"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void displayResourceDVD() {
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        resourceID = Integer.parseInt(search.getText());
        System.out.println(resourceID);
        id.setText(Integer.toString(resourceID));
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * from res_resource,dvd_dvd where res_ResourceId = dvd_ResourceId and res_ResourceId = "+resourceID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                title.setText(rs.getString("res_Title"));
                year.setText(rs.getString("res_Year"));
                desc.setText(rs.getString("res_Description"));
                director.setText(rs.getString("dvd_Director"));
                runtime.setText(rs.getString("dvd_Runtime"));
                language.setText(rs.getString("dvd_Language"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void displayResourceLaptop() {
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        resourceID = Integer.parseInt(search.getText());
        System.out.println(resourceID);
        id.setText(Integer.toString(resourceID));
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * from res_resource,lab_laptop where res_ResourceId = lab_ResourceId and res_ResourceId = "+resourceID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                title.setText(rs.getString("res_Title"));
                year.setText(rs.getString("res_Year"));
                desc.setText(rs.getString("res_Description"));
                model.setText(rs.getString("lab_Model"));
                man.setText(rs.getString("lab_Manufacturer"));
                os.setText(rs.getString("lab_OS"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateInfoRes(String field, String value, Connection connection){
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE res_resource SET "+ field + "= '" + value + "' " +
                    "WHERE res_ResourceId =" + resourceID + "");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateInfoBok(String field, String value, Connection connection){
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE bok_book SET "+ field + "= '" + value + "' " +
                    "WHERE bok_ResourceId =" + resourceID + "");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateInfoDvd(String field, String value, Connection connection){
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE dvd_dvd SET "+ field + "= '" + value + "' " +
                    "WHERE dvd_ResourceId =" + resourceID + "");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateInfoLaptop(String field, String value, Connection connection){
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE lab_laptop SET "+ field + "= '" + value + "' " +
                    "WHERE lab_ResourceId =" + resourceID + "");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateInfoDvdInt(String field, int value, Connection connection){
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE dvd_dvd SET "+ field + "= '" + value + "' " +
                    "WHERE dvd_ResourceId =" + resourceID + "");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void checkDataBook(ActionEvent event){
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        if (!titleNew.getText().isEmpty()){
            updateInfoRes("res_Title", titleNew.getText(),connection);
            System.out.println(titleNew.getText());
        }
        if (!yearNew.getText().isEmpty()){
            updateInfoRes("res_Year", yearNew.getText(),connection);
        }
        if (!descNew.getText().isEmpty()){
            updateInfoRes("res_Description", descNew.getText(),connection);
        }
        if (!authorNew.getText().isEmpty()){
            updateInfoBok("bok_Author", authorNew.getText(),connection);
        }
        if (!publisherNew.getText().isEmpty()){
            updateInfoBok("bok_Publisher", publisherNew.getText(),connection);
        }
        if (!isbnNew.getText().isEmpty()){
            updateInfoBok("bok_ISBN", isbnNew.getText(),connection);
        }
        if (!genreNew.getText().isEmpty()){
            updateInfoBok("bok_Genre", genreNew.getText(),connection);
        }
        if (!languageNew.getText().isEmpty()){
            updateInfoBok("bok_Language", languageNew.getText(),connection);
        }
        JOptionPane.showMessageDialog(null, "Information updated successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        changeScene("/javafx/ManageResources.fxml", "ManageResourcesController", event);

    }

    public void checkDataDVD(ActionEvent event){
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        if (!titleNew.getText().isEmpty()){
            updateInfoRes("res_Title", titleNew.getText(),connection);
            System.out.println(titleNew.getText());
        }
        if (!yearNew.getText().isEmpty()){
            updateInfoRes("res_Year", yearNew.getText(),connection);
        }
        if (!descNew.getText().isEmpty()){
            updateInfoRes("res_Description", descNew.getText(),connection);
        }
        if (!directorNew.getText().isEmpty()){
            updateInfoDvd("dvd_Director", directorNew.getText(),connection);
        }
        if (!runtimeNew.getText().isEmpty()){
            updateInfoDvdInt("dvd_Runtime", Integer.parseInt(runtimeNew.getText()),connection);
        }
        if (!languageNew.getText().isEmpty()){
            updateInfoDvd("dvd_Language", languageNew.getText(),connection);
        }
        JOptionPane.showMessageDialog(null, "Information updated successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        changeScene("/javafx/ManageResources.fxml", "ManageResourcesController", event);

    }

    public void checkDataLaptop(ActionEvent event){
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        if (!titleNew.getText().isEmpty()){
            updateInfoRes("res_Title", titleNew.getText(),connection);
            System.out.println(titleNew.getText());
        }
        if (!yearNew.getText().isEmpty()){
            updateInfoRes("res_Year", yearNew.getText(),connection);
        }
        if (!descNew.getText().isEmpty()){
            updateInfoRes("res_Description", descNew.getText(),connection);
        }
        if (!modelNew.getText().isEmpty()){
            updateInfoLaptop("lab_Model", modelNew.getText(),connection);
        }
        if (!manNew.getText().isEmpty()){
            updateInfoLaptop("lab_Manufacturer", man.getText(),connection);
        }
        if (!osNew.getText().isEmpty()){
            updateInfoLaptop("lab_OS", os.getText(),connection);
        }
        JOptionPane.showMessageDialog(null, "Information updated successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        changeScene("/javafx/ManageResources.fxml", "ManageResourcesController", event);

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
            } else if (type == "ProfileController") {
                EditResourceController profileController=loader.getController();
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
