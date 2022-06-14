package application.Extras.Recus_Commandes.COMMANDES;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import application.Database.ConnectionBD;
import application.Extras.Recus_Commandes.ACHATS.AcheterController;
import application.Extras.Recus_Commandes.RECUS.RecuController;

public class CommandeFController implements Initializable {
	
	static Connection conn = null;
	static ResultSet rs = null, rst = null;
    static Statement st = null, stat = null;
	
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
	
	ObservableList<CommandeF> liste;
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

    @FXML private DatePicker textFieldDateCom;

    @FXML private TextField textFieldQte;
    
    @FXML private ComboBox<String> textFieldArt;
    
    
	/*-----------Les colonnes de TableView----------------*/
    
    @FXML private TableView<CommandeF> listeCom;
    
    @FXML private TableColumn<CommandeF, Integer> idCom;

    @FXML private TableColumn<CommandeF, String> dateCom;

    @FXML private TableColumn<CommandeF, Integer> qteCom;
    
    @FXML private TableColumn<CommandeF, String> ArtCom;

    @FXML private Hyperlink menu;
    
    
    @Override
	public void initialize(URL loc, ResourceBundle rb) {

		textFieldArt.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(AcheterController.SelectallArticl)));
		System.out.println("Liste de tous les articles: " + RecuController.fillComboBox(AcheterController.SelectallArticl));
		
		chargerEnreg(1, 0);
    	updateTableView();

	}
    
    
    // Afficher le premier enregistrement de la BD
 	@FXML void premierEnreg(ActionEvent event) {
 		try {
 			int numCom = 1;
 			chargerEnreg(numCom, 0);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}
 	

 	// Afficher le precedent enregistrement de la BD
 	@FXML void precedentEnreg(ActionEvent event) {
 		try {
 			int numCom = Integer.valueOf(textFieldCode.getText()).intValue();
 			chargerEnreg(numCom, -1);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}
 	

 	// Afficher l'enregistrement suivant de la BD
 	@FXML void suivantEnreg(ActionEvent event) {
 		try {
 			int numCom = Integer.valueOf(textFieldCode.getText()).intValue();
 			chargerEnreg(numCom, 1);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}
 	

 	// Afficher le dernier enregistrement de la BD
 	@FXML void dernierEnreg(ActionEvent event) {

 		try {
 			int numCom = nbTotalEnreg();
 			chargerEnreg(numCom, 0);

 		} catch (Exception erreur) {
 			System.out.println(erreur);

 		}
 	}
 	
 	
 	// Aller au menu des 4 boutons de l'app
 	@FXML void goto_MenuRecCom(ActionEvent e) {
		new application.Extras.Recus_Commandes.ACHATS.AcheterController().goto_MenuRecCom(e);
 	}
 	
 	
 	// Effectuer un new enregistrement
 	@FXML void nouveau(ActionEvent event) {
 		reset_champ();
 	}


	// Afficher la ligne selectionnï¿½e dans les champs de saisie respectifs
 	@FXML void selectionner(MouseEvent evnt) {

 		index = listeCom.getSelectionModel().getSelectedIndex();
 		if (index <= -1) {
 			return;
 		}
 		textFieldCode.setText(idCom.getCellData(index).toString());
 		((DatePicker) textFieldDateCom).setPromptText(dateCom.getCellData(index).toString());
 		textFieldQte.setText(qteCom.getCellData(index).toString());
 		((ComboBox<String>) textFieldArt).setValue(ArtCom.getCellData(index).toString());

 	}

 	@FXML
    void ajouter(ActionEvent event) {
 		conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty() & ConnectionBD.isNombre(textFieldQte.getText())) {
      
			int id_Articl = AcheterController.elSelected(AcheterController.req_idArt, textFieldArt.getSelectionModel().getSelectedItem());
			
			/*String as = new String ("/");
			String das = new String("-");
			String ladate = String.valueOf(textFieldDateCom.getEditor().getText());
			ladate = ladate.replaceAll(as, das);
			System.out.println(ladate);
			java.util.Date utilDate = new java.util.Date(ladate.getTime());
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	        String date = DATE_FORMAT.format(ladate);
	        System.out.println("Today in MM-dd-yyyy format : " + date);*/
			
	        LocalDate ladate = textFieldDateCom.getValue();
            String sql = "INSERT INTO commande_f (id_CommandeF, date_cf, qte_Com, id_Article) VALUES (NULL, '" 
            		+ ladate + "', '" 
                    + textFieldQte.getText() + "', '"
            		+ id_Articl + "');";
            System.out.println(sql);
            try {
            	
            	st = conn.createStatement();
				st.executeUpdate(sql);
				
				new Alert(Alert.AlertType.INFORMATION, "Commande ajoutée avec succès").showAndWait();
				updateTableView();
				reset_champ();
				
				st.close();
	            conn.close();
	            
			} catch (Exception erreur) {
				System.out.println(erreur);
             	new Alert(Alert.AlertType.ERROR, "Mauvaise insertion des données").showAndWait();
			}
            
        } else {

        	alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une insertion dans la base de données.");
            alerte.setHeaderText("Champs de saisie vides !");
            alerte.setTitle("Erreur");
            alerte.showAndWait();
        }
    }

 	
    @FXML
    void modifier(ActionEvent event) {
    	conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty() & ConnectionBD.isNombre(textFieldQte.getText())) {
        	
			int id_Articl = AcheterController.elSelected(AcheterController.req_idArt, textFieldArt.getSelectionModel().getSelectedItem());
			
	        LocalDate ladate = textFieldDateCom.getValue();

            String sql = "UPDATE commande_f SET "; 
            sql = sql + "date_cf ='" + ladate + "'";
            sql = sql + ", qte_Com ='" + textFieldQte.getText() + "', id_Article ='" + id_Articl +
            		"' WHERE id_CommandeF= " + textFieldCode.getText() +";";
            try {
            	st = conn.createStatement();
            	st.executeUpdate(sql);

 	            new Alert(Alert.AlertType.INFORMATION, "Commande modifiée avec succès").showAndWait();
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


    @FXML
    void supprimer(ActionEvent event) {
    	conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty()) {

			String sql = "DELETE FROM commande_f WHERE id_CommandeF = " + textFieldCode.getText() + ";";

			Alert on_supprime = new Alert(Alert.AlertType.CONFIRMATION);
			on_supprime.setContentText("Etes vous sûr de vouloir supprimer ses données?");
			on_supprime.setHeaderText("Suppression des données");
			on_supprime.setTitle("Confirmation pour suppression");
			Optional<ButtonType> action = on_supprime.showAndWait();

			if (action.get() == ButtonType.OK) {
				try {
					st = conn.createStatement();
					st.executeUpdate(sql);

					new Alert(Alert.AlertType.INFORMATION, "Commande supprimée avec succès").showAndWait();
					reset_champ();
					updateTableView();

					st.close();
					conn.close();

				} catch (Exception exception) {
					new Alert(Alert.AlertType.ERROR, "Mauvaise suppression des données").showAndWait();
				}
			}

		} else {

			alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une suppression dans la base de données.");
			alerte.setHeaderText("Champs de saisie vides !");
			alerte.setTitle("Erreur");
			alerte.showAndWait();
		}

    }
    
 	
 	// Tester les champs vides
 	boolean isFieldsEmpty() {

 		//date vide? textFieldDate.getEditor().getText().isEmpty()
 		String date_cf = String.valueOf(textFieldDateCom.getValue());
 		if ((textFieldQte.getText().trim().isBlank()) || date_cf.isEmpty() || textFieldArt.getSelectionModel().isEmpty()) {
 			return true;

 		} else {
 			return false;
 		}
 	}
 	
 	
    void reset_champ() {

		textFieldDateCom.setValue(null);
		textFieldDateCom.setPromptText(null);
		textFieldQte.clear();
		((ComboBox<String>) textFieldArt).setValue("Selectionner l'article");

	}
    
 // Afficher les donnï¿½es de la BD dans la tableView
 	void updateTableView() {

 		idCom.setCellValueFactory(new PropertyValueFactory<CommandeF, Integer>("idCom"));
 		dateCom.setCellValueFactory(new PropertyValueFactory<CommandeF, String>("dateCom"));
 		qteCom.setCellValueFactory(new PropertyValueFactory<CommandeF, Integer>("qteCom"));
 		ArtCom.setCellValueFactory(new PropertyValueFactory<CommandeF, String>("ArtCom"));

 		liste = ConnectionBD.getCommandes();
 		listeCom.setItems(liste);
 	}

 	int numeroEnreg(int idCais) {
 		int numEnreg = 0;
 		conn = ConnectionBD.CheckConnection();
 		try {
 			st = conn.createStatement();
 			rs = st.executeQuery(ConnectionBD.SelectallCom);

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
 		int numCommande = 0;
 		conn = ConnectionBD.CheckConnection();
 		try {
 			st = conn.createStatement();
 			rs = st.executeQuery(ConnectionBD.SelectallCom);

 			while (rs.next()) {
 				numEnreg++;
 				if (numEnreg == numLog) {
 					numCommande = rs.getInt(1);
 					break;
 				}
 			}
 			rs.close();
 			st.close();
 			conn.close();
 		} catch (Exception sqlExcptn) {
 			System.out.println(sqlExcptn);
 		}
 		return numCommande;
 	}

 	int nbTotalEnreg() {
 		int nbTEnreg = 0;
 		conn = ConnectionBD.CheckConnection();
 		try {
 			st = conn.createStatement();
 			String sql = "SELECT count(id_CommandeF) FROM commande_f;";
 			ResultSet rs = st.executeQuery(sql);
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

 	
 	void chargerEnreg(int numCommande_f, int sens) {
        int n = numeroEnreg(numCommande_f) + sens;
        int num_com = trouverCleEnreg(n);
        conn = ConnectionBD.CheckConnection();
        String article_name = null;
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM commande_f WHERE id_CommandeF= " + num_com;
            rs = st.executeQuery(sql);
            if(rs.next()) {
                textFieldCode.setText(String.valueOf(rs.getInt(1)));
                textFieldDateCom.setPromptText(rs.getString(2));
                textFieldQte.setText(String.valueOf(rs.getInt(3)));
                
                String req_idArt = "SELECT libelle_Article FROM articles WHERE id_Article= '" + rs.getInt(4) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idArt);
	    			if (rst.first()) {
	    				article_name = rst.getString("libelle_Article");
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 dans chargerEnreg()");
	    		}
                textFieldArt.setPromptText(article_name);

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
