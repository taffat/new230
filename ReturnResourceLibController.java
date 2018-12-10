import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class ReturnResourceLibController implements Initializable {

    @FXML
    private TableView<ReturnResourceTable> returnTable;
    @FXML
    private TableColumn<ReturnResourceTable, String> col_username;
    @FXML
    private TableColumn<ReturnResourceTable, String> col_resourcetitle;
    @FXML
    private TableColumn<ReturnResourceTable, String> col_overdue;
    @FXML
    private TableColumn<ReturnResourceTable, Integer> col_fine;
    @FXML
    private TableColumn<ReturnResourceTable, Integer> col_id;

    private String username;

    ObservableList<ReturnResourceTable> returnList = FXCollections.observableArrayList();

    public void getUsername(String userName){
        username = userName;
    }

    public void initialize(URL location, ResourceBundle resources) {

        // Establishes connection to database, allowing queries to be made.
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();

        // Creates an SQL statement which is sent to the connected database, and stores the results in a set.
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT ret_Id,use_Username, res_Title, IFNULL(fin_FineAmount, 0) AS fin_FineAmount, IFNULL(fin_DaysOverdue, 0) AS fin_DaysOverdue " +
                    "FROM res_resource INNER JOIN rcp_resourcecopy ON rcp_ResourceId = res_ResourceId " +
                    "INNER JOIN bor_borrow ON bor_ResourceCopyId = rcp_CopyId " +
                    "INNER JOIN use_user ON bor_UserId = use_UserId " +
                    "INNER JOIN ret_return ON bor_Id = ret_BorrowId " +
                    "LEFT JOIN fin_fine ON ret_return.ret_Id = fin_fine.fin_ReturnId WHERE ret_Status = 'Pending' ORDER BY fin_DaysOverdue ASC");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int overdue = rs.getInt(("fin_DaysOverdue"));
                if (overdue == 0) {
                    returnList.add(new ReturnResourceTable(rs.getString("use_Username"), rs.getString("res_Title"), "Not overdue", rs.getInt("fin_FineAmount"),rs.getInt("ret_Id")));
                } else {
                    returnList.add(new ReturnResourceTable(rs.getString("use_Username"), rs.getString("res_Title"), rs.getString("fin_DaysOverDue"), rs.getInt("fin_FineAmount"),rs.getInt("ret_Id")));
                }
            }

            returnTable.setOnMousePressed(event -> {
                if (event.getClickCount() == 2 && event.isPrimaryButtonDown()) {
                    int fine = returnTable.getSelectionModel().getSelectedItem().getFine();
                    int id = returnTable.getSelectionModel().getSelectedItem().getId();
                    String userName = returnTable.getSelectionModel().getSelectedItem().getUserName();
                    int response = JOptionPane.showConfirmDialog(null, "Would you like to authorize this return? (Yes to authorize, No to decline, Close to do nothing)?", "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.CLOSED_OPTION) {
                        System.out.println("No option clicked");
                    } else if(response == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(null, "Return request declined!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                        goToLibIssueDeskMouse(event);
                    } else if (response == JOptionPane.YES_OPTION) {
                        if (fine == 0) {
                            updateReturn(id);
                        }else{
                            updateReturn(id);
                            updateFine(id,fine,userName);
                        }
                        JOptionPane.showMessageDialog(null, "Return successfully approved!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                        goToLibIssueDeskMouse(event);
                    }
                }
            });


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Populate TableView cells  the results from the queries
        col_username.setCellValueFactory(new PropertyValueFactory<>("userName"));
        col_resourcetitle.setCellValueFactory(new PropertyValueFactory<>("resourceTitle"));
        col_overdue.setCellValueFactory(new PropertyValueFactory<>("overdue"));
        col_fine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        returnTable.setItems(returnList);

    }

    public void updateReturn(int retId){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("update ret_return set ret_Status = 'Approved' where ret_Id = "+ retId);
            stmt.executeUpdate();
            
            //now find requests and change oldest request to reservation (if any)
            if(checkForRequests(retId)) {
            	
            	PreparedStatement updateRequest = conn.prepareStatement("update bor_Status = 'Reserved' where bor_Id = " + getOldestRequest(retId));
            	updateRequest.executeQuery();
            	
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private String getOldestRequest(int retId) throws SQLException {
		
    	DatabaseManager db = new DatabaseManager();
    	Connection connection = db.getConnection();
    	
    	PreparedStatement getFirstRequest = connection.prepareStatement("SELECT bor_Id FROM bor_borrow WHERE bor_ResourceCopyId =" + getCopyId(retId) + "LIMIT 1");
    	ResultSet firstRequest = getFirstRequest.executeQuery();
    	
    	firstRequest.beforeFirst();
    	firstRequest.next();
		return firstRequest.getString("bor_Id");
	}

	private String getCopyId(int retId) throws SQLException {
		//connect to database
    	DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		PreparedStatement findCopyId = connection.prepareStatement("SELECT bor_ResourceCopyId FROM bor_borrow WHERE bor_Id =" + getBorrowId(retId));
		ResultSet copyId = findCopyId.executeQuery();
		
		copyId.beforeFirst();
		copyId.next();
		
		return copyId.getString("bor_ResourceCopyId");
	}

	private boolean checkForRequests(int retId) throws SQLException {
		//connect to database
    	DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		
		PreparedStatement checkRequests = connection.prepareStatement("SELECT * FROM bor_borrow WHERE bor_Id = " + getBorrowId(retId) + " AND bor_Status = 'Requested");
		
		ResultSet checkResult = checkRequests.executeQuery();
		
		checkResult.beforeFirst();
		checkResult.next();
		
		if(checkResult.next()) {
			return true;
		} else {
			return false;
		}		
	}

	private int getBorrowId(int retId) throws SQLException {
		//connect to database
    	DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		
		PreparedStatement getBorrowId = connection.prepareStatement("SELECT ret_BorrowId WHERE ret_Id =" + retId);
		ResultSet borrowId = getBorrowId.executeQuery();
		borrowId.beforeFirst();
		borrowId.next();
		return borrowId.getInt("ret_BorrowId");
		
	}


    public void updateFine(int retId,int fine,String username){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("update fin_fine set fin_Status = 'Approved' where fin_ReturnId = "+ retId);
            stmt.executeUpdate();
            PreparedStatement stmt2 = conn.prepareStatement("update use_user,ret_return,bor_borrow " +
                    "set use_Balance = use_Balance - "+fine+" where ret_Id = ret_BorrowId and ret_BorrowId = bor_Id and use_Username = '" +username+"'");
            stmt2.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


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


}
