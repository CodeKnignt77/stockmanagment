package ma.project;

import java.util.List;

public class Facture {
	private int id;
	private String numero;
	private String date;
	private Client client;
	private List<Produit> lignesProduits;
	private double totalHT;
	private double totalTVA;
	private double totalTTC;
	
	public Facture() {
		super();
	}
	public Facture(int id, String numero, String date, Client client, List<Produit> lignesProduits) {
		super();
		this.id = id;
		this.numero = numero;
		this.date = date;
		this.client = client;
		this.lignesProduits = lignesProduits;
	}
	public int getId() {
		return id;
	}
	public String getNumero() {
		return numero;
	}
	public String getDate() {
		return date;
	}
	public Client getClient() {
		return client;
	}
	public List<Produit> getLignesProduits() {
		return lignesProduits;
	}
	public double getTotalHT() {
		return totalHT;
	}
	public double getTotalTVA() {
		return totalTVA;
	}
	public double getTotalTTC() {
		return totalTTC;
	}
	public void calculerTotaux() {
		totalHT = 0.0;
		totalTVA = 0.0;
		totalTTC = 0.0;
		for (Produit p : lignesProduits) {
			totalTTC += p.getPrixVenteTTC();
			
		};
		totalHT = totalTTC / 1.20;        
		totalTVA = totalTTC - totalHT;
	}
	}
