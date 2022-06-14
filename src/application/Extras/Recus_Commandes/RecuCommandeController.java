package application.Extras.Recus_Commandes;

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
import application.DashboardController;
import application.CheminFXML.RouteInterfaceFXML;
import application.Extras.Categories_Rayons.CategorieRayonController;


public class RecuCommandeController implements Initializable{
	Stage stage = new Stage();
	
	@FXML private Button btnAch;

	@FXML private Button btnCom;
	
	@FXML private Button btnLiv;

    @FXML private Button btnRec;

    @FXML private Hyperlink menu;
    
    @Override
	public void initialize(URL loc, ResourceBundle res) {
		
	}

    @FXML void form_Rec(ActionEvent event) {
    	try {
			
			Parent recu = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueRecu));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(recu));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
    }

    
    @FXML void form_Com(ActionEvent event) {
    	try {
			
			Parent commandeF = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueCommande_Fournisseur));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(commandeF));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}

    }
    
    @FXML void form_Ach(ActionEvent event) {
    	try {
			
			Parent acheter = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueAcheter));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(acheter));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
    }
    
    
    @FXML void form_Liv(ActionEvent event) {
    	try {
			
			Parent livrer = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueLivrer));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(livrer));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
    }
    
    @FXML void goto_Menu(ActionEvent event) {
		new application.ARTICLES.ArticleController().goto_Menu(event);

    }
    
    
    @FXML
    void go_formArt(ActionEvent event) {
    	new DashboardController().form_Art(event);
    	
    }

    @FXML
    void go_formCai(ActionEvent event) {
    	new DashboardController().form_Cai(event);
    	
    }
    
    
    @FXML
    void go_formCli(ActionEvent event) {
    	new DashboardController().form_Cli(event);
    
    }
    
    @FXML
    void go_formFour(ActionEvent event) {
    	new DashboardController().form_Fourn(event);
    	
    }
    
    
    @FXML
    void go_formRay(ActionEvent event) {
    	new CategorieRayonController().form_Ray(event);
    	
    }
    
    
    @FXML
    void go_formCat(ActionEvent event) {
    	new CategorieRayonController().form_Cat(event);
    	
    }


}
