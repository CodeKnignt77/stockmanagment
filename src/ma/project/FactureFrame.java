package ma.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class FactureFrame extends JFrame {

    private DefaultTableModel tableModel;
    private JTextField txtClientId, txtRefProduit, txtQuantite;
    private JLabel lblClientNom, lblTotalTTC, lblNumFacture;
    private HashMap<String, Client> clients = new HashMap<>();
    private HashMap<String, Produit> produits = new HashMap<>();
    private double totalTTC = 0;
    private int numeroFacture = 0;

    public FactureFrame() {
        chargerClients();
        chargerProduits();
        incrementerNumeroFacture();
        initUI();
    }

    private void chargerClients() {
        try (BufferedReader br = new BufferedReader(new FileReader("clients.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue;
                String[] p = ligne.split("\\|");
                String id = p[0];
                String nom = p[1];
                String tel = p[2];
                String adresse = p[3];
                String ice = p[4];
                clients.put(id, new Client(id, nom, tel, adresse, ice));
            }
        } catch (Exception e) {
            clients.put("C001", new Client("C001", "Mohammed Benali", "0661234567", "Casablanca", "001234567890123"));
            clients.put("C002", new Client("C002", "SARL ImportExport", "0522888999", "Rabat", "002345678901234"));
        }
    }

    private void chargerProduits() {
        try (BufferedReader br = new BufferedReader(new FileReader("produits.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue;
                String[] p = ligne.split("\\|");
                String ref = p[0];
                String designation = p[1];
                double prixHT = Double.parseDouble(p[2]);
                produits.put(ref, new Produit(ref, designation, prixHT));
            }
        } catch (Exception e) {
            produits.put("P101", new Produit("P101", "Samsung Galaxy S24", 10200.00));
            produits.put("P202", new Produit("P202", "Sac de ciment 50kg", 102.00));
            produits.put("P303", new Produit("P303", "Huile d'olive 5L", 144.00));
        }
    }

    private void incrementerNumeroFacture() {
        try (BufferedReader br = new BufferedReader(new FileReader("compteur.txt"))) {
            numeroFacture = Integer.parseInt(br.readLine().trim());
        } catch (Exception e) {
            numeroFacture = 0;
        }
        numeroFacture++;
        try (PrintWriter pw = new PrintWriter("compteur.txt")) {
            pw.println(numeroFacture);
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("StockSecure - Nouvelle Facture");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // === HEADER ===
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(0, 102, 204));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Ligne 1 : Numéro facture + Date live
        JPanel ligne1 = new JPanel(new GridLayout(1, 2, 20, 0));
        ligne1.setOpaque(false);
        lblNumFacture = new JLabel("Facture N° F" + new SimpleDateFormat("yyyy").format(new Date()) + "-" + String.format("%04d", numeroFacture));
        lblNumFacture.setFont(new Font("Arial", Font.BOLD, 24));
        lblNumFacture.setForeground(Color.WHITE);
        ligne1.add(lblNumFacture);

        JLabel lblDate = new JLabel();
        lblDate.setFont(new Font("Arial", Font.BOLD, 18));
        lblDate.setForeground(Color.WHITE);
        lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
        ligne1.add(lblDate);

        Timer timer = new Timer(1000, e -> {
            lblDate.setText("Date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });
        timer.start();

        // Ligne 2 : Client
        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        ligne2.setOpaque(false);
        ligne2.add(new JLabel("Client ID :")).setForeground(Color.WHITE);
        txtClientId = new JTextField(15);
        txtClientId.addActionListener(e -> chercherClient());
        ligne2.add(txtClientId);

        ligne2.add(new JLabel("Client :")).setForeground(Color.WHITE);
        lblClientNom = new JLabel("Entrez l'ID client...");
        lblClientNom.setForeground(Color.YELLOW);
        lblClientNom.setFont(new Font("Arial", Font.BOLD, 16));
        ligne2.add(lblClientNom);

        // Ligne 3 : Saisie produit + quantité
        JPanel ligne3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        ligne3.setOpaque(false);
        ligne3.add(new JLabel("Référence produit :")).setFont(new Font("Arial", Font.BOLD, 18));
        txtRefProduit = new JTextField(30);
        txtRefProduit.setFont(new Font("Arial", Font.BOLD, 20));
        txtRefProduit.addActionListener(e -> txtQuantite.requestFocus());
        ligne3.add(txtRefProduit);

        ligne3.add(new JLabel("Quantité :")).setFont(new Font("Arial", Font.BOLD, 18));
        txtQuantite = new JTextField("1", 8);
        txtQuantite.setFont(new Font("Arial", Font.BOLD, 24));
        txtQuantite.addActionListener(e -> ajouterProduit());
        ligne3.add(txtQuantite);

        JButton btnAdd = new JButton("AJOUTER AU PANIER");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdd.setBackground(new Color(0, 150, 0));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> ajouterProduit());
        ligne3.add(btnAdd);

        header.add(ligne1);
        header.add(ligne2);
        header.add(ligne3);
        add(header, BorderLayout.NORTH);

        // === TABLEAU ===
        tableModel = new DefaultTableModel(new String[]{"Produit", "PU HT", "Qté", "Total HT"}, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // === TOTAL + PDF ===
        JPanel bas = new JPanel(new BorderLayout());
        lblTotalTTC = new JLabel("TOTAL TTC : 0.00 DH");
        lblTotalTTC.setFont(new Font("Arial", Font.BOLD, 32));
        lblTotalTTC.setForeground(new Color(0, 150, 0));
        bas.add(lblTotalTTC, BorderLayout.WEST);

        JButton btnPDF = new JButton("IMPRIMER PDF + LOGO");
        btnPDF.setFont(new Font("Arial", Font.BOLD, 20));
        btnPDF.setBackground(new Color(0, 150, 0));
        btnPDF.setForeground(Color.WHITE);
        btnPDF.addActionListener(e -> genererPDFAvecLogo());
        bas.add(btnPDF, BorderLayout.EAST);
        add(bas, BorderLayout.PAGE_END);

        setVisible(true);
    }

    private void chercherClient() {
        String id = txtClientId.getText().toUpperCase().trim();
        Client c = clients.get(id);
        if (c != null) {
            lblClientNom.setText(c.nom + " - " + c.telephone);
            lblClientNom.setForeground(Color.BLUE);
            txtRefProduit.requestFocus();
        } else {
            lblClientNom.setText("Client inconnu !");
            lblClientNom.setForeground(Color.RED);
        }
    }

    private void ajouterProduit() {
        String ref = txtRefProduit.getText().toUpperCase().trim();
        Produit p = produits.get(ref);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Produit non trouvé !");
            return;
        }
        int qte = 1;
        try { qte = Integer.parseInt(txtQuantite.getText()); } catch (Exception ignored) {}
        if (qte < 1) qte = 1;

        double totalLigne = p.prixHT * qte;
        tableModel.addRow(new Object[]{p.designation, p.prixHT, qte, totalLigne});
        totalTTC += totalLigne * 1.20;
        lblTotalTTC.setText(String.format("TOTAL TTC : %.2f DH", totalTTC));

        txtRefProduit.setText("");
        txtQuantite.setText("1");
        txtRefProduit.requestFocus();
    }

    private void genererPDFAvecLogo() {
        int width = 600, height = 850;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("FACTURE OFFICIELLE", 150, 60);

        try {
            BufferedImage logo = ImageIO.read(new File("logo.png"));
            g.drawImage(logo, 220, 80, 160, 160, null);
        } catch (Exception e) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("StockSecure Maroc", 220, 180);
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(lblNumFacture.getText(), 50, 280);
        g.drawString("Client : " + lblClientNom.getText(), 50, 310);
        g.drawString("Date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), 50, 340);

        int y = 400;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString(tableModel.getValueAt(i, 0).toString(), 50, y);
            g.drawString(tableModel.getValueAt(i, 2).toString(), 300, y);
            g.drawString(tableModel.getValueAt(i, 1) + " DH", 380, y);
            g.drawString(tableModel.getValueAt(i, 3) + " DH", 480, y);
            y += 30;
        }

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(new Color(0, 150, 0));
        g.drawString(lblTotalTTC.getText(), 200, y + 50);

        g.dispose();

        try {
            ImageIO.write(img, "png", new File("Facture_F" + numeroFacture + ".png"));
            JOptionPane.showMessageDialog(this, "Facture générée : Facture_F" + numeroFacture + ".png");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du PDF");
        }
    }

    class Client {
        String id, nom, telephone, adresse, ice;
        Client(String i, String n, String t, String a, String ic) {
            id = i; nom = n; telephone = t; adresse = a; ice = ic;
        }
    }

    class Produit {
        String ref, designation;
        double prixHT;
        Produit(String r, String d, double p) {
            ref = r; designation = d; prixHT = p;
        }
    }
}