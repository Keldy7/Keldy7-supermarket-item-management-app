package application.CLIENTS;

public class Client {
	
	private int idCli;
	private String nomCli;
    private String preCli;
	private String adrCli;
	private String telCli;
	  
	Client() {
		super();
	 } 
	  
	public Client(int idCli, String nomCli, String preCli, String adrCli, String telCli) {
		super();
		this.idCli = idCli;
		this.nomCli = nomCli;
		this.preCli = preCli;
		this.adrCli = adrCli;
		this.telCli = telCli;
	  }
	
	public int getIdCli() {
		return idCli;
	}
	
	public void setIdCli(int idCli) {
		this.idCli = idCli;
	}
	
	public String getNomCli() {
		return nomCli;
	}
	
	public void setNomCli(String nomCli) {
		this.nomCli = nomCli;
	}
	
	public String getPreCli() {
		return preCli;
	}
	
	public void setPreCli(String preCli) {
		this.preCli = preCli;
	}
	
	public String getAdrCli() {
		return adrCli;
	}
	
	public void setAdrCli(String adrCli) {
		this.adrCli = adrCli;
	}
	
	public String getTelCli() {
		return telCli;
	}
	
	public void setTelCli(String telCli) {
		this.telCli = telCli;
	}

  
}
