package application.LoadingPage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import application.CheminFXML.RouteInterfaceFXML;


public class LoadingPageController implements Initializable {
	
	@FXML private ProgressBar progressBar;
	
	@FXML private ImageView ImagLoading;
	
	double progress;
	Stage stage = new Stage();

	@Override
	public void initialize(URL localisation, ResourceBundle ressource) {
		/*PauseTransition tmps = new PauseTransition(Duration.seconds(5));
        tmps.setOnFinished((ActionEvent action) -> {

            System.out.println("Début");
            affich_formConn(action);
        });
        tmps.play();*/
		//augmenterBar();

		progressBar.setStyle("-fx-accent: #00FF00;");
		while (progress < 1) {
			progress += 0.1;
		    progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
			//progressBar.setProgress(progress);
			
			PauseTransition temps = new PauseTransition(Duration.seconds(2));
			temps.setOnFinished((ActionEvent action) -> {
				affich_formConn(action);
	        });
			temps.play();
			
		}
		
	}
	
	void affich_formConn(ActionEvent event) {
		try {
			Parent dashboard = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.PageConnexion));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(dashboard));
			stage.show();
			
			dashboard.requestFocus();
            ((Stage) ImagLoading.getScene().getWindow()).close();
            ((Stage) ((Stage) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			System.out.print("");
		}
	}

}
