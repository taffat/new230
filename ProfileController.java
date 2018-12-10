/**
 * To give librarian's the ability to create new Users.
 * @author Samuel Dobbie (966537), Matt Ashman (912639), Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
 */

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;



public class ProfileController {

    @FXML private Label idNum;
    @FXML private Label username;
    @FXML private Label firstname;
    @FXML private Label surname;
    @FXML private Label phonenum;
    @FXML private Label address;
    @FXML private Label postcode;
    @FXML private Label balanceOrEmploymentDate;
    @FXML private Text idNumText;
    @FXML private Text adjustText;
    @FXML private ImageView profilePIC;
    @FXML private Label statusLabel;
    @FXML private TextField usernameNEW;
    @FXML private TextField firstnameNEW;
    @FXML private TextField lastnameNEW;
    @FXML private TextField phonenumNEW;
    @FXML private TextField addressNEW;
    @FXML private TextField postcodeNEW;
    @FXML private  Image image;
    @FXML private  ImageView profileImage;
    private File file;

    private String userName;

    /**
     * Gets the username from the dash controller.
     * @param usernameUse the username of the logged in user.
     */
    public void getUsername(String usernameUse) throws IOException {
        userName = usernameUse;



        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();

        try {
            PreparedStatement checkUserType = connection.prepareStatement("select use_UserType from use_user where use_Username = '" + userName + "'");


            ResultSet userType = checkUserType.executeQuery();


            userType.beforeFirst();
            userType.next();
            int userTypeNum = userType.getInt("use_UserType");
            if(userTypeNum == 1) {
                setLibLabels();
            } else {
                setUserLabels();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        addImage();
    }



    /**
     * sets the labels on the scene to match the librarian's info.
     */
    private void setLibLabels() {

        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();

        // change text
        idNumText.setText("Staff Number:");
        adjustText.setText("Date of Employment");

        try {
            PreparedStatement stmtUser = conn.prepareStatement("select use_UserId,use_Username,use_FirstName,use_LastName,use_PhoneNumber,use_Address,use_Postcode,use_Balance from use_user where use_Username = '" + userName + "'");
            ResultSet rs = stmtUser.executeQuery();

            PreparedStatement stmtLib = conn.prepareStatement("select lib_StaffNumber, lib_EmploymentDate from lib_librarian where lib_UserId = '" + getUserID() + "'");
            ResultSet rsLib = stmtLib.executeQuery();

            while (rs.next() && rsLib.next()) {
                idNum.setText(Integer.toString(rsLib.getInt("lib_StaffNumber")));
                username.setText(rs.getString("use_Username"));
                firstname.setText(rs.getString("use_FirstName"));
                surname.setText(rs.getString("use_LastName"));
                phonenum.setText(rs.getString("use_PhoneNumber"));
                address.setText(rs.getString("use_Address"));
                postcode.setText(rs.getString("use_Postcode"));
                /*
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String strDate = dateFormat.format(rsLib.getDate("lib_EmploymentDate"));
                balanceOrEmploymentDate.setText(strDate);
                */
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * sets the labels on the scene to match the user's info.
     */
    public void setUserLabels(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement("select use_UserId,use_Username,use_FirstName,use_LastName,use_PhoneNumber,use_Address,use_Postcode,use_Balance from use_user where use_Username = '" + userName + "'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                idNum.setText(rs.getString("use_UserId"));
                username.setText(rs.getString("use_Username"));
                firstname.setText(rs.getString("use_FirstName"));
                surname.setText(rs.getString("use_LastName"));
                phonenum.setText(rs.getString("use_PhoneNumber"));
                address.setText(rs.getString("use_Address"));
                postcode.setText(rs.getString("use_Postcode"));
                balanceOrEmploymentDate.setText(rs.getString("use_Balance"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addImage() throws IOException {
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        String userID = getUserID();

        try {
            PreparedStatement stmt = connection.prepareStatement("Select use_ProfileImage from use_user WHERE use_UserId =" + userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                byte[] image = rs.getBytes(1);
                BufferedImage readImage = ImageIO.read(new ByteArrayInputStream(image));

                if (readImage != null){
                    Image imageToAdd = SwingFXUtils.toFXImage(readImage, null);
                    profilePIC.setImage(imageToAdd);
                }
            }
            else {
                System.out.println("There isn't a picture for that user");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            System.out.println("There's an error in your SQL");
        }
    }


    /**
     * Updates the user's information.
     * @param field name of the field to be changed.
     * @param value the new data to be entered.
     * @param connection ??
     */
    public void updateInfo(String field, String value, Connection connection){
        String useUserID = getUserID();
        try {
            PreparedStatement stmtOne = connection.prepareStatement("UPDATE use_user SET "+ field + "= '" + value + "' " +
                    "WHERE use_UserId = '" + useUserID + "'");
            stmtOne.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get's the ID of the user.
     * @return Id of the user.
     */
    public String getUserID(){
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.getConnection();
        String userID = "";
        try {
            PreparedStatement stmt = conn.prepareStatement("select use_UserId from use_user where use_Username = '" + userName + "'");
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
     * Changes scene to the user dashboard scene.
     * @param event An action (button click) has taken place.
     */
    public void goToUserDash(ActionEvent event){
        try {

            DatabaseManager db = new DatabaseManager();
            Connection connection = db.getConnection();

            PreparedStatement checkUserType = connection.prepareStatement("select use_UserType from use_user where use_Username = '" + userName + "'");
            ResultSet userType = checkUserType.executeQuery();

            userType.beforeFirst();
            userType.next();
            int userTypeNum = userType.getInt("use_UserType");
            if(userTypeNum == 1) {
                System.out.println("librarian");
                changeScene("/javafx/LibrarianDashboard.fxml", "LibrarianController", event);
                
            } else {
                changeScene("/javafx/UserDashboard.fxml", "DashboardController", event);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Changes scene to the edit user scene.
     * @param event An action (button click) has taken place.
     */
    public void goToEditUser(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/javafx/EditUser.fxml"));

            Parent homeWindow = loader.load();
            Scene edituser = new Scene(homeWindow);

            EditProfileController controller = loader.getController();
            controller.getUsername(userName);

            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(edituser);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes scene to the login scene.
     * @param event An action (button click) has taken place.
     */
    public void goToLogin(ActionEvent event){

        changeScene("/javafx/Login.fxml", "LoginController", event);
    }

    private void changeScene(String scene, String type, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(scene));

            Parent homeWindow = loader.load();
            Scene login = new Scene(homeWindow);

            if (type == "DashboardController") {
                DashboardController controller = loader.getController();
                controller.getUsername(userName);
            } else if (type == "ProfileController"){
                ProfileController profileController=loader.getController();
                profileController.getUsername(userName);
            } else if (type == "LibrarianController") {
            	LibrarianController controller = loader.getController();
	            controller.getUsername(userName);
            }
            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(login);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(){
        Avatar myAvatar = new Avatar();
        Stage stage = new Stage();
        myAvatar.start(stage);
    }

    public void loadImage(){
        String url = Avatar.testUser.getAvatarFilePath();
        System.out.println(url);
        file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\latest\\src\\" + url);
        image = new Image(file.toURI().toString());
        profilePIC.setImage(image);
    }
    public FileInputStream getFis() throws FileNotFoundException {
        System.out.println(file.getAbsolutePath());
        FileInputStream fiss = new FileInputStream(file);
        return fiss;
    }


}
