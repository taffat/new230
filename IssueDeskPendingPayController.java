/**
 * bookController.java
 * Implements the filter: book function, allowing users to filter resources by type book, contact database and display results in a table
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.9
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class IssueDeskPendingPayController implements Initializable {

	//Variables for bookController's JavaFX objects
	@FXML
	private TableView <PendingPaymentTable> payTable;
	@FXML
	private TableColumn<PendingPaymentTable, String> col_username;
	@FXML
	private TableColumn<PendingPaymentTable, Float> col_balance;
	@FXML
	private TableColumn<PendingPaymentTable, Float> col_amount;
	@FXML
	private TableColumn<PendingPaymentTable, String> col_status;
	ObservableList <PendingPaymentTable> payList = FXCollections.observableArrayList();


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Establishes connection to database, allowing queries to be made.
		DatabaseManager db = new DatabaseManager();
		Connection conn = db.getConnection();

		// Creates an SQL statement which is sent to the connected database, and stores the results in a set.
		try {
			PreparedStatement stmt = conn.prepareStatement("select pay_Amount, pay_Status, use_Username, use_Balance from pay_payment, use_user where pay_UserId = use_UserId and pay_Status = 'Pending'" );
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				payList.add(new PendingPaymentTable(rs.getString("use_Username"),rs.getFloat("use_Balance"),rs.getFloat("pay_Amount"),rs.getString("pay_Status")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Populate TableView cells  the results from the queries
		col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
		col_balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
		col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

		payTable.setItems(payList);


		payTable.setOnMousePressed(event -> {
			if (event.getClickCount() == 2 && event.isPrimaryButtonDown() ){
				String username = payTable.getSelectionModel().getSelectedItem().getUsername();
				int userID = getUserID(username);
				String status ="";
				//yes/no confirmation
				int response = JOptionPane.showConfirmDialog(null, "Would you like to authorize this payment (Yes to authorize, No to decline, Close to do nothing)?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CLOSED_OPTION) {
					System.out.println("No option clicked");

				} else if(response == JOptionPane.NO_OPTION) {
					//if librarian has declined
					status = "Declined";
					updateStatus(userID,status);
					JOptionPane.showMessageDialog(null, "Payment successfully declined!", "Success!", JOptionPane.INFORMATION_MESSAGE);
					goToLibIssueDeskMouse(event);
				} else if (response == JOptionPane.YES_OPTION) {
					//if librarian has confirmed then...
					System.out.println("Yes button clicked");
					try {
						PreparedStatement stmt = conn.prepareStatement("select pay_Amount, use_Balance from pay_payment, use_user where pay_UserId = use_UserId and use_UserId = "+userID );
						ResultSet rs = stmt.executeQuery();
						float userAmount = 0;
						float userBalance = 0;
						while (rs.next()) {
							userAmount = rs.getFloat("pay_Amount");
							userBalance = rs.getFloat("use_Balance");
							System.out.println(userAmount);
							System.out.println(userBalance);
						}
						userBalance = userBalance + userAmount;
						updateBalance(userBalance,userID);
						status = "Approved";
						updateStatus(userID,status);
						JOptionPane.showMessageDialog(null, "Payment successfully authorized!", "Success!", JOptionPane.INFORMATION_MESSAGE);
						goToLibIssueDeskMouse(event);
					} catch (SQLException ee) {
						// TODO Auto-generated catch block
						ee.printStackTrace();
					} catch (Exception ee) {
						// TODO Auto-generated catch block
						ee.printStackTrace();
					}
				}
			}
		});


	}

	public void updateBalance(Float balance, int userID){
		DatabaseManager db = new DatabaseManager();
		Connection conn = db.getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement("update use_user set use_Balance = 0 where use_UserId = "+ StaticUserInfo.getUserId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateStatus(int userID, String status){
		DatabaseManager db = new DatabaseManager();
		Connection conn = db.getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement("update pay_payment set pay_Status = '"+status+"' where pay_UserId = "+ userID);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Changes scene to Lib issue desk.
	 * @param event event An action (button click) has taken place.
	 */
	public void goToLibIssueDesk(ActionEvent event){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/IssueDeskLibrarian.fxml"));

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
	 * Changes scene to Lib issue desk.
	 * @param event event An action (button click) has taken place.
	 */
	public void goToLibIssueDeskMouse(MouseEvent event){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/IssueDeskLibrarian.fxml"));

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
	 * Method to obtain the UserID from their username.
	 * @return User's UserID
	 */
	public Integer getUserID(String username){
		DatabaseManager db = new DatabaseManager();
		Connection conn = db.getConnection();
		int userID =0;
		try {
			PreparedStatement stmt = conn.prepareStatement("select use_UserId from use_user where use_Username = '" + username + "'");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				userID = rs.getInt("use_UserId");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userID;
	}

}
