package application.FOURNISSEURS;


public class Fournisseur {

  private int idF;
  private String nomF;
  private String adrF;
  private String telF;
  
  Fournisseur() {
	super();
  } 
  
  public Fournisseur(int idF, String nomF, String adrF, String telF) {
	super();
	this.idF = idF;
	this.nomF = nomF;
    this.adrF = adrF;
    this.telF = telF;
  }

	public int getIdF() {
		return idF;
	}
	
	public void setIdF(int idF) {
		this.idF = idF;
	}
	
	public String getNomF() {
		return nomF;
	}
	
	public void setNomF(String nomF) {
		this.nomF = nomF;
	}
	
	public String getAdrF() {
		return adrF;
	}
	
	public void setAdrF(String adrF) {
		this.adrF = adrF;
	}
	
	public String getTelF() {
		return telF;
	}
	
	public void setTelF(String telF) {
		this.telF = telF;
	}
	  
  
 }