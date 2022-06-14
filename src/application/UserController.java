package application;

import java.net.URL;

import java.sql.*;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import application.Database.ConnectionBD;



public class UserController implements Initializable {
	
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;
	Stage stage = new Stage();
	
	public static String nom = null;

	@FXML private TextField userName;

	@FXML private PasswordField passWord;

	@FXML private Button btnC;

	
	@Override
	public void initialize(URL localisation, ResourceBundle ressource) {

	}
	
	
	@FXML
	void close_app(MouseEvent event) {
		System.exit(0);
	}

	/*@FXML
	void goto_ins(MouseEvent event) {
		s_inscrire(null);

	}

	@FXML
	void s_inscrire(ActionEvent event) {
		
		try {
			Parent fxml_ins = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.PageInscription));

			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(fxml_ins));
			stage.show();

		} catch (IOException e) {
			new Alert(Alert.AlertType.ERROR, "Affichage erreur s_inscrire").showAndWait();
		}

	}*/
	

	@FXML
	void se_connecter(ActionEvent event){
		
		conn = ConnectionBD.CheckConnection();
    	/*SecretKey cle = ChiffrerPassword.generateur();

    	ArrayList<String> allPassWordsList = ChiffrerPassword.tousPassword();
    	ArrayList<String> allPassWordsDecrypt = new ArrayList<String>();
    	String lesD = null;
    	
    	for (String ele : allPassWordsList) {
    	      System.out.println(ele);
    		try {
				lesD = ChiffrerPassword.decrypt(ele, cle);
				System.out.println(lesD);
			} catch (Exception e) {
				System.out.println("decrypt " + e);
				lesD = "rien";

			}
    		allPassWordsDecrypt.add(lesD);
    		System.out.println(allPassWordsDecrypt);
    	}*/

		if (!isFieldsEmpty()) {
			

			String sql = "SELECT * FROM users WHERE UserName='" + userName.getText() + "' and Password ='" + passWord.getText() + "';";
			try {
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if (rs.first()) {

					nom = rs.getString(1); //Recuperer le nom de l'administrateur qui s'est connecté
					new application.ARTICLES.ArticleController().goto_Menu(event); //afficher le dashboard
					
					rs.close();
					st.close();
					conn.close();

				} else {
					new Alert(Alert.AlertType.ERROR, "Identifiants inconnus").showAndWait();
					userName.clear();
					passWord.clear();

				}

			} catch (Exception e) {
				new Alert(Alert.AlertType.ERROR, "Mauvaise recupération des données saisies").showAndWait();
				passWord.clear();

			}
			
		} else {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
			alert.setContentText("Veuillez remplir tous les champs de saisies pour vous connecter à l'application.");
			alert.setHeaderText("Champs de saisie vides !");
			alert.setTitle("Erreur");
			alert.showAndWait();
		}

	}

	// Tester les champs vides
	boolean isFieldsEmpty() {
		
		if (("".equals(userName.getText())) || ("".equals(passWord.getText()))) {
			System.out.println("Champs vides");
			return true;

		} else {
			return false;
		}
	}


}
