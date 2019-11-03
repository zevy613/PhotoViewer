import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class PhotoViewer  extends Application {
     
    private ImageView centerStage;
    private ImageView leftButton;
    private ImageView rightButton;
    private File photos = new File("C:\\Users\\default.DESKTOP-0LDO0LD\\Pictures\\Saved Pictures");
    private BorderPane pane = new BorderPane();
    private StackPane sPane = new StackPane();
    private Scene scene;
    private HBox forPhotos =  new HBox();
    private MenuBar menuBar = new MenuBar();
    private Menu fileMenu = new Menu("File");
    private Menu editMenu = new Menu("Edit");  
    private DirectoryChooser dc = new DirectoryChooser();
    protected Window ownerWindow;
    private Insets inPhotos = new Insets(10);
    private int index = 0;
    private ArrayList<ImageView> imageViewArray = new ArrayList();
    private ArrayList<Image> imageArray = new ArrayList();

    @Override
    public void start(Stage primaryStage) {
        
        scene = new Scene(pane, 800, 600);
        pane.setStyle("-fx-background-color: white;");
        primaryStage.setTitle("Viewer");
        // primaryStage.setMinWidth(1000);
//         primaryStage.setMinHeight(800);
        primaryStage.setScene(scene);
        primaryStage.show(); 
        
        setUpMenu();
        setUpBottom(photos);
        setUpLeftRight(); 
    }
    
    public void setUpMenu() {
        MenuItem open = new MenuItem("Open");
        MenuItem exit = new MenuItem("Exit");
        MenuItem rotateLeft = new MenuItem("Rotate Left");
        MenuItem rotateRight = new MenuItem("Rotate Right");


        
        open.setOnAction((ActionEvent e) -> {
            photos = dc.showDialog(ownerWindow);
            setUpBottom(photos);
        });
        exit.setOnAction(e -> {
            Platform.exit();
        });
        rotateLeft.setOnAction(e -> {
            centerStage.setRotate(centerStage.getRotate() - 90);
        });
        rotateRight.setOnAction(e -> {
            centerStage.setRotate(centerStage.getRotate() + 90);
        });
        
        fileMenu.getItems().addAll(open, exit);
        editMenu.getItems().addAll(rotateLeft, rotateRight);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        pane.setTop(menuBar);
    }
    
    public void setUpCenter(Image image) {
      
        centerStage = new ImageView(image);
        centerStage.setRotate(0);
        centerStage.setPreserveRatio(true);
        centerStage.setFitHeight(400);
        centerStage.setFitWidth(400);
        sPane.getChildren().clear();
        sPane.getChildren().add(centerStage);
        sPane.setAlignment(Pos.CENTER);
        sPane.setPrefHeight(400);
        sPane.setPrefWidth(400);
        pane.setCenter(sPane);
    }
    
    public void setUpBottom(File photos1) {
        
        File[] photoList = photos1.listFiles();
        ArrayList <File> fileArray = new ArrayList(Arrays.asList(photoList));
        forPhotos.getChildren().clear();
        imageViewArray.clear();
        imageArray.clear();
        
        try {
            for(int i = 0; i < fileArray.size(); i++) {
                imageArray.add(new Image(new FileInputStream(fileArray.get(i))));
            }
        }
        catch(FileNotFoundException ex) {
        }
        
        for(int i = 0; i < imageArray.size(); i++) {
            imageViewArray.add(new ImageView(imageArray.get(i)));
            imageViewArray.get(i).setPreserveRatio(true);
            imageViewArray.get(i).setFitHeight(100);
            imageViewArray.get(i).setFitWidth(100);
            Image temp = imageArray.get(i);
            int tempint = i;
            imageViewArray.get(i).setOnMouseClicked(e -> {
                index = tempint;
                setUpCenter(temp);
                rotateBottom(index);
            });
        }
        for(int i = 0; i < 5; i++) {
            VBox vbox = new VBox();
            vbox.getChildren().add(imageViewArray.get(i));
            vbox.setPadding(inPhotos);
            forPhotos.getChildren().add(vbox);
            forPhotos.setAlignment(Pos.CENTER);
            pane.setBottom(forPhotos);
        }
        setUpCenter(imageArray.get(0));
    }
    
    public void setUpLeftRight() {
        
    	leftButton = new ImageView (new Image("left_arrow.png"));
        leftButton.setFitWidth(100);
        leftButton.setFitHeight(100);
        leftButton.setPreserveRatio(true);
        HBox hbox = new HBox();
        hbox.setPrefHeight(100);
        hbox.setPrefWidth(100);
    	hbox.getChildren().add(leftButton);
    	hbox.setAlignment(Pos.CENTER);
        
    	leftButton.setOnMouseClicked(e -> {
            if(index >0) {
                index--;
                Image temp = imageArray.get(index);
                setUpCenter(temp);
                rotateBottom(index);
            }
            else if(index == 0) {
                index = imageArray.size()-1;
                Image temp = imageArray.get(index);
                setUpCenter(temp);
                rotateBottom(index);
            }
    	});
        
    	rightButton = new ImageView (new Image("right_arrow.png"));
    	rightButton.setFitHeight(100);
        rightButton.setFitWidth(100);
        rightButton.setPreserveRatio(true);
        HBox hbox1 = new HBox();
        hbox1.setPrefHeight(100);
        hbox1.setPrefWidth(100);
    	hbox1.getChildren().add(rightButton);
    	hbox1.setAlignment(Pos.CENTER);
        
        rightButton.setOnMouseClicked(e -> { 
            
            if(index < imageViewArray.size()-1) {
                index++;
                Image temp = imageArray.get(index);
                setUpCenter(temp);
                rotateBottom(index);
            }
            else if(index == imageViewArray.size()-1) {
                index = 0;
                Image temp = imageArray.get(index);
                setUpCenter(temp);
                rotateBottom(index);
            }
    	});
        
        pane.setLeft(hbox);
    	pane.setRight(hbox1);   	
    }
    
    public void rotateBottom(int index) {
        if(index < imageViewArray.size()-4){
            forPhotos.getChildren().clear();
            for(int i = index; i < index+5; i++) {
                    VBox vbox = new VBox();
                    vbox.getChildren().add(imageViewArray.get(i));
                    vbox.setPadding(inPhotos);
                    forPhotos.getChildren().add(vbox);
                    forPhotos.setAlignment(Pos.CENTER);
                    pane.setBottom(forPhotos);
            }
        }
    }
    
    public static void main(String[] args)
    {
       launch(args);
    }
}