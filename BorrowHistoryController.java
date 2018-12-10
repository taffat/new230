/**
 * Implements borrow history for copies.
 * @author Jack Long (965615)
 * Copyright: No copyright
 * @version 0.1
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
import javafx.scene.control.TextField;
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

public class BorrowHistoryController implements Initializable {

	@FXML private TableView<BorrowHistoryTable> table;
	@FXML private TableColumn<BorrowHistoryTable, String> col_username;
	@FXML private TableColumn<BorrowHistoryTable, String> col_firstName;
	@FXML private TableColumn<BorrowHistoryTable, String> col_lastName;
	@FXML private TableColumn<BorrowHistoryTable, String> col_dateBorrowed;
	@FXML private TableColumn<BorrowHistoryTable, String> col_dateReturned;
	@FXML private Label lblStatus;
	@FXML private TextField search;
	private String username;
	
	ObservableList<BorrowHistoryTable> bhlist = FXCollections.observableArrayList();
	
	/**
     * Gets the username from the login controller.
     * @param userName the username of the logged in librarian.
     */
    public void getUsername(String usernameLib){
        username = usernameLib;
    }
 
    public void initialize(URL location, ResourceBundle resources) {
    	bhlist.clear();
        lblStatus.setText("");
		DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        int resCopyID = 1;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT use_Username, use_FirstName, use_LastName, bor_DateBorrowed, ret_DateReturned, bor_returned FROM use_user, bor_borrow, ret_return WHERE use_UserId = bor_UserId AND ret_BorrowId = bor_Id AND bor_Status = 'Returned' ORDER BY bor_dateBorrowed ASC;"); //AND ret_return.ret_BorrowId = bor_borrow.bor_ResourceCopyId                         
            ResultSet rs = stmt.executeQuery();
            	while (rs.next()) {
            		int retruned = Integer.parseInt(rs.getString("bor_returned"));
            		if (retruned == 1) {
            			bhlist.add(new BorrowHistoryTable(resCopyID, rs.getString("use_Username"), rs.getString("use_FirstName"), rs.getString("use_LastName"), rs.getString("bor_DateBorrowed"), rs.getString("ret_DateReturned")));
            			resCopyID++;
            		}
            	}
        } catch (Exception e) {
        	//TODO 
        }
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_dateBorrowed.setCellValueFactory(new PropertyValueFactory<>("dateBorrowed"));
        col_dateReturned.setCellValueFactory(new PropertyValueFactory<>("dateReturned"));
        table.setItems(bhlist);
        if (bhlist.isEmpty()) {
        	lblStatus.setText("There is no borrow history.");
            lblStatus.setTextFill(Color.RED);
        }
   }
    
	public void displayTable() {
		System.out.println("borrows");
		bhlist.clear();
		DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        int resCopyID = Integer.parseInt(search.getText());
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT use_Username, use_FirstName, use_LastName, bor_DateBorrowed, ret_DateReturned, bor_returned FROM use_user, bor_borrow, ret_return WHERE use_UserId = bor_UserId  AND ret_BorrowId = bor_Id AND bor_Status = 'Returned' AND bor_ResourceCopyId = " + resCopyID ); //AND ret_return.ret_BorrowId = bor_borrow.bor_ResourceCopyId             
            ResultSet rs = stmt.executeQuery();
            	while (rs.next()) {
            		int retruned = Integer.parseInt(rs.getString("bor_returned"));
            		if (retruned == 1) {
            			bhlist.add(new BorrowHistoryTable(resCopyID, rs.getString("use_Username"), rs.getString("use_FirstName"), rs.getString("use_LastName"), rs.getString("bor_DateBorrowed"), rs.getString("ret_DateReturned")));
            		}
            	}
        } catch (Exception e) {
        	//TODO 
        }
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_dateBorrowed.setCellValueFactory(new PropertyValueFactory<>("dateBorrowed"));
        col_dateReturned.setCellValueFactory(new PropertyValueFactory<>("dateReturned"));
        table.setItems(bhlist);
        if (bhlist.isEmpty()) {
        	lblStatus.setText("There is no borrow history for this copy.");
            lblStatus.setTextFill(Color.RED);
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
}
	


