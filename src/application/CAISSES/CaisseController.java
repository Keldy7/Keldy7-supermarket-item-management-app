package application.CAISSES;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
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

public class CaisseController implements Initializable{
	
	static Connection conn = null;
	static ResultSet rs = null;
	static Statement st = null;
	
	Stage stage = new Stage();
	
	ObservableList<Caisse> liste;
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

    @FXML private TextField textFieldNom;

    @FXML private TextField textFieldTel;
    
    
	/*-----------Les colonnes de TableView----------------*/
    
    @FXML private TableView<Caisse> listeCai;
    
    @FXML private TableColumn<Caisse, Integer> idCai;

    @FXML private TableColumn<Caisse, String> nomCai;

    @FXML private TableColumn<Caisse, String> telCai;

    @FXML private Hyperlink menu;
    
    
    @Override
	public void initialize(URL loc, ResourceBundle rb) {

		chargerEnreg(1, 0);
    	updateTableView();

	}
    
 // Afficher le premier enregistrement de la BD
 	@FXML void premierEnreg(ActionEvent event) {
 		try {
 			int numCai = 1;
 			chargerEnreg(numCai, 0);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}

 	// Afficher le precedent enregistrement de la BD
 	@FXML void precedentEnreg(ActionEvent event) {
 		try {
 			int numCai = Integer.valueOf(textFieldCode.getText()).intValue();
 			chargerEnreg(numCai, -1);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}

 	
 	// Afficher l'enregistrement suivant de la BD
 	@FXML void suivantEnreg(ActionEvent event) {
 		try {
 			int numCai = Integer.valueOf(textFieldCode.getText()).intValue();
 			chargerEnreg(numCai, 1);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}

 	
 	// Afficher le dernier enregistrement de la BD
 	@FXML void dernierEnreg(ActionEvent event) {
 		try {
 			int numCai = nbTotalEnreg();
 			chargerEnreg(numCai, 0);

 		} catch (Exception erreur) {
 			System.out.println(erreur);

 		}
 	}

  
 	// Aller au menu principal de l'app
 	@FXML void goto_Menu(ActionEvent e) {
		new application.ARTICLES.ArticleController().goto_Menu(e);
 	}
 	

 	// Effectuer un new enregistrement
 	@FXML void nouveau(ActionEvent event) {
 		reset_champ();
 	}


	// Afficher la ligne selectionnï¿½e dans les champs de saisie respectifs
 	@FXML void selectionner(MouseEvent evnt) {

 		index = listeCai.getSelectionModel().getSelectedIndex();
 		if (index <= -1) {
 			return;
 		}
 		textFieldCode.setText(idCai.getCellData(index).toString());
 		textFieldNom.setText(nomCai.getCellData(index).toString());
 		textFieldTel.setText(telCai.getCellData(index).toString());
 		
 	}

 	
 	@FXML
    void ajouter(ActionEvent event) {
 		conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty() || ConnectionBD.isLettre(textFieldNom.getText())) {
      
            String sql = "INSERT INTO caisses (id_Caisse, nom_Caissiere, tel_Caissiere) VALUES ( NULL, '" 
            		+ textFieldNom.getText() + "','" 
            		+ textFieldTel.getText() + "');";
            System.out.println(sql);
            try {
            	
            	st = conn.createStatement();
				st.executeUpdate(sql);
				
				new Alert(Alert.AlertType.INFORMATION, "Caissier(e) ajouté(e) avec succès").showAndWait();
				updateTableView();
				reset_champ();
				
				st.close();
	            conn.close();
	            
			} catch (Exception erreur) {
             	new Alert(Alert.AlertType.ERROR, "Mauvaise insertion des données").showAndWait();
			}
            
        } else {
        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une insertion dans la base de données.");
            alert.setHeaderText("Champs de saisie vides !");
            alert.setTitle("Erreur");
            alert.showAndWait();
        }
    }

 	
 	
    @FXML
    void modifier(ActionEvent event) {
    	conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty()) {

            String sql = "UPDATE caisses SET "; 
            sql = sql + "nom_Caissiere ='" + textFieldNom.getText() + "'";
            sql = sql + " ,tel_Caissiere ='" + textFieldTel.getText() + "' WHERE id_Caisse =" + textFieldCode.getText() +";";
            
            try {
            	st = conn.createStatement();
            	st.executeUpdate(sql);

 	            new Alert(Alert.AlertType.INFORMATION, "Caissier(e) modifié(e) avec succès").showAndWait();
				updateTableView();
				reset_champ();

 	            st.close();
 	            conn.close();
             }
             catch(Exception exception){
             	new Alert(Alert.AlertType.ERROR, "Mauvaise modification des données").showAndWait();

             }
        }else {
        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une modification dans la base de données.");
            alert.setHeaderText("Champs de saisie vides !");
            alert.setTitle("Erreur");
            alert.showAndWait();
         
         }
    }

    

    @FXML
    void supprimer(ActionEvent event) {
    	conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty()) {

			String sql = "DELETE FROM caisses WHERE id_Caisse = " + textFieldCode.getText() + ";";

			Alert on_supprime = new Alert(Alert.AlertType.CONFIRMATION);
			on_supprime.setContentText("Etes vous sûr de vouloir supprimer ses données?");
			on_supprime.setHeaderText("Suppression des données");
			on_supprime.setTitle("Confirmation pour suppression");
			Optional<ButtonType> action = on_supprime.showAndWait();

			if (action.get() == ButtonType.OK) {
				try {
					st = conn.createStatement();
					st.executeUpdate(sql);

					new Alert(Alert.AlertType.INFORMATION, "Caissier(e) supprimé(e) avec succès").showAndWait();
					reset_champ();
					updateTableView();

					st.close();
					conn.close();

				} catch (Exception exception) {
					new Alert(Alert.AlertType.ERROR, "Mauvaise suppression des données").showAndWait();
				}
			}

		} else {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
			alert.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une suppression dans la base de données.");
			alert.setHeaderText("Champs de saisie vides !");
			alert.setTitle("Erreur");
			alert.showAndWait();
		}

    }

    void reset_champ() {

    	textFieldNom.clear();
		textFieldTel.clear();		
	}
    
    
    
    // Tester les champs vides
  	boolean isFieldsEmpty() {

  		if (textFieldNom.getText().isBlank() || textFieldTel.getText().isBlank()) {
  			System.out.println("Champs vides");
  			return true;

  		} else {
  			return false;
  		}
  	}
  	
  	
    
    // Afficher les donnï¿½es de la BD dans la tableView
 	void updateTableView() {

 		idCai.setCellValueFactory(new PropertyValueFactory<Caisse, Integer>("idCai"));
 		nomCai.setCellValueFactory(new PropertyValueFactory<Caisse, String>("nomCai"));
 		telCai.setCellValueFactory(new PropertyValueFactory<Caisse, String>("telCai"));
 		
 		liste = ConnectionBD.getCaisses();
 		listeCai.setItems(liste);
 	}

 	int numeroEnreg(int idCais) {
 		int numEnreg = 0;
 		conn = ConnectionBD.CheckConnection();
 		try {
 			st = conn.createStatement();
 			rs = st.executeQuery(ConnectionBD.SelectallCai);

 			while (rs.next()) {
 				numEnreg++;
 				int num = rs.getInt(1);
 				if (num == idCais)
 					break;
 			}
 			rs.close();
 			st.close();
 			conn.close();
 		} catch (Exception sqlExcptn) {
 			System.out.println(sqlExcptn);
 		}
 		return numEnreg;
 	}

 	// Trouver la cle d'un enregistrement a partir de son numero logique
 	static int trouverCleEnreg(int numLog) {
 		int numEnreg = 0;
 		int numCais = 0;
 		conn = ConnectionBD.CheckConnection();
 		try {
 			st = conn.createStatement();
 			rs = st.executeQuery(ConnectionBD.SelectallCai);

 			while (rs.next()) {
 				numEnreg++;
 				if (numEnreg == numLog) {
 					numCais = rs.getInt(1);
 					break;
 				}
 			}
 			rs.close();
 			st.close();
 			conn.close();
 		} catch (Exception sqlExcptn) {
 			System.out.println(sqlExcptn);
 		}
 		return numCais;
 	}

 	int nbTotalEnreg() {
 		int nbTEnreg = 0;
 		conn = ConnectionBD.CheckConnection();
 		try {
 			st = conn.createStatement();
 			String sql = "SELECT count(id_Caisse) FROM caisses;";
 			rs = st.executeQuery(sql);
 			if (rs.next()) {
 				nbTEnreg = rs.getInt(1);
 			}
 			rs.close();
 			st.close();
 			conn.close();
 		} catch (Exception sqlExcptn) {
 			System.out.println(sqlExcptn);
 		}
 		return nbTEnreg;
 	}

 	
 	void chargerEnreg(int numFournisseur, int sens) {
        int n = numeroEnreg(numFournisseur) + sens;
        int num_cai = trouverCleEnreg(n);
        conn = ConnectionBD.CheckConnection();
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM caisses WHERE id_Caisse = " + num_cai;
            rs = st.executeQuery(sql);
            if(rs.next()) {
                textFieldCode.setText(String.valueOf(rs.getInt(1)));
                textFieldNom.setText(rs.getString(2));
                textFieldTel.setText(rs.getString(3));
                  
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
