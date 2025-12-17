package ma.project;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

     

public class Main {
	public static void main(String[] args) {
		new LoginFrame();
        System.out.println("==============================================================================================================================");
        System.out.println("  Gestion Sécurisée des Stocks & Facturation");
        System.out.println("  RSA 4096 bits + Attaques Cryptographiques");
        System.out.println("  Par CHAOUKI YASSER - 2025/2026");
        System.out.println("=============================================================================================================================="
        		+ "");
        

                // ====================== 1. Création du client ======================
                Client client = new Client(
                    1,
                    "Mohamed Alami",
                    "06 65 43 21 98",
                    "78 Rue Moulay Youssef, Rabat",
                    "001987654000321",
                    "mohamed@alami.ma"
                );

                // ====================== 2. Création des produits ======================
                Produit p1 = new Produit(1, "MacBook Pro M3", 18000.0, 23400.0, 5);
                Produit p2 = new Produit(2, "iPhone 16", 12000.0, 15600.0, 10);
                Produit p3 = new Produit(3, "AirPods Pro 2", 2200.0, 2860.0, 20);

                // ====================== 3. Liste des produits pour la facture ======================
                List<Produit> lignes = new ArrayList<>();
                lignes.add(p1);
                lignes.add(p2);
                lignes.add(p3);

                // ====================== 4. Création de la facture ======================
                Facture facture = new Facture(1, "FAC-2025-000001", "30/11/2025", client, lignes);

                // ====================== 5. Calcul automatique des totaux ======================
                facture.calculerTotaux();

                // ====================== 6. Affichage complet de la facture ======================
                System.out.println("════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
                System.out.println("       FACTURE OFFICIELLE MAROCAINE");
                System.out.println("════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
                System.out.println("Numéro      : " + facture.getNumero());
                System.out.println("Date        : " + facture.getDate());
                System.out.println("Client      : " + client.getNomComplet());
                System.out.println("ICE Client  : " + client.getICE());
                System.out.println("Téléphone   : " + client.getPhoneNumber());
                System.out.println("Adresse     : " + client.getAdress());
                System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
                for (Produit p : facture.getLignesProduits()) {
                    System.out.printf("%-25s %,12.2f MAD (TVA: %.2f MAD)%n",
                        p.getName(), p.getPrixVenteTTC(), p.getMontantTVA());
                }
                System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
                System.out.printf("Total HT     : %,15.2f MAD%n", facture.getTotalHT());
                System.out.printf("Total TVA 20%% : %,15.2f MAD%n", facture.getTotalTVA());
                System.out.printf("TOTAL TTC    : %,15.2f MAD%n", facture.getTotalTTC());
                System.out.println("════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
                System.out.println("   Système développé par CHAOUKI YASSER"
                		+ " - 2025");
                System.out.println("════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
        }
	
    
}