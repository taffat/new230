/**
 * Implements the Transaction history interface.
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

public class TransactionController implements Initializable {

    @FXML private TableView<FineTable> fineTable;
    @FXML private TableColumn<ReserveTable,String> fin_Date;
    @FXML private TableColumn<ReserveTable,String> fin_FineAmount;
    @FXML private TableColumn<ReserveTable,String> fin_DaysOverdue;
    
    @FXML private TableView<PaymentTable> paymentTable;
    @FXML private TableColumn<ReserveTable,String> pay_Date;
    @FXML private TableColumn<ReserveTable,String> pay_Amount;
    
    @FXML public Label balance;
    
    private String username;

    ObservableList<FineTable> fineList = FXCollections.observableArrayList();
    ObservableList<PaymentTable> paymentList = FXCollections.observableArrayList();

    /**
     * Gets the username from the login controller.
     * @param userName the username of the logged in user.
     */
    public void getUsername(String userName){
        username = userName;
        displayTable();
        displayBalance();
    }

    public void displayBalance(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT use_Balance FROM use_user WHERE use_Username = '"+ username +"'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String amount = rs.getString("use_Balance");
                balance.setText("£" + amount);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
        
    /**
     * Displays a table of the User's transaction history if they have one, otherwise displays a message. 
     */
    public void displayTable(){
        
    	DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT DATE_FORMAT(ret_DateReturned,\"%d/%c/%Y %H:%i\") AS fin_Date,\r\n" + 
            		"		 fin_FineAmount,\r\n" + 
            		"		 fin_DaysOverdue\r\n" + 
            		"FROM fin_fine, ret_return, bor_borrow, use_user\r\n" + 
            		"WHERE fin_ReturnId = ret_Id\r\n" + 
            		"AND ret_BorrowId = bor_Id\r\n" + 
            		"AND bor_UserId = use_UserId\r\n" + 
            		"AND use_Username = '" + username +
            		"' ORDER BY fin_Id DESC;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
               	fineList.add(new FineTable(rs.getString("fin_Date"),rs.getInt("fin_FineAmount"),rs.getInt("fin_DaysOverDue")));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fin_Date.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        fin_FineAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        fin_DaysOverdue.setCellValueFactory(new PropertyValueFactory<>("days"));
        fineTable.setItems(fineList);
        
        try {
            PreparedStatement stmt2 = conn.prepareStatement("SELECT	DATE_FORMAT(pay_Date,\"%d/%c/%Y %H:%i\") AS pay_Date,\r\n" + 
            		"         pay_Amount\r\n" + 
            		"FROM pay_payment, use_user\r\n" + 
            		"WHERE pay_UserId = use_UserId\r\n" + 
            		"AND use_Username = '" + username + 
            		"' ORDER BY pay_Id DESC;");
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                paymentList.add(new PaymentTable(rs2.getString("pay_Date"),rs2.getInt("pay_Amount"))); 
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pay_Date.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        pay_Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentTable.setItems(paymentList);
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
