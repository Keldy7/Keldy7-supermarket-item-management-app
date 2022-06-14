package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

import application.CheminFXML.RouteInterfaceFXML;


public class Main extends Application {
    
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		try {
			
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.BootPage));  
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//LauncherImpl.launchApplication(Main.class,LauncherPreloader.class, args);
		launch(args);
	}
}
