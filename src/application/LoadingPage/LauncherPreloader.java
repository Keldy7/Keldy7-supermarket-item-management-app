package application.LoadingPage;

import application.CheminFXML.RouteInterfaceFXML;
import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LauncherPreloader extends Preloader  {
	private Stage preloaderStage;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.preloaderStage = stage;
		Parent loading = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.BootPage));
		
		stage.setScene(new Scene(loading));
		stage.show();
	}
	
	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		if (info.getType() == Type.BEFORE_START) {
			preloaderStage.hide();
		}
	}

}
