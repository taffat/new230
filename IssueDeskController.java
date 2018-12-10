/**
 * To enable users to request resources.
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version 1.0
 */

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IssueDeskController {

	@FXML private TextField paymentAmount;
	@FXML private Label statusLabel;
	private String username;
	private int resourceId;
	private int copyId;
	private String task;
	
	@FXML
	private TableView <BookTable> paymentRequestsTable;
	
	@FXML
	private TableColumn<BookTable, String> col_username;
	
	@FXML
	private TableColumn<BookTable, String> col_amountOwed;
	
	@FXML
	private TableColumn<BookTable, String> col_paymentAmount;
	
	@FXML
	private Text confirmationMessage;
	
	private int borrowId;
	
	/*
	 *  implements Initializable
	 * 
	ObservableList <BookTable> bookList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Assigns the values of the queries to the TableView.
		col_username.setCellValueFactory(new PropertyValueFactory<>("use_Username"));
		col_amountOwed.setCellValueFactory(new PropertyValueFactory<>("use_Balance"));
		col_paymentAmount.setCellValueFactory(new PropertyValueFactory<>("pay_Amount"));
		
		// Establishes connection to database, allowing queries to be made.
		DatabaseManager db = new DatabaseManager();
		Connection DbCon = db.getConnection();
		
		// Creates an SQL statement which is sent to the connected database, and stores the results in a set.
		try {
			 PreparedStatement infoRetrieval = DbCon.prepareStatement("select use_Username, use_Balance, pay_Amount from use_user, pay_payments where pay_Status = 'pending' ");
			 ResultSet bookInfo = infoRetrieval.executeQuery();
			 if (bookInfo.next()){
			    while (bookInfo.next()) {
			         bookList.add(new BookTable(bookInfo.getInt("bok_ResourceId"), bookInfo.getString("bok_Author"),bookInfo.getString("bok_Genre"),bookInfo.getString("bok_ISBN"), bookInfo.getString("bok_Language"), 
			        		 bookInfo.getString("res_Title"), bookInfo.getString("bok_Publisher")));
			    }
			 } else {
			       lblStatus.setText("Resources not found");
			       lblStatus.setTextFill(Color.RED);
			 }
		} catch (SQLException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		} catch (Exception e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		}
		
		// Populate TableView cells  the results from the queries
		col_ResourceId.setCellValueFactory(new PropertyValueFactory<>("ResourceId"));
		col_Author.setCellValueFactory(new PropertyValueFactory<>("Author"));
		col_Genre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
		col_ISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		col_Language.setCellValueFactory(new PropertyValueFactory<>("Language"));
		col_Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		col_Publisher.setCellValueFactory(new PropertyValueFactory<>("Publisher"));
		tblBook.setItems(bookList);
	}*/
	
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    
    public void setConfirmationMessage(String title) {
    	confirmationMessage.setText("Are you sure you want to borrow '" + title + "'?");
    }
    
    public void setCopyId(int copyId) {
    	System.out.println("issue desk " + copyId);
    	this.copyId = copyId;
    }
    
    @FXML
    public void authorizePayment(ActionEvent event) {
    	DatabaseManager db = new DatabaseManager();
   		Connection connection = db.getConnection();
        PreparedStatement stmt;
   		try {
   			stmt = connection.prepareStatement("UPDATE use_user SET use_Balance = use_Balance + " + paymentAmount.getText() + " WHERE use_Username = '" + username + "'");
   			stmt.executeUpdate();
   			JOptionPane.showMessageDialog(null, "Payment noted, the Librarian will approve it soon.", "Success!", JOptionPane.INFORMATION_MESSAGE);
            goToUserIssueDesk(event);
   		} catch (SQLException e) {
   			e.printStackTrace();
   		}
    }
    
    @FXML
    public void authorizeBorrow(ActionEvent event) {
    	DatabaseManager db = new DatabaseManager();
   		Connection connection = db.getConnection();
        PreparedStatement stmt;
   		try {
   			stmt = connection.prepareStatement("UPDATE bor_borrow SET bor_Status = 'On Loan' WHERE bor_Id = " + borrowId);
   			stmt.executeUpdate();
   			JOptionPane.showMessageDialog(null, "Payment noted, the Librarian will approve it soon.", "Success!", JOptionPane.INFORMATION_MESSAGE);
            goToUserIssueDesk(event);
   		} catch (SQLException e) {
   			e.printStackTrace();
   		}
    }
    
    // LIB BORROW AUTH: PreparedStatement stmt = connection.prepareStatement("INSERT INTO bor_borrow VALUES (NULL, " + userId + ", " + resourceId + ", " + timestamp + ", NULL, 0)");
	
	@FXML
	public void requestResource(ActionEvent event) {
		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		
		try {
			PreparedStatement setCopyRequested = connection.prepareStatement("INSERT INTO bor_borrow (bor_Id , "
					+ "bor_UserId ,bor_ResourceCopyId , bor_DateBorrowed, bor_DateDue, bor_returned, bor_Status) "
					+ "VALUES (NULL, " + getUserId(connection) + ", "+ resourceId +", NOW(), NULL, 0, 'Requested')");
			setCopyRequested.execute();
			JOptionPane.showMessageDialog(null, "Resource has been requested", "Success!", JOptionPane.INFORMATION_MESSAGE);
			goToUserDash(event);			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
    
    
	@FXML
	public void borrowResource(ActionEvent event) {
		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		System.out.println(copyId);
		try {
			int userId = getUserId(connection);
			Timestamp timestamp = getCurrentTimestamp();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO bor_borrow VALUES (NULL, " + userId + ", " + copyId + ", '" + timestamp + "', NULL, 0, 'Pending')");
			stmt.executeUpdate();
			 JOptionPane.showMessageDialog(null, "Borrow request noted, a librarian will handle your request soon.", "Success!", JOptionPane.INFORMATION_MESSAGE);
			goToUserDash(event);
		} catch (SQLException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		} catch (Exception e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		}
	}
	
	@FXML
	public void returnResource() {
		System.out.println("You want to return a resource");
	}
	
	@FXML
	public void addMoney() {
	}
	
	
	/*
	 
	 DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        PreparedStatement stmt;
        ResultSet result;
		try {
			stmt = connection.prepareStatement("SELECT use_Balance FROM use_user WHERE use_Username = '" + username + "'");
			result = stmt.executeQuery();
			int userBalance = -1;
	        while (result.next()) {
	        	userBalance = result.getInt(1);
	        }
	        if (userBalance >= 0) {
	        	statusLabel.setText("You don't owe any money!");
	        	// Hide field and button
	        } else {
	        	statusLabel.setText("You owe " + userBalance + ". Enter between 0.01 and " + userBalance);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//statusLabel.setText();
	 
	 */
	
	
	private int getUserId(Connection connection) {
		PreparedStatement stmtTwo;
		try {
			stmtTwo = connection.prepareStatement("SELECT use_UserId FROM use_user WHERE use_Username = '" + StaticUserInfo.getUsername() + "'");
			
			ResultSet result = stmtTwo.executeQuery();
			int userId = 0;
			while (result.next()) {
			   userId = result.getInt(1);
			}
			return userId;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	@FXML
	public void payFine(ActionEvent event) throws ParseException {
		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT use_Balance FROM use_user WHERE use_Username = '" + StaticUserInfo.getUsername() + "'");
			ResultSet rs = stmt.executeQuery();
			
			int userBalance = 0;
			while(rs.next()) {
				userBalance = rs.getInt(1);
			}	
			
			if (userBalance >= 0) {
				JOptionPane.showMessageDialog(null, "You don't owe any money!", "Unsuccessful!", JOptionPane.INFORMATION_MESSAGE);
	            goToUserIssueDesk(event);
			} else {
				// Get user ID
				if (Float.parseFloat(paymentAmount.getText()) > (userBalance *-1)) {
					JOptionPane.showMessageDialog(null, "You're trying to pay more than you owe!", "Unsuccessful!", JOptionPane.INFORMATION_MESSAGE);
				} else if (Float.parseFloat(paymentAmount.getText()) < 0.01) {
					JOptionPane.showMessageDialog(null, "You're trying to pay too small an amount!", "Unsuccessful!", JOptionPane.INFORMATION_MESSAGE);
				} else {
					int userId = getUserId(connection);
					System.out.println(userId);
					Timestamp timestamp = getCurrentTimestamp();
					PreparedStatement stmtTwo = connection.prepareStatement("INSERT INTO pay_payment VALUES (NULL, " + userId + ", '" + timestamp + "', " + paymentAmount.getText() + ", 'Pending')");
					stmtTwo.executeUpdate();
					JOptionPane.showMessageDialog(null, "Payment noted, the Librarian will approve it soon.", "Success!", JOptionPane.INFORMATION_MESSAGE);
		            goToUserIssueDesk(event);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void goToPaymentRequests(ActionEvent event) {
		changeScene("/javafx/IssueDeskPaymentRequests.fxml", "IssueDeskPayController", event);
	}
	
	public void goToPayFine(ActionEvent event) {
		changeScene("/javafx/IssueDeskPayFine.fxml", "IssueDeskController", event);
	}
	
	@FXML
	public void borrowRequests(ActionEvent event) {
		changeScene("/javafx/IssueDeskBorrowRequests.fxml", "BorrowRequestController", event);
	}
	
	@FXML
	public void returnRequests(ActionEvent event) {
		changeScene("/javafx/IssueDeskReturnRequests.fxml", "", event);
	}
	
	@FXML
	 public void goToUserDash(ActionEvent event){
		changeScene("/javafx/UserDashboard.fxml", "DashboardController", event);
    }
	
	@FXML
	 public void goToLibDash(ActionEvent event){
		changeScene("/javafx/LibrarianDashboard.fxml", "LibrarianController", event);
   }
	
	@FXML
	public void goToUserIssueDesk(ActionEvent event){
		changeScene("/javafx/IssueDeskUser.fxml", "IssueDeskController", event);
	}
	
	@FXML
	public void goToLibIssueDesk(ActionEvent event){
		changeScene("/javafx/IssueDeskLibrarian.fxml", "IssueDeskController", event);
	}
	
	@FXML
	public void goToBrowse(ActionEvent event){
		changeScene("/javafx/ResourceViewsTemplate.fxml", "ResourceController", event);
	}
	
	@FXML
	public void goToReturnResource(ActionEvent event) {
		System.out.println(username);
		changeScene("/javafx/ReturningResource.fxml", "IssueDeskReturnController", event);
	}
	
	@FXML
	public void changeScene(String sceneName, String type, ActionEvent event) {
		try {
	           FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(getClass().getResource(sceneName));

	           Parent homeWindow = loader.load();
	           Scene borrowedItems = new Scene(homeWindow);


		        if (type == "LibrarianController") {
		        	LibrarianController controller=loader.getController();
		        	controller.getUsername(username);
		        } else if (type == "DashboardController") {
		        	DashboardController controller = loader.getController();
		        	controller.getUsername(username);
		        } else if (type == "AccountCreationController") {
		        	AccountCreationController controller = loader.getController();
		        	controller.getUsername(username);
		        } else if (type == "ReservedController") {
		        	ReservedController controller=loader.getController();
		        	controller.getUsername(username);
		        } else if (type == "RequestsController") {
		        	RequestsController controller = loader.getController();
		        	controller.getUsername(username);
		        } else if (type == "BorrowingController") {
		        	BorrowingController controller = loader.getController();
		        	controller.getUsername(username);
		        } else if (type == "IssueDesksController") {
		        	IssueDeskController controller = loader.getController();
		        	controller.setUsername(username);
		        } else if (type == "IssueDeskReturnController") {
		        	ReturningResourceController controller = loader.getController();
		        	controller.getUsername(username);
		        } else if (type == "ResourceController") {
		        	ResourceController controller = loader.getController();
		        	controller.getUsername(username);
		        }
	           
	           // Get the stage
	           Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
	           window.setScene(borrowedItems);
	           window.show();

	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	}
	
	public Timestamp getCurrentTimestamp() {
		String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = format.parse(currentTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Timestamp(date.getTime());
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
}
