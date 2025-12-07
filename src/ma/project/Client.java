package ma.project;

public class Client {
	private int id;
	private String nomComplet;
	private String phoneNumber;
	private String adress;
	private String ICE;
	private String email;
	// constructor
	public Client(int id, String nomComplet, String phoneNumber, String adress, String iCE, String email) {
		super();
		this.id = id;
		this.nomComplet = nomComplet;
		this.phoneNumber = phoneNumber;
		this.adress = adress;
		ICE = iCE;
		this.email = email;
	}
	// Getters & Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomComplet() {
		return nomComplet;
	}
	public void setNomComplet(String nomComplet) {
		this.nomComplet = nomComplet;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getICE() {
		return ICE;
	}
	public void setICE(String iCE) {
		ICE = iCE;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Client [id=" + id + ", nomComplet=" + nomComplet + ", phoneNumber=" + phoneNumber + ", adress=" + adress
				+ ", ICE=" + ICE + ", email=" + email + "]";
	}
	

}
