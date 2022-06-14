package application.Database;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.*;

/*Possibilit� d'utiliser cette classe ChiffrerPassword si on cr�e une colonne exemple Barcode qui
 * va stocker la variable secretKy retourn�e de la fonction generateur() � chaque inscription d'un utilisateur afin de pouvoir decrypter 
 * un password avec la m�me valeur de sa cl� secrete.
 */

public class ChiffrerPassword {
	
    static Cipher cipher; 
    static Connection conn = null;
    static Statement st = null;
    static ResultSet rs = null;
    
    //Recup�re tous les mots de passe de la table users de la bd
    public static ArrayList<String> tousPassword() {
    	ArrayList<String> PassWordsList = new ArrayList<String>(); 
    	String reqallPassWord = "SELECT Password from users";
    	conn = ConnectionBD.CheckConnection();
    	try {
    		st = conn.createStatement();
			rs = st.executeQuery(reqallPassWord);
			
			while (rs.next()) {
				PassWordsList.add(rs.getString(1));
            }
			return PassWordsList;

    	}catch(Exception exception){
        	System.out.println(exception);
        	return null;
        }
		
	}
    
    public static SecretKey generateur() {
    	SecretKey secretKy = null;
    	try {
    		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    		keyGenerator.init(128); //ou � 192 ou 256
    		secretKy = keyGenerator.generateKey();

    	} catch (NoSuchAlgorithmException er) {
    		System.out.println("Impossible de g�nerer la cl� secr�te");
    	}
    	
    	return secretKy;
	}
    
  
   
	public static String encrypt(String mdpNoCrypted, SecretKey secretCle)throws Exception {
		
		cipher = Cipher.getInstance("AES");	 
        cipher.init(Cipher.ENCRYPT_MODE, secretCle); //initialise le mode de cryptage � partir du mod�le de Cle secr�te g�ner�

        byte[] mdpByte = mdpNoCrypted.getBytes(); //Traduire le mdp en byte
        byte[] mdpByteCrypt = cipher.doFinal(mdpByte);
        Encoder encodeur = Base64.getEncoder(); //definition de l'encodage
        String encryptedPassword = encodeur.encodeToString(mdpByteCrypt); //encoder le mot de passe traduit en Byte
        return encryptedPassword;
    }

    public static String decrypt(String mdpCrypted, SecretKey secretCle)throws Exception {
    	
    	cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretCle);

        Decoder decodeur = Base64.getDecoder(); //definition du decodage
        byte[] mdpByteCrypted = decodeur.decode(mdpCrypted);
        byte[] mdpByteDecrypted = cipher.doFinal(mdpByteCrypted);
        String decryptedPassword = new String(mdpByteDecrypted);
        
        return decryptedPassword;
    }

}
