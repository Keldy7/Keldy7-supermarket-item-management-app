package application.Extras.Recus_Commandes.COMMANDES;


public class CommandeF {
	
	private int idCom;
	private String dateCom;
	private int qteCom;
    private String ArtCom;
    
	CommandeF() {
		super();
	}

	public CommandeF(int idCom, String dateCom, int qteCom, String ArtCom) {
		super();
		this.idCom = idCom;
		this.dateCom = dateCom;
		this.qteCom = qteCom;
		this.ArtCom = ArtCom;
	}

	public int getIdCom() {
		return idCom;
	}

	public void setIdCom(int idCom) {
		this.idCom = idCom;
	}

	public String getDateCom() {
		return dateCom;
	}

	public void setDateCom(String dateCom) {
		this.dateCom = dateCom;
	}

	public int getQteCom() {
		return qteCom;
	}

	public void setQteCom(int qteCom) {
		this.qteCom = qteCom;
	}

	public String getArtCom() {
		return ArtCom;
	}

	public void setArtCom(String artCom) {
		ArtCom = artCom;
	}

}
