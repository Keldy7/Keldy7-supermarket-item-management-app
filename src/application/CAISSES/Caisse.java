package application.CAISSES;

public class Caisse {
	private int idCai;
	private String nomCai;
    private String telCai;
    
	Caisse() {
		super();
	}

	public Caisse(int idCai, String nomCai, String telCai) {
		super();
		this.idCai = idCai;
		this.nomCai = nomCai;
		this.telCai = telCai;
	}

	public int getIdCai() {
		return idCai;
	}

	public void setIdCai(int idCai) {
		this.idCai = idCai;
	}

	public String getNomCai() {
		return nomCai;
	}

	public void setNomCai(String nomCai) {
		this.nomCai = nomCai;
	}

	public String getTelCai() {
		return telCai;
	}

	public void setTelCai(String telCai) {
		this.telCai = telCai;
	}
	
	
    
    
}
