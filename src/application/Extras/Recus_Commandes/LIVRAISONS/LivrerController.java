package application.Extras.Recus_Commandes.LIVRAISONS;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import application.Database.ConnectionBD;
import application.Extras.Recus_Commandes.ACHATS.AcheterController;
import application.Extras.Recus_Commandes.RECUS.RecuController;


public class LivrerController implements Initializable {

	static String SelectallFour = "SELECT nom_Fournisseur FROM fournisseurs;";
	static String SelectallCom = "SELECT DISTINCT date_cf FROM commande_f;";
	String req_idCom = "SELECT id_CommandeF FROM commande_f WHERE date_cf= '";
	String req_idFou = "SELECT id_Fournisseur FROM fournisseurs WHERE nom_fournisseur= '";

	static Connection conn = null;
	static ResultSet rs = null, rst = null, rslt = null;
	static Statement st = null, stat = null, sta = null;

	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);

	ObservableList<Livrer> liste;
	static int laCommande = 1, leFournisseur = 1;


	/*---------------Les boutons-------------------*/

	@FXML private Button btn_nouveau;

	@FXML private Button btn_ajout;

	@FXML private Button btn_modif;

	@FXML private Button btn_sup;
	
	/*---------------Les comboBox-------------------*/
	
	@FXML private TextField textFieldQteL;

    @FXML private DatePicker textFieldDatLiv;
	
	@FXML private ComboBox<String> textFieldCom;

	@FXML private ComboBox<String> textFieldFour;

	/*-----------Les colonnes de TableView----------------*/

	@FXML private TableView<Livrer> listeLiv;

	@FXML private TableColumn<Livrer, String> idCom;

	@FXML private TableColumn<Livrer, String> idF;
	
	@FXML private TableColumn<Livrer, String> dateLiv;
	
	@FXML private TableColumn<Livrer, Integer> qteLiv;

	@FXML private Hyperlink menu;


	@Override
	public void initialize(URL loc, ResourceBundle rb) {
		
		listeLiv.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());
		
		textFieldCom.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(SelectallCom)));
		System.out.println("Liste de tous les noms de caissiers: " + RecuController.fillComboBox(SelectallCom));

        textFieldFour.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(SelectallFour)));
		System.out.println("Liste de tous les noms de clients ayant fait des achats: " + RecuController.fillComboBox(SelectallFour));

		updateTableView();

	}

	
	// Aller au menu des 4 boutons de l'app
	@FXML void goto_MenuRecCom(ActionEvent e) {
		new application.Extras.Recus_Commandes.ACHATS.AcheterController().goto_MenuRecCom(e);

	}

	// Effectuer un new enregistrement
	@FXML void nouveau(ActionEvent event) {
		reset_champ();
	}

	
	
	@FXML void ajouter(ActionEvent event) {

		conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty() & ConnectionBD.isNombre(textFieldQteL.getText())) {

			int id_Com = AcheterController.elSelected(req_idCom, textFieldCom.getSelectionModel().getSelectedItem());
			int id_Fou = AcheterController.elSelected(req_idFou, textFieldFour.getSelectionModel().getSelectedItem());
			
			//Convertir l'element selectionné du combobox de date de commande en LocalDate
			LocalDate Comdate = LocalDate.parse(textFieldCom.getSelectionModel().getSelectedItem(), DateTimeFormatter.ISO_DATE);
			//Convertir la date de livraison saisie en LocalDate
		    LocalDate Livdate = textFieldDatLiv.getValue();		   
		    
		    if (Livdate.isBefore(Comdate) ) {
				new Alert(Alert.AlertType.ERROR, "Dates incorrectes").showAndWait();
		    }else{

				String sql = "INSERT INTO livrer (CommandeF_id, Fournisseur_id, date_Liv, qte_Liv) VALUES ('"
						 + id_Com + "','" 
						+ id_Fou + "','" + Livdate + "','" + textFieldQteL.getText() + "');";
				System.out.println(sql);
				
				try {
					st = conn.createStatement();
					st.executeUpdate(sql);

					new Alert(Alert.AlertType.INFORMATION, "Livraison ajoutée avec succès").showAndWait();
					updateTableView();
					reset_champ();

					st.close();
					
				} catch (Exception exception) {
					System.out.println(exception);
					new Alert(Alert.AlertType.ERROR, "Mauvaise insertion des données").showAndWait();

				}
				try {
					conn.close();
					
				} catch (SQLException e) {
					System.out.println("erreur ohh " + e);
				}

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

		if (!isFieldsEmpty() & ConnectionBD.isNombre(textFieldQteL.getText())) {
			
			int id_Commande = AcheterController.elSelected(req_idCom, textFieldCom.getValue());			
			int id_Fournisseur = AcheterController.elSelected(req_idFou, textFieldFour.getValue());
			
			LocalDate Comdate = LocalDate.parse(textFieldCom.getSelectionModel().getSelectedItem(), DateTimeFormatter.ISO_DATE);
		    LocalDate Livdate = textFieldDatLiv.getValue();
			
		    if (Livdate.isBefore(Comdate) ) {
				new Alert(Alert.AlertType.ERROR, "Dates incorrectes").showAndWait();
		    }else{
		    	String sql = "UPDATE livrer SET CommandeF_id= '" + id_Commande + "', Fournisseur_id= '" + id_Fournisseur + "', date_Liv= '";
				 sql = sql + Livdate + "',qte_Liv= '" + textFieldQteL.getText();
				 sql = sql + "' WHERE CommandeF_id= " + laCommande + " AND Fournisseur_id= " + leFournisseur + ";";
		         System.out.println(sql);

				 try {
					st = conn.createStatement();
					st.executeUpdate(sql);

					new Alert(Alert.AlertType.INFORMATION, "Livraison modifiée avec succès").showAndWait();
					updateTableView();
					reset_champ();

					st.close();
					conn.close();
				 }catch (Exception exception) {
					System.out.println(exception);
					new Alert(Alert.AlertType.ERROR, "Mauvaise modification des données").showAndWait();

				}
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

			String sql = "DELETE FROM livrer WHERE CommandeF_id = " + laCommande + " AND Fournisseur_id= " + leFournisseur + " AND qte_Liv= " + textFieldQteL.getText() + ";";
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

					new Alert(Alert.AlertType.INFORMATION, "Livraison supprimée avec succès").showAndWait();
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
	
	
	private class RowSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
            int ix = newVal.intValue();
            if ((ix == liste.size() || ix < -1)) {
                return; // Données invalide
            }
            
            try {
            	Livrer liv = liste.get(ix);

    			leFournisseur = AcheterController.elSelected(req_idFou, liv.getIdF());    			
    			laCommande = AcheterController.elSelected(req_idCom, liv.getIdCom());

                textFieldCom.setValue(liv.getIdCom());
                textFieldFour.setValue(liv.getIdF());
                textFieldQteL.setText(String.valueOf(liv.getQteLiv()));
                textFieldDatLiv.setValue(LocalDate.parse(String.valueOf(liv.getDateLiv())));
                
            }catch(Exception ex){
            	System.out.println();
            }            
        }
		
    }
	
	
	// Tester les champs vides
	boolean isFieldsEmpty() {

		if (("".equals(textFieldQteL.getText())) 
				|| textFieldCom.getSelectionModel().isEmpty()
				|| textFieldFour.getSelectionModel().isEmpty()) {
			System.out.println("Champs vides");
			return true;

		} else {
			return false;
		}
	}
	
	

	// Vider les champs de saisie
	void reset_champ() {
		
		textFieldDatLiv.setValue(null);
		textFieldDatLiv.setPromptText(null);
		textFieldQteL.clear();
		((ComboBox<String>) textFieldCom).setValue("Selectionner la date de commande");
		((ComboBox<String>) textFieldFour).setValue("Selectionner le fournisseur");

	}
	

	// Afficher les données de la BD dans la tableView
	void updateTableView() {
		
		idCom.setCellValueFactory(new PropertyValueFactory<Livrer, String>("idCom"));	
		idF.setCellValueFactory(new PropertyValueFactory<Livrer, String>("idF"));
		dateLiv.setCellValueFactory(new PropertyValueFactory<Livrer, String>("dateLiv"));
		qteLiv.setCellValueFactory(new PropertyValueFactory<Livrer, Integer>("qteLiv"));		

		liste = ConnectionBD.getLivraisons();
		listeLiv.setItems(liste);
	}
}
