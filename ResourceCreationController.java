

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.internal.util.xml.impl.Input;

import javax.swing.*;


public class ResourceCreationController {
    @FXML private TextField title;
    @FXML private TextField year;
    @FXML private TextField manufacturer;
    @FXML private TextField model;
    @FXML private TextField installedOS;
    @FXML private TextField desc;

    @FXML private TextField author;
    @FXML private TextField publisher;
    @FXML private TextField genre;
    @FXML private TextField isbn;
    @FXML private TextField language;

    @FXML private TextField director;
    @FXML private TextField runtime;
    @FXML private TextField subtitleLang;
    
    @FXML private Label statusLabel;

    private FileChooser fileChooser;
    private File file;
    private Stage stage;
    @FXML private ImageView imageView;
    private Image image;
    private FileInputStream fis;

    private String username;
    private final int RES_TYPE_LAPTOP = 2;
    private final int RES_TYPE_BOOK = 0;
    private final int RES_TYPE_DVD = 1;


    /**
     * Gets the username from the login controller.
     * @param username the username of the logged in librarian.
     */
    public void getUsername(String username){
        this.username = username;
    }
    
    /**
     * Gets the username from the login controller.
     * @param username the username of the logged in librarian.
     */
    public void getUsername(String username, Stage stage){
        this.username = username;
        this.stage = stage;
    }

    public void addFileChooser() throws MalformedURLException {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg"));
        file = fileChooser.showOpenDialog(stage);

        System.out.println(file.getAbsolutePath());

        image = new Image(file.toURI().toString());
        imageView.setImage(image);
        
    }

    public FileInputStream getFis() throws FileNotFoundException {
        System.out.println(file.getAbsolutePath());
        FileInputStream fiss = new FileInputStream(file);
        return fiss;

    }

    /**
     * Creates a new laptop resource.
     * @param event An action (button click) has taken place.
     */
    @FXML
    public void createLaptopResource(ActionEvent event) {
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        //setDefaultStyle(laptopFields);
        boolean hasPassed = validationCheckLaptop() && validationCheckNumber(year.getText(), "year");
        try {
            if (hasPassed) {
                statusLabel.setText("");
                // Insert general resource data
                PreparedStatement ps = connection.prepareStatement("insert into res_resource " +
                        "(res_ResourceType, res_Title,res_Year,res_ThumbnailImage,res_Description) values (?,?,?,?,?)");
                ps.setInt(1,RES_TYPE_LAPTOP);
                ps.setString(2,title.getText());
                ps.setString(3,year.getText());
                FileInputStream fis = getFis();
                ps.setBinaryStream(4,fis);
                ps.setString(5,desc.getText());

                ps.executeUpdate();
                // Get resourceId of newly added resource
                int resourceId = getResourceId(connection);

                System.out.print(resourceId);
                // Insert laptop specific data if error free
                if (resourceId != -1) {
                    PreparedStatement ps2 = connection.prepareStatement("insert into lab_laptop (lab_resourceId, lab_Manufacturer,lab_Model, lab_OS) values (?,?,?,?)");
                    ps2.setInt(1,resourceId);
                    ps2.setString(2,manufacturer.getText());
                    ps2.setString(3,model.getText());
                    ps2.setString(4,installedOS.getText());
                    ps2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Laptop resource successfully added.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    goToManageResources(event);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a new Book resource.
     * @param event An action (button click) has taken place.
     */
    @FXML
    public void createBookResource(ActionEvent event) {
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        //setDefaultStyle(bookFields);
        boolean hasPassed = validationCheckBook() && validationCheckNumber(year.getText(), "year");
        try {
            if (hasPassed) {
                statusLabel.setText("");
                // Insert general resource data
                PreparedStatement ps = connection.prepareStatement("insert into res_resource " +
                        "(res_ResourceType, res_Title,res_Year,res_ThumbnailImage,res_Description) values (?,?,?,?,?)");
                ps.setInt(1,RES_TYPE_BOOK);
                ps.setString(2,title.getText());
                ps.setString(3,year.getText());
                FileInputStream fis = getFis();
                ps.setBinaryStream(4,fis);
                ps.setString(5,desc.getText());
                ps.executeUpdate();

                int resourceId = getResourceId(connection);
                // Insert book specific data if error free
                if (resourceId != -1) {
                    PreparedStatement ps2 = connection.prepareStatement("insert into bok_book (bok_ResourceId, bok_Author,bok_Publisher, bok_Genre, bok_ISBN, bok_Language) values (?,?,?,?,?,?)");
                    ps2.setInt(1,resourceId);
                    ps2.setString(2,author.getText());
                    ps2.setString(3,publisher.getText());
                    ps2.setString(4,genre.getText());
                    ps2.setString(5,isbn.getText());
                    ps2.setString(6,language.getText());
                    ps2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Book resource successfully added.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    goToManageResources(event);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a new DVD resource.
     * @param event An action (button click) has taken place.
     */
    @FXML
    public void createDvdResource(ActionEvent event) {
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getConnection();
        //setDefaultStyle(dvdFields);
        boolean hasPassed = validationCheckDvd() && validationCheckNumber(year.getText(), "year")  && validationCheckNumber(runtime.getText(), "runtime");
        try {
            if (hasPassed) {
                statusLabel.setText("");
                // Insert general resource data
                PreparedStatement pst = connection.prepareStatement("insert into res_resource " +
                        "(res_ResourceType, res_Title,res_Year,res_ThumbnailImage,res_Description) values (?,?,?,?,?)");
                pst.setInt(1,RES_TYPE_DVD);
                pst.setString(2,title.getText());
                pst.setString(3,year.getText());
                FileInputStream fis = getFis();
                pst.setBinaryStream(4,fis);
                System.out.println(desc.getText());
                pst.setString(5,desc.getText());

                pst.executeUpdate();

                // Get resourceId of newly added resource
                int resourceId = getResourceId(connection);
                // Insert book specific data if error free
                if (resourceId != -1) {
                    PreparedStatement ps2 = connection.prepareStatement("insert into dvd_dvd (dvd_ResourceId, dvd_Director,dvd_Runtime, dvd_Language) values (?,?,?,?)");
                    ps2.setInt(1,resourceId);
                    ps2.setString(2,director.getText());
                    ps2.setString(3,runtime.getText());
                    ps2.setString(4,language.getText());
                    ps2.executeUpdate();

                    if (!subtitleLang.getText().isEmpty()){
                        PreparedStatement stmtFour = connection.prepareStatement("INSERT INTO dvds_dvdsubtitles VALUES(" + resourceId + ",'" + subtitleLang.getText() +"')");
                        stmtFour.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "DVD resource successfully added.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    goToManageResources(event);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setDefaultStyle(TextField[] fields) {
    	for (int i = 0; i < fields.length; i++) {
    		fields[i].setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%); -fx-border-color:#888; -fx-border-radius:5px;");
    	}
    }

    private int getResourceId(Connection connection) {
        PreparedStatement stmtTwo;
        try {
            stmtTwo = connection.prepareStatement("SELECT res_ResourceId FROM res_resource WHERE res_Title = '" + title.getText() + "'");
            ResultSet result = stmtTwo.executeQuery();
            int resourceId = 0;
            while (result.next()) {
                resourceId = result.getInt(1);
            }
            return resourceId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private boolean validationCheckLaptop() {
        // Check if any fields are empty
        if (title.getText().isEmpty() || year.getText().isEmpty() || manufacturer.getText().isEmpty() || model.getText().isEmpty() || installedOS.getText().isEmpty()) {
            statusLabel.setText("Please complete all fields.");
            statusLabel.setTextFill(Color.RED);
            return false;
        }
        return true;
    }
    private boolean validationCheckBook() {
        // Check if any fields are empty
        if (title.getText().isEmpty() || year.getText().isEmpty() || author.getText().isEmpty() || publisher.getText().isEmpty()) {
            statusLabel.setText("Please complete all fields.");
            statusLabel.setTextFill(Color.RED);
            return false;
        }
        return true;
    }
    private boolean validationCheckDvd(){
        // Check if any fields are empty
        if (title.getText().isEmpty() || year.getText().isEmpty() || director.getText().isEmpty() || runtime.getText().isEmpty()) {
            statusLabel.setText("Please complete all fields.");
            statusLabel.setTextFill(Color.RED);
            return false;
        }
        return true;
    }
    
    private boolean validationCheckNumber(String value, String type) {
    	 // Check if a number was entered in year field
        try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			if (type == "year") {
				statusLabel.setText("Please enter a valid year (i.e. 1990)");
				year.setStyle("-fx-background-color:#FFC6CB; -fx-border-color:#FF7063; -fx-border-radius:5px;");
			} else {
				statusLabel.setText("Please enter a valid runtime value (i.e. 124)");
				runtime.setStyle("-fx-background-color:#FFC6CB; -fx-border-color:#FF7063; -fx-border-radius:5px;");
			}
			statusLabel.setTextFill(Color.RED);
			return false;
		}
        return true;
    }

    /**
     * Changes scene to User dashboard.
     * @param event An action (button click) has taken place.
     */
    public void goToManageResources(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/javafx/ManageResources.fxml"));

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
