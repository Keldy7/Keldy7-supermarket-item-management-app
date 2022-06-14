package application.Extras.Recus_Commandes.RECUS;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//import java.awt.image.BufferedImage;


import application.Database.ConnectionBD;
import application.Extras.Recus_Commandes.ACHATS.AcheterController;


public class RecuPrintController implements Initializable {
	
	static Connection conn = null;
	static ResultSet rs = null, rst = null, rsult = null;
    static Statement st = null, stat = null, state = null;
	
	Stage stage = new Stage();
    static String contenu = null;

	@FXML private Button btn_pdf;

    @FXML private Button btn_recherche;

    @FXML private Hyperlink menu;

    @FXML private TextArea textAreaRec;

    @FXML private ComboBox<String> textFieldCli;
    


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		textAreaRec.setDisable(true);
		btn_pdf.setDisable(true);
		
		textFieldCli.setItems(FXCollections.observableArrayList(RecuController.fillComboBox(RecuController.SelectallCli)));
		System.out.println("Liste de tous les noms de clients ayant fait des achats: " 
		+ RecuController.fillComboBox(RecuController.SelectallCli));
		
	}
	
	
	@FXML
    void goto_Menu(ActionEvent e) {
		new application.ARTICLES.ArticleController().goto_Menu(e);

    }
	
	@FXML
    void rechercher(ActionEvent event) {
		
		textAreaRec.setDisable(false);
		textAreaRec.setEditable(false);
		textAreaRec.setStyle("-fx-effect: dropshadow(three-pass-box,  #ffd95e, 10.0, 0.0, 0.0, 0.0); -fx-effect: innershadow(three-pass-box, #fdc357, 10.0, 0.0, 0.0, 0.0);");
		
		List<String> options = new ArrayList<String>(); //liste des montants
		List<Integer> mt_list = new ArrayList<Integer>(); 
		List<String> lib_list = new ArrayList<String>();  //liste des libelles des articles
		List<String> prix_list = new ArrayList<String>(); //liste des prix des articles
		List<String> qte_list = new ArrayList<String>(); //liste des qtes des articles achetes
		
        if (!textFieldCli.getSelectionModel().isEmpty()) {
        	
        	if (!textAreaRec.getText().isEmpty()) { //Tester si le TextArea n'est vide
    			textAreaRec.setText(null);
    		}else {
    			btn_pdf.setDisable(false);
    		}
        	
        	        	
        	String req_idCli = "SELECT id_Client FROM clients WHERE CONCAT(nom_Client,' ',prenom_Client)= '";
			try {
				
				int idCli_edit = AcheterController.elSelected(req_idCli, textFieldCli.getSelectionModel().getSelectedItem());
				String sql = "SELECT DISTINCT R.numero_Recu, R.qte_Achat, R.montant_Achat, R.Caisse_id\r\n"
	        			+ "FROM recus R\r\n"
	        			+ "INNER JOIN acheter ach\r\n"
	        			+ "ON R.Client_id = '" + idCli_edit + "' AND ach.id_Client = '" + idCli_edit +"'\r\n"
	        			+ "INNER JOIN articles A\r\n"
	        			+ "ON ach.id_Article = A.id_Article;";
				
				String reqlib = "SELECT A.libelle_Article, A.prix_Article FROM acheter ach, articles A\r\n"
	        			+ "WHERE ach.id_Client ='" + idCli_edit + "'AND ach.id_Article = A.id_Article;";
	        	
	        	conn = ConnectionBD.CheckConnection();
	        	try {
	                st = conn.createStatement();
	                rs = st.executeQuery(sql);
	                String num = "", caissier_name = "", text = "";
	                int montant = 0;
	
	                while (rs.next()) {

	                	num = rs.getString(1);
	                	qte_list.add(String.valueOf(rs.getInt(2))); //La qte acheté de l'article
	                	options.add(String.valueOf(rs.getInt(3)));
	                	mt_list.add(rs.getInt(3)); //Le montant(qte_Achat * prix_Article)*/

	                	String req_NomCai = "SELECT nom_Caissiere FROM caisses WHERE id_Caisse= '" + rs.getInt(4) + "';";
	                			
	    	    		try {
	    	    			stat = conn.createStatement();
	    	    			rst = stat.executeQuery(req_NomCai);
	    	    			if (rst.first()) {
	    	    				caissier_name = rst.getString(1);
	    	    			}
	    	    		} catch (Exception erreur) {
	    	    			System.out.println("Erreur1 à cause de nom_Caissiere dans rechercher()" + erreur);
	    	    		}
	               }
	                
	                //Recuperer le libelle et le prix des articles achetes par un client
	                conn = ConnectionBD.CheckConnection();
		        	try {
		                state = conn.createStatement();
		                rsult = state.executeQuery(reqlib);
		                
		                while (rsult.next()) {
		                	lib_list.add(rsult.getString(1));
		                	prix_list.add(rsult.getString(2));
							
						}

		        	}catch (Exception ex) {
		        		System.out.println("Impossible de récuperer les lib et prix des articles " + ex);
		        	}
		        	
	                //Recupération de tous les elmnts de la liste resultante du ResultSet
		        	for (int s = 0; s < lib_list.size(); s++) {
		        		if (s == 3) {
		            	    text = "\n" + text + lib_list.get(s) + "    " + prix_list.get(s) + "     " + qte_list.get(s) + "    \t" 
		            	    		+ options.get(s) +"\t\n";
						} else if (s == 7){
		            	    text = "\n" + text + lib_list.get(s) + "     " + prix_list.get(s) + "     " + qte_list.get(s) + "     \t"
		            	    		+ options.get(s) +"\t\n";
						} else if (s == 11){
		            	    text = "\n" + text + lib_list.get(s) + "\t" + prix_list.get(s) + "  \t" + qte_list.get(s) + "  \t\t"
		            	    		+ options.get(s) +"\t\n";
						} else if (s == 15){
							text = "\n" + text + lib_list.get(s) + "\t" + prix_list.get(s) + "  \t" + qte_list.get(s) + "  \t\t"
		            	    		+ options.get(s) +"\t\n";						} else if (s == 19){
						}else if (s == 23){
							text = "\n" + text + lib_list.get(s) + "\t" + prix_list.get(s) + "  \t" + qte_list.get(s) + "  \t\t"
		            	    		+ options.get(s) +"\t\n";						}else if (s == 27){
						}else if (s == 31){
							text = "\n" + text + lib_list.get(s) + "\t" + prix_list.get(s) + "  \t" + qte_list.get(s) + "  \t\t"
		            	    		+ options.get(s) +"\t\n";						
						}else {
							text = text + lib_list.get(s) + "\t       " + prix_list.get(s) + "    \t" + qte_list.get(s) + "    \t" 
						+ options.get(s) +"\n";

						}
	            	}

		        	//calcul du montant total des achats
		        	for (int i = 0; i < mt_list.size(); i++) {
	            	    montant += Integer.valueOf(mt_list.get(i));

					}

	            	contenu = "\n****************__ STIC MARKET __*****************\n\t\t"
	            			+ "Yamoussoukro, INPHB Centre\n\t         "
	            			+ "Téléphone: +225 01 71 45 42 58\nCaissière: " + caissier_name + "\t\t   N°Reçu: " + num
	            			+ "\nHeure: " + ConnectionBD.dateActuel() + "\n\n" + text + "\n\t\t\t Montant total: " + montant
	            			+ "Fr\n\n________________Passez une belle journée._______________";
	            	textAreaRec.appendText(contenu);
	            	
	            	rs.close();
	                st.close();
	                conn.close();
	                 
	        	}catch(Exception sqlExcptn) {
	                System.out.println(sqlExcptn);
	            }
	        	  
			} catch (Exception e) {
				e.printStackTrace();
			}                
                     	
        }else {
        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
            alert.setContentText("Veuillez remplir le champs de saisie pour effectuer une recherche dans la base de données.");
            alert.setHeaderText("Champs de saisie du Client concerné est vide !");
            alert.setTitle("Erreur");
            alert.showAndWait();
        }
    }
	

	
	@FXML
    void printPDF(ActionEvent event) {
		try {
			print();

		} catch (Exception erreur) {
			erreur.printStackTrace();			
		}
    }
	
	/*private boolean print(Paper paper, Printer printer) {
		BufferedImage image = null;
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        ImageView ivImage = new ImageView(fxImage);

        PageLayout pageLayout = printer.createPageLayout(paper, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);

		//Calcul pour centrer le contenu à imprimer sur le papier A4
        double scaleX = pageLayout.getPrintableWidth() / fxImage.getWidth();
        double scaleY = pageLayout.getPrintableHeight() / fxImage.getHeight();
        ivImage.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        
        System.out.println("PageLayout: " + pageLayout.toString());
        boolean printed = job.printPage(ivImage);
        if (printed)
        {
            job.endJob();
            return true;
        }
        else
        {
            return false;
        }
    }*/
	
    void print() {
		this.textAreaRec.setStyle("-fx-effect: dropshadow(three-pass-box, #e5a527, 10.0, 0.25, 0.0, 0.0);");

        // Le Printer object par défault est CutePDF Writer
		/* Savoir tous les printers installés sur la machine
		 * ObservableSet<Printer> printers = Printer.getAllPrinters();
			for(Printer nom_printer : printers) 
			{
			    textAreaRec.appendText(nom_printer.getName()+"\n");
			}  
		 */	    

        Printer printer = Printer.getDefaultPrinter();

        // Mode PORTRAIT ou LANDSCAPE ou REVERSE_LANDSCAPE
        //MarginType: EQUAL ou DEFAULT ou EQUAL_OPPOSITES ou HARDWARE_MINIMUM
        PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

        // Creation d'un printer job.
        final PrinterJob  job = PrinterJob.createPrinterJob();
        /*Pane p = new Pane();
         *Image image = new Image("C:\\Users\\Aude KOUASSY\\eclipse-workspace\\GestionArticles\\src\\images\\main_icon.jpg");
        p.getChildren().add(new ImageView(image));*/
        Text textPDF = new Text();
        textPDF.setText(contenu);


        if (job != null) {
        	System.out.println(job.getJobSettings());
            boolean success = job.printPage(layout, textPDF);
            if (success) {
            	//Renvoie true si le travail peut être mis en file d'attente avec succès dans la file d'attente de l'imprimante.
                job.endJob();
            }else {
                System.out.println("Impression echouée.");
            }
            
        }else{
        	System.out.println("Ne peux pas créer un Printer job");
        }
        
        
    }
   }
    
    /*if (job.showPrintDialog(this.rootPane.getScene().getWindow())) {
    // Print out the specified pane.
    job.printPage(layout, this.textAreaRec);
	}
	else {
	    System.out.println("Print canceled.");
	}*/
	
	// Finish the print job.
	//job.endJob();

	/*
	 * SELECT DISTINCT R.numero_Recu, R.date_Recu, A.prix_Article, R.qte_Achat, R.montant_Achat, R.Caisse_id
FROM recus R, articles A
INNER JOIN acheter ach
ON R.Client_id = '3' AND ach.id_Client = '3'
INNER JOIN articles A
ON ach.id_Article = A.id_Article;
	 */

	/*
	*SELECT DISTINCT A.libelle_Article,R.numero_Recu, R.date_Recu, A.id_Article, R.qte_Achat, R.montant_Achat, R.Caisse_id
FROM recus R
INNER JOIN acheter ach
ON R.Client_id = '4' AND ach.id_Client = '4'
INNER JOIN articles A
ON ach.id_Article = A.id_Article;*/

	/*
	*SELECT A.libelle_Article, ach.id_Article FROM acheter ach, articles A
WHERE ach.id_Client = '4' AND ach.id_Article = A.id_Article;*/

