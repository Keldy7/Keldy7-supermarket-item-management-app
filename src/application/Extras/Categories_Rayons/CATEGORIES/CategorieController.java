package application.Extras.Categories_Rayons.CATEGORIES;

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

public class CategorieController implements Initializable {
	
	static Connection conn = null;
	static ResultSet rs = null;
	static Statement st = null;
	
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
	static Stage stage = new Stage();
	
    ObservableList<Categorie> liste;
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
    
    /*-----------Les colonnes de TableView----------------*/


    @FXML private TableView<Categorie> listeCat;

    @FXML private TableColumn<Categorie, Integer> idC;

    @FXML private TableColumn<Categorie, String> libC;

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
    		int numCa = 1;
    		chargerEnreg(numCa, 0);
    		
    	}catch (Exception erreur) {
            System.out.println(erreur);
    	}
    }
    
    //Afficher le precedent enregistrement de la BD
    @FXML void precedentEnreg(ActionEvent event) {
    	try {
	    	int numCa = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numCa, -1);
	    	
	    }catch (Exception erreur) {
	        System.out.println(erreur);
		}
    }
    
    //Afficher l'enregistrement suivant de la BD
    @FXML void suivantEnreg(ActionEvent event) {
    	try {
	    	int numCa = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numCa, 1);
	    	
	    }catch (Exception erreur) {
	        System.out.println(erreur);
		}
    }
    
    //Afficher le dernier enregistrement de la BD
    @FXML void dernierEnreg(ActionEvent event) {
    	try {
	    	int numCa = nbTotalEnreg();
	    	chargerEnreg(numCa, 0);
	    	
    	}catch (Exception erreur) {
            System.out.println(erreur);

    	}
    }
    
    
    //Afficher la ligne selectionnée dans les champs de saisie respectifs
    @FXML void selectionner(MouseEvent evnt){
     	index = listeCat.getSelectionModel().getSelectedIndex();
     	if (index <= -1){
     		return;
     	}
 		textFieldCode.setText(idC.getCellData(index).toString());
 		textFieldLib.setText(libC.getCellData(index).toString());
 		
     } 
    
    //Ajouter des données 
    @FXML void ajouter(ActionEvent event){
        
    	conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty()) {
        	
            String sql = "INSERT INTO categories (id_Categorie, libelle_Categorie) VALUES ( NULL, '" +textFieldLib.getText() + "');";
            try {
            	
            	st = conn.createStatement();
		        st.executeUpdate(sql);
		        
		        new Alert(Alert.AlertType.INFORMATION, "Catégorie ajoutée avec succès").showAndWait();
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
         
         if (!isFieldsEmpty()) {
 	            
            String sql = "UPDATE categories SET "; 
            sql = sql + "libelle_Categorie ='" + textFieldLib.getText() + "' ";
            sql = sql +"WHERE id_Categorie =" + textFieldCode.getText() +";";
            try {
            	st = conn.createStatement();
 	            st.executeUpdate(sql);

 	            new Alert(Alert.AlertType.INFORMATION, "Catégorie modifiée avec succès").showAndWait();
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
	            String sql = "DELETE FROM categories WHERE id_Categorie= " + textFieldCode.getText() + ";";
	            try {
	            	st = conn.createStatement();
		            st.executeUpdate(sql);
		            
		            new Alert(Alert.AlertType.INFORMATION, "Catégorie supprimée avec succès").showAndWait();
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
    boolean isFieldsEmpty() {
        
        if (("".equals(textFieldLib.getText()))) {
            System.out.println("Champs vides");
            return true;
            
        } else {
            return false;
        }
    }
    
    
    //Vider les champs de saisie
    void reset_champ() {
        textFieldLib.setText("");
        
    }
    
    
    //Afficher les données de la BD dans la tableView
    void updateTableView() {
    	
    	 idC.setCellValueFactory(new PropertyValueFactory<Categorie,Integer>("idC")); 
	     libC.setCellValueFactory(new PropertyValueFactory<Categorie,String>("libC"));
	     liste = ConnectionBD.getCategories();
	     listeCat.setItems(liste);
    }

     int numeroEnreg(int idCategorie) {
        int numEnreg = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(ConnectionBD.SelectallCat);
            
            while (rs.next()) {
                numEnreg++;
                int num = rs.getInt(1);
                if(num == idCategorie)
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
        int numCat = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(ConnectionBD.SelectallCat);
            
            while (rs.next()) {
                numEnreg++;
                if(numEnreg == numLog) {
                	numCat = rs.getInt(1);
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
        return numCat;
    }

    int nbTotalEnreg() {
        int nbTEnreg = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            String sql = "SELECT count(id_Categorie) FROM categories ;";
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
    
    void chargerEnreg(int numCategorie, int sens) {
        int n = numeroEnreg(numCategorie) + sens;
        int num_cat = trouverCleEnreg(n);
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM categories WHERE id_Categorie = " + num_cat;
            rs = st.executeQuery(sql);
            if(rs.next()) {
                textFieldCode.setText(String.valueOf(rs.getInt(1)));
                textFieldLib.setText(rs.getString(2));
                  
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
