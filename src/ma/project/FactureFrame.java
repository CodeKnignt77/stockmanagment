package ma.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FactureFrame extends JFrame {
	private DefaultTableModel tableModel;
    private JTextField txtClientId, txtRefProduit, txtQuantite;
    private JLabel lblClientNom, lblTotalTTC, lblNumFacture;
    private HashMap<String, String> clients = new HashMap<>();
    private HashMap<String, Produit> produits = new HashMap<>();
    private int numeroFacture = 0;
    private JLabel lblClient;
    
    private JLabel lblTotal;
    

    public FactureFrame() {
        chargerDonnees();
        initUI();
    }

    private void chargerDonnees() {
        // Clients (ID → Nom complet)
        clients.put("C001", "Mohammed Benali - Casablanca");
        clients.put("C002", "SARL ImportExport - Rabat");
        clients.put("C003", "Pharmacie Al Massira - Marrakech");

        // Produits (Référence → Objet Produit)
        produits.put("P101", new Produit("Samsung Galaxy S24", 10200.00));
        produits.put("P202", new Produit("Sac de ciment 50kg", 102.00));
        produits.put("P303", new Produit("Huile d'olive 5L", 144.00));
        produits.put("P404", new Produit("Laptop Dell XPS", 15000.00));
        produits.put("P505", new Produit("Pack eau 12x1.5L", 54.00));
    }

    private void initUI() {
        setTitle("StockSecure - Facturation Rapide");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new GridLayout(3, 2, 10, 10));
        header.setBackground(new Color(0, 100, 200));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        header.add(new JLabel("Client ID :", JLabel.RIGHT)).setForeground(Color.WHITE);
        txtClientId = new JTextField();
        txtClientId.addActionListener(e -> verifierClient());
        header.add(txtClientId);

        header.add(new JLabel("Client :", JLabel.RIGHT)).setForeground(Color.WHITE);
        lblClient = new JLabel("Entrez un ID client...");
        lblClient.setForeground(Color.YELLOW);
        header.add(lblClient);

        header.add(new JLabel("Réf Produit :", JLabel.RIGHT)).setForeground(Color.WHITE);
        txtRefProduit = new JTextField();
        txtRefProduit.addActionListener(e -> txtQuantite.requestFocus());
        header.add(txtRefProduit);

        add(header, BorderLayout.NORTH);

        // Tableau
        tableModel = new DefaultTableModel(new String[]{"Produit", "PU HT", "Qté", "Total HT"}, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Saisie quantité + ajout
        JPanel saisie = new JPanel();
        saisie.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtQuantite = new JTextField("1", 5);
        txtQuantite.setFont(new Font("Arial", Font.BOLD, 20));
        txtQuantite.addActionListener(e -> ajouterProduit());

        JButton btnAjouter = new JButton("AJOUTER PRODUIT");
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 18));
        btnAjouter.addActionListener(e -> ajouterProduit());

        saisie.add(new JLabel("Quantité :"));
        saisie.add(txtQuantite);
        saisie.add(btnAjouter);

        // Total + PDF
        JPanel bas = new JPanel(new BorderLayout());
        lblTotal = new JLabel("TOTAL TTC : 0.00 DH");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 30));
        lblTotal.setForeground(new Color(0, 180, 0));
        bas.add(lblTotal, BorderLayout.WEST);

        JButton btnPDF = new JButton("IMPRIMER FACTURE");
        btnPDF.setFont(new Font("Arial", Font.BOLD, 20));
        btnPDF.setBackground(new Color(0, 150, 0));
        btnPDF.setForeground(Color.WHITE);
        btnPDF.addActionListener(e -> genererPDFSimple());
        bas.add(btnPDF, BorderLayout.EAST);

        JPanel sud = new JPanel(new BorderLayout());
        sud.add(saisie, BorderLayout.NORTH);
        sud.add(bas, BorderLayout.SOUTH);
        add(sud, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void verifierClient() {
        String id = txtClientId.getText().toUpperCase();
        String nom = clients.get(id);
        if (nom != null) {
            lblClient.setText(nom);
            lblClient.setForeground(Color.CYAN);
            txtRefProduit.requestFocus();
        } else {
            lblClient.setText("Client inconnu !");
            lblClient.setForeground(Color.RED);
        }
    }

    private void ajouterProduit() {
        String ref = txtRefProduit.getText().toUpperCase();
        Produit p = produits.get(ref);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Produit non trouvé !");
            return;
        }
        int qte = 1;
        try { qte = Integer.parseInt(txtQuantite.getText()); } catch (Exception ignored) {}
        if (qte < 1) qte = 1;

        double totalLigne = p.prixHT * qte;
        tableModel.addRow(new Object[]{p.nom, p.prixHT, qte, totalLigne});

        
        double totalTTC = totalLigne * 1.20;
        lblTotal.setText(String.format("TOTAL TTC : %.2f DH", totalTTC));

        txtRefProduit.setText("");
        txtQuantite.setText("1");
        txtRefProduit.requestFocus();
    }

    
    private void genererPDFSimple() {
        JOptionPane.showMessageDialog(this,
            "FACTURE GÉNÉRÉE !\n\n" +
            "Client : " + lblClient.getText() + "\n" +
            "Date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) + "\n" +
            lblTotal.getText() + "\n\n" +
            "Prête pour impression !",
            "Facture OK", JOptionPane.INFORMATION_MESSAGE);
    }

    class Produit {
        String nom;
        double prixHT;
        Produit(String n, double p) { nom = n; prixHT = p; }
    }
}