package application.Extras.Recus_Commandes.ACHATS;

public class Acheter {
	private String Cli;
	private String Art;
	
	Acheter() {
		super();
	}

	public Acheter(String cli, String art) {
		super();
		Cli = cli;
		Art = art;
	}

	public String getCli() {
		return Cli;
	}

	public void setCli(String cli) {
		Cli = cli;
	}

	public String getArt() {
		return Art;
	}

	public void setArt(String art) {
		Art = art;
	}
	
	

	
}
