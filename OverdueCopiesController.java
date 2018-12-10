/**
 * Implements the viewing of overdue copies.
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

public class OverdueCopiesController implements Initializable {
	
	@FXML private TableView<OverdueCopiesTable> table;
	@FXML private TableColumn<OverdueCopiesTable, String> col_username;
	@FXML private TableColumn<OverdueCopiesTable, String> col_bookTitle;
	@FXML private TableColumn<OverdueCopiesTable, String> col_daysOverdue;
	@FXML private Label lblStatus;
	@FXML private TextField search;
	private String username;
	
	ObservableList<OverdueCopiesTable> overlist = FXCollections.observableArrayList();

	
	/**
     * Gets the username from the login controller.
     * @param userName the username of the logged in librarian.
     */
	public void getUsername(String usernameLib){
        username = usernameLib;
	}
	
	public void initialize(URL location, ResourceBundle resources) {
			DatabaseManager db = new DatabaseManager();
	        Connection conn = db.getConnection();
	        try {
	            PreparedStatement stmt = conn.prepareStatement("SELECT res_Title, use_Username, IF ((DATEDIFF(NOW(), bor_DateDue) > 0), DATEDIFF(NOW(), bor_DateDue), 1 ) AS DaysOverDue  FROM bor_borrow,rcp_resourcecopy, res_resource,use_user WHERE bor_borrow.bor_ResourceCopyId = rcp_resourcecopy.rcp_CopyId  AND rcp_resourcecopy.rcp_ResourceId = res_resource.res_ResourceId AND use_user.use_UserId = bor_borrow.bor_UserId AND bor_DateDue < NOW()  AND bor_returned = 0; ");
	            ResultSet rs = stmt.executeQuery();
	            	while (rs.next()) {     		
	            		overlist.add(new OverdueCopiesTable(rs.getString("use_Username"), rs.getString("res_title"), rs.getString("DaysOverDue"))); 
	            	}
	        } catch (Exception e) {
	        	//TODO 
	        }
	        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
	        col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
	        col_daysOverdue.setCellValueFactory(new PropertyValueFactory<>("daysOverdue"));
	        table.setItems(overlist);
	        if (overlist.isEmpty()) {
	        	lblStatus.setText("There are no overdue copies.");
	            lblStatus.setTextFill(Color.RED);
	        }
	}
	
	public void displayTable() {
		overlist.clear();
		lblStatus.setText("");
		DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        String searchUsername = search.getText();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT res_Title, use_Username, IF ((DATEDIFF(NOW(), bor_DateDue) > 0), DATEDIFF(NOW(), bor_DateDue), 1 ) AS DaysOverDue  FROM bor_borrow,rcp_resourcecopy, res_resource,use_user WHERE bor_borrow.bor_ResourceCopyId = rcp_resourcecopy.rcp_CopyId  AND rcp_resourcecopy.rcp_ResourceId = res_resource.res_ResourceId AND use_user.use_UserId = bor_borrow.bor_UserId AND bor_DateDue < NOW()  AND bor_returned = 0 AND use_user.use_Username = '" + searchUsername + "'");
            ResultSet rs = stmt.executeQuery();
            	while (rs.next()) { 
            			overlist.add(new OverdueCopiesTable(rs.getString("use_Username"), rs.getString("res_title"), rs.getString("DaysOverDue"))); 
            	}
        } catch (Exception e) {
        	//TODO 
        }  
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_daysOverdue.setCellValueFactory(new PropertyValueFactory<>("daysOverdue"));
        table.setItems(overlist);
        if (overlist.isEmpty()) {
        	lblStatus.setText("There are no overdue copies for user: " + searchUsername);
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
