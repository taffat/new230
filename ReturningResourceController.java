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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.sql.Timestamp;
import java.text.DateFormat;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ReturningResourceController implements Initializable {

    @FXML private TableView<ReturnTable> table;
    @FXML private TableColumn<ReturnTable,String> col_title;
    @FXML private TableColumn<ReturnTable,String> col_dueDate;
    @FXML private TableColumn<ReturnTable,String> col_year;
    @FXML private TableColumn<ReturnTable,String> col_resourcetype;
    @FXML private TableColumn<ReturnTable,String> col_id;
    @FXML private Label lblStatus;
    @FXML private Label fineStatus;
    private String username;
    private final int BOOKDVD_FINE_DAY = 2;
    private final int LAPTOP_FINE_DAY = 10;
    private int resourceTypeInt;

    ObservableList<ReturnTable> oblist = FXCollections.observableArrayList();


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
        System.out.println(username);
        String userID = getUserID();
        System.out.println(userID);
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT res_Title, res_Year, res_ResourceType , DATE_FORMAT(bor_DateDue,\"%d/%c/%Y %H:%i\") AS bor_DateDue,bor_Id from res_resource, rcp_resourcecopy, bor_borrow WHERE res_ResourceId = rcp_ResourceId AND rcp_CopyId = bor_ResourceCopyId AND bor_returned = 0 AND bor_UserId = "+ userID+" ORDER BY bor_DateDue DESC" );
            ResultSet rs = stmt.executeQuery();
            boolean values = false;
            while (rs.next()) {
                values = true;
                String resourceType = "";
                resourceTypeInt = Integer.parseInt(rs.getString("res_ResourceType"));
                if (resourceTypeInt == 0){
                    resourceType = "Book";
                }else if (resourceTypeInt == 1){
                    resourceType = "DVD";
                }else if (resourceTypeInt == 2){
                    resourceType = "Laptop";
                }
                String dueDate = "";
                if (rs.getString("bor_DateDue") == null){
                    dueDate = "No date set";
                }else{
                    dueDate = rs.getString("bor_DateDue");
                }
                int id = rs.getInt("bor_Id");

                oblist.add(new ReturnTable(rs.getString("res_Title"),rs.getString("res_Year"),resourceType,dueDate,id));
            }
            if (values == false){
                lblStatus.setText("You have not got any items to return.");
                lblStatus.setTextFill(Color.RED);
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
        col_id.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        table.setItems(oblist);

        table.setOnMousePressed(event -> {
            if (event.getClickCount() == 1 && event.isPrimaryButtonDown() ){
                int borId = table.getSelectionModel().getSelectedItem().getBorrowId();
                int overdue = isOverdue(borId,conn);
                if (overdue == 0){
                    lblStatus.setText("This item is not overdue. Double click to return.");
                    lblStatus.setTextFill(Color.RED);
                    fineStatus.setText("");
                }else{
                    lblStatus.setText("This item is "+ isOverdue(borId,conn) + " day(s) overdue. Double click to return.");
                    fineStatus.setText("A fine amount of £" + calculateFine(overdue) + ".00 will be added to your account.");
                    lblStatus.setTextFill(Color.RED);
                }

            }

            if (event.getClickCount() == 2 && event.isPrimaryButtonDown()){
                int borId = table.getSelectionModel().getSelectedItem().getBorrowId();
                int overdue = isOverdue(borId,conn);
                int fine = calculateFine(overdue);
                returnResource(borId,overdue,fine,conn);
                JOptionPane.showMessageDialog(null, "Return requested, a librarian will authorize shortly.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                goToLibIssueDeskMouse(event);

            }
        });

    }

    public void returnResource(int borId, int overdue,int fine, Connection conn){
        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("insert into ret_return (ret_BorrowId, ret_DateReturned,ret_Overdue) values (?,?,?)");
            ps.setInt(1,borId);
            ps.setTimestamp(2,getCurrentTimestamp());
            if (overdue == 0){
                ps.setInt(3,0);
            }else{
                ps.setInt(3,1);
            }
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        try {
            PreparedStatement ps = null;
            ps = conn.prepareStatement("update bor_borrow set bor_returned = 1 where bor_Id = "+borId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (overdue != 0){
            try {
                PreparedStatement ps = null;
                ps = conn.prepareStatement("insert into fin_fine (fin_ReturnId, fin_FineAmount,fin_DaysOverdue) values (?,?,?)");

                ps.setInt(1,getReturnId(borId));
                ps.setInt(2,fine);
                ps.setInt(3,overdue);
                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public int isOverdue(int id,Connection conn){
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT CASE WHEN DATEDIFF(NOW(), bor_DateDue) > 1 THEN DATEDIFF(NOW(), bor_DateDue) WHEN NOW() > bor_DateDue THEN 1 ELSE 0 END AS day FROM bor_borrow WHERE bor_borrow.bor_Id = "+ id);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                return rs.getInt("day");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public int calculateFine(int overdue){
        if (resourceTypeInt == 0 || resourceTypeInt ==1){
            if (overdue > 13){
                return 25;
            }else{
                return overdue*BOOKDVD_FINE_DAY;
            }
        }else{
            if (overdue > 10){
                return 100;
            }else{
                return overdue*LAPTOP_FINE_DAY;
            }
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

    public int getReturnId(int borId){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        int retId=0;
        try {
            PreparedStatement stmt = conn.prepareStatement("select ret_Id from ret_return where ret_BorrowId = " + borId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                retId = rs.getInt("ret_Id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println(retId);
        return retId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Changes scene to User dashboard
     * @param event ??
     */
    public void goToUserIssueDesk(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/javafx/IssueDeskUser.fxml"));

            Parent homeWindow = loader.load();
            Scene borrowedItems = new Scene(homeWindow);
            IssueDeskController controller = loader.getController();
            controller.setUsername(username);
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
            loader.setLocation(getClass().getResource("/javafx/IssueDeskUser.fxml"));

            Parent homeWindow = loader.load();
            Scene borrowedItems = new Scene(homeWindow);
            IssueDeskController controller = loader.getController();
            controller.setUsername(username);

            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(borrowedItems);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
