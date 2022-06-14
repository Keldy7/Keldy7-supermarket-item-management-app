package application.ARTICLES;

public class Article {


  private int idA;
  private String libA;
  private int prixA;
  private String Cat;
  private String Ray;

  
  Article() {
	  super();
  } 
  
  public Article(int idA, String libA, int prixA, String Cat, String Ray) {
	  super();
	   this.idA = idA;
	   this.libA = libA;
	   this.prixA = prixA;
	   this.Cat = Cat;
	   this.Ray = Ray;
  }

public int getIdA() {
	return idA;
}

public void setIdA(int idA) {
	this.idA = idA;
}

public String getLibA() {
	return libA;
}

public void setLibA(String libA) {
	this.libA = libA;
}

public int getPrixA() {
	return prixA;
}

public void setPrixA(int prixA) {
	this.prixA = prixA;
}

public String getCat() {
	return Cat;
}

public void setCat(String cat) {
	Cat = cat;
}

public String getRay() {
	return Ray;
}

public void setRay(String ray) {
	Ray = ray;
}


 }
