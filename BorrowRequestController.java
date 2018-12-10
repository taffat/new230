/**
 * Implements borrow request authorisation table.
 * @author Samuel Dobbie (966537)
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class BorrowRequestController implements Initializable {

	@FXML private TableView<BorrowRequestTable> table;
	@FXML private TableColumn<BorrowRequestTable, String> resourceId;
	@FXML private TableColumn<BorrowRequestTable, String> resourceTitle;
	@FXML private TableColumn<BorrowRequestTable, String> username;
	@FXML private TableColumn<BorrowRequestTable, String> availableCopies;
	@FXML private TableColumn<BorrowRequestTable, String> resourceType;
	private int resId;
	
	ObservableList<BorrowRequestTable> brlist = FXCollections.observableArrayList();
	
    public void initialize(URL location, ResourceBundle resources) {
		DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        try {
        	PreparedStatement stmtOne = conn.prepareStatement("SELECT bor_Id,\r\n" + 
        			"		 res_Title,\r\n" + 
        			"		 use_Username,\r\n" + 
        			"		 (SELECT COUNT(rcp_resourcecopy.rcp_CopyId)\r\n" + 
        			"			FROM rcp_resourcecopy\r\n" + 
        			"			WHERE rcp_ResourceId = res_ResourceId   ) AS res_CopyCount ,\r\n" + 
        			"		 CASE WHEN res_ResourceType = 0 THEN 'Book'\r\n" + 
        			"            WHEN res_ResourceType = 0 THEN 'DVD'\r\n" + 
        			"				ELSE 'Laptop' END AS res_ResourceType		  \r\n" + 
        			"FROM use_user, bor_borrow, rcp_resourcecopy, res_resource \r\n" + 
        			"WHERE bor_UserId = use_UserId\r\n" + 
        			"AND bor_ResourceCopyId = rcp_CopyId\r\n" + 
        			"AND rcp_ResourceId = res_ResourceId\r\n" + 
        			"AND bor_borrow.bor_Status = 'Pending';");                        
            ResultSet rsOne = stmtOne.executeQuery();
            	while (rsOne.next()) {
            		brlist.add(new BorrowRequestTable(rsOne.getInt("bor_Id"), rsOne.getString("res_Title"), rsOne.getString("use_Username"), rsOne.getInt("res_CopyCount"), rsOne.getString("res_ResourceType")));
            	}
        } catch (Exception e) {
        	//TODO 
        }
        resourceId.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        resourceTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        availableCopies.setCellValueFactory(new PropertyValueFactory<>("numberAvailable"));
        resourceType.setCellValueFactory(new PropertyValueFactory<>("type"));
        table.setItems(brlist);
        
        table.setOnMousePressed(event -> {
			if (event.getClickCount() == 2 && event.isPrimaryButtonDown() ){
				int borrowId = table.getSelectionModel().getSelectedItem().getBorrowId();

		        try {
		        	int response = JOptionPane.showConfirmDialog(null, "Would you like to authorize the borrowing of this resource (Yes to authorize, No to decline, Close to do nothing)?", "Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.CLOSED_OPTION) {
						System.out.println("No option clicked");

					} else if(response == JOptionPane.NO_OPTION) {
						//if librarian has declined
						JOptionPane.showMessageDialog(null, "Payment successfully declined!", "Success!", JOptionPane.INFORMATION_MESSAGE);
					} else if (response == JOptionPane.YES_OPTION) {
						//if librarian has confirmed
						PreparedStatement stmtTwo = conn.prepareStatement("UPDATE bor_borrow SET bor_Status = 'On Loan' WHERE bor_Id = " + borrowId);                        
						stmtTwo.executeUpdate();
						goToLibIssueDeskMouse(event);
					}
		        } catch (Exception e) {
		        	//TODO 
		        }

			}
		});
		
   }
	
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
}
	


