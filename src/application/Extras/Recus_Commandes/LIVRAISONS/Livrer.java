package application.Extras.Recus_Commandes.LIVRAISONS;

public class Livrer {
	
	private String idCom;
	private String idF;
	private String dateLiv;
	private Integer qteLiv;
	
	Livrer() {
		super();
	}
	
	public Livrer(String idCom, String idF, String dateLiv, Integer qteLiv) {
		super();
		this.idCom = idCom;
		this.idF = idF;
		this.dateLiv = dateLiv;
		this.qteLiv = qteLiv;
	}
	
	public String getIdCom() {
		return idCom;
	}
	
	public void setIdCom(String idCom) {
		this.idCom = idCom;
	}
	
	public String getIdF() {
		return idF;
	}
	
	public void setIdF(String idF) {
		this.idF = idF;
	}
	
	public String getDateLiv() {
		return dateLiv;
	}
	
	public void setDateLiv(String dateLiv) {
		this.dateLiv = dateLiv;
	}
	
	public Integer getQteLiv() {
		return qteLiv;
	}
	
	public void setQteLiv(Integer qteLiv) {
		this.qteLiv = qteLiv;
	}


}
