/**
 * ResourceController.Java
 *  
 * @author Chaye Novak (902037), Samuel Dobbie (966537 - Resource Information Page)
 * Copyright: No copyright
 * @version 2.0
 */

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import javafx.stage.Stage;

public class ResourceController implements Initializable {
	
	// Filter controls.
	@FXML
	private RadioButton radBooks;
	
	@FXML
	private RadioButton radDVDs;
	
	@FXML
	private RadioButton radLaptops;

	// Book Table controls and elements
	/*@FXML
	private TableColumn<ResourceTable, String> col_Author;
	
	@FXML
	private TableColumn<ResourceTable, String> col_Genre;
	
	@FXML
	private TableColumn<ResourceTable, String> col_ISBN;
	
	@FXML
	private TableColumn <ResourceTable, String> col_Language;
	*/
	@FXML
	private TableColumn <ResourceTable, String> col_Title;
	
	/*@FXML
	private TableColumn <ResourceTable, String> col_Publisher;
	
	//DVD Table controls and elements.
	@FXML
	private TableColumn<ResourceTable, String> col_Director;
	
	@FXML
	private TableColumn<ResourceTable, String> col_Runtime;	
	
	//private TableColumn<ResourceTable, String> col_LanguageDvd;
	// Laptop Table controls and elements
	@FXML
	private TableColumn<ResourceTable, String> col_Manufacturer;
	
	@FXML
	private TableColumn<ResourceTable, String> col_Model;
	
	@FXML
	private TableColumn<ResourceTable, String> col_OS;
	*/
	// Resource Controller specific elements
	@FXML
	private TableColumn<ResourceTable, String> col_ResourceId;
	
	@FXML
	private TableColumn<ResourceTable, String> col_Type;
	
	@FXML
	private TableColumn<ResourceTable, String> col_Year;
	
	@FXML
	private TableColumn <LaptopTable, String> col_Copies;
	
	
	@FXML
	private TableView <ResourceTable> tblResources;
	
	@FXML
	private Label lblStatus;
	
	@FXML
	private TextField searchBox;
	
	@FXML
	private ImageView imgDashboard;
	
	@FXML
	private Button btnUserDash;
	
	ObservableList <ResourceTable> resourceList = FXCollections.observableArrayList();
	
	private String username;
	
    public void getUsername(String username){
        this.username = username;
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		// Establishes connection to database, allowing queries to be made.
		DatabaseManager db = new DatabaseManager();
		Connection DbCon = db.getConnection();
		
		// Creates an SQL statement which is sent to the connected database, and stores the results in a set.
		try {
			 PreparedStatement infoRetrieval = DbCon.prepareStatement("SELECT *, (SELECT COUNT(rcp_resourcecopy.rcp_CopyId) FROM rcp_resourcecopy WHERE rcp_ResourceId = res_ResourceId ) AS res_CopyCount FROM res_resource LEFT JOIN bok_book ON bok_ResourceId = res_ResourceId LEFT JOIN dvd_dvd ON dvd_ResourceId = res_ResourceId LEFT JOIN lab_laptop ON lab_ResourceId = res_ResourceId");
			 ResultSet resourceInfo = infoRetrieval.executeQuery();
			    while (resourceInfo.next()) {
			    	String resourceType = "";
                    int result = Integer.parseInt(resourceInfo.getString("res_ResourceType"));
                    if (result == 0){
                        resourceType = "Book";
                    }else if (result == 1){
                        resourceType = "DVD";
                    }else if (result == 2){
                        resourceType = "Laptop";
                    }
			         resourceList.add(new ResourceTable(resourceInfo.getInt("res_ResourceId"), resourceInfo.getInt("res_Year"), resourceType, resourceInfo.getString("bok_Language"), resourceInfo.getString("bok_Author"), resourceInfo.getString("bok_ISBN"), resourceInfo.getString("res_Title"), 
			        		 resourceInfo.getString("bok_Publisher"), resourceInfo.getString("bok_Genre"), resourceInfo.getString("dvd_Director"), resourceInfo.getInt("dvd_runtime"),
			        		 resourceInfo.getString("lab_manufacturer"), resourceInfo.getString("lab_model"), resourceInfo.getString("lab_OS"), resourceInfo.getInt("res_CopyCount")));
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
		//col_Author.setCellValueFactory(new PropertyValueFactory<>("Author"));
		col_Type.setCellValueFactory(new PropertyValueFactory<>("resourceType"));
		//col_Genre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
		//col_ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		//col_Language.setCellValueFactory(new PropertyValueFactory<>("Language"));
		col_Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		col_Year.setCellValueFactory(new PropertyValueFactory<>("Year"));
		//col_Publisher.setCellValueFactory(new PropertyValueFactory<>("Publisher"));
		//col_Director.setCellValueFactory(new PropertyValueFactory<>("Director"));
		//col_Runtime.setCellValueFactory(new PropertyValueFactory<>("Runtime"));
		//col_Manufacturer.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
		//col_Model.setCellValueFactory(new PropertyValueFactory<>("Model"));
		//col_OS.setCellValueFactory(new PropertyValueFactory<>("OS"));
		col_Copies.setCellValueFactory(new PropertyValueFactory<>("Copies"));
		tblResources.setItems(resourceList);
		
		tblResources.setOnMousePressed(event -> {
			if (event.getClickCount() == 2 && event.isPrimaryButtonDown() ){
				int resourceId = tblResources.getSelectionModel().getSelectedItem().getResourceId();
				String resourceType = tblResources.getSelectionModel().getSelectedItem().getResourceType();
				
				try {
					changeSceneOnResourceSelected(event, resourceId, resourceType);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
	}
	
	private void changeSceneOnResourceSelected(MouseEvent bookSelected, int resourceId, String resourceType) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			
			if (resourceType == "Book") {
				loader.setLocation(getClass().getResource("/javafx/BookInformationView.fxml"));
			} else if (resourceType == "DVD") {
				loader.setLocation(getClass().getResource("/javafx/DVDInformationView.fxml"));
			} else {
				loader.setLocation(getClass().getResource("/javafx/LaptopInformationView.fxml"));
			}
			
            Parent dashboard = loader.load();
            Scene books = new Scene(dashboard);
			
            ResourceViewController controller = loader.getController();
            controller.addResourceImage(resourceId);
            controller.getUsername(username);
            controller.setResourceId(resourceId);
            int resourceRating = getResourceRating(resourceId);
            if (resourceRating != -1) {
            	controller.addResourceRating(resourceId, resourceRating);
            }
            controller.setResourceValues(resourceId);
            
			if (resourceType.equals("Book")) {
				controller.setBookValues(resourceId);
			} else if (resourceType.equals("DVD")) {
				controller.setDvdValues(resourceId);
			} else {
				controller.setLaptopValues(resourceId);
			}
            
			// Get the stage
            Stage window = (Stage)((Node) bookSelected.getSource()).getScene().getWindow();
			window.setScene(books);
			window.show();
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public int getResourceRating(int resourceId) {
		DatabaseManager db = new DatabaseManager();
   		Connection connection = db.getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT res_Rating FROM res_resource WHERE res_ResourceId = " + resourceId);
            ResultSet rs = stmt.executeQuery();
            int resourceRating = -1;
			while (rs.next()) {
				resourceRating = rs.getInt(1);
			}
			return resourceRating;
        } catch (SQLException ex) {
        	  System.out.println("There's an error in your SQL");
        }
        return -1;
	}
	
	public void filterByResource(ActionEvent event) throws Exception {
		// if user clicks this, then load.
		try {
		if (radBooks.isSelected()) {
			changeSceneOnBookSelected(event);
			radBooks.setSelected(true);
			radBooks.disarm();
		}
		else if (radDVDs.isSelected()) {
			changeSceneOnDvdSelected(event);
			radDVDs.setSelected(true);
			radDVDs.disarm();
		}else if (radLaptops.isSelected()) {
			changeSceneOnLaptopSelected(event);
			radLaptops.setSelected(true);
			radLaptops.disarm();
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Filter resources by books
	 * @param bookSelected An action (button click) has taken place.
	 * @throws Exception ??
	 */
	private void changeSceneOnBookSelected(ActionEvent bookSelected) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/bookResourceView.fxml"));
			
            Parent dashboard = loader.load();
            Scene books = new Scene(dashboard);
            
            BookController controller = loader.getController();
            controller.getUsername(username);
            
			
			// Get the stage
            Stage window = (Stage)((Node) bookSelected.getSource()).getScene().getWindow();
			window.setScene(books);
			window.show();
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Filter resources by DVD
	 * @param dvdSelected An action (button click) has taken place.
	 * @throws Exception ??
	 */
	private void changeSceneOnDvdSelected(ActionEvent dvdSelected) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/DVDResourceView.fxml"));
			
            Parent dashboard = loader.load();
            Scene dvds = new Scene(dashboard);
			
            DvdController controller = loader.getController();
            controller.getUsername(username);
            
			// Get the stage
            Stage window = (Stage)((Node) dvdSelected.getSource()).getScene().getWindow();
			window.setScene(dvds);
			window.show();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Filter resources by laptops
	 * @param laptopSelected An action (button click) has taken place.
	 * @throws Exception ??
	 */
	private void changeSceneOnLaptopSelected(ActionEvent laptopSelected) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/LaptopResourceView.fxml"));
			
            Parent dashboard = loader.load();
            Scene laptops = new Scene(dashboard);
			
            LapController controller = loader.getController();
            controller.getUsername(username);
            
			// Get the stage
            Stage window = (Stage)((Node) laptopSelected.getSource()).getScene().getWindow();
			window.setScene(laptops);
			window.show();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
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
            Scene dashboard = new Scene(homeWindow);
	    DashboardController dashboardController=loader.getController();
	    dashboardController.getUsername(username);

            // Get the stage
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(dashboard);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void searchResources(KeyEvent event) {
    	//
    	FilteredList <ResourceTable> filterResources = new FilteredList<>(resourceList, p -> true);
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
    		SortedList <ResourceTable> sortedList = new SortedList <> (filterResources);
    		sortedList.comparatorProperty().bind(tblResources.comparatorProperty());;
    		tblResources.setItems(sortedList);
    	});
	}
}
