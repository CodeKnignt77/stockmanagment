package ma.project;

public class Produit {
	private int ref;
	private String name;
	private double prixAchatHT;
	private double prixVenteTTC;
	private int quantity;
	// constructor
	public Produit(int ref, String name, double prixAchatHT, double prixVenteTTC, int quantity) {
		super();
		this.ref = ref;
		this.name = name;
		this.prixAchatHT = prixAchatHT;
		this.prixVenteTTC = prixVenteTTC;
		this.quantity = quantity;
	}
	// Getters & Setters
	public int getRef() {
		return ref;
	}

	public void setRef(int ref) {
		this.ref = ref;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrixAchatHT() {
		return prixAchatHT;
	}

	public void setPrixAchatHT(double prixAchatHT) {
		this.prixAchatHT = prixAchatHT;
	}

	public double getPrixVenteTTC() {
		return prixVenteTTC;
	}

	public void setPrixVenteTTC(double prixVenteTTC) {
		this.prixVenteTTC = prixVenteTTC;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    public double getMontantTVA() {
        return prixVenteTTC - prixAchatHT;  // because prixVenteTTC already includes 20% TVA
    }
    
	
	

}
