package application.Extras.Categories_Rayons.RAYONS;

public class Rayon {
	private int idR;
	private String libR;
	private int nplace;
	
	public Rayon() {
		super();
	}

	public Rayon(int idR, String libR, int nplace) {
		super();
		this.idR = idR;
		this.libR = libR;
		this.nplace = nplace;
	}

	public int getIdR() {
		return idR;
	}

	public void setIdR(int idR) {
		this.idR = idR;
	}

	public String getLibR() {
		return libR;
	}

	public void setLibR(String libR) {
		this.libR = libR;
	}

	public int getNplace() {
		return nplace;
	}

	public void setNplace(int nplace) {
		this.nplace = nplace;
	}
	

}
