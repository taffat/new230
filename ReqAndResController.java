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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReqAndResController implements Initializable {

    @FXML private TableView<ReqAndResTable> table;
    @FXML private TableColumn<ReqAndResTable,String> col_title;
    @FXML private TableColumn<ReqAndResTable,String> col_year;
    @FXML private TableColumn<ReqAndResTable,String> col_resourcetype;
    @FXML private TableColumn<ReqAndResTable,String> col_status;
    @FXML private Label lblStatus;
    @FXML private TextField searchBox;
    private String username;


    ObservableList<ReqAndResTable> oblist = FXCollections.observableArrayList();


    // Why is this get (same name in other methods too)?
    public void getUsername(String userName){
        username = userName;
        displayTable();
    }

    public void displayTable(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT res_Title, " + 
            		"		 res_Year, " + 
            		"		 res_ResourceType, " + 
            		"		 bor_Status " + 
            		"FROM res_resource, rcp_resourcecopy, bor_borrow, use_user " + 
            		"WHERE res_ResourceId = rcp_ResourceId " + 
            		"AND rcp_CopyId = bor_ResourceCopyId " + 
            		"AND bor_UserId = use_UserId " + 
            		"AND (bor_Status = 'Requested' OR bor_Status = 'Reserved') " + 
            		"AND use_Username = '" + username + "' " + 
            		"ORDER BY bor_Status DESC;");
                        
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
                String available = "";
                String status = rs.getString("bor_Status");
                if (status.equalsIgnoreCase("Reserved")){
                	available = "Available to Borrow";
                }else{
                	available = "Not Available";
                }
                oblist.add(new ReqAndResTable(rs.getString("res_Title"),rs.getString("res_Year"),resourceType,available));
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
        col_status.setCellValueFactory(new PropertyValueFactory<>("available"));
        table.setItems(oblist);
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
