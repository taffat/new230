import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

/**
* Avatar.java - This class handles the creation of user avatars
* Avatars MUST be 300x300px
* 
* @author Chaye Novak 902037 (1.0), Daniel Clarke 961235 (2.0 onwards)
* - no copyright
* @version 2.4
*/
public class Avatar extends Application {
	private String username;
	public void getUsername(String username){
        this.username = username;
    }
	static User testUser = new User("","Dj", "D", "C", "01234567890", "1 Fake Lane", 
		    "FAKELN", "\\javafx\\images\\avatars\\default\\blank.png", (float) 1.50);
	
	boolean drawingOval = false;
	boolean drawingLine = false;
	double lineX = 0;
	double lineY = 0;
	Image blank = new Image("\\javafx\\images\\avatars\\default\\blank.png");
	Image custom = new Image("\\javafx\\images\\avatars\\custom.png");
	Image defP = new Image("\\javafx\\images\\avatars\\default\\defProfile.png");
	Image defS = new Image("\\javafx\\images\\avatars\\default\\defSunny.png");
	Image defW = new Image("\\javafx\\images\\avatars\\default\\defWater.png");
	Image defT = new Image("\\javafx\\images\\avatars\\default\\defThink.png");
	Image defH = new Image("\\javafx\\images\\avatars\\default\\defHearts.png");
	Image defL = new Image("\\javafx\\images\\avatars\\default\\defLightning.png");
	ImageView preview = new ImageView(blank);

	@Override
	public void start(Stage priStage) {
		
		ListView<Object> avatarList = new ListView<>();
		avatarList.getItems().addAll("Profile","Sunny","Underwater","Thinking","Hearts", "Lightning", "Blank (New Custom)");
		avatarList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		avatarList.setMaxHeight(190);
		avatarList.setMinWidth(200);
		avatarList.setMaxWidth(200);
		
		Button previewBtn = new Button("Preview");
		Button selectBtn = new Button("Select");
		Button editBtn = new Button("Edit");
		Button backToProfile = new Button ("Back");

		preview.setTranslateX(400);
		
		StackPane layout = new StackPane();
		layout.setPadding(new Insets(20, 420, 20, 20));
		StackPane.setAlignment(avatarList, Pos.TOP_LEFT);
		layout.getChildren().addAll(avatarList);
		
		StackPane.setAlignment(previewBtn, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(selectBtn, Pos.BOTTOM_LEFT);
		selectBtn.setTranslateX(60);
		StackPane.setAlignment(editBtn, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(backToProfile, Pos.BOTTOM_LEFT);
		editBtn.setTranslateX(111);
		backToProfile.setTranslateX(151);
		layout.getChildren().addAll(previewBtn, selectBtn, editBtn, backToProfile);
		
		StackPane.setAlignment(preview, Pos.TOP_RIGHT);
		layout.getChildren().addAll(preview);
		
		backToProfile.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/javafx/UserDashboard.fxml"));
					
		            Parent dashboard = loader.load();
		            Scene profile = new Scene(dashboard);
					
		            DashboardController controller = loader.getController();
		            username = StaticUserInfo.getUsername();
		            controller.getUsername(username);
		            
					// Get the stage
		            Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
					window.setScene(profile);
					window.show();
					
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		
		previewBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String selection = (String) avatarList.getSelectionModel().getSelectedItem();
				if (selection.equals("Profile")) {
					preview.setImage(defP);
				} else if (selection.equals("Sunny")) {
					preview.setImage(defS);
				} else if (selection.equals("Underwater")) {
					preview.setImage(defW);
				} else if (selection.equals("Thinking")) {
					preview.setImage(defT);
				} else if (selection.equals("Hearts")) {
					preview.setImage(defH);
				} else if (selection.equals("Lightning")) {
					preview.setImage(defL);
				} else if (selection.equals("Blank (New Custom)")) {
					preview.setImage(blank);
				}
				
			}
		});
		
		selectBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if (preview.getImage() != custom) {
					String selection = (String) avatarList.getSelectionModel().getSelectedItem();
					if (selection.equals("Profile")) {
						testUser.setAvatarFilePath("\\javafx\\images\\avatars\\default\\defProfile.png");
						priStage.close();
					} else if (selection.equals("Sunny")) {
						testUser.setAvatarFilePath("\\javafx\\images\\avatars\\default\\defSunny.png");
						priStage.close();
					} else if (selection.equals("Underwater")) {
						testUser.setAvatarFilePath("\\javafx\\images\\avatars\\default\\defWater.png");
						priStage.close();
					} else if (selection.equals("Thinking")) {
						testUser.setAvatarFilePath("\\javafx\\images\\avatars\\default\\defThink.png");
						priStage.close();
					} else if (selection.equals("Hearts")) {
						testUser.setAvatarFilePath("\\javafx\\images\\avatars\\default\\defHearts.png");
						priStage.close();
					} else if (selection.equals("Lightning")) {
						testUser.setAvatarFilePath("\\javafx\\images\\avatars\\default\\defLightning.png");
						priStage.close();
					} else if (selection.equals("Blank (New Custom)")) {
						if (preview.getImage() == blank) {
							editImage(blank);
						} else {
							testUser.setAvatarFilePath("\\javafx\\images\\avatars\\custom.png");
							priStage.close();
						}
					}
				} else {
					testUser.setAvatarFilePath("\\javafx\\images\\avatars\\custom.png");
					priStage.close();
				}
			}
		});
		
		editBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String selection = (String) avatarList.getSelectionModel().getSelectedItem();
				if (selection.equals("Profile")) {
					editImage(defP);
				} else if (selection.equals("Sunny")) {
					editImage(defS);
				} else if (selection.equals("Underwater")) {
					editImage(defW);
				} else if (selection.equals("Thinking")) {
					editImage(defT);
				} else if (selection.equals("Hearts")) {
					editImage(defH);
				} else if (selection.equals("Lightning")) {
					editImage(defL);
				} else if (selection.equals("Blank (New Custom)")) {
					editImage(blank);
				}
			}
		});
		
		Scene scene2 = new Scene(layout, 560, 340);
		priStage.setScene(scene2);
		priStage.show();
	}


	public static void main(String[] args){
		System.out.println(testUser.getAvatarFilePath());
		launch(args);
		System.out.println(testUser.getAvatarFilePath());

	}
	
	private void editImage(Image imageIn) {
		Stage secStage = new Stage();
		Path path = new Path();
		path.setStrokeWidth(1);
		Canvas canvas = new Canvas(300,300);
		canvas.setFocusTraversable(true);
		canvas.requestFocus();
		GraphicsContext gContext = canvas.getGraphicsContext2D();
		Button saveBtn = new Button("Save Image");

		gContext.setLineWidth(2);
		
		canvas.setOnKeyPressed(
				new EventHandler<KeyEvent>() {
			double xPosStart;
			double yPosStart;
			double xPosEnd;
			double yPosEnd;
			double xMidPos;
			double yMidPos;
			double width;
			double height;
			@Override
			public void handle(KeyEvent event){
				if (event.getCode() == KeyCode.CONTROL){
					System.out.println("CONTROL SEEN");
					drawingOval = true;
					canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
							new EventHandler<MouseEvent>(){
						public void handle(MouseEvent event) {
							path.setVisible(false);
							xPosStart = event.getX();
							yPosStart = event.getY();
						}
					});
					canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
							new EventHandler<MouseEvent>(){
						@Override
						public void handle(MouseEvent event) {
							xPosEnd = event.getX();
							yPosEnd = event.getY();
							if (xPosEnd >= xPosStart) {
								width = xPosEnd-xPosStart;
								xMidPos = xPosStart;
							} else {
								width = xPosStart-xPosEnd;
								xMidPos = xPosEnd;
							}
							if (yPosEnd >= yPosStart) {
								height = yPosEnd-yPosStart;
								yMidPos = yPosStart;
							} else {
								height = yPosStart-yPosEnd;
								yMidPos = yPosEnd;
							}
							System.out.println("xstart, ystart, xend, yend: " + xPosStart + " " + yPosStart + " " + xPosEnd + " " + yPosEnd);
							System.out.println("xmid, ymid, width, height: " + xMidPos + " " + yMidPos + " " + width + " " + height);
							path.setVisible(true);
							gContext.setFill(Color.GREY);
							gContext.strokeOval(xMidPos, yMidPos, width, height);
							gContext.fillOval(xMidPos, yMidPos, width, height);
							drawingOval = false;
						}
					});
				}
			}
		});
		
		if (!drawingOval) {
			canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
					new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2){ // Double click for line start/stop points
						if (drawingLine) {
	                        gContext.strokeLine(lineX, lineY, event.getSceneX(), event.getSceneY());
						} else {
							lineX = event.getSceneX();
							lineY = event.getSceneY();
						}
						drawingLine = !drawingLine;
					} else {
						gContext.moveTo(event.getX(), event.getY()); // Click + drag for freehand
						gContext.stroke();
					}
				}
			});
		
			canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
					new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event) {
					gContext.lineTo(event.getX(), event.getY());
					gContext.stroke();
				}
			});
		}
		
		StackPane sPane = new StackPane();
		sPane.getChildren().setAll(
				new ImageView(imageIn));
		
		sPane.getChildren().add(canvas);
	    saveBtn.setTranslateY(130);
		secStage.setTitle("Draw Avatar");
		sPane.getChildren().add(saveBtn);
		Scene scene = new Scene(sPane, 300, 300);
		secStage.setScene(scene);
		secStage.show();
		
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				saveBtn.setVisible(false);
				WritableImage img = sPane.snapshot(new SnapshotParameters(), null);
				// State file directory to save to + name of image
				// Included mine as example
				File output = new File("\\\\tawe_dfs\\students\\9\\912639\\Documents\\YEAR 2\\sosoclose\\src\\javafx\\avatars\\" + "custom.png");
			    BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
				try{
					ImageIO.write((RenderedImage) bImage, "png", output);
					secStage.close();
					custom = img;
					preview.setImage(custom);
				} catch (IOException ioe) {
					throw new RuntimeException(ioe);
				}
			}
		});
		
	}
	
}

