package application.Actions_Admin.Add_new_user;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import application.CheminFXML.RouteInterfaceFXML;
import application.Database.ChiffrerPassword;
import application.Database.ConnectionBD;

public class AddUserController implements Initializable {
	
	Stage stage = new Stage();
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);

	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

    @FXML private PasswordField confirm_newPwd;

    @FXML private TextField newName_user;

    @FXML private PasswordField newPwd;

    @FXML private Pane pane_Anchor;

    @FXML private TextField ton_pwd;
    
   

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
    
    
    @FXML
    void clearFields(ActionEvent event) {
    	ton_pwd.clear();
    	newName_user.clear();
    	newPwd.clear();
    	confirm_newPwd.clear();
    }

    @FXML
    public void close_vue(MouseEvent event) {
    	try {
			Parent dash = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueDashboard));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(dash));
			stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); //Fermer l'interface precedente


		} catch (Exception erreur) {
			System.out.println("fermer modifPwd " + erreur);
		}
    }

    @FXML
    void save_User(ActionEvent event) {
    	
    	conn = ConnectionBD.CheckConnection();
    	ArrayList<String> allPassWordList = ChiffrerPassword.tousPassword();
    	
    	if (!isFieldsEmpty()) {
    		
    		if (allPassWordList.contains(ton_pwd.getText())) {
    			
    			if ((newPwd.getText()).equals(confirm_newPwd.getText())) {
    				
        			String sql = "INSERT INTO users(UserName, Password, id) VALUES ('" + newName_user.getText() 
    				+ "','" + confirm_newPwd.getText() + "', NULL);";
    				try {
    		        	st = conn.createStatement();
    			            st.executeUpdate(sql);
    		
    			            new Alert(Alert.AlertType.INFORMATION, "Informations enregistrées avec succès").showAndWait();
    			            clearFields(event);
    			            
    			            st.close();
    			            conn.close();
    		         }
    		         catch(Exception exception){
    		        	 System.out.println(exception);
    		         	 new Alert(Alert.AlertType.ERROR, "Informations non enregistrées.").showAndWait();
    		         }
    		        
    			}else{
    				new Alert(Alert.AlertType.ERROR, "Nouveau mot de passe non identique à celui de la confirmation.").showAndWait();
    				clearFields(event);
    				
    			}
        	}else{
    			new Alert(Alert.AlertType.ERROR, "Votre mot de passe saisi est incorrect").showAndWait();
				ton_pwd.clear();

            }        		
    	
    	}else{

			alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer un enregistrement dans la base de données.");
			alerte.setHeaderText("Champs de saisie vides !");
			alerte.setTitle("Erreur");
			alerte.showAndWait();
		}
    		
    	
    	/*ArrayList<String> allPassWordList = ChiffrerPassword.tousPassword();
    	
        if (!isFieldsEmpty()) {

        	if (allPassWordList.contains(ton_pwd.getText())) {
        		
        		if ((newPwd.getText()).equals(confirm_newPwd.getText())) {
                	SecretKey cle = ChiffrerPassword.generateur();
                	String mdpCryp = null;

                	try {
    					mdpCryp = ChiffrerPassword.encrypt(confirm_newPwd.getText(), cle);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}

            		String sql = "INSERT INTO users(UserName, Password, id) VALUES ('" + newName_user.getText() 
            				+ "','" + mdpCryp + "', NULL);";
            		try {
                    	st = conn.createStatement();
         	            st.executeUpdate(sql);

         	            new Alert(Alert.AlertType.INFORMATION, "Informations enregistrées avec succès").showAndWait();
         	            clearFields(event);
         	            
         	            st.close();
         	            conn.close();
                     }
                     catch(Exception exception){
                    	 System.out.println(exception);
                     	new Alert(Alert.AlertType.ERROR, "Informations non enregistrées.").showAndWait();
                     }
                    
            	}else{
    				new Alert(Alert.AlertType.ERROR, "Nouveau mot de passe non identique à celui de la confirmation.").showAndWait();
            		
            	}
        	}
        	
        }else{
			new Alert(Alert.AlertType.ERROR, "Votre mot de passe saisi est incorrect").showAndWait();

        }*/
    }

    
  //Tester les champs vides
    boolean isFieldsEmpty() {
        
        if (ton_pwd.getText().isBlank() || newName_user.getText().isBlank() 
        		|| newPwd.getText().isBlank() 
        		|| confirm_newPwd.getText().isBlank() ) {
            return true;
            
        } else {
            return false;
        }
    }

}


