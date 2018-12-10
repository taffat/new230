/**
* LapController.java
* Implements the filter: Laptop function, allowing users to filter resources by type Laptop, contact database and display results in a table
* @manufacturer Chaye Novak (902037)
* Copyright: No copyright 
* @version 1.0
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

	public class LapController implements Initializable {
		
		//Variables for bookController's JavaFX objects
		@FXML
		private TableView <LaptopTable> tblLaptop;
		
		@FXML
		private TableColumn<LaptopTable, String> col_ResourceId;
		
		@FXML
		private TableColumn<LaptopTable, String> col_Manufacturer;
		
		@FXML
		private TableColumn<LaptopTable, String> col_Model;
		
		@FXML
		private TableColumn<LaptopTable, String> col_OS;
		
		@FXML
		private TableColumn <LaptopTable, String> col_Title;
		
		@FXML
		private TableColumn <LaptopTable, String> col_Year;
		
		@FXML
		private TableColumn <LaptopTable, String> col_Copies;
		
		@FXML
		private Label lblStatus;
		
		@FXML
		private RadioButton radBooks;
		
		@FXML
		private RadioButton radDVDs;
		
		@FXML
		private RadioButton radAll;
		
		@FXML
		private TextField searchBox;
		
		@FXML
		private ImageView imgDashboard;
		
		@FXML
		private Button btnUserDash;
		
		ObservableList <LaptopTable> laptopList = FXCollections.observableArrayList();
		
		private String username;
		
	    public void getUsername(String username){
	        this.username = username;
	    }
		
		/**
		 * Overrides the initialize method with new functions and parameters.
		 */
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			
			// Establishes connection to database, allowing queries to be made.
			DatabaseManager db = new DatabaseManager();
			Connection DbCon = db.getConnection();
			
					
			try {
				 PreparedStatement infoRetrieval = DbCon.prepareStatement("SELECT lab_ResourceId, lab_Manufacturer, lab_Model, lab_OS, res_Title, res_Year, (SELECT COUNT(rcp_resourcecopy.rcp_CopyId) FROM rcp_resourcecopy WHERE rcp_resourcecopy.rcp_ResourceId = lab_ResourceId) AS lab_CopyCount from lab_laptop, res_resource where lab_resourceId = res_ResourceId ORDER BY lab_Model  ASC");
				 ResultSet laptopInfo = infoRetrieval.executeQuery();
				 if (laptopInfo.next()){
				    while (laptopInfo.next()) {
				         laptopList.add(new LaptopTable(laptopInfo.getInt("lab_ResourceId"), laptopInfo.getString("lab_Manufacturer"), laptopInfo.getString("lab_Model"), laptopInfo.getString("lab_OS"), laptopInfo.getString("res_Title"), laptopInfo.getInt("res_Year"), laptopInfo.getInt("lab_CopyCount")));
				    }
				 } else {
				       lblStatus.setText("Resources not found");
				       lblStatus.setTextFill(Color.RED);
				 }
			} catch (SQLException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			} catch (Exception e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			}
			col_ResourceId.setCellValueFactory(new PropertyValueFactory<>("ResourceId"));
			col_Manufacturer.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
			col_Model.setCellValueFactory(new PropertyValueFactory<>("Model"));
			col_OS.setCellValueFactory(new PropertyValueFactory<>("OS"));
			col_Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
			col_Year.setCellValueFactory(new PropertyValueFactory<>("Year"));
			col_Copies.setCellValueFactory(new PropertyValueFactory<>("Copies"));
			tblLaptop.setItems(laptopList);
		
			tblLaptop.setOnMousePressed(event -> {
				if (event.getClickCount() == 2 && event.isPrimaryButtonDown() ){
					int resourceId = tblLaptop.getSelectionModel().getSelectedItem().getResourceId();
					
					try {
						changeSceneOnResourceSelected(event, resourceId);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		
		}
		
		private void changeSceneOnResourceSelected(MouseEvent bookSelected, int resourceId) throws Exception {
			try {
				FXMLLoader loader = new FXMLLoader();
				
				loader.setLocation(getClass().getResource("/javafx/LaptopInformationView.fxml"));
				
	            Parent dashboard = loader.load();
	            Scene books = new Scene(dashboard);
				
	            ResourceViewController controller = loader.getController();
	            controller.getUsername(username);
	            controller.addResourceImage(resourceId);
	            controller.setResourceId(resourceId);
	            int resourceRating = getResourceRating(resourceId);
	            if (resourceRating != -1) {
	            	controller.addResourceRating(resourceId, resourceRating);
	            }
	            controller.getUsername(username);
	            controller.setResourceValues(resourceId);
				controller.setLaptopValues(resourceId);
				
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
			} else if (radAll.isSelected()) {
				changeSceneOnAllSelected(event);
				radAll.setSelected(true);
				radAll.disarm();
			}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
		 * Filter resources by books
		 * @param bookSelected An action (button click) has taken place.
		 * @throws Exception ??
		 */
		private void changeSceneOnAllSelected(ActionEvent allSelected) throws Exception {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/javafx/resourceViewsTemplate.fxml"));
				
	            Parent dashboard = loader.load();
	            Scene browse = new Scene(dashboard);
	            
	            ResourceController controller = loader.getController();
	            controller.getUsername(username);
				
				// Get the stage
	            Stage window = (Stage)((Node) allSelected.getSource()).getScene().getWindow();
				window.setScene(browse);
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
		
		/**
		 * Implements a search function for resources.
		 * @param event An action (button click) has taken place.
		 */
		public void searchResources(KeyEvent event) {
	    	//
	    	FilteredList <LaptopTable> filterResources = new FilteredList<>(laptopList, p -> true);
	    	searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
	    		filterResources.setPredicate(items -> {
	    			
	    			if (newValue == null || newValue.isEmpty()){
	    				return true;
	    			}
	    			String typedText = newValue;
	    			
	    			if (items.getTitle().toLowerCase().indexOf(typedText) != -1) {
	    				return true;
	    			}

	    			if (items.getManufacturer().toLowerCase().indexOf(typedText) != -1) {
	    				
	    				return true;
	    			}
	    			
					if (items.getModel().toLowerCase().indexOf(typedText) != -1) {
					    				
					    return true;
					}
					
					if (items.getOS().toLowerCase().indexOf(typedText) != -1) {
	    				
					    return true;
					}
					
	    			return false;
	    		});
	    		SortedList <LaptopTable> sortedList = new SortedList <> (filterResources);
	    		sortedList.comparatorProperty().bind(tblLaptop.comparatorProperty());;
	    		tblLaptop.setItems(sortedList);
	    	});
	    }
	}

