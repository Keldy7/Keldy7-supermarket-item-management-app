package application.Actions_Admin.Faire_une_recherche;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Database.ConnectionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class FaireRechercheController implements Initializable {
	@FXML private Button btnRech;
	
	@FXML private Button btnReset;
	
	@FXML private Label label_nbResultat;

	@FXML private Pane pane_Anchor;
	
	@FXML private RadioButton radAchats;

    @FXML private RadioButton radArticles;

    @FXML private RadioButton radCaisses;

    @FXML private RadioButton radCategories;

    @FXML private RadioButton radClients;

    @FXML private RadioButton radCommandes;

    @FXML private RadioButton radFournisseurs;

    @FXML private RadioButton radLivraisons;

    @FXML private RadioButton radRayons;

    @FXML private RadioButton radRecus;

    @FXML private TextArea textAreaInfo;

    @FXML private TextField textFieldSearch;
    
	@FXML private ToggleGroup  lesTables;
	
	List<String> options = new ArrayList<String>();

    Alert alert = new Alert(Alert.AlertType.WARNING);
    
	static Connection conn = null;
    static PreparedStatement st;
    static Statement stat = null;
    static ResultSet rs = null;
	
    static String SelectallCli = "SELECT * FROM clients ORDER BY id_Client asc";
	static String SelectallCom = "SELECT * FROM commande_f ORDER BY id_CommandeF asc;";
	static String SelectallRec = "SELECT * FROM recus ORDER BY id_Recu asc;";
	static String SelectallAch = "SELECT * FROM acheter;";
	static String SelectallLiv = "SELECT * FROM livrer;";


    
    @Override
	public void initialize(URL url, ResourceBundle rb) {

	}

    @FXML void clearFields(ActionEvent event) {
    	resetChamps();
    }

    @FXML void close_vue(MouseEvent event) {
		new application.Actions_Admin.Add_new_user.AddUserController().close_vue(event);
    }

    @FXML void rechercherElement(ActionEvent event) {
    	if (!textFieldSearch.getText().isBlank()) {
        	   	if (radAchats.isSelected()) {
        	   		//infos_Achats();

				}else if (radArticles.isSelected()) {
					infos_Articles();
					
				}else if (radCaisses.isSelected()) {
					infos_Caisses();

				}else if (radCategories.isSelected()) {
					infos_Categories();

				}else if (radClients.isSelected()) {
					//infos_Clients();

				}else if (radCommandes.isSelected()) {
					//infos_Commandes();
					
				}else if (radFournisseurs.isSelected()) {
					infos_Fournisseurs();
					
				}else if (radLivraisons.isSelected()) {				
					//infos_Livraisons();
					
				}else if (radRayons.isSelected()) {
					infos_Rayons();
					
				}else if (radRecus.isSelected()) {
					//infos_Recus();
					
				}else {
		            alert.setContentText("Aucune table n'a eté cochée.");
		            alert.setHeaderText(null);
		            alert.setTitle(null);
		            alert.showAndWait();
		            textFieldSearch.clear();
				}
    	}else {
    		alert.setContentText("Champ de saisie vide");
            alert.setHeaderText(null);
            alert.setTitle(null);
            alert.showAndWait();
		}
    } 
    
    
    List<String> infos_Articles(){
    	   conn = ConnectionBD.CheckConnection();
    	   textFieldSearch.setDisable(false);
    	   String lesinfo = "";
    	   
    	  String SelectallArt = "SELECT * FROM articles WHERE libelle_Article LIKE '%" + textFieldSearch.getText()
    	  						+ "%' OR prix_Article LIKE '%" + textFieldSearch.getText() + "%' ORDER BY id_Article asc ;";
    	  System.out.println(SelectallArt);
   		
    	  try {
   		
   		st = conn.prepareStatement(SelectallArt);
   		rs = st.executeQuery();
   		
   		while(rs.next()) {
   			options.add(rs.getString(2));
   			options.add(String.valueOf(rs.getInt(3)));

   		
   		}
   		rs.close();
   		st.close();
   		conn.close();
   		
   		if(options.size() <= 0) {
   			lesinfo = "\n\n\n\n\t\t\tAucun résultat trouvé";
 		}else {
 			
 			for (int s = 0; s < options.size(); s++) {
 				if (s == 1) {
 					lesinfo = lesinfo + options.get(s) +"     \n";
 				} else if (s == 3){					
 					lesinfo = "\n" + lesinfo + options.get(s) +"     \n";
 				} else if (s == 5){
 					lesinfo = "\n" + lesinfo + options.get(s) +"     \n";
 				} else if (s == 7){
 					lesinfo = "\n" + lesinfo + options.get(s) +"      \n";
 				} else if (s == 9){
 					lesinfo = "\n" + lesinfo + options.get(s) +"      \n";
 				}else if (s == 11){
 					lesinfo = "\n" + lesinfo + options.get(s) +"      \n";						
 				}else if (s == 13){
 					lesinfo = "\n" + lesinfo + options.get(s) +"      \n";						
 				}else if (s == 15){
 					lesinfo = "\n" + lesinfo + options.get(s) +"      \n";
 				
 				}else {
 					lesinfo += options.get(s) +"    \t";
 			
 					}
 				}
 			}
 		int rsFind = options.size() / 2;
 		if (rsFind <= 1 ) {
 			label_nbResultat.setText(String.valueOf(rsFind) + " résultat trouvé");
 		
 		} else {
 			label_nbResultat.setText(String.valueOf(rsFind) + " résultats trouvés");
 		
 		}
 		  		
   		String entete = "\t\t       LISTE DES ARTICLES\t\t\nLibéllé\t\tPrix\n";
   		textFieldSearch.setDisable(true);
   		btnRech.setDisable(true);
   		
   		textAreaInfo.setText(entete + lesinfo);		    
   		
   		}catch (SQLException ersql) {
   			System.out.println(ersql); 
   		}  		
   		
   		return options;
       }
     
    
    
    List<String> infos_Rayons(){
   	   conn = ConnectionBD.CheckConnection();
   	   textFieldSearch.setDisable(false);
   	   String info = "";
   	   
   	  String SelectallRay = "SELECT * FROM rayons WHERE libelle_Rayon LIKE '%" + textFieldSearch.getText()
   	  						+ "%' OR nbre_place LIKE '%" + textFieldSearch.getText() + "%' ORDER BY id_Rayon asc ;";
   	  System.out.println(SelectallRay);
  		
   	  try {
  		
  		st = conn.prepareStatement(SelectallRay);
  		rs = st.executeQuery();
  		
  		while(rs.next()) {
  			options.add(rs.getString(2));
  			options.add(String.valueOf(rs.getInt(3)));

  		
  		}
  		rs.close();
  		st.close();
  		conn.close();
  		
  		if(options.size() <= 0) {
			info = "\n\n\n\n\t\t\tAucun résultat trouvé";
		}else {
			
			for (int s = 0; s < options.size(); s++) {
				if (s == 1) {
					info = info + "\t" + options.get(s) +"     \n";
				} else if (s == 3){					
					info = "\n" + info + options.get(s) +"     \n";
				} else if (s == 5){
					info = "\n" + info + options.get(s) +"     \n";
				} else if (s == 7){
					info = "\n" + info + options.get(s) +"      \n";
				} else if (s == 9){
					info = "\n" + info + options.get(s) +"      \n";
				}else if (s == 11){
					info = "\n" + info + options.get(s) +"      \n";						
				}else if (s == 13){
					info = "\n" + info + options.get(s) +"      \n";						
				}else if (s == 15){
					info = "\n" + info + options.get(s) +"      \n";
				
				}else {
					info += options.get(s) +"    \t";
			
					}
				}
			}
		int rsFind = options.size() / 2;
		if (rsFind <= 1 ) {
			label_nbResultat.setText(String.valueOf(rsFind) + " résultat trouvé");
		
		} else {
			label_nbResultat.setText(String.valueOf(rsFind) + " résultats trouvés");
		
		}
		  		
  		String entete = "\t\t       LISTE DES RAYONS\t\t\nLibéllé\t\t\t\tNombre de place\n\n";
  		textFieldSearch.setDisable(true);
  		btnRech.setDisable(true);
  		
  		textAreaInfo.setText(entete + "\n" + info);		    
  		
  		}catch (SQLException ersql) {
  			System.out.println(ersql); 
  		}  		
  		
  		return options;
      }
    
    
    List<String> infos_Categories(){
  	   conn = ConnectionBD.CheckConnection();
  	   textFieldSearch.setDisable(false);
  	   String result = "";
  	   
  	  String SelectallCat = "SELECT * FROM categories WHERE libelle_Categorie LIKE '%" + textFieldSearch.getText() + "%' ORDER BY id_Categorie asc ;";
  	  System.out.println(SelectallCat); 		
  	  try {
 		
 		st = conn.prepareStatement(SelectallCat);
 		rs = st.executeQuery();
 		
 		while(rs.next()) {
 			options.add(rs.getString(2));
 		
 		}
 		rs.close();
 		st.close();
 		conn.close();
 		
 		if(options.size() <= 0) { //Si rs.next() ne retourne rien et liste est vide
 			result = "\n\n\n\n\t\t\tAucun résultat trouvé";
 		}else {
 			
 			for (int s = 0; s < options.size(); s++) {
 				
 					result += "\n\t\t\t    " + options.get(s);
 				
 				}
 			}
 		//determiner le nbre de resultats que retourne la requete sql
 		if (options.size() <= 1 ) {
 			label_nbResultat.setText(String.valueOf(options.size()) + " résultat trouvé");
 		
 		} else {
 			label_nbResultat.setText(String.valueOf(options.size()) + " résultats trouvés");
 		
 		}
 		
 		String entete = "\t\t       LISTE DES CATEGORIES\t\t\n\t\t\t     Libéllé\n\n";
 		textFieldSearch.setDisable(true);
 		btnRech.setDisable(true);
 		
 		textAreaInfo.setText(entete + "\n" + result);		    
 		
 		}catch (SQLException ersql) {
 		System.out.println(ersql); 
 		}
 		
 		return options;
     }
    
    
    
    List<String> infos_Caisses(){
 	  conn = ConnectionBD.CheckConnection();
 	  textFieldSearch.setDisable(false);
 	  String resultat = "";
 	   
 	  String SelectallCai = "SELECT * FROM caisses WHERE nom_Caissiere LIKE '%" + textFieldSearch.getText() 
 	  						+ "%' OR tel_Caissiere LIKE '%" + textFieldSearch.getText() + "%' ORDER BY id_Caisse asc ;";
 	  System.out.println(SelectallCai);
		
 	  try {
		
		st = conn.prepareStatement(SelectallCai);
		rs = st.executeQuery();
		
		while(rs.next()) {
			options.add(rs.getString(2));
			options.add(rs.getString(3));
		
		}
		rs.close();
		st.close();
		conn.close();
		if(options.size() <= 0) {
			resultat = "\n\n\n\n\t\t\tAucun résultat trouvé";
		}else {
			
			for (int s = 0; s < options.size(); s++) {
				if (s == 1) {
					resultat = resultat + "\t" + options.get(s) +"     \n";
				} else if (s == 3){					
					resultat = "\n" + resultat + options.get(s) +"     \n";
				} else if (s == 5){
					resultat = "\n" + resultat + options.get(s) +"     \n";
				} else if (s == 7){
					resultat = "\n" + resultat + options.get(s) +"      \n";
				} else if (s == 9){
					resultat = "\n" + resultat + options.get(s) +"      \n";
				}else if (s == 11){
					resultat = "\n" + resultat + options.get(s) +"      \n";						
				}else if (s == 13){
					resultat = "\n" + resultat + options.get(s) +"      \n";						
				}else if (s == 15){
					resultat = "\n" + resultat + options.get(s) +"      \n";
				
				}else {
					resultat += options.get(s) +"    \t";
			
					}
				}
			}
		int rsFind = options.size() / 2;
		if (rsFind <= 1 ) {
			label_nbResultat.setText(String.valueOf(rsFind) + " résultat trouvé");
		
		} else {
			label_nbResultat.setText(String.valueOf(rsFind) + " résultats trouvés");
		
		}
		
		String entete = "\t\t       LISTE DES CAISSIERES\t\t\nNom et Prénoms        Contact téléphonique\n\n";
		textFieldSearch.setDisable(true);
		btnRech.setDisable(true);
		
		textAreaInfo.setText(entete + resultat);		    
		
		}catch (SQLException ersql) {
		System.out.println(ersql); 
		}		
		
		return options;
    }
    
    
    List<String> infos_Fournisseurs(){
	  conn = ConnectionBD.CheckConnection();
	  textFieldSearch.setDisable(false);
	  String infos = "";

	   //textFieldSearch.setOnKeyReleased(e ->{ });
	   String SelectallFour = "SELECT * FROM fournisseurs WHERE nom_Fournisseur LIKE '%" + textFieldSearch.getText() + "%' OR address_Fournisseur LIKE '%" + textFieldSearch.getText()
	   				 + "%' OR tel_Fournisseur LIKE '%" + textFieldSearch.getText() + "%' ORDER BY id_Fournisseur asc ;";
	   System.out.println(SelectallFour);

	   try {
		   
			st = conn.prepareStatement(SelectallFour);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	options.add(rs.getString(2));
	        	options.add(rs.getString(3));
	        	options.add(rs.getString(4));

	        }
	        rs.close();
			st.close();
			conn.close();
			if(options.size() <= 0) {
				infos = "\n\n\n\n\t\t\tAucun résultat trouvé";
			}else {
				
				for (int s = 0; s < options.size(); s++) {
	        		if (s == 2) {
	            	    infos = infos + options.get(s) +"     \n";
					} else if (s == 5){
						infos = "\n" + infos + options.get(s) +"     \n";
					} else if (s == 8){
						infos = "\n" + infos + options.get(s) +"\t\n";
					} else if (s == 11){
						infos = "\n" + infos + options.get(s) +"\t\n";
					} else if (s == 14){
						infos = "\n" + infos + options.get(s) +"\t\n";
					}else if (s == 17){
						infos = "\n" + infos + options.get(s) +"\t\n";						
					}else if (s == 20){
						infos = "\n" + infos + options.get(s) +"\t\n";						
					}else if (s == 23){
						infos = "\n" + infos + options.get(s) +"\t\n";
					
					}else {
						infos += options.get(s) +"    \t";

					}
	        	}
			}
			int rsFind = options.size() / 3; //determiner le nbre de resultats que retourne la requete sql
			if (rsFind <= 1 ) {
				label_nbResultat.setText(String.valueOf(rsFind) + " résultat trouvé");

			} else {
				label_nbResultat.setText(String.valueOf(rsFind) + " résultats trouvés");

			}
			
			String entete = "\t\t       LISTE DES FOURNISSEURS\t\t\nNom et Prénoms     Adresse géographique     Contact téléphonique\n\n";
		    textFieldSearch.setDisable(true);
		    btnRech.setDisable(true);

		    textAreaInfo.setText(entete + infos);		    
			
	    }catch (SQLException ersql) {
	    	System.out.println(ersql); 
	    }
	     	    
	   return options;	   
   }
   
   void resetChamps() {
	   
	   options.removeAll(options); //2eme methode options.clear();
	   textAreaInfo.clear();
	   textFieldSearch.clear();
	   label_nbResultat.setText("");
	   textFieldSearch.setDisable(false);
	   btnRech.setDisable(false);
	   radAchats.setSelected(false);
	   radArticles.setSelected(false);
	   radCaisses.setSelected(false);
	   radCategories.setSelected(false);
	   radClients.setSelected(false);
	   radCommandes.setSelected(false);
	   radFournisseurs.setSelected(false);
	   radLivraisons.setSelected(false);
	   radRayons.setSelected(false);
	   radRecus.setSelected(false);
   }
    
}
