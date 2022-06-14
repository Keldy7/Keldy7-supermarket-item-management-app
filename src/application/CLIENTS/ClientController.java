package application.CLIENTS;

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



public class ClientController implements Initializable {
	
	static ResultSet rs = null;
	static PreparedStatement stat = null;
	static Statement st = null;
	static Connection conn = null;

	Stage stage = new Stage();
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);

    ObservableList<Client> liste;
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
    
    @FXML private TextField textFieldPre;

    @FXML private TextField textFieldTel;
    
    /*-----------Les colonnes de TableView----------------*/
    
    @FXML private TableView<Client> listeCli;
    
    @FXML private TableColumn<Client, Integer> idCli;

    @FXML private TableColumn<Client, String> nomCli;
    
    @FXML private TableColumn<Client, String> preCli;
    
    @FXML private TableColumn<Client, String> adrCli;

    @FXML private TableColumn<Client, String> telCli;
    
    @FXML private Hyperlink menu;
    
    
    //Initialiser ClientController
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
    		int numC = 1;
    		chargerEnreg(numC, 0);
    		
    	}catch (Exception erreur) {
            System.out.println(erreur);
    	}
    }
    
    
    //Afficher le precedent enregistrement de la BD
    @FXML void precedentEnreg(ActionEvent event) {
    	try {
	    	int numC = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numC, -1);
	    	
	    }catch (Exception erreur) {
	        System.out.println(erreur);
		}
    }
    
    
    //Afficher l'enregistrement suivant de la BD
    @FXML void suivantEnreg(ActionEvent event) {
    	try {
	    	int numC = Integer.valueOf(textFieldCode.getText()).intValue();
	    	chargerEnreg(numC, 1);
	    	
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
    	
     	index = listeCli.getSelectionModel().getSelectedIndex();
     	if (index <= -1){
     		return;
     	}
 		textFieldCode.setText(idCli.getCellData(index).toString());
 		textFieldNom.setText(nomCli.getCellData(index).toString());
 		textFieldPre.setText(preCli.getCellData(index).toString());
 		textFieldAdr.setText(adrCli.getCellData(index).toString());
 		textFieldTel.setText(telCli.getCellData(index).toString());
 	
     } 
    
    
    //Ajouter des données 
    @FXML void ajouter(ActionEvent event){
        
    	conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty() & ConnectionBD.isLettre(textFieldNom.getText()) 
        		& ConnectionBD.isLettre(textFieldPre.getText())
        		& ConnectionBD.isLettre(textFieldAdr.getText())) {
        	
            String sql = "INSERT INTO clients (id_Client, nom_Client, prenom_Client, adress_Client, tel_Client) "
            		+ "VALUES ( NULL, '" + textFieldNom.getText() + "','" + textFieldPre.getText()
            		+ "','" + textFieldAdr.getText() + "','" + textFieldTel.getText() + "');";
            try {
            	
            	st = conn.createStatement();
		        st.executeUpdate(sql);
		        
		        new Alert(Alert.AlertType.INFORMATION, "Client(e) ajouté(e) avec succès").showAndWait();
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
         
         if (!isFieldsEmpty() & ConnectionBD.isLettre(textFieldNom.getText()) 
         		& ConnectionBD.isLettre(textFieldPre.getText())
         		& ConnectionBD.isLettre(textFieldAdr.getText())) {
 	            
            String sql = "UPDATE clients SET "; 
            sql = sql + "nom_Client ='" + textFieldNom.getText() + "'";
            sql = sql + " ,prenom_Client ='" + textFieldPre.getText() + "'";
            sql = sql + " ,adress_Client ='" + textFieldAdr.getText() + "'";
            sql = sql + " ,tel_Client ='" + textFieldTel.getText() + "' WHERE id_Client =" + textFieldCode.getText() +";";
            try {
            	st = conn.createStatement();
 	            st.executeUpdate(sql);

 	            new Alert(Alert.AlertType.INFORMATION, "Client(e) modifié(e) avec succès").showAndWait();
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
	            String sql = "DELETE FROM clients WHERE id_Client= " + textFieldCode.getText() + ";";
	            try {
	            	st = conn.createStatement();
		            st.executeUpdate(sql);
		            
		            new Alert(Alert.AlertType.INFORMATION, "Client(e) supprimé(e) avec succès").showAndWait();
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
        
        if (textFieldNom.getText().isBlank() 
        		|| textFieldPre.getText().isBlank() 
        		|| textFieldAdr.getText().isBlank() || textFieldTel.getText().isBlank()) {
            System.out.println("Champs pas vides");
            return true;
            
        } else {
            return false;
        }
    }
    
    
    //Vider les champs de saisie
    void reset_champ() {
        textFieldNom.clear();
        textFieldPre.clear();
        textFieldAdr.clear();
        textFieldTel.clear();
    }
    
    
    //Afficher les données de la BD dans la tableView
    void updateTableView() {
    	
	     idCli.setCellValueFactory(new PropertyValueFactory<Client,Integer>("idCli")); 
	     nomCli.setCellValueFactory(new PropertyValueFactory<Client,String>("nomCli"));
	     preCli.setCellValueFactory(new PropertyValueFactory<Client,String>("preCli")); 
	     adrCli.setCellValueFactory(new PropertyValueFactory<Client,String>("adrCli")); 
	     telCli.setCellValueFactory(new PropertyValueFactory<Client,String>("telCli"));
	     
	     liste = ConnectionBD.getClients();
	     listeCli.setItems(liste);
    }

     int numeroEnreg(int idClient) {
        int numEnreg = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(ConnectionBD.SelectallCli);
            
            while (rs.next()) {
                numEnreg++;
                int num = rs.getInt(1);
                if(num == idClient)
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
        int numCli = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(ConnectionBD.SelectallCli);
            
            while (rs.next()) {
                numEnreg++;
                if(numEnreg == numLog) {
                	numCli = rs.getInt(1);
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
        return numCli;
    }

    int nbTotalEnreg() {
        int nbTEnreg = 0;
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            String sql = "SELECT count(id_Client) FROM clients ;";
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

    // sens = -1 precedent	  sens = 0 	 premier ou dernier sens = 1 suivant
    
    void chargerEnreg(int numClient, int sens) {
        int n = numeroEnreg(numClient) + sens;
        int num_cli = trouverCleEnreg(n);
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM clients WHERE id_Client = " + num_cli;
            rs = st.executeQuery(sql);
            if(rs.next()) {
                textFieldCode.setText(String.valueOf(rs.getInt(1)));
                textFieldNom.setText(rs.getString(2));
                textFieldPre.setText(rs.getString(3));
                textFieldAdr.setText(rs.getString(4));
                textFieldTel.setText(rs.getString(5));
                  
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
