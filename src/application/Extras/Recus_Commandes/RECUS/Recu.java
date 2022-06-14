package application.Extras.Recus_Commandes.RECUS;

public class Recu {
	
	private int idR;
	private String numR;
	private int qteAchat;
	private int mtAchat;
    private String dateAchat;
    private String Cai;
	private String Cli;
	
	Recu() {
		super();
	}

	public Recu(String numR, String dateAchat, int qteAchat, int mtAchat, String Cai, String Cli, int idR) {
		super();
		this.idR = idR;
		this.Cai = Cai;
		this.Cli = Cli;
		this.numR = numR;
		this.qteAchat = qteAchat;
		this.mtAchat = mtAchat;
		this.dateAchat = dateAchat;
		
	}

	public int getIdR() {
		return idR;
	}

	public void setIdR(int idR) {
		this.idR = idR;
	}

	public String getNumR() {
		return numR;
	}

	public void setNumR(String numR) {
		this.numR = numR;
	}

	public int getQteAchat() {
		return qteAchat;
	}

	public void setQteAchat(int qteAchat) {
		this.qteAchat = qteAchat;
	}

	public int getMtAchat() {
		return mtAchat;
	}

	public void setMtAchat(int mtAchat) {
		this.mtAchat = mtAchat;
	}

	public String getDateAchat() {
		return dateAchat;
	}

	public void setDateAchat(String dateAchat) {
		this.dateAchat = dateAchat;
	}

	public String getCai() {
		return Cai;
	}

	public void setCai(String cai) {
		Cai = cai;
	}

	public String getCli() {
		return Cli;
	}

	public void setCli(String cli) {
		Cli = cli;
	}
	

}
