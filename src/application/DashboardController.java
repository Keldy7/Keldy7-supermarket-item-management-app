package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.ARTICLES.Article;
import application.CheminFXML.RouteInterfaceFXML;
import application.Database.ConnectionBD;
import application.Extras.Recus_Commandes.ACHATS.Acheter;


public class DashboardController implements Initializable {
	
	@FXML private AnchorPane anchor_Pane;
	
	@FXML private Button btnAdd_user;
	
	@FXML private Button btnArt;
	
	@FXML private Button btnCai;
	
	@FXML private Button btnRecCom;
	
	@FXML private Button btnCli;
	
	@FXML private Button btnRechercher;
	
	@FXML private Button btnFou;
	
	@FXML private Button btnCatRay;
	
	@FXML private Button btnPasswd;
		
	@FXML private Label nomAdmin;
	
	@FXML private ProgressIndicator progressIndicateur;
	
	Stage stage = new Stage();
	
	@Override
	public void initialize(URL loc, ResourceBundle rb) {
	
		//listeActions.setStyle("-fx-font-family : Aharoni; -fx-background-color:#ffd95e;");
		//listeActions.getItems().setAll("Modifier votre mot de passe", "Inscrire un nouvel utilisateur");
		nomAdmin.setText(UserController.nom);	
		btnPasswd.setDisable(false);
		btnAdd_user.setDisable(false);
		
		List<Acheter> articlesAchetes = ConnectionBD.getAchats();
		List<Article> articlesTotal = ConnectionBD.getArticles();

	    System.out.println("Total des articles achetes: " + articlesAchetes.size() + " Total des articles: " + articlesTotal.size());
        double achat_Pourcentage = (articlesAchetes.size()), article_Pourcentage = (articlesTotal.size());
    	progressIndicateur.setStyle("-fx-accent: #fdc357;");
        progressIndicateur.setProgress(achat_Pourcentage / article_Pourcentage);	

	}
	
	 
	/*@FXML
    void les_Actions(MouseEvent event) {
		System.out.println("dedans");
		Parent root ;
		if (listeActions.getSelectionModel().getSelectedItem() == "Modifier votre mot de passe") {
			System.out.println("aahh "+listeActions.getSelectionModel().getSelectedItem());
			try {
				root = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueRecuCommande));
				Scene scene_Modif = new Scene(root);
				
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.setScene(scene_Modif);
				stage.show();
	            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente

				
			} catch (IOException e) {
				System.out.println("Modif" + e);			
			}
			
		}else {
			System.out.println("oohh "+ listeActions.getSelectionModel().getSelectedItem());

			try {
				root= FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.PageInscription));
				Scene scene_Ins = new Scene(root);
				
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.setScene(scene_Ins);
				stage.show();
	            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente

				
			} catch (IOException e) {
				System.out.println("Ins" + e);			
			}
		}
    }*/
	
	/*public static void main(String[] args) { 
		 
		BigDecimal num = new BigDecimal(2718.28);
 
		System.out.println(toLetter(num)); 
 
 
	}
 
	private static final DecimalFormat DFORMAT = new DecimalFormat("###0.00");
	private static final NumberFormat FORMATTER = new RuleBasedNumberFormat(RuleBasedNumberFormat.SPELLOUT);
	private static String toLetter(BigDecimal num) {
		String[] s = DFORMAT.format(num).split(Pattern.quote(String.valueOf(DFORMAT.getDecimalFormatSymbols().getDecimalSeparator())));
		BigInteger intPart = new BigInteger(s[0]);
		if ( s.length==1 ) {
			return FORMATTER.format(intPart);
		}
		else {
			BigInteger decPart = new BigInteger(s[1]);
			return FORMATTER.format(intPart)
					// pour les parties fixes il faudrait faire un resourcebundle
				 + " euro" 
				 + (intPart.intValue()>1?"s":"")
			     + " et " 
			     + FORMATTER.format(decPart)
			     + " cent"
				 + (decPart.intValue()>1?"s":"");
		}
	}
	
	*
	*
	*
	*import com.ibm.icu.math.BigDecimal;
	import com.ibm.icu.text.NumberFormat;
	import com.ibm.icu.text.RuleBasedNumberFormat;
	 
	public class Demo {
		public static void main(String[] args) { 
	 
			BigDecimal num = new BigDecimal(2718.28);
			NumberFormat formatter = 
			    new RuleBasedNumberFormat(RuleBasedNumberFormat.SPELLOUT);
			String result = formatter.format(num);
			System.out.println(result);
	 
		}
}*/
	
    
    @FXML
    void add_Newuser(ActionEvent event) {
		
		try {
			Parent newUser = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.PageInsc_NewUser));
			btnAdd_user.setDisable(true);
			btnPasswd.setDisable(false);
			btnRechercher.setDisable(false);

			anchor_Pane.getChildren().removeAll();
			anchor_Pane.getChildren().setAll(newUser);				

		} catch (Exception erreur) {
			System.out.println("newUser " + erreur);
		}
    }
	
	
	@FXML
    void goto_Modifpwd(ActionEvent event) {
		
		try {
			Parent modif = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.PageModif));
			btnPasswd.setDisable(true);
			btnAdd_user.setDisable(false);
			btnRechercher.setDisable(false);
			
			anchor_Pane.getChildren().removeAll();
			anchor_Pane.getChildren().setAll(modif);				

		} catch (Exception erreur) {
			System.out.println("modifpwd " + erreur);
		}
    }
	
	
	@FXML
    void goto_DoSearch(ActionEvent event) {
		
		try {
			Parent rech = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.PageRechercher));
			btnPasswd.setDisable(false);
			btnAdd_user.setDisable(false);
			btnRechercher.setDisable(true);
			
			anchor_Pane.getChildren().removeAll();
			anchor_Pane.getChildren().setAll(rech);				

		} catch (Exception erreur) {
			System.out.println("doSearch " + erreur);
		}
    }
	
	
	
	//Afficher le formulaire Client
	@FXML public void form_Cli(ActionEvent event){
		
		try {
			Parent client = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueClient));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(client));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
		
	}
	
	//Afficher le formulaire Article
	@FXML public void form_Art(ActionEvent event) {
			
		try {
			Parent article = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueArticle));
			
			//stage.initModality(Modality.APPLICATION_MODAL);// on ne peut pas ecrire dans les autres fenetres
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(article));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
	}
	
	//Afficher le formulaire Recu Acheter Livrer et CommandeF
	@FXML void form_RecCom(ActionEvent event) {
		try {
			
			Parent RecCom = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueRecuCommande));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(RecCom));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}	
	}
	
	//Afficher le formulaire Caisse
	@FXML public void form_Cai(ActionEvent event) {
		try {
			
			Parent caisse = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueCaisse));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(caisse));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
	}
	
	//Afficher le formulaire Fournisseur
	@FXML public void form_Fourn(ActionEvent event) {
		try {
			
			Parent fournisseur = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueFournisseur));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(fournisseur));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
		
	}
	
	
	//Afficher le formulaire Categorie et Rayon
	@FXML void form_CatRay(ActionEvent event) {
		try {
			
			Parent CatRay = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueCategRayon));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(CatRay));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
		
	}
	
	
	//Fermer la session ouverte et afficher le formulaire de connexion
	@FXML
    void deConnexion(MouseEvent event) {
		try {
			Parent connect = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.PageConnexion));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(connect));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
    }
	
	
}
