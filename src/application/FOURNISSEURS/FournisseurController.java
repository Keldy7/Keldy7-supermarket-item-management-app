package application.FOURNISSEURS;

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

import application.Database.ConnectionBD;



public class FournisseurController implements Initializable {
	
	static Connection conn = null;
	static ResultSet rs = null;
	static Statement st = null;

	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
	
	 ObservableList<Fournisseur> liste ;
	 int index = -1;
	 
	
	/*---------------Les boutons-------------------*/

	@FXML private Button btn_nouveau;

	@FXML private Button btn_ajout;
	
	@FXML private Button btn_modif;
	
	@FXML private Button btn_sup;
	
	@FXML private Button btnPremier;
	
	@FXML private Button btnDernier;
		
	@FXML private Button btnSuivant;

	@FXML private Button btnPrecedent;
	
	
	/*---------------Les zones de saisie-------------------*/
	
	@FXML private TextField textFieldAdr;

    @FXML private TextField textFieldCode;

    @FXML private TextField textFieldNom;

    @FXML private TextField textFieldTel;
    
    /*-----------Les colonnes de TableView----------------*/
    
    @FXML private TableView<Fournisseur> listeFour;
    
    @FXML private TableColumn<Fournisseur, Integer> idF;

    @FXML private TableColumn<Fournisseur, String> nomF;
    
    @FXML private TableColumn<Fournisseur, String> adrF;

    @FXML private TableColumn<Fournisseur, String> telF;
    
    @FXML private Hyperlink menu;
    
   
    
    //Initialiser FournisseurController
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
    		int numF = 1;
    		chargerEnreg(numF, 0);
    		
    	}catch (Exception erreur) {
            System.out.println(erreur);
    	}
    }
    
    
    //Afficher le precedent enregistrement de la BD
    @FXML void precedentEnreg(ActionEvent event) {
    	try {
	    	int numF = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numF, -1);
	    	
	    }catch (Exception erreur) {
	        System.out.println(erreur);
		}
    }
    
    
    //Afficher l'enregistrement suivant de la BD
    @FXML void suivantEnreg(ActionEvent event) {
    	try {
	    	int numF = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numF, 1);
	    	
	    }catch (Exception erreur) {
	        System.out.println(erreur);
		}
    }
    
    
    //Afficher le dernier enregistrement de la BD
    @FXML void dernierEnreg(ActionEvent event) {
    	try {
	    	int numF = nbTotalEnreg();
	    	chargerEnreg(numF, 0);
	    	
    	}catch (Exception erreur) {
            System.out.println(erreur);

    	}
    }
    
    
    //Afficher la ligne selectionnée dans les champs de saisie respectifs
    @FXML void selectionner(MouseEvent evnt){
     	index = listeFour.getSelectionModel().getSelectedIndex();
     	if (index <= -1){
     		return;
     	}
 		textFieldCode.setText(idF.getCellData(index).toString());
 		textFieldNom.setText(nomF.getCellData(index).toString());
 		textFieldAdr.setText(adrF.getCellData(index).toString());
 		textFieldTel.setText(telF.getCellData(index).toString());

     } 
     
    @FXML void ajouter(ActionEvent event){
        
        conn = ConnectionBD.CheckConnection();
	            
        if (!isFieldsEmpty()) {
      
            String sql = "INSERT INTO fournisseurs (id_Fournisseur, nom_Fournisseur, address_Fournisseur, tel_Fournisseur) VALUES ( NULL, '" 
            		+ textFieldNom.getText() + "','" 
            		+ textFieldAdr.getText() + "','" 
            		+ textFieldTel.getText() + "');";
            System.out.println(sql);
            try {
            	
            	st = conn.createStatement();
				st.executeUpdate(sql);
				
				new Alert(Alert.AlertType.INFORMATION, "Fournisseur ajouté avec succès").showAndWait();
				updateTableView();
				reset_champ();
				
				st.close();
	            conn.close();
	            
			} catch (Exception erreur) {
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

            String sql = "UPDATE fournisseurs SET "; 
            sql = sql + "nom_Fournisseur ='" + textFieldNom.getText() + "'";
            sql = sql + " ,address_Fournisseur ='" + textFieldAdr.getText() + "'";
            sql = sql + " ,tel_Fournisseur ='" + textFieldTel.getText() + "' WHERE id_Fournisseur =" + textFieldCode.getText() +";";
            
            try {
            	st = conn.createStatement();
            	st.executeUpdate(sql);

 	            new Alert(Alert.AlertType.INFORMATION, "Fournisseur modifié avec succès").showAndWait();
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
            String sql = "DELETE FROM fournisseurs WHERE id_Fournisseur= " + textFieldCode.getText() + ";";
            System.out.println(sql);

            try {
            	st = conn.createStatement();
	            st.executeUpdate(sql);
	            
	            new Alert(Alert.AlertType.INFORMATION, "Fournisseur supprimé avec succès").showAndWait();
	            reset_champ();
				updateTableView();

	            st.close();
	            conn.close();
	            
            }
            catch(Exception exception){
            	new Alert(Alert.AlertType.ERROR, "Mauvaise suppression des données").showAndWait();

            }
            
        }else{

            alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une suppression dans la base de données.");
            alerte.setHeaderText("Champs de saisie vides !");
            alerte.setTitle("Erreur");
            alerte.showAndWait();
        }
            
    }
    
   
    //Tester les champs vides
    boolean isFieldsEmpty() {
        
        if (("".equals(textFieldNom.getText())) || ("".equals(textFieldAdr.getText())) || ("".equals(textFieldTel.getText()))) {
            System.out.println("Champs pas vides");
            return true;
            
        } else {
            return false;
        }
    }
    
    
    //Vider les champs de saisie
    void reset_champ() {

        textFieldNom.clear();
        textFieldAdr.clear();
        textFieldTel.clear();
    }
    
    
    //Afficher les données de la BD dans la tableView    
     void updateTableView() {
    	
    	 idF.setCellValueFactory(new PropertyValueFactory<Fournisseur,Integer>("idF")); 
	     nomF.setCellValueFactory(new PropertyValueFactory<Fournisseur,String>("nomF")); 
	     adrF.setCellValueFactory(new PropertyValueFactory<Fournisseur,String>("adrF")); 
	     telF.setCellValueFactory(new PropertyValueFactory<Fournisseur,String>("telF"));
	     liste = ConnectionBD.getFournisseurs();
	     listeFour.setItems(liste);
	     
     }
     
     
     int numeroEnreg(int idFour) {
        int numEnreg = 0;
        conn = ConnectionBD.CheckConnection();

        try {
            st = conn.createStatement();            
            rs = st.executeQuery(ConnectionBD.SelectallFour);
            
            while (rs.next()) {
                numEnreg++;
                int num = rs.getInt(1);
                if(num == idFour)
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
        int numFour = 0;
        conn = ConnectionBD.CheckConnection();

        try {
            st = conn.createStatement();            
            rs = st.executeQuery(ConnectionBD.SelectallFour);
            
            while (rs.next()) {
                numEnreg++;
                if(numEnreg == numLog) {
                	numFour = rs.getInt(1);
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
        return numFour;
    }

    
    int nbTotalEnreg() {
        int nbTEnreg = 0;
        conn = ConnectionBD.CheckConnection();

        try {
            st = conn.createStatement();
            String sql = "SELECT count(id_Fournisseur) FROM fournisseurs ;";
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

    void chargerEnreg(int numFournisseur, int sens) {
        int n = numeroEnreg(numFournisseur) + sens;
        int num_four = trouverCleEnreg(n);
        conn = ConnectionBD.CheckConnection();

        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM fournisseurs WHERE id_Fournisseur = " + num_four;
            rs = st.executeQuery(sql);
            if(rs.next()) {
                textFieldCode.setText(String.valueOf(rs.getInt(1)));
                textFieldNom.setText(rs.getString(2));
                textFieldAdr.setText(rs.getString(3));
                textFieldTel.setText(rs.getString(4));
                  
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
