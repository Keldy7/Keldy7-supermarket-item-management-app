package application.ARTICLES;

import java.net.URL;
import java.sql.*;
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
import application.Extras.Recus_Commandes.RECUS.RecuController;


public class ArticleController implements Initializable {

	String req_idCat = "SELECT id_Categorie FROM categories WHERE libelle_Categorie= '";
	String req_idRay = "SELECT id_Rayon FROM rayons WHERE libelle_Rayon= '";
	String req_libCat = "SELECT libelle_Categorie FROM categories;";
	String req_libRay = "SELECT libelle_Rayon FROM rayons;";
	
	static Connection conn = null;
	static ResultSet rs = null, rst = null, rslt = null;
	static Statement st = null, stat = null, sta = null;

	Stage stage = new Stage();
	Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);

	ObservableList<Article> liste;
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

	@FXML private TextField textFieldCode;

	@FXML private TextField textFieldLib;

	@FXML private TextField textFieldPrix;

	@FXML private ComboBox<String> textFieldCat;

	@FXML private ComboBox<String> textFieldRay;

	/*-----------Les colonnes de TableView----------------*/

	@FXML private TableView<Article> listeArt;

	@FXML private TableColumn<Article, Integer> idA;

	@FXML private TableColumn<Article, String> libA;

	@FXML private TableColumn<Article, Integer> prixA;

	@FXML private TableColumn<Article, String> Cat;

	@FXML private TableColumn<Article, String> Ray;

	@FXML private Hyperlink menu;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		textFieldCat.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(req_libCat)));
		System.out.println("Liste de tous les catégories: " + RecuController.fillComboBox(req_libCat));

		textFieldRay.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(req_libRay)));
		System.out.println("Liste de tous les rayons: " + RecuController.fillComboBox(req_libRay));
		
		chargerEnreg(1, 0);
		updateTableView();
	}

	// Aller au menu principal de l'app
	@FXML public void goto_Menu(ActionEvent e) {
		try {
			Parent dashboard = FXMLLoader.load(getClass().getClassLoader().getResource(RouteInterfaceFXML.VueDashboard));

			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(new Scene(dashboard));
			stage.show();
			((Stage) ((Node) e.getSource()).getScene().getWindow()).close(); // Fermer l'interface precedente

		} catch (Exception erreur) {
			erreur.printStackTrace();
		}
	}
	
	

	// Afficher le premier enregistrement de la BD
	@FXML void premierEnreg(ActionEvent event) {
		try {
			int numA = 1;
			chargerEnreg(numA, 0);

		} catch (Exception erreur) {
			System.out.println(erreur);
		}
	}
	

	// Afficher le precedent enregistrement de la BD
	@FXML void precedentEnreg(ActionEvent event) {
		try {
			int numA = Integer.valueOf(textFieldCode.getText()).intValue();
			chargerEnreg(numA, -1);

		} catch (Exception erreur) {
			System.out.println(erreur);
		}
	}

	// Afficher l'enregistrement suivant de la BD
	@FXML void suivantEnreg(ActionEvent event) {
		try {
			int numA = Integer.valueOf(textFieldCode.getText()).intValue();
			chargerEnreg(numA, 1);

		} catch (Exception erreur) {
			System.out.println(erreur);
		}
	}

	// Afficher le dernier enregistrement de la BD
	@FXML void dernierEnreg(ActionEvent event) {
		try {
			int numA = nbTotalEnreg();
			chargerEnreg(numA, 0);

		} catch (Exception erreur) {
			System.out.println(erreur);

		}
	}
	

	// Effectuer un new enregistrement
	@FXML void nouveau(ActionEvent event) {
		reset_champ();
	}

	// Afficher la ligne selectionnée dans les champs de saisie respectifs
	@FXML void selectionner(MouseEvent evnt) {

		index = listeArt.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		textFieldCode.setText(idA.getCellData(index).toString());
		textFieldLib.setText(libA.getCellData(index).toString());
		textFieldPrix.setText(prixA.getCellData(index).toString());
		((ComboBox<String>) textFieldCat).setValue(Cat.getCellData(index).toString());
		((ComboBox<String>) textFieldRay).setValue(Ray.getCellData(index).toString());

	}


	@FXML
	void ajouter(ActionEvent event) {
		conn = ConnectionBD.CheckConnection();


		if (!isFieldsEmpty() & ConnectionBD.isLettre(textFieldLib.getText())
				& ConnectionBD.isNombre(textFieldPrix.getText())) {

			// int output = textFieldCat.getSelectionModel().getSelectedIndex(); recupï¿½re
			// l'index de l'elem selectionnï¿½ (de 0 ï¿½ n)
			int id_Categ = AcheterController.elSelected(req_idCat, textFieldCat.getSelectionModel().getSelectedItem());			
			int id_Rayon = AcheterController.elSelected(req_idRay, textFieldRay.getSelectionModel().getSelectedItem());
			

			String sql = "INSERT INTO articles (id_Article, libelle_Article, prix_Article, id_Categorie, id_Rayon) VALUES (NULL, '"
					+ textFieldLib.getText() + "','" 
					+ textFieldPrix.getText() + "','" + id_Categ + "','" + id_Rayon + "');";
			System.out.println(sql);
			try {
				st = conn.createStatement();
				st.executeUpdate(sql);

				new Alert(Alert.AlertType.INFORMATION, "Article ajouté avec succès").showAndWait();
				updateTableView();
				reset_champ();

				st.close();
				conn.close();
			} catch (Exception exception) {
				System.out.println(exception);
				new Alert(Alert.AlertType.ERROR, "Mauvaise insertion des données").showAndWait();

			}

		} else {

			alerte.setHeaderText("Champs de saisie vides !");
			alerte.setContentText("Veuillez remplir tous les champs de saisies pour effectuer une insertion dans la base de données.");
			alerte.setTitle("Erreur");
			alerte.showAndWait();
		}

	}

	@FXML
	void modifier(ActionEvent event) {

		conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty() & ConnectionBD.isLettre(textFieldLib.getText())
				& ConnectionBD.isNombre(textFieldPrix.getText())) {
			
			int id_Categ = AcheterController.elSelected(req_idCat, textFieldCat.getSelectionModel().getSelectedItem());			
			int id_Rayon = AcheterController.elSelected(req_idRay, textFieldRay.getSelectionModel().getSelectedItem());
			
			 String sql = "UPDATE articles SET "; 
			 sql = sql + "libelle_Article ='" + textFieldLib.getText() + "'"; 
			 sql = sql + " ,prix_Article ='" + textFieldPrix.getText() + "'";
			 sql = sql + " ,id_Categorie ='" + id_Categ + "' ,id_Rayon ='" + id_Rayon + "' ";
			 sql = sql + "WHERE id_Article= " + textFieldCode.getText() +";";
	         System.out.println(sql);

			try {
				st = conn.createStatement();
				st.executeUpdate(sql);

				new Alert(Alert.AlertType.INFORMATION, "Article modifié avec succès").showAndWait();
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

	@FXML
	void supprimer(ActionEvent event) {

		conn = ConnectionBD.CheckConnection();

		if (!isFieldsEmpty()) {

			String sql = "DELETE FROM articles WHERE id_Article = " + textFieldCode.getText() + ";";

			Alert on_supprime = new Alert(Alert.AlertType.CONFIRMATION);
			on_supprime.setContentText("Etes vous sûr de vouloir supprimer ses données?");
			on_supprime.setHeaderText("Suppression des données");
			on_supprime.setTitle("Confirmation pour suppression");
			Optional<ButtonType> action = on_supprime.showAndWait();

			if (action.get() == ButtonType.OK) {
				try {
					st = conn.createStatement();
					st.executeUpdate(sql);

					new Alert(Alert.AlertType.INFORMATION, "Article supprimé avec succès").showAndWait();
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

		if (textFieldLib.getText().trim().isBlank() || textFieldPrix.getText().trim().isBlank() 
				|| textFieldCat.getSelectionModel().isEmpty() 
				|| textFieldRay.getSelectionModel().isEmpty()) {
			return true;

		} else {
			return false;
		}
	}
	
	

	// Vider les champs de saisie
	void reset_champ() {

		textFieldLib.clear();
		textFieldPrix.clear();
		((ComboBox<String>) textFieldCat).setValue("Selectionner la catégorie");
		((ComboBox<String>) textFieldRay).setValue("Selectionner le rayon");

	}

	// Afficher les donnï¿½es de la BD dans la tableView
	void updateTableView() {

		idA.setCellValueFactory(new PropertyValueFactory<Article, Integer>("idA"));
		libA.setCellValueFactory(new PropertyValueFactory<Article, String>("libA"));
		prixA.setCellValueFactory(new PropertyValueFactory<Article, Integer>("prixA"));
		Cat.setCellValueFactory(new PropertyValueFactory<Article, String>("Cat"));
		Ray.setCellValueFactory(new PropertyValueFactory<Article, String>("Ray"));
		
		liste = ConnectionBD.getArticles();
		listeArt.setItems(liste);
	}

	int numeroEnreg(int idFour) {
		int numEnreg = 0;

		try {
			conn = ConnectionBD.CheckConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(ConnectionBD.SelectallArt);

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

			ResultSet rs = st.executeQuery(ConnectionBD.SelectallArt);

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
		int num_art = trouverCleEnreg(n);
		String rayon_name = null, categorie_name = null;
		conn = ConnectionBD.CheckConnection();
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT * FROM articles WHERE id_Article = " + num_art;
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				textFieldCode.setText(String.valueOf(rs.getInt(1)));
				textFieldLib.setText(rs.getString(2));
				textFieldPrix.setText(rs.getString(3));
				
				String req_idCat = "SELECT libelle_Categorie FROM categories WHERE id_Categorie= '" + rs.getInt(6) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idCat);
	    			if (rst.first()) {
	    				categorie_name = rst.getString("libelle_Categorie");
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 dans chargerEnreg()");
	    		}
	    		String req_idRay = "SELECT libelle_Rayon FROM rayons WHERE id_Rayon= '" + rs.getInt(7) + "';";

	    		try {
	    			sta = conn.createStatement();
	    			rslt = sta.executeQuery(req_idRay);
	    			if (rslt.first()) {
	    				rayon_name = rslt.getString("libelle_Rayon");
	    			}
	    		} catch (Exception exception) {
	    			System.out.println(exception);
	    			System.out.println("Erreur11 dans chargerEnreg()");
	    		}
				textFieldCat.setPromptText(categorie_name);
				textFieldRay.setPromptText(rayon_name);

			}
			rs.close();
			st.close();
			conn.close();
		} catch (Exception sqlExcptn) {
			System.out.println(sqlExcptn);
		}
	}

}
