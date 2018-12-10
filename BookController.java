/**
 * bookController.java
 * Implements the filter: book function, allowing users to filter resources by type book, contact database and display results in a table
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.9
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

public class BookController implements Initializable {

	//Variables for bookController's JavaFX objects
	@FXML
	private TableView <BookTable> tblBook;

	@FXML
	private TableColumn<BookTable, String> col_ResourceId;

	@FXML
	private TableColumn<BookTable, String> col_Author;

	@FXML
	private TableColumn<BookTable, String> col_Genre;

	@FXML
	private TableColumn<BookTable, String> col_ISBN;

	@FXML
	private TableColumn <BookTable, String> col_Language;

	@FXML
	private TableColumn <BookTable, String> col_Title;

	@FXML
	private TableColumn <BookTable, String> col_Publisher;
	
	@FXML
	private TableColumn <LaptopTable, String> col_Year;
	
	@FXML
	private TableColumn <LaptopTable, String> col_Copies;

	@FXML
	private Label lblStatus;

	@FXML
	private RadioButton radDVDs;

	@FXML
	private RadioButton radLaptops;
	
	@FXML
	private RadioButton radAll;
	
	@FXML
	private TextField searchBox;
	
	@FXML
	private ImageView imgDashboard;
	
	@FXML
	private Button btnUserDash;

	ObservableList <BookTable> bookList = FXCollections.observableArrayList();

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
			PreparedStatement infoRetrieval = DbCon.prepareStatement("select bok_ResourceId, bok_Author, bok_Genre, bok_ISBN, bok_Language, res_Title, bok_Publisher, res_Year, (SELECT COUNT(rcp_resourcecopy.rcp_CopyId) FROM rcp_resourcecopy WHERE rcp_resourcecopy.rcp_ResourceId = bok_ResourceId) AS bok_CopyCount from bok_book, res_resource where bok_resourceId = res_ResourceId;");
			ResultSet bookInfo = infoRetrieval.executeQuery();
			if (bookInfo.next()){
				while (bookInfo.next()) {
					bookList.add(new BookTable(bookInfo.getInt("bok_ResourceId"), bookInfo.getString("bok_Author"),bookInfo.getString("bok_Genre"),bookInfo.getString("bok_ISBN"), bookInfo.getString("bok_Language"),
							bookInfo.getString("res_Title"), bookInfo.getString("bok_Publisher"), bookInfo.getInt("res_Year"), bookInfo.getInt("bok_CopyCount")));
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

		// Populate TableView cells  the results from the queries
		col_ResourceId.setCellValueFactory(new PropertyValueFactory<>("ResourceId"));
		col_Author.setCellValueFactory(new PropertyValueFactory<>("Author"));
		col_Genre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
		col_ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		col_Language.setCellValueFactory(new PropertyValueFactory<>("Language"));
		col_Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
		col_Publisher.setCellValueFactory(new PropertyValueFactory<>("Publisher"));
		col_Year.setCellValueFactory(new PropertyValueFactory<>("Year"));
		col_Copies.setCellValueFactory(new PropertyValueFactory<>("Copies"));
		tblBook.setItems(bookList);

		tblBook.setOnMousePressed(event -> {
			if (event.getClickCount() == 2 && event.isPrimaryButtonDown() ){
				int resourceId = tblBook.getSelectionModel().getSelectedItem().getResourceId();
				
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
			
			loader.setLocation(getClass().getResource("/javafx/BookInformationView.fxml"));
			
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
			controller.setBookValues(resourceId);
            
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
		if (radDVDs.isSelected()) {
			changeSceneOnDvdSelected(event);
			radDVDs.setSelected(true);
			radDVDs.disarm();
		} else if (radLaptops.isSelected()) {
			changeSceneOnLaptopSelected(event);
			radLaptops.setSelected(true);
			radLaptops.disarm();
		} else if (radAll.isSelected()) {
			changeSceneOnAllSelected(event);
			radLaptops.setSelected(true);
			radLaptops.disarm();
		}
		
		} catch (IOException e) {
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

	/*
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

	public void searchResources(KeyEvent event) {
		//
		FilteredList <BookTable> filterResources = new FilteredList<>(bookList, p -> true);
		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			filterResources.setPredicate(items -> {
				if (newValue == null || newValue.isEmpty()){
					return true;
				}
				String typedText = newValue;
  
    			/*if (items.getResourceId().indexOf(typedText) != -1) {
    				
    				return true;
    			}*/

				if (items.getTitle().toLowerCase().indexOf(typedText) != -1) {

					return true;
				}
				if (items.getAuthor().toLowerCase().indexOf(typedText) != -1) {

					return true;
				}
				if (items.getPublisher().toLowerCase().indexOf(typedText) != -1) {
					return true;
				}
				if (items.getGenre().toLowerCase().indexOf(typedText) != -1) {
					return true;
				}
				if (items.getIsbn().indexOf(typedText) != -1) {
					return true;
				}
				if (items.getLanguage().toLowerCase().indexOf(typedText) != -1) {
					return true;
				}
				return false;
			});
			SortedList <BookTable> sortedList = new SortedList <> (filterResources);
			sortedList.comparatorProperty().bind(tblBook.comparatorProperty());;
			tblBook.setItems(sortedList);
		});
	}

}
