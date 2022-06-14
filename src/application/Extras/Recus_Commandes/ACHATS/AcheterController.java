package application.Extras.Recus_Commandes.ACHATS;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import application.CheminFXML.RouteInterfaceFXML;
import application.Database.ConnectionBD;
import application.Extras.Recus_Commandes.RECUS.RecuController;


public class AcheterController implements Initializable{
	

	public static String SelectallCli = "SELECT CONCAT(nom_Client,' ',prenom_Client) FROM clients;";
	public static String SelectallArticl = "SELECT libelle_Article FROM articles;";
	
	public static String req_idArt = "SELECT id_Article FROM articles WHERE libelle_Article= '";
    String req_idCli = "SELECT id_Client FROM clients WHERE CONCAT(nom_Client,' ',prenom_Client)= '";

	static Connection conn = null;
	static ResultSet rs = null, rst = null, rslt = null;
	static Statement st = null, stat = null, sta = null;

	Stage stage = new Stage();
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);

	ObservableList<Acheter> liste;
	static int leClient = 1, lArticle = 1;


	/*---------------Les boutons-------------------*/

	@FXML private Button btn_nouveau;

	@FXML private Button btn_ajout;

	@FXML private Button btn_modif;

	@FXML private Button btn_sup;


	/*---------------Les comboBox-------------------*/

	@FXML private ComboBox<String> textFieldCli;

	@FXML private ComboBox<String> textFieldArt;

	/*-----------Les colonnes de TableView----------------*/

	@FXML private TableView<Acheter> listeAch;

	@FXML private TableColumn<Acheter, String> Cli;

	@FXML private TableColumn<Acheter, String> Art;

	@FXML private Hyperlink menu;
	


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		listeAch.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());
		textFieldCli.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(SelectallCli)));
		System.out.println("Liste de tous les clients: " + RecuController.fillComboBox(SelectallCli));

		textFieldArt.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(SelectallArticl)));
		System.out.println("Liste de tous les rayons: " + RecuController.fillComboBox(SelectallArticl));
		updateTableView();
		
	}
	
	
	// Aller au menu des 4 boutons de l'app
	@FXML public void goto_MenuRecCom(ActionEvent e) {
		try {
			Parent rec_com = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueRecuCommande));

			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(rec_com));
			stage.show();
			((Stage) ((Node) e.getSource()).getScene().getWindow()).close(); // Fermer l'interface precedente

		} catch (Exception erreur) {
			erreur.printStackTrace();
		}
	}
	
	
	private class RowSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
            int ix = newVal.intValue();
            if ((ix == liste.size() || ix < -1)) {
                return; // Données invalide
            }
            
            try {
            	Acheter ach = liste.get(ix);

    			leClient = elSelected(req_idCli, ach.getCli());    			
    			lArticle = elSelected(req_idArt, ach.getArt());

                textFieldCli.setValue(ach.getCli());
                textFieldArt.setValue(ach.getArt());
                
            }catch(Exception ex){
            	System.out.println();
            }
            
        }
    }	
	
	
	// Effectuer un new enregistrement
	@FXML void nouveau(ActionEvent event) {
		reset_champ();
	}


	@FXML void ajouter(ActionEvent event) {
		conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty()) {
			
			int id_Cli = elSelected(req_idCli, textFieldCli.getSelectionModel().getSelectedItem());
			int id_Art = elSelected(req_idArt, textFieldArt.getSelectionModel().getSelectedItem());

			String sql = "INSERT INTO acheter (id_Article, id_Client) VALUES ('"
					 + id_Art + "','" 
					+ id_Cli + "');";
			System.out.println(sql);
			try {
				st = conn.createStatement();
				st.executeUpdate(sql);

				new Alert(Alert.AlertType.INFORMATION, "Achat ajouté avec succès").showAndWait();
				updateTableView();
				reset_champ();

				st.close();
			} catch (Exception exception) {
				System.out.println(exception);
				new Alert(Alert.AlertType.ERROR, "Mauvaise insertion des données").showAndWait();

			}try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("");
			}

		} else {

			alerte.setHeaderText("Champs de saisie vides !");
			alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une insertion dans la base de données.");
			alerte.setTitle("Erreur");
			alerte.showAndWait();
		}

	}
	
	

	@FXML void modifier(ActionEvent event) {
		 

		conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty()) {
			int id_Client = elSelected(req_idCli, textFieldCli.getValue());
			int id_Article = elSelected(req_idArt, textFieldArt.getValue());
			
			 String sql = "UPDATE acheter SET id_Article= '" + id_Article + "', id_Client= '" + id_Client + "'";
			  sql = sql + " WHERE id_Article= " + lArticle + " AND id_Client= " + leClient + ";";
	         System.out.println(sql);

			try {
				st = conn.createStatement();
				st.executeUpdate(sql);

				new Alert(Alert.AlertType.INFORMATION, "Achat modifié avec succès").showAndWait();
				updateTableView();
				reset_champ();

				st.close();
				conn.close();
			} catch (Exception exception) {
				new Alert(Alert.AlertType.ERROR, "Mauvaise modification des données").showAndWait();

			}
		} else {

			alerte.setHeaderText("Champs de saisie vides !");
			alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une modification dans la base de données.");
			alerte.setTitle("Erreur");
			alerte.showAndWait();

		}

	}

	@FXML void supprimer(ActionEvent event) {

		conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty()) {

			String sql = "DELETE FROM acheter WHERE id_Article = " + lArticle 
					+ " AND id_Client=" + leClient + ";";
			System.out.println(sql);

			Alert on_supprime = new Alert(Alert.AlertType.CONFIRMATION);
			on_supprime.setContentText("Etes vous sûr de vouloir supprimer ses données?");
			on_supprime.setHeaderText("Suppression des données");
			on_supprime.setTitle("Confirmation pour suppression");
			Optional<ButtonType> action = on_supprime.showAndWait();

			if (action.get() == ButtonType.OK) {
				try {
					st = conn.createStatement();
					st.executeUpdate(sql);

					new Alert(Alert.AlertType.INFORMATION, "Achat supprimé avec succès").showAndWait();
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

		if (textFieldCli.getSelectionModel().isEmpty() || textFieldArt.getSelectionModel().isEmpty()) {
			System.out.println("Champs vides");
			return true;

		} else {
			return false;
		}
	}
	
	
	//------------RECUPERATION DE L'ID DE L'ELEMENT SELECTIONNEE--------------
	public static int elSelected(String lareq, String tblename) {
		conn = ConnectionBD.CheckConnection();

		int id = 0;
		lareq = lareq + tblename + "';";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(lareq);
			if (rs.first()) {
				id = rs.getInt(1);
			}
			rs.close();
			st.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
		return id;

	}

			
	// Vider les champs de saisie
	void reset_champ() {

		((ComboBox<String>) textFieldCli).setValue("Selectionner le client");
		((ComboBox<String>) textFieldArt).setValue("Selectionner l'article");

	}

	// Afficher les données de la BD dans la tableView
	void updateTableView() {

		Cli.setCellValueFactory(new PropertyValueFactory<Acheter, String>("Cli"));
		Art.setCellValueFactory(new PropertyValueFactory<Acheter, String>("Art"));
		
		liste = ConnectionBD.getAchats();
		listeAch.setItems(liste);
		
	}

}
