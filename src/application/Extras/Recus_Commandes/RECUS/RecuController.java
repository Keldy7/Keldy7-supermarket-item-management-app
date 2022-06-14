package application.Extras.Recus_Commandes.RECUS;

import java.net.URL;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import application.CheminFXML.RouteInterfaceFXML;
import application.Database.ConnectionBD;
import application.Extras.Recus_Commandes.ACHATS.AcheterController;

public class RecuController implements Initializable {
	
	static String SelectallCaiss = "SELECT nom_Caissiere FROM caisses;";
	static String SelectallCli = "SELECT DISTINCT CONCAT(Cli.nom_Client,' ',Cli.prenom_Client) FROM clients Cli, acheter Ach WHERE Cli.id_Client = Ach.id_Client;";
	static String SelectallArticl = "SELECT Art.libelle_Article FROM articles Art, acheter Ach WHERE Art.id_Article = Ach.id_Article;";
	
	String req_Cai = "SELECT id_Caisse FROM caisses WHERE nom_Caissiere= '";
	String req_Cli = "SELECT id_Client FROM clients WHERE CONCAT(nom_Client,' ',prenom_Client)= '";
	String req_Art = "SELECT prix_Article FROM articles WHERE libelle_Article= '";
	
	static ResultSet rs = null, rst = null, rslt = null;
	static Statement st = null, stat = null, sta = null;
	static Connection conn = null;

	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
	Stage stage = new Stage();

    ObservableList<Recu> liste;
    int index = -1;

	
	/*---------------Les boutons-------------------*/
	
	@FXML private Button btn_nouveau;

	@FXML private Button btn_ajout;
	
	@FXML private Button btn_modif;
	
	@FXML private Button btn_sup;
	
	@FXML private Button btn_print;
	
	@FXML private Button btnPremier;
	
	@FXML private Button btnDernier;
		
	@FXML private Button btnSuivant;

	@FXML private Button btnPrecedent;
	
	
	/*---------------Les zones de saisie-------------------*/
	
	@FXML private TextField textFieldNum;

    @FXML private TextField textFieldCode;

    @FXML private TextField textFieldQteA;
    
    @FXML private ComboBox<String> textFieldCai;

    @FXML private ComboBox<String> textFieldCli;
    
    @FXML private ComboBox<String> textFieldArt;

    
    /*-----------Les colonnes de TableView----------------*/
    
    @FXML private TableView<Recu> listeRec;
    
    @FXML private TableColumn<Recu, Integer> idR;
    
    @FXML private TableColumn<Recu, String> numR;

    @FXML private TableColumn<Recu, String> dateAchat;
    
    @FXML private TableColumn<Recu, Integer> qteAchat;
    
    @FXML private TableColumn<Recu, Integer> mtAchat;

    @FXML private TableColumn<Recu, String> Cai;
    
    @FXML private TableColumn<Recu, String> Cli;

    @FXML private Hyperlink menu;
    
    
    //Initialiser ReçuController
    public void initialize(URL url, ResourceBundle rb) {
        
        textFieldCai.setItems(FXCollections.observableArrayList(fillComboBox(SelectallCaiss)));
		System.out.println("Liste de tous les noms de caissiers: " + fillComboBox(SelectallCaiss));

        textFieldCli.setItems(FXCollections.observableArrayList(fillComboBox(SelectallCli)));
		System.out.println("Liste de tous les noms de clients ayant fait des achats: " + fillComboBox(SelectallCli));

        textFieldArt.setItems(FXCollections.observableArrayList(fillComboBox(SelectallArticl)));
		System.out.println("Liste de tous les articles achetés: " + fillComboBox(SelectallArticl));

    	chargerEnreg(1, 0);
    	updateTableView();		    	

    }
    
    
    //Afficher l'interface d'impression d'un reçu
    @FXML void form_PrintRec(ActionEvent e) {
    	try {
			Parent recPDF = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueRecuPrint));
			
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(recPDF));
			stage.show();
			((Stage) ((Node) e.getSource()).getScene().getWindow()).close(); // Fermer l'interface precedente


		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
    }
    
    
    // Aller au menu des 4 boutons de l'app
 	@FXML
 	void goto_MenuRecCom(ActionEvent e) {
		new application.Extras.Recus_Commandes.ACHATS.AcheterController().goto_MenuRecCom(e);

 	}
    
    //Effectuer un new enregistrement
    @FXML void nouveau(ActionEvent event) {
    	reset_champ();
    }
    
    
    // Afficher le premier enregistrement de la BD
 	@FXML void premierEnreg(ActionEvent event) {
 		try {
 			int numR = 1;
 			chargerEnreg(numR, 0);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}

 	// Afficher le precedent enregistrement de la BD
 	@FXML void precedentEnreg(ActionEvent event) {
 		try {
 			int numR = Integer.valueOf(textFieldCode.getText()).intValue();
 			chargerEnreg(numR, -1);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}

 	// Afficher l'enregistrement suivant de la BD
 	@FXML void suivantEnreg(ActionEvent event) {
 		try {
 			int numR = Integer.valueOf(textFieldCode.getText()).intValue();
 			chargerEnreg(numR, 1);

 		} catch (Exception erreur) {
 			System.out.println(erreur);
 		}
 	}

 	// Afficher le dernier enregistrement de la BD
 	@FXML void dernierEnreg(ActionEvent event) {
 		try {
 			int numR = nbTotalEnreg();
 			chargerEnreg(numR, 0);

 		} catch (Exception erreur) {
 			System.out.println(erreur);

 		}
 	}

   
    
    //Afficher la ligne selectionnée dans les champs de saisie respectifs
    @FXML void selectionner(MouseEvent evnt){
    	
     	index = listeRec.getSelectionModel().getSelectedIndex();
     	if (index <= -1){
     		return;
     	}
 		textFieldCode.setText(idR.getCellData(index).toString());
 		textFieldNum.setText(numR.getCellData(index).toString());
 		textFieldQteA.setText(qteAchat.getCellData(index).toString());
 		((ComboBox<String>) textFieldCai).setValue(Cai.getCellData(index).toString());
 		((ComboBox<String>) textFieldCli).setValue(Cli.getCellData(index).toString());
		textFieldArt.setPromptText(null);
 	
     }
    
    
    //Ajouter des données 
    @FXML void ajouter(ActionEvent event){
        
    	conn = ConnectionBD.CheckConnection();
        
        if (!isFieldsEmpty()) {
        	
			int id_Caiss = AcheterController.elSelected(req_Cai, textFieldCai.getSelectionModel().getSelectedItem());
			int id_Clien = AcheterController.elSelected(req_Cli, textFieldCli.getSelectionModel().getSelectedItem());			
			int prix = AcheterController.elSelected(req_Art, textFieldArt.getSelectionModel().getSelectedItem());
			
			prix = prix * (Integer.valueOf(textFieldQteA.getText()));

            String sql = "INSERT INTO recus(id_Recu, Caisse_id, Client_id, numero_Recu, qte_Achat, montant_Achat, date_Recu) "
            		+ "VALUES ( NULL, '" + id_Caiss + "','" + id_Clien + "','" 
            		+ textFieldNum.getText() + "','" 
            		+ textFieldQteA.getText() + "','" 
                    + prix + "','" 
            		+ ConnectionBD.dateActuel()+ "');";
            try {
            	
            	st = conn.createStatement();
		        st.executeUpdate(sql);
		        
		        new Alert(Alert.AlertType.INFORMATION, "Reçu ajouté avec succès").showAndWait();
		        updateTableView();
				reset_champ();
				
		        st.close();
		        conn.close();
		        
            }catch(Exception exception){
            	System.out.println(exception);
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
 	        
        	int id_Caiss = AcheterController.elSelected(req_Cai, textFieldCai.getSelectionModel().getSelectedItem());
 			int id_Clien = AcheterController.elSelected(req_Cli, textFieldCli.getSelectionModel().getSelectedItem());			
 			int prix = AcheterController.elSelected(req_Art, textFieldArt.getSelectionModel().getSelectedItem());
 			
			prix = prix * (Integer.valueOf(textFieldQteA.getText()));
 			
            String sql = "UPDATE recus SET Caisse_id ='" + id_Caiss +  "', Client_id ='" + id_Clien + "'";
            sql = sql + " ,numero_Recu ='" + textFieldNum.getText() + "'";
            sql = sql + " ,qte_Achat ='" + textFieldQteA.getText() + "', montant_Achat ='" + prix;
            sql = sql + "' ,date_Recu ='" + ConnectionBD.dateActuel() + "' WHERE id_Recu =" + textFieldCode.getText() +";";
            System.out.println(sql);

            try {
            	st = conn.createStatement();
 	            st.executeUpdate(sql);

 	            new Alert(Alert.AlertType.INFORMATION, "Reçu modifié avec succès").showAndWait();
 	            updateTableView();
				reset_champ();

 	            st.close();
 	            conn.close();
             }
             catch(Exception exception){
             	new Alert(Alert.AlertType.ERROR, "Mauvaise modification des données").showAndWait();
             	System.out.println(exception);
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

		if (("".equals(textFieldNum.getText()) || ("".equals(textFieldQteA.getText())) || textFieldCai.getSelectionModel().isEmpty() || textFieldCli.getSelectionModel().isEmpty())) {

			String sql = "DELETE FROM recus WHERE id_Recu = " + textFieldCode.getText() + ";";

			Alert on_supprime = new Alert(Alert.AlertType.CONFIRMATION);
			on_supprime.setContentText("Etes vous sûr de vouloir supprimer ses données?");
			on_supprime.setHeaderText("Suppression des données");
			on_supprime.setTitle("Confirmation pour suppression");
			Optional<ButtonType> action = on_supprime.showAndWait();

			if (action.get() == ButtonType.OK) {
				try {
					st = conn.createStatement();
					st.executeUpdate(sql);

					new Alert(Alert.AlertType.INFORMATION, "Reçu supprimé avec succès").showAndWait();
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

		if (textFieldNum.getText().isBlank() || textFieldQteA.getText().isBlank()
				|| textFieldCai.getSelectionModel().isEmpty()
				|| textFieldCli.getSelectionModel().isEmpty()
				|| textFieldArt.getSelectionModel().isEmpty()) {
			System.out.println("Champs vides");
			return true;

		} else {
			return false;
		}
	}
	
	
	// Fonction de type liste de chaine de caracteres pour remplir le comboBox de Caissiere Client et Article
	public static List<String> fillComboBox(String query) {

		// Declaration d'une liste de chaine de caracteres
		List<String> options = new ArrayList<>();

		try {
			conn = ConnectionBD.CheckConnection();
			try {
				st = conn.createStatement();
				rs = st.executeQuery(query);

				while (rs.next()) {
					options.add(rs.getString(1));

				}
				st.close();
				rs.close();
				conn.close();

			} catch (Exception sqlExcptn) {
				System.out.println("Requete non executée");
			}

		} catch (Exception ex) {
			System.out.println("Connexion echouée à la base de données.");

		}
		return options;
	}
	

	// Vider les champs de saisie
	void reset_champ() {

		textFieldNum.clear();
		textFieldQteA.clear();
		((ComboBox<String>) textFieldCai).setValue("Selectionner le caissier");
		((ComboBox<String>) textFieldCli).setValue("Selectionner le client");
		((ComboBox<String>) textFieldArt).setValue("Selectionner l'article");

	}
	

	// Afficher les données de la BD dans la tableView
	void updateTableView() {
		
		numR.setCellValueFactory(new PropertyValueFactory<Recu, String>("numR"));
		dateAchat.setCellValueFactory(new PropertyValueFactory<Recu, String>("dateAchat"));
		qteAchat.setCellValueFactory(new PropertyValueFactory<Recu, Integer>("qteAchat"));
		mtAchat.setCellValueFactory(new PropertyValueFactory<Recu, Integer>("mtAchat"));
		Cai.setCellValueFactory(new PropertyValueFactory<Recu, String>("Cai"));
		Cli.setCellValueFactory(new PropertyValueFactory<Recu, String>("Cli"));
		idR.setCellValueFactory(new PropertyValueFactory<Recu, Integer>("idR"));

		liste = ConnectionBD.getRecus();
		listeRec.setItems(liste);
	}

	int numeroEnreg(int idFour) {
		int numEnreg = 0;
		conn = ConnectionBD.CheckConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(ConnectionBD.SelectallRec);

			while (rs.next()) {
				numEnreg++;
				int num = rs.getInt(1);
				if (num == idFour)
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
		int numFour = 0;
		conn = ConnectionBD.CheckConnection();
		try {
			Statement st = conn.createStatement();

			ResultSet rs = st.executeQuery(ConnectionBD.SelectallRec);

			while (rs.next()) {
				numEnreg++;
				if (numEnreg == numLog) {
					numFour = rs.getInt(1);
					break;
				}
			}
			rs.close();
			st.close();
			conn.close();
		} catch (Exception sqlExcptn) {
			System.out.println(sqlExcptn);
		}
		return numFour;
	}

	int nbTotalEnreg() {
		int nbTEnreg = 0;
		conn = ConnectionBD.CheckConnection();
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT count(id_Article) FROM articles;";
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

	void chargerEnreg(int numArticle, int sens) {
		int n = numeroEnreg(numArticle) + sens;
		int num_rec = trouverCleEnreg(n);
		String client_name = null, caissier_name = null;
		conn = ConnectionBD.CheckConnection();
		try {
			
			Statement st = conn.createStatement();
			String sql = "SELECT * FROM recus WHERE id_Recu = " + num_rec;
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				textFieldCode.setText(String.valueOf(rs.getInt(1)));
				
				String req_idCai = "SELECT nom_Caissiere FROM caisses WHERE id_Caisse= '" + rs.getInt(2) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idCai);
	    			if (rst.first()) {
	    				caissier_name = rst.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 dans chargerEnreg()");
	    		}
	    		String req_idRay = "SELECT libelle_Rayon FROM rayons WHERE id_Rayon= '" + rs.getInt(3) + "';";

	    		try {
	    			sta = conn.createStatement();
	    			rslt = sta.executeQuery(req_idRay);
	    			if (rslt.first()) {
	    				client_name = rslt.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println(exception);
	    			System.out.println("Erreur11 dans chargerEnreg()");
	    		}
				textFieldCai.setPromptText(caissier_name);
				textFieldCli.setPromptText(client_name);
				textFieldNum.setText(rs.getString(4));
				textFieldQteA.setText(String.valueOf(rs.getInt(5)));

			}
			rs.close();
			st.close();
			conn.close();
		} catch (Exception sqlExcptn) {
			System.out.println(sqlExcptn);
		}
	}
    
    
}
