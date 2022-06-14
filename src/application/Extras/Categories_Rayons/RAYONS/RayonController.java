package application.Extras.Categories_Rayons.RAYONS;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import application.Database.ConnectionBD;



public class RayonController implements Initializable{
	
	static Connection conn = null;
	static ResultSet rs = null;
	static Statement st = null;
	
	Stage stage = new Stage();
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);

    ObservableList<Rayon> liste;
	 int index = -1;
	
	/*---------------Les boutons-------------------*/
	
	@FXML private Button btnDernier;

    @FXML private Button btnPrecedent;

    @FXML private Button btnPremier;

    @FXML private Button btnSuivant;

    @FXML private Button btn_ajout;

    @FXML private Button btn_modif;

    @FXML private Button btn_nouveau;

    @FXML private Button btn_sup;
    
	/*---------------Les zones de saisie-------------------*/

    @FXML private TextField textFieldCode;

    @FXML private TextField textFieldLib;
    
    @FXML private TextField textFieldNplace;

    
    /*-----------Les colonnes de TableView----------------*/


    @FXML private TableView<Rayon> listeRay;

    @FXML private TableColumn<Rayon, Integer> idR;

    @FXML private TableColumn<Rayon, String> libR;

    @FXML private TableColumn<Rayon, Integer> nplace;

    @FXML private Hyperlink menu;

    @Override
	public void initialize(URL url, ResourceBundle rb) {

    	chargerEnreg(1, 0);
    	updateTableView();		
	}


	//Aller au menu principal de l'app
    @FXML void  goto_Menu(ActionEvent e) {
		new application.ARTICLES.ArticleController().goto_Menu(e);

    }
    
    
    //Effectuer un new enregistrement
    @FXML void nouveau(ActionEvent event) {
    	reset_champ();
    }
    
    
  //Afficher le premier enregistrement de la BD
    @FXML void premierEnreg(ActionEvent event)  {
    	try {
    		int numRa = 1;
    		chargerEnreg(numRa, 0);
    		
    	}catch (Exception erreur) {
            System.out.println(erreur);
    	}
    }
    
    
    //Afficher le precedent enregistrement de la BD
    @FXML void precedentEnreg(ActionEvent event) {
    	try {
	    	int numRa = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numRa, -1);
	    	
	    }catch (Exception erreur) {
	        System.out.println(erreur);
		}
    }
    
    
    //Afficher l'enregistrement suivant de la BD
    @FXML void suivantEnreg(ActionEvent event) {
    	try {
	    	int numRa = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numRa, 1);
	    	
	    }catch (Exception erreur) {
	        System.out.println(erreur);
		}
    }
    
    
    //Afficher le dernier enregistrement de la BD
    @FXML void dernierEnreg(ActionEvent event) {
    	try {
	    	int numRa = nbTotalEnreg();
	    	chargerEnreg(numRa, 0);
	    	
    	}catch (Exception erreur) {
            System.out.println(erreur);

    	}
    }    
    
    
    //Afficher la ligne selectionnée dans les champs de saisie respectifs
    @FXML void selectionner(MouseEvent evnt){
     	index = listeRay.getSelectionModel().getSelectedIndex();
     	if (index <= -1){
     		return;
     	}
 		textFieldCode.setText(idR.getCellData(index).toString());
 		textFieldLib.setText(libR.getCellData(index).toString());
 		textFieldNplace.setText(nplace.getCellData(index).toString());

     } 
    
    
    //Ajouter des données 
    @FXML void ajouter(ActionEvent event){
        
    	conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty() & ConnectionBD.isLettre(textFieldLib.getText())
        		& ConnectionBD.isNombre(textFieldNplace.getText())) {
        	
            String sql = "INSERT INTO rayons (id_Rayon, libelle_Rayon, nbre_place) VALUES ( NULL, '" 
            + textFieldLib.getText() + "','" + textFieldNplace.getText() + "');";
            System.out.println(sql);
            try {
            	
            	st = conn.createStatement();
		        st.executeUpdate(sql);
		        
		        new Alert(Alert.AlertType.INFORMATION, "Rayon ajouté avec succès").showAndWait();
		        updateTableView();
				reset_champ();
				
		        st.close();
		        conn.close();
		        
            }catch(Exception exception){
            	new Alert(Alert.AlertType.ERROR, "Mauvaise insertion des données").showAndWait();
            }
        
        } else {

            alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une insertion dans la base de données.");
            alerte.setHeaderText("Champs de saisie vides !");
            alerte.setTitle("Erreur");
            alerte.showAndWait();
        }            
        
    }
    
    
    @FXML void modifier(ActionEvent event) {
    	
    	 conn = ConnectionBD.CheckConnection();
         
         if (!isFieldsEmpty() & ConnectionBD.isLettre(textFieldLib.getText()) 
        		 & ConnectionBD.isNombre(textFieldNplace.getText())) {
 	            
            String sql = "UPDATE rayons SET "; 
            sql = sql + "libelle_Rayon ='" + textFieldLib.getText() + "','";
            sql = sql + "nbre_place ='" + textFieldNplace.getText() + "' ";
            sql = sql +"WHERE id_Rayon =" + textFieldCode.getText() +";";
            try {
            	st = conn.createStatement();
 	            st.executeUpdate(sql);

 	            new Alert(Alert.AlertType.INFORMATION, "Rayon modifié avec succès").showAndWait();
 	            updateTableView();
				reset_champ();

 	            st.close();
 	            conn.close();
             }
             catch(Exception exception){
             	new Alert(Alert.AlertType.ERROR, "Mauvaise modification des données").showAndWait();
             }
            
         }else {

             alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une modification dans la base de données.");
             alerte.setHeaderText("Champs de saisie vides !");
             alerte.setTitle("Erreur");
             alerte.showAndWait();
          
          }
    	
    }
    
    
    @FXML void supprimer(ActionEvent event) {
    	
    	  	conn = ConnectionBD.CheckConnection();
    	  	
            if (!isFieldsEmpty()) {
	            String sql = "DELETE FROM rayons WHERE id_Rayon= " + textFieldCode.getText() + ";";
	            try {
	            	st = conn.createStatement();
		            st.executeUpdate(sql);
		            
		            new Alert(Alert.AlertType.INFORMATION, "Rayon supprimé avec succès").showAndWait();
		            reset_champ();
		            updateTableView();
		            
		            st.close();
		            conn.close();
		            
	            }catch(Exception exception){
	            	new Alert(Alert.AlertType.ERROR, "Mauvaise suppression des données").showAndWait();
	            }
	            
            }else {

                alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une suppression dans la base de données.");
                alerte.setHeaderText("Champs de saisie vides !");
                alerte.setTitle("Erreur");
                alerte.showAndWait();
	        }
    	
    }      

    
    //Tester les champs vides
     private boolean isFieldsEmpty() {
        
        if ("".equals(textFieldLib.getText())) {
            System.out.println("Champs vides");
            return true;
            
        } else {
            return false;
        }
    }
    
    //Vider les champs de saisie
    void reset_champ() {

        textFieldLib.setText("");
        textFieldNplace.setText("");
        
    }
    
    //Afficher les données de la BD dans la tableView
    void updateTableView() {
    	
    	 idR.setCellValueFactory(new PropertyValueFactory<Rayon,Integer>("idR")); 
	     libR.setCellValueFactory(new PropertyValueFactory<Rayon,String>("libR"));
	     nplace.setCellValueFactory(new PropertyValueFactory<Rayon,Integer>("nplace"));
	     liste = ConnectionBD.getRayons();
	     listeRay.setItems(liste);
    }

     int numeroEnreg(int idRayon) {
        int numEnreg = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(ConnectionBD.SelectallRay);
            
            while (rs.next()) {
                numEnreg++;
                int num = rs.getInt(1);
                if(num == idRayon)
                    break;
            }
            rs.close();
            st.close();
            conn.close();
        }
        catch(Exception sqlExcptn) {
            System.out.println(sqlExcptn);
        }
        return numEnreg;
    }
    
  //Trouver la cle d'un enregistrement a partir de son numero logique
    static int trouverCleEnreg(int numLog) {
        int numEnreg = 0;
        int numRay = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(ConnectionBD.SelectallRay);
            
            while (rs.next()) {
                numEnreg++;
                if(numEnreg == numLog) {
                	numRay = rs.getInt(1);
                    break;
                }
            }
            rs.close();
            st.close();
            conn.close();
        }
        catch(Exception sqlExcptn) {
            System.out.println(sqlExcptn);
        }
        return numRay;
    }

    int nbTotalEnreg() {
        int nbTEnreg = 0;
        conn = ConnectionBD.CheckConnection();
        try {
           st = conn.createStatement();
            String sql = "SELECT count(id_Rayon) FROM rayons ;";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                nbTEnreg = rs.getInt(1);
            }
            rs.close();
            st.close();
            conn.close();
        }
        catch(Exception sqlExcptn) {
            System.out.println(sqlExcptn);
        }
        return nbTEnreg;
    }

    //sens = -1 precedent    sens = 0 premier ou dernier sens = 1 suivant
    
    void chargerEnreg(int numRayon, int sens) {
        int n = numeroEnreg(numRayon) + sens;
        int num_ray = trouverCleEnreg(n);
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM rayons WHERE id_Rayon = " + num_ray;
            rs = st.executeQuery(sql);
            if(rs.next()) {
                textFieldCode.setText(String.valueOf(rs.getInt(1)));
                textFieldLib.setText(rs.getString(2));
                textFieldNplace.setText(String.valueOf(rs.getInt(3)));
            }
            rs.close();
            st.close();
            conn.close();
        }
        catch(Exception sqlExcptn) {
            System.out.println(sqlExcptn);
        }
    }
        
}
