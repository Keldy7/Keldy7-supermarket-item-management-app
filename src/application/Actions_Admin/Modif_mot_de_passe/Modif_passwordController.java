package application.Actions_Admin.Modif_mot_de_passe;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import application.UserController;
import application.Database.ChiffrerPassword;
import application.Database.ConnectionBD;


public class Modif_passwordController implements Initializable{

	@FXML private Pane pane_Anchor;
	
	@FXML private TextField ancien_pwd;

    @FXML private PasswordField confirm_pwd;

    @FXML private PasswordField new_pwd;
	
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);

	Connection conn = null;
	Statement st = null;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ancien_pwd.requestFocus();		
	}
	
	
	@FXML
	void close_vue(MouseEvent event) {
		new application.Actions_Admin.Add_new_user.AddUserController().close_vue(event);
		
	}

	
	@FXML
    void clearFields(ActionEvent event) {
		ancien_pwd.clear();
		new_pwd.clear();
		confirm_pwd.clear();
    }
	
	@FXML
    void modif_Password(ActionEvent event) {

   	    conn = ConnectionBD.CheckConnection();
    	ArrayList<String> allPassWordList = ChiffrerPassword.tousPassword();


        if (!isFieldsEmpty()) {

        	if (allPassWordList.contains(ancien_pwd.getText())) {
        		
        		if ((new_pwd.getText()).equals(confirm_pwd.getText())) {
        			
            		String sql = "UPDATE users SET Username='" + UserController.nom 
            				+ "' ,Password='" + confirm_pwd.getText() 
            				+ "' WHERE Password='" + ancien_pwd.getText() + "';";
            		
            		try {
                    	st = conn.createStatement();
         	            st.executeUpdate(sql);

         	            new Alert(Alert.AlertType.INFORMATION, "Mot de passe modifié avec succès").showAndWait();
         	            clearFields(event);
         	            
         	            st.close();
         	            conn.close();
                     
            		}catch(Exception exception){
                    	 System.out.println(exception);
                     	new Alert(Alert.AlertType.ERROR, "Mauvaise modification du mot de passe").showAndWait();
                    }
                    
            	}else{
    				new Alert(Alert.AlertType.ERROR, "Le nouveau mot de passe n'est pas pareil que celui de la confirmation.").showAndWait();
     	            clearFields(event);
            		
            	}
        		
        	}else{
    			new Alert(Alert.AlertType.ERROR, "Votre ancien mot de passe saisi est incorrect").showAndWait();
				ancien_pwd.clear();

            }    
    	
        } else {

			alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une modification de votre mot de passe dans la base de données.");
			alerte.setHeaderText("Champs de saisie vides !");
			alerte.setTitle("Erreur");
			alerte.showAndWait();
		}

    }

	//Tester les champs vides
    boolean isFieldsEmpty() {
        
        if (ancien_pwd.getText().trim().isBlank() || new_pwd.getText().trim().isBlank() || ("".equals(confirm_pwd.getText()))) {
            return true;
            
        } else {
            return false;
        }
    }
}
