/**
 * Implements the viewing of overdue copies.
 * @author Jack Long (965615)
 * Copyright: No copyright
 * @version 0.1
*/


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ResourceAdditionController implements Initializable {
	

	@FXML private TextField search;
	public String username;
	@FXML private ComboBox<String> loanDuration;
	@FXML private Label title;
	@FXML private Label type;
	@FXML private Label id;
	@FXML private Label selectedDuration;

	private int resourceID;
	private int duration;

	public void getUsername(String usernamee){
		username = usernamee;
	}


	public void displayResource() {
		DatabaseManager db = new DatabaseManager();
		Connection conn = db.getConnection();
		resourceID = Integer.parseInt(search.getText());
		System.out.println(resourceID);
		id.setText(Integer.toString(resourceID));
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * from res_resource where res_ResourceId = "+resourceID);
			ResultSet rs = stmt.executeQuery();
			int resourceType;
			while (rs.next()) {
				System.out.println(rs.getString("res_Title"));
				title.setText(rs.getString("res_Title"));
				resourceType = rs.getInt("res_ResourceType");
				if (resourceType == 0){
					type.setText("Book");
				}else if (resourceType == 2){
					type.setText("Laptop");
				}else{
					type.setText("DVD");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addCopy(ActionEvent event) {
		 if (selectedDuration.getText().equals("1 day")){
		 	duration = 1;
		 }else if(selectedDuration.getText().equals("7 days")){
		 	duration = 7;
		 }else if (selectedDuration.getText().equals("14 days")){
		 	duration = 14;
		 }else if(selectedDuration.getText().equals("31 days")){
		 	duration = 31;
		 }
		DatabaseManager db = new DatabaseManager();
		Connection connection = db.getConnection();
		try {
			// Insert general resource data
			PreparedStatement ps = connection.prepareStatement("insert into rcp_resourcecopy " +
					"(rcp_ResourceID, rcp_loanDuration,rcp_Status) values (?,?,?)");
			ps.setInt(1, resourceID);
			ps.setInt(2, duration);
			ps.setInt(3, 0);
			ps.executeUpdate();

			JOptionPane.showMessageDialog(null, "Copy added successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
			goToCopy(event);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void goToCopy(ActionEvent event){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/javafx/ResourceCopyAddition.fxml"));

			Parent homeWindow = loader.load();
			Scene borrowedItems = new Scene(homeWindow);

			ResourceAdditionController controller=loader.getController();
			controller.getUsername(username);

			// Get the stage
			Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
			window.setScene(borrowedItems);
			window.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedDuration.textProperty().bind(loanDuration.getSelectionModel().selectedItemProperty());
	}
}
