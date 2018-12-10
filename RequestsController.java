/**
 * Implements the Requesting Items interface.
 * @author Matt Ashman
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RequestsController implements Initializable {

    @FXML private TableView<RequestTable> table;
    @FXML private TableColumn<RequestTable,String> col_title;
    @FXML private TableColumn<RequestTable,String> col_available;
    @FXML private TableColumn<RequestTable,String> col_year;
    @FXML private Label lblStatus;
    private String username;


    ObservableList<RequestTable> oblist = FXCollections.observableArrayList();

    /**
     * Gets the username from the login controller.
     * @param userName the username of the logged in user. 
     */
    public void getUsername(String userName){
        username = userName;
        displayTable();
    }

    /**
     * Method to obtain the UserID from their username.
     * @return User's UserID
     */
    public String getUserID(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        String userID = "";
        try {
            PreparedStatement stmt = conn.prepareStatement("select use_UserId from use_user where use_Username = '" + username + "'");
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
     * Displays a table of the User's requested items if they have one, otherwise displays a message.
     */
    public void displayTable(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        String userID = getUserID();
        try {
            PreparedStatement stmt = conn.prepareStatement("select * from bor_borrow,res_resource where bor_UserId = "+ userID );
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                while (rs.next()) {
                    oblist.add(new RequestTable(rs.getString("res_Title"),rs.getString("res_Year"),rs.getString("bor_DateDue")));
                }
            }else{
                lblStatus.setText("You are not currently requesting any items.");
                lblStatus.setTextFill(Color.RED);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        col_available.setCellValueFactory(new PropertyValueFactory<>("available"));
        table.setItems(oblist);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Changes scene to User dashboard.
     * @param event event An action (button click) has taken place.
     */
    public void goToUserDash(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/javafx/UserDashboard.fxml"));

            Parent homeWindow = loader.load();
            Scene borrowedItems = new Scene(homeWindow);

            DashboardController dashboardController=loader.getController();
            dashboardController.getUsername(username);
            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(borrowedItems);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
