import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

/**
 * To view detailed information about a single resource
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version: 1.0
 */

public class LibrarianResourceViewController {
	
	// Add copies
	
	@FXML private ImageView resourceImage;
	@FXML private ImageView resourceRating;
	@FXML private Text resourceTitle;
	@FXML private Text resourceDescription;
	@FXML private TableView <ResourceCopyTable> tblResourceCopy;
    @FXML private TableColumn<ResourceCopyTable,String> rcp_CopyId;
    @FXML private TableColumn<ResourceCopyTable,String> rcp_loanDuration;
    @FXML private TableColumn<ResourceCopyTable,String> rcps_ResourceCopyStatus;
	
    
    ObservableList <ResourceCopyTable> resourceCopyList = FXCollections.observableArrayList();
	
	@FXML
	public void goToCopyCreation(ActionEvent event) {
//		try {
//	        FXMLLoader loader = new FXMLLoader();
//	        loader.setLocation(getClass().getResource("/javafx/UserDashboard.fxml"));
//	
//	        Parent homeWindow = loader.load();
//	        Scene login = new Scene(homeWindow);
//	        
//        	ResourceCreationController controller=loader.getController();
//	        controller.getUsername(username);
//	
//	        // Get the stage
//	        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
//	        window.setScene(login);
//	        window.show();
//	
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
	}
	
	private void changeScene(String scene, String type, ActionEvent event) {
		
	}
	
	public void setTitle(int resourceId) {
		String title = queryDatabase("res_Title", resourceId); 
		resourceTitle.setText(title);
	}
	
	public void setDescription(int resourceId) {
		String description = queryDatabase("res_Description", resourceId); 
		resourceDescription.setText(description);
	}
	
	public String queryDatabase(String columnName, int resourceId) {
		DatabaseManager db = new DatabaseManager();
   		Connection connection = db.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("Select " + columnName + " from res_resource WHERE res_ResourceId = " + resourceId);
            ResultSet rs = stmt.executeQuery();
            String result = "";
			while (rs.next()) {
			   result = rs.getString(1);
			}
			return result;
        } catch (SQLException ex) {
        	  System.out.println("There's an error in your SQL");
        }
        return null;
	}
	
	@FXML
    public void addResourceImage(int resourceId) throws IOException {
    	DatabaseManager db = new DatabaseManager();
   		Connection connection = db.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("Select res_ThumbnailImage from res_resource WHERE res_ResourceId = " + resourceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] image = rs.getBytes(1);
                BufferedImage readImage = ImageIO.read(new ByteArrayInputStream(image));

                if (readImage != null){
                    Image imageToAdd = SwingFXUtils.toFXImage(readImage, null);
                    resourceImage.setImage(imageToAdd);
                }
            }
            else {
                System.out.println("There isn't a picture for that user");
            }
        } catch (SQLException ex) {
        	  System.out.println("There's an error in your SQL2");
        }
    }
	
	@FXML
    public void addResourceRating(int resourceId, int rating) throws IOException {
		
		// get the rating
		
		File file;
		switch (rating) {
			case 5:
				file = new File("javafx\\images\\FiveStarBlob.bin");
				break;
			case 4:
				file = new File("javafx\\images\\FourStarBlob.bin");
				break;
			case 3:
				file = new File("javafx\\images\\ThreeStarBlob.bin");
				break;
			case 2:
				file = new File("javafx\\images\\TwoStarBlob.bin");
				break;
			case 1:
				file = new File("javafx\\images\\OneStarBlob.bin");
				break;
			default:
				file = null;
				break;
		}
		
    	 byte[] image = Files.readAllBytes(file.toPath());
         BufferedImage readImage = ImageIO.read(new ByteArrayInputStream(image));

         Image imageToAdd = SwingFXUtils.toFXImage(readImage, null);
         resourceRating.setImage(imageToAdd);
    }
	
	public void initializeTable(int resourceId) {
		// Establishes connection to database, allowing queries to be made.
		DatabaseManager db = new DatabaseManager();
		Connection DbCon = db.getConnection();
		
		// Creates an SQL statement which is sent to the connected database, and stores the results in a set.
		try {
			 PreparedStatement infoRetrieval = DbCon.prepareStatement("SELECT rcp_CopyId, rcp_loanDuration, rcps_ResourceCopyStatus FROM rcp_resourcecopy, rcps_resourcecopystatus WHERE rcp_Status = rcps_ResourceCopyStatusId AND rcp_ResourceId = " + resourceId);
			 ResultSet resourceInfo = infoRetrieval.executeQuery();
			 System.out.println(resourceInfo);
			 if (resourceInfo.next()){
			    while (resourceInfo.next()) {
			    	resourceCopyList.add(new ResourceCopyTable(resourceInfo.getInt("rcp_CopyId"), resourceInfo.getInt("rcp_loanDuration"), resourceInfo.getString("rcps_ResourceCopyStatus")));
			    }
			 }
		} catch (SQLException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		} catch (Exception e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		}
		// Populate TableView cells  the results from the queries
		rcp_CopyId.setCellValueFactory(new PropertyValueFactory<>("copyId"));
		rcp_loanDuration.setCellValueFactory(new PropertyValueFactory<>("days"));
		rcps_ResourceCopyStatus.setCellValueFactory(new PropertyValueFactory<>("copyStatus"));
		tblResourceCopy.setItems(resourceCopyList);		
	}		
}
