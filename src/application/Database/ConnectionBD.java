package application.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import application.ARTICLES.Article;
import application.CAISSES.Caisse;
import application.CLIENTS.Client;
import application.Extras.Categories_Rayons.CATEGORIES.Categorie;
import application.Extras.Categories_Rayons.RAYONS.Rayon;
import application.Extras.Recus_Commandes.ACHATS.Acheter;
import application.Extras.Recus_Commandes.COMMANDES.CommandeF;
import application.Extras.Recus_Commandes.LIVRAISONS.Livrer;
import application.Extras.Recus_Commandes.RECUS.Recu;
//import application.Extras.Recus_Commandes.RECUS.RecuPrint;
import application.FOURNISSEURS.Fournisseur;



public class ConnectionBD {
	
    public static Connection conn = null;
    static PreparedStatement st;
    public static Statement stat = null, sta = null;
    public static ResultSet rst = null, rslt = null, rs = null;
	
    public static String SelectallFour = "SELECT * FROM fournisseurs ORDER BY id_Fournisseur asc";
    public static String SelectallArt = "SELECT * FROM articles ORDER BY id_Article asc";
    public static String SelectallCli = "SELECT * FROM clients ORDER BY id_Client asc";
    public static String SelectallCat = "SELECT * FROM categories ORDER BY id_Categorie asc";
    public static String SelectallRay = "SELECT * FROM rayons ORDER BY id_Rayon asc";
    public static String SelectallCai = "SELECT * FROM caisses ORDER BY id_Caisse asc";
	public static String SelectallCom = "SELECT * FROM commande_f ORDER BY id_CommandeF asc;";
	public static String SelectallRec = "SELECT * FROM recus ORDER BY id_Recu asc;";
	public static String SelectallAch = "SELECT * FROM acheter;";
	public static String SelectallLiv = "SELECT * FROM livrer;";


	public static String dbname = "gestion_sup";
	public static String driver ="com.mysql.jdbc.Driver";
    public static String url = "jdbc:mysql://localhost:3306/" + dbname + "?useUnicode=true&characterEncoding=utf8";
    public static String nomUser = "root";
    public static String motP = "";
	
    
	public static Connection CheckConnection(){
		
        
		try {
            Class.forName(driver);
            Connection conn = (Connection) DriverManager.getConnection(url, nomUser, motP);
	        System.out.println("Connection reuissie à notre base de donnees: " + dbname);
	        return conn;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
    }
	
	//Fonction donnant l'heure courante
    public static String dateActuel() {
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();        
		return formatDate.format(date);	
   }
	
    //Tester si le champ de saisie recois un nombre
    public static boolean isNombre(String texfield) {
    	Pattern p = Pattern.compile("[0-9]+");
    	Matcher m = p.matcher(texfield);
    	if (m.find() && m.group().equals(texfield)) {
    		return true;

    	}else{
    		Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Saisie de nombres");
            alert.setHeaderText(null);
            alert.setContentText("S'il vous plaît veuiller saisir un nombre.");
            alert.showAndWait();
    		return false;

    	}
    	
    }
    
    
  //Tester si le champ de saisie recoit que des lettres
    public static boolean isLettre(String texField) {
    	Pattern p = Pattern.compile("[a-zA-Z]+");
    	Matcher m = p.matcher(texField);
    	if (m.find() && m.group().equals(texField)) {
    		return true;

    	}else{
    		Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Saisie de lettres");
            alert.setHeaderText(null);
            alert.setContentText("S'il vous plaît veuiller saisir des lettres.");
            alert.showAndWait();
    		return false;

    	}
    	
    }
	
	public static ObservableList<Fournisseur> getFournisseurs(){
		Connection conn = CheckConnection();
		ObservableList<Fournisseur> listeFour = FXCollections.observableArrayList();

		try {
			st = conn.prepareStatement(SelectallFour);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
				   listeFour.add(new Fournisseur(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getFournisseurs() " + e);
		}

		return listeFour;
		
	}
	
	
	public static ObservableList<Article> getArticles(){
		Connection conn = CheckConnection();
		ObservableList<Article> listeArt = FXCollections.observableArrayList();
        
        String categorie_name = null, rayon_name = null;

		try {
			st = conn.prepareStatement(SelectallArt);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	
	        	//Recuperer les libelles à partir du ResultatSet
	    		String req_idCat = "SELECT libelle_Categorie FROM categories WHERE id_Categorie= '" + rs.getInt(5) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idCat);
	    			if (rst.first()) {
	    				categorie_name = rst.getString("libelle_Categorie");
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 à cause de libelle_Categorie dans getArticles()");
	    		}
	    		
	    		
	    		String req_idRay = "SELECT libelle_Rayon FROM rayons WHERE id_Rayon= '" + rs.getInt(6) + "';";
	    		try {
	    			sta = conn.createStatement();
	    			rslt = sta.executeQuery(req_idRay);
	    			if (rslt.first()) {
	    				rayon_name = rslt.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur2 à cause de libelle_Rayon dans getArticles()");
	    		}
	    		rst.close();
				stat.close();
				//Ajouter le tout
	        	listeArt.add(new Article(rs.getInt(1), rs.getString(2),rs.getInt(3), categorie_name,  rayon_name));
	        }
	        
	        rs.close();
			st.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("Erreur dans getArticles() " + e);
		}

		return listeArt;
		
	}
	
	
	public static ObservableList<Client> getClients(){
		Connection conn = CheckConnection();
		ObservableList<Client> listeCli = FXCollections.observableArrayList();

		try {
			st = conn.prepareStatement(SelectallCli);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	listeCli.add(new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
	        }
	        rs.close();
			st.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("Erreur dans getClients() " + e);
		}
		
		return listeCli;
		
	}
	
	
	public static ObservableList<Categorie> getCategories(){
		Connection conn = CheckConnection();
		ObservableList<Categorie> listeCat = FXCollections.observableArrayList();

		try {
			st = conn.prepareStatement(SelectallCat);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	listeCat.add(new Categorie(rs.getInt(1), rs.getString(2)));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getCategories() " + e);
		}
		
		return listeCat;
		
	}
	
	
	public static ObservableList<Rayon> getRayons(){
		Connection conn = CheckConnection();
		ObservableList<Rayon> listeRay = FXCollections.observableArrayList();

		try {
			st = conn.prepareStatement(SelectallRay);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	listeRay.add(new Rayon(rs.getInt(1), rs.getString(2), rs.getInt(3)));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getRayons() " + e);
		}
		
		return listeRay;
		
	}


	public static ObservableList<Caisse> getCaisses() {
		Connection conn = CheckConnection();
		ObservableList<Caisse> listeCai = FXCollections.observableArrayList();

		try {
			st = conn.prepareStatement(SelectallCai);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	listeCai.add(new Caisse(rs.getInt(1), rs.getString(2), rs.getString(3)));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getCaisses() " + e);
		}
		
		return listeCai;
		
	}
	
	
	public static ObservableList<CommandeF> getCommandes() {
		Connection conn = CheckConnection();
		ObservableList<CommandeF> listeCom = FXCollections.observableArrayList();
        String article_name = null;

		try {
			st = conn.prepareStatement(SelectallCom);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	//Recuperer les libelles à partir du ResultatSet
	    		String req_idCat = "SELECT libelle_Article FROM articles WHERE id_Article= '" + rs.getInt(4) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idCat);
	    			if (rst.first()) {
	    				article_name = rst.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 à cause de libelle_Article dans getCommandes()");
	    		}
	    		
	    		rst.close();
				stat.close();
				
	        	listeCom.add(new CommandeF(rs.getInt(1), rs.getString(2), rs.getInt(3), article_name));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getCommandes() " + e);
		}
		
		return listeCom;
		
	}
	
	
	public static ObservableList<Livrer> getLivraisons() {
		Connection conn = CheckConnection();
		ObservableList<Livrer> listeLiv = FXCollections.observableArrayList();
        String datecom = null, fournisseur_name = null;

		try {
			st = conn.prepareStatement(SelectallLiv);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	//Recuperer les libelles à partir du ResultatSet
	    		String req_idCom = "SELECT date_cf FROM commande_f WHERE id_CommandeF= '" + rs.getInt(1) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idCom);
	    			if (rst.first()) {
	    				datecom = rst.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 à cause de nom_Caissiere dans getLivraisons()");
	    		}
	    		
	    		
	    		String req_idFour = "SELECT nom_Fournisseur FROM fournisseurs WHERE id_Fournisseur= '" + rs.getInt(2) + "';";
	    		try {
	    			sta = conn.createStatement();
	    			rslt = sta.executeQuery(req_idFour);
	    			if (rslt.first()) {
	    				fournisseur_name = rslt.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur2 à cause de nom_Fournisseur dans getLivraisons()");
	    		}
	    		rst.close();
				stat.close();
	        	listeLiv.add(new Livrer(datecom, fournisseur_name, rs.getString(3), rs.getInt(4)));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getLivraisons() " + e);
		}
		
		return listeLiv;
		
	}
	
	
	public static ObservableList<Recu> getRecus() {
		Connection conn = CheckConnection();
		ObservableList<Recu> listeRec = FXCollections.observableArrayList();
        String caissier_name = null, client_name = null;

		try {
			st = conn.prepareStatement(SelectallRec);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	//Recuperer les libelles à partir du ResultatSet
	    		String req_idCai = "SELECT nom_Caissiere FROM caisses WHERE id_Caisse= '" + rs.getInt(2) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idCai);
	    			if (rst.first()) {
	    				caissier_name = rst.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 à cause de nom_Caissiere dans getRecus()");
	    		}
	    		
	    		
	    		String req_idCl = "SELECT nom_Client, prenom_Client FROM clients WHERE id_Client= '" + rs.getInt(3) + "';";
	    		try {
	    			sta = conn.createStatement();
	    			rslt = sta.executeQuery(req_idCl);
	    			if (rslt.first()) {
	    				client_name = rslt.getString(1)  + " " + rslt.getString(2);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur2 à cause de nom_Client dans getRecus()");
	    		}
	    		rst.close();
				stat.close();
	        	listeRec.add(new Recu(rs.getString(4), rs.getString(7), rs.getInt(5), rs.getInt(6), caissier_name, client_name, rs.getInt(1)));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getRecus() " + e);
		}
		
		return listeRec;
		
	}
	
	
	public static ObservableList<Acheter> getAchats() {
		Connection conn = CheckConnection();
		ObservableList<Acheter> listeAch = FXCollections.observableArrayList();
        String article_name = null, client_name = null;

		try {
			st = conn.prepareStatement(SelectallAch);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	//Recuperer les libelles à partir du ResultatSet
	    		String req_idArt = "SELECT libelle_Article FROM articles WHERE id_Article= '" + rs.getInt(1) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idArt);
	    			if (rst.first()) {
	    				article_name = rst.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 à cause de libelle_Article dans getAchats()");
	    		}
	    		
	    		
	    		String req_idCl = "SELECT CONCAT(nom_Client,' ',prenom_Client) FROM clients WHERE id_Client= '" + rs.getInt(2) + "';";
	    		try {
	    			sta = conn.createStatement();
	    			rslt = sta.executeQuery(req_idCl);
	    			if (rslt.first()) {
	    				client_name = rslt.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur2 à cause de nom_Client dans getRecus()");
	    		}
	    		rst.close();
				stat.close();
	        	listeAch.add(new Acheter(client_name, article_name));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getAchats() " + e);
		}
		
		return listeAch;
		
	}

	
	

	/*public static ObservableList<RecuPrint> getRecuPrint() {
		Connection conn = CheckConnection();
		ObservableList<RecuPrint> listeAch = FXCollections.observableArrayList();
        PreparedStatement st;
        String article_name = null, client_name = null;
        String sql = "SELECT DISTINCT R.numero_Recu, R.date_Recu, A.libelle_Article, A.prix_Article, R.qte_Achat, R.montant_Achat, R.Caisse_id\r\n"
    			+ "FROM recus R\r\n"
    			+ "INNER JOIN acheter ach\r\n"
    			+ "ON R.Client_id = '3' AND ach.id_Client = '3'\r\n"
    			+ "INNER JOIN articles A\r\n"
    			+ "ON ach.id_Article = A.id_Article;";
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
	        
	        while(rs.next()) {
	        	//Recuperer les libelles à partir du ResultatSet
	    		String req_idArt = "SELECT libelle_Article FROM articles WHERE id_Article= '" + rs.getInt(1) + "';";
	    		try {
	    			stat = conn.createStatement();
	    			rst = stat.executeQuery(req_idArt);
	    			if (rst.first()) {
	    				article_name = rst.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur1 à cause de libelle_Article dans getAchats()");
	    		}
	    		
	    		
	    		String req_idCl = "SELECT nom_Client FROM clients WHERE id_Client= '" + rs.getInt(2) + "';";
	    		try {
	    			sta = conn.createStatement();
	    			rslt = sta.executeQuery(req_idCl);
	    			if (rslt.first()) {
	    				client_name = rslt.getString(1);
	    			}
	    		} catch (Exception exception) {
	    			System.out.println("Erreur2 à cause de nom_Client dans getRecus()");
	    		}
	    		rst.close();
				stat.close();
	        	//listeAch.add(new RecuPrint(client_name, article_name, null));
	        }
			
		} catch (Exception e) {
			System.out.println("Erreur dans getAchats() " + e);
		}
		
		return listeAch;
		
	}*/
}
