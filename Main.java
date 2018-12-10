/**
 * Main.java
 * Implements the initialisation of the application.
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application {
	
	// The width of the pane
	private static final int sceneX = 600;
	// The height of the pane
	private static final int sceneY = 400;

    /*
     * We can use this to change scenes a bit cleaner if wanted
     *     private static Stage guiStage;
    public static Stage getStage() {
        return guiStage;
    }	*/
	
	/**
	 * Starts the application and load necessary assets.
	 * @param primaryStage the stage object
	 */
	public void start(Stage primaryStage) {
		try {
			// Creates a loader and retrieves the login GUI
			Parent root = FXMLLoader.load(getClass().getResource("/javafx/Login.fxml"));
			primaryStage.setMinHeight(350);
			primaryStage.setMinWidth(375);
			primaryStage.setTitle("Tawe-Lib");
			primaryStage.getIcons().add(new Image("file:javafx\\images\\WindowBarLogo.png"));
			//guiStage = primaryStage;
			Scene loginWindow = new Scene(root, sceneX, sceneY);
			// Set the scene
			primaryStage.setScene(loginWindow);
			// Show the scene
			primaryStage.show();
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

