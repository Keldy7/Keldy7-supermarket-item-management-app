package application.Extras.Categories_Rayons;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import application.CheminFXML.RouteInterfaceFXML;


public class CategorieRayonController implements Initializable{
	Stage stage = new Stage();

	@FXML private Button btnCat;

    @FXML private Button btnRay;

    @FXML private Hyperlink menu;

    
    @Override
	public void initialize(URL loc, ResourceBundle res) {
		
	}
    
    
    @FXML public void form_Cat(ActionEvent event) {
    	try {
			
			Parent categorie = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueCategorie));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(categorie));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
    }

    
    @FXML public void form_Ray(ActionEvent event) {
    	try {
			
			Parent rayon = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueRayon));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(rayon));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}

    }

    @FXML void goto_Menu(ActionEvent event) {
		new application.ARTICLES.ArticleController().goto_Menu(event);

    }

}
