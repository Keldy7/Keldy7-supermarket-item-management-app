package application.Extras.Categories_Rayons.CATEGORIES;

public class Categorie {
	private int idC;
	private String libC;
	
	Categorie() {
		super();
	}
	
	public Categorie(int idC, String libC) {
		super();
		this.idC = idC;
		this.libC = libC;

	}

	public int getIdC() {
		return idC;
	}

	public void setIdC(int idC) {
		this.idC = idC;
	}

	public String getLibC() {
		return libC;
	}

	public void setLibC(String libC) {
		this.libC = libC;
	}
	
	
}


