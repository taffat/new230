
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CopyTableController {
	
	@FXML private Text copyTitle;
	@FXML private TableColumn <ResourceCopyTable, Integer> copyId;
	@FXML private TableColumn<ResourceCopyTable, Integer> days;
	@FXML private TableColumn<ResourceCopyTable, String> copyStatus;
	@FXML private TableView <ResourceCopyTable> tblResources;
	
	ObservableList <ResourceCopyTable> resourceList = FXCollections.observableArrayList();
	
	private String username;
	private String title;
	private int resourceId;
	private int copyNumber;
	
	public void setCopyTitle(String resourceName) {
		title = resourceName;
		copyTitle.setText("All copies for " + resourceName);
	}
	
    public void setUsername(String username){
        this.username = username;
    }
    
    public void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }

	public void createTable() {
	
		// Establishes connection to database, allowing queries to be made.
		DatabaseManager db = new DatabaseManager();
		Connection DbCon = db.getConnection();
		
		// Creates an SQL statement which is sent to the connected database, and stores the results in a set.
		try {
			 PreparedStatement stmt = DbCon.prepareStatement("SELECT rcp_CopyId, rcp_loanDuration, rcps_ResourceCopyStatus FROM rcp_resourcecopy, rcps_resourcecopystatus WHERE rcp_Status = rcps_ResourceCopyStatusId AND rcp_ResourceId = " + resourceId);
			 ResultSet resourceInfo = stmt.executeQuery();
			    while (resourceInfo.next()) {
			         resourceList.add(new ResourceCopyTable(resourceInfo.getInt("rcp_CopyId"), resourceInfo.getInt("rcp_loanDuration"), resourceInfo.getString("rcps_ResourceCopyStatus")));
			    }

		} catch (SQLException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		} catch (Exception e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		}
		
		copyId.setCellValueFactory(new PropertyValueFactory<>("copyId"));
		days.setCellValueFactory(new PropertyValueFactory<>("days"));
		copyStatus.setCellValueFactory(new PropertyValueFactory<>("copyStatus"));
		tblResources.setItems(resourceList);
		
		tblResources.setOnMousePressed(event -> {
			if (event.getClickCount() == 2 && event.isPrimaryButtonDown() ){
				String availability = tblResources.getSelectionModel().getSelectedItem().getCopyStatus();
				this.copyNumber = tblResources.getSelectionModel().getSelectedItem().getCopyId();
				if (availability.equals("Available")) {
					try {
						PreparedStatement stmtTwo = DbCon.prepareStatement("SELECT use_Balance FROM use_User WHERE use_Username = '" + StaticUserInfo.getUsername() + "'");
						ResultSet rsTwo = stmtTwo.executeQuery();
						
						PreparedStatement stmtThree = DbCon.prepareStatement("SELECT * FROM bor_borrow, use_user WHERE bor_UserId = use_UserId AND bor_DateDue < NOW() AND bor_Status = 'On Loan' AND use_Username = '" + StaticUserInfo.getUsername() + "'");
						ResultSet rsThree = stmtThree.executeQuery();
						
						boolean hasOverdue = false;
						if (rsThree.next()) {
							hasOverdue = true;
						}
						
						int userBalance = -1;
						while(rsTwo.next()) {
							userBalance = rsTwo.getInt(1);
						}
						if (userBalance >= 0 && hasOverdue == false) {
							changeSceneAvailableResource(event);
						} else {
				   			JOptionPane.showMessageDialog(null, "You currently have outstanding fine(s). Please resolve these before trying to borrow.", "Unsuccessful!", JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (availability.equals("Reserved")) {
					//do nothing
				} else if (availability.equals("On Loan")) {
					Connection connection = db.getConnection();
					
					try {
						PreparedStatement setCopyRequested = connection.prepareStatement("INSERT INTO bor_borrow (bor_Id , "
								+ "bor_UserId ,bor_ResourceCopyId , bor_DateBorrowed, bor_DateDue, bor_returned, bor_Status) "
								+ "VALUES (NULL, " + getUserId(connection) + ", "+ resourceId +", NOW(), NULL, 0, 'Requested')");
						setCopyRequested.execute();
						
						PreparedStatement setDueDate = connection.prepareStatement("UPDATE bor_borrow SET bor_DateDue = '" + getDueDate() 
							+ "' WHERE bor_ResourceCopyId = " + copyNumber + " AND bor_Status = 'On Loan'");
						setDueDate.execute();
						JOptionPane.showMessageDialog(null, "Resource has been requested", "Success!", JOptionPane.INFORMATION_MESSAGE);
						//goToUserDash(event);			
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private String getDueDate() throws SQLException {
		
		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List<Date> dates = new ArrayList<Date>();
		
		
		PreparedStatement getIssueDate = connection.prepareStatement("SELECT bor_DateBorrowed FROM bor_borrow WHERE bor_ResourceCopyId = " + copyNumber + " AND bor_Status = 'On Loan'");
		
		
		//here is where the bug is
		ResultSet issueDate = getIssueDate.executeQuery();
		
		issueDate.beforeFirst();
		issueDate.next();
		String issueDateString = issueDate.getString("bor_DateBorrowed");
		
		
		//add tomorrow to the array of dates
		dates.add(getTomorrow(format));
		
		
		//add end of loan period to array
		dates.add(getLoanDurationEnd(format, issueDateString));
		
		
		
		//return latest date in array
		return format.format(Collections.max(dates));
	}

	private Date getLoanDurationEnd(DateFormat format, String issueDate) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(issueDate));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		c.add(Calendar.DATE, 1);  
				
		return c.getTime();
		
		
	}

	private Date getTomorrow(DateFormat format) {
		String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(currentTime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		c.add(Calendar.DATE, 1);  
				
		return c.getTime();
		
		
		
	}

	public void changeSceneAvailableResource(MouseEvent event) {
		changeScene("/javafx/IssueDeskBorrowConfirmation.fxml", "IssueDeskController", event);
	}
	
	public void goToBrowse(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/resourceViewsTemplate.fxml"));
			
            Parent browseScene = loader.load();
            Scene browse = new Scene(browseScene);
			
			// Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
			window.setScene(browse);
			window.show();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
    
	@FXML
	public void changeScene(String sceneName, String type, MouseEvent event) {
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
		        } else if (type.equals("IssueDeskController")) {
		        	IssueDeskController controller = loader.getController();
		        	controller.setUsername(username);
		        	controller.setConfirmationMessage(title);
		        	controller.setResourceId(resourceId);
		        	System.out.println("copy no in copytable " + copyNumber);
		        	controller.setCopyId(copyNumber);
		        } else if (type == "IssueDeskPayController"){

				}
	           
	           // Get the stage
	           Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
	           window.setScene(borrowedItems);
	           window.show();

	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	}
	
	private int getUserId(Connection connection) {
		PreparedStatement stmtTwo;
		try {
			stmtTwo = connection.prepareStatement("SELECT use_UserId FROM use_user WHERE use_Username = '" + username + "'");
			
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
	
}
