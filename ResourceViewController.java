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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

/**
 * To view detailed information about a single resource
 * @author Samuel Dobbie (966537)
 * Copyright: No copyright
 * @version: 1.0
 */

public class ResourceViewController {
	
	// Add copies
	
	@FXML private ImageView resourceImage;
	
	@FXML private ImageView resourceRating;
	
	@FXML private Text resourceTitle;
	
	@FXML private Text resourceDescription;
	
	@FXML private Text resourceYear;
	
	@FXML private Text bookAuthor;
	
	@FXML private Text bookPublisher;
	
	@FXML private Text bookGenre;
	
	@FXML private Text bookIsbn;
	
	@FXML private Text bookLanguage;
	
	@FXML private Text dvdDirector;
	
	@FXML private Text dvdRuntime;
	
	@FXML private Text dvdLanguage;
	
	@FXML private Text dvdSubtitles;
	
	@FXML private Text laptopManufacturer;
	
	@FXML private Text laptopModel;
	
	@FXML private Text laptopOperatingSystem;
	
	private String username;
	private int resourceId;
	private String title;
	
    public void getUsername(String userName){
        this.username = userName;
    }
    
    public void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }
	
	@FXML
	public void goToReviews() {
		System.out.println("Go to reviews");
	}
	
	@FXML
	public void goToBorrowCopyList(ActionEvent event) {
		changeScene("/javafx/ResourceCopyInformation.fxml", "CopyTableController", event);
	}
	
	@FXML
	public void goToBorrow(ActionEvent event) {
		changeScene("/javafx/IssueDeskBorrowConfirmation.fxml", "IssueDeskController", event);
	}
	
	@FXML
	public void changeScene(String sceneName, String type, ActionEvent event) {
		try {
	           FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(getClass().getResource(sceneName));

	           Parent homeWindow = loader.load();
	           Scene borrowedItems = new Scene(homeWindow);

	           
	           
		        if (type.equals("IssueDeskController")) {
		        	IssueDeskController controller = loader.getController();
		        	controller.setUsername(username);
		        	controller.setResourceId(resourceId);
		        	controller.setConfirmationMessage(resourceTitle.getText());
		        } else if (type.equals("CopyTableController")) {
		        	CopyTableController controller = loader.getController();
		        	controller.setUsername(username);
		        	controller.setCopyTitle(title);
		        	controller.setResourceId(resourceId);
		        	controller.createTable();
		        }
	           
	           // Get the stage
	           Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
	           window.setScene(borrowedItems);
	           window.show();

	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	}
	
	public void setResourceValues(int resourceId) {
		title = queryDatabase("res_Title", "res_resource", "res_ResourceId", resourceId);
		String description = queryDatabase("res_Description", "res_resource", "res_ResourceId", resourceId);
		String year = queryDatabase("res_Year", "res_resource", "res_ResourceId", resourceId);
		if (title != null) {
			resourceTitle.setText(title);
		} else {
			resourceTitle.setText("Title: N/A");
		}
		if (description != null) {
			resourceDescription.setText(description);
		} else {
			resourceDescription.setText("No description available.");
		}
		if (year != null) {
			resourceYear.setText("Year: " + year);
		} else {
			resourceYear.setText("Year: N/A");
		}
	}
	
	public void setBookValues(int resourceId) {
		String author = queryDatabase("bok_Author", "bok_book", "bok_ResourceId", resourceId);
		String publisher = queryDatabase("bok_Publisher", "bok_book", "bok_ResourceId", resourceId);
		String genre = queryDatabase("bok_Genre", "bok_book", "bok_ResourceId", resourceId);
		String isbn = queryDatabase("bok_ISBN", "bok_book", "bok_ResourceId", resourceId);
		String language = queryDatabase("bok_Language", "bok_book", "bok_ResourceId", resourceId);
		if (author != null) {
			bookAuthor.setText("Author: " + author);
		} else {
			bookAuthor.setText("Author: N/A");
		}
		
		if (publisher != null) {
			bookPublisher.setText("Publisher: " + publisher);
		} else {
			bookPublisher.setText("Publisher: N/A");
		}
		
		if (genre != null) {
			bookGenre.setText("Genre: " + genre);
		} else {
			bookGenre.setText("Genre: N/A");
		}
		
		if (isbn != null) {
			bookIsbn.setText("ISBN: " + isbn);
		} else {
			bookIsbn.setText("ISBN: N/A");
		}
		
		if (language != null) {
			bookLanguage.setText("Language: " + language);
		} else {
			bookLanguage.setText("Language: N/A");
		}
	}
	
	public void setDvdValues(int resourceId) {
		String director = queryDatabase("dvd_Director", "dvd_dvd", "dvd_ResourceId", resourceId);
		String runtime = queryDatabase("dvd_Runtime", "dvd_dvd", "dvd_ResourceId", resourceId);
		String language = queryDatabase("dvd_Language", "dvd_dvd", "dvd_ResourceId", resourceId);
		String subtitles = queryDatabase("dvd_Subtitles", "dvd_dvd", "dvd_ResourceId", resourceId);
		
		if (director != null) {
			dvdDirector.setText("Director: " + director);
		} else {
			dvdDirector.setText("Director: N/A");
		}
		
		if (runtime != null) {
			dvdRuntime.setText("Runtime: " + runtime);
		} else {
			dvdRuntime.setText("Runtime: N/A");
		}
		
		if (language != null) {
			dvdLanguage.setText("Language: " + language);
		} else {
			dvdLanguage.setText("Language: N/A");
		}
		
		if (subtitles != null) {
			dvdSubtitles.setText("Subtitles: " + subtitles);
		} else {
			dvdSubtitles.setText("Subtitles: N/A");
		}
	}
	
	public void setLaptopValues(int resourceId) {
		String manufacturer = queryDatabase("lab_Manufacturer", "lab_laptop", "lab_ResourceId", resourceId);
		String model = queryDatabase("lab_Model", "lab_laptop", "lab_ResourceId", resourceId);
		String operatingSystem = queryDatabase("lab_OS", "lab_laptop", "lab_ResourceId", resourceId);
		
		if (manufacturer != null) {
			laptopManufacturer.setText("Manufacturer: " + manufacturer);
		} else {
			laptopManufacturer.setText("Manufacturer: N/A");
		}
		
		if (model != null) {
			laptopModel.setText("Model: " + model);
		} else {
			laptopModel.setText("Model: N/A");
		}
		
		if (operatingSystem != null) {
			laptopOperatingSystem.setText("Operating System: " + operatingSystem);
		} else {
			laptopOperatingSystem.setText("Operating System: N/A");
		}
	}

	public String queryDatabase(String columnName, String tableName, String idName, int resourceId) {
		DatabaseManager db = new DatabaseManager();
   		Connection connection = db.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("Select " + columnName + " from " + tableName + " WHERE " + idName + " = " + resourceId);
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
		System.out.println(rating);
		File file;
		switch (rating) {
			case 5:
				file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\sosoclose\\src\\javafx\\images\\FiveStarBlob.bin");
				break;
			case 4:
				file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\sosoclose\\src\\javafx\\images\\FourStarBlob.bin");
				break;
			case 3:
				file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\sosoclose\\src\\javafx\\images\\ThreeStarBlob.bin");
				break;
			case 2:
				file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\sosoclose\\src\\javafx\\images\\TwoStarBlob.bin");
				break;
			case 1:
				file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\sosoclose\\src\\javafx\\images\\OneStarBlob.bin");
				break;
			default:
				file = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\sosoclose\\src\\javafx\\images\\NotYetRated.bin");;
				break;
		}
		
    	 byte[] image = Files.readAllBytes(file.toPath());
         BufferedImage readImage = ImageIO.read(new ByteArrayInputStream(image));

         Image imageToAdd = SwingFXUtils.toFXImage(readImage, null);
         resourceRating.setImage(imageToAdd);
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

}
