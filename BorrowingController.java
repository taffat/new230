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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BorrowingController implements Initializable {

    @FXML private TableView<BorrowTable> table;
    @FXML private TableColumn<BorrowTable,String> col_title;
    @FXML private TableColumn<BorrowTable,String> col_dueDate;
    @FXML private TableColumn<BorrowTable,String> col_year;
    @FXML private TableColumn<BorrowTable,String> col_resourcetype;
    @FXML private Label lblStatus;
    @FXML private TextField searchBox;
    private String username;


    ObservableList<BorrowTable> oblist = FXCollections.observableArrayList();


    // Why is this get (same name in other methods too)?
    public void getUsername(String userName){
        username = userName;
        displayTable();
    }

    //Method to obtain the UserID from their username.
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

    public void displayTable(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        String userID = getUserID();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT  res_Title, res_Year, res_ResourceType , DATE_FORMAT(bor_DateDue,\"%d/%c/%Y %H:%i\") AS bor_DateDue \r\n" + 
            		"from res_resource, rcp_resourcecopy, bor_borrow, use_user \r\n" +
            		"WHERE res_ResourceId = rcp_ResourceId \r\n" + 
            		"AND rcp_CopyId = bor_ResourceCopyId \r\n" + 
            		"AND bor_UserId = use_UserId\r\n" + 
            		"AND bor_Status = 'On Loan'\r\n" + 
            		"AND use_Username = '" + username +"' ORDER BY bor_DateDue DESC;" );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	String resourceType = "";
                int result = Integer.parseInt(rs.getString("res_ResourceType"));
                if (result == 0){
                	resourceType = "Book";
                }else if (result == 1){
                	resourceType = "DVD";
                }else if (result == 2){
                	resourceType = "Laptop";
                }
                String dueDate = "";
                if (rs.getString("bor_DateDue") == null){
                	dueDate = "No date set";
                }else{
                	dueDate = rs.getString("bor_DateDue");
                }

                oblist.add(new BorrowTable(rs.getString("res_Title"),rs.getString("res_Year"),resourceType,dueDate));
            
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
        col_resourcetype.setCellValueFactory(new PropertyValueFactory<>("resourcetype"));
        col_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        table.setItems(oblist);
    }

    public void searchResources(KeyEvent event) {
    	FilteredList <BorrowTable> filterResources = new FilteredList<>(oblist, p -> true);
    	searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
    		filterResources.setPredicate(items -> {	
    			if (newValue == null || newValue.isEmpty()) {
    				return true;
    			}
    			String typedText = newValue;
    			
    			// Searches the tableview for whether title entered in textfield exisits, if so, show that.
    			if (items.getTitle().toLowerCase().indexOf(typedText) != -1) {
    				return true;
    			}
    			return false;
    		});
    		SortedList <BorrowTable> sortedList = new SortedList <> (filterResources);
    		sortedList.comparatorProperty().bind(table.comparatorProperty());;
    		table.setItems(sortedList);
    	});
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Changes scene to User dashboard
     * @param event ??
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
