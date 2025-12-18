package ma.project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EtatStockFrame extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblDate;
    private List<Produit> listeProduits = new ArrayList<>();

    public EtatStockFrame() {
        initUI();
        chargerProduits();
    }

    private void genererImageTableau(String titreFichier) {
        // Capture du tableau
        JViewport viewport = table.getParent() instanceof JViewport ? (JViewport) table.getParent() : null;
        Component comp = viewport != null ? viewport : table;

        int width = comp.getWidth();
        int height = comp.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        comp.paint(g);
        g.dispose();

        try {
            ImageIO.write(img, "png", new File(titreFichier + ".png"));
            JOptionPane.showMessageDialog(this, "Image générée : " + titreFichier + ".png");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération de l'image !");
        }
    }
    
    private void chargerProduits() {
        listeProduits.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("produits.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue;
                String[] p = ligne.split("\\|");
                if (p.length < 4) continue;
                String ref = p[0].trim();
                String designation = p[1].trim();
                double prixHT = Double.parseDouble(p[2].trim());
                int quantite = Integer.parseInt(p[3].trim());
                listeProduits.add(new Produit(ref, designation, prixHT, quantite));
            }
        } catch (Exception e) {
            listeProduits.add(new Produit("P101", "Samsung Galaxy S24", 10200.00, 5));
            listeProduits.add(new Produit("P202", "Sac de ciment 50kg", 102.00, 50));
            listeProduits.add(new Produit("P303", "Huile d'olive 5L", 144.00, 20));
        }
        rafraichirTableau();
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        for (Produit p : listeProduits) {
            String stockStatus = p.quantite > 0 ? p.quantite + " unité(s)" : "Rupture de stock";
            String color = p.quantite == 0 ? "red" : (p.quantite < 10 ? "orange" : "green");
            tableModel.addRow(new Object[]{p.ref, p.designation, String.format("%.2f DH", p.prixHT), stockStatus});
        }
    }

    private void initUI() {
        setTitle("StockSecure - État du Stock");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // === HEADER ===
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(0, 102, 204));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        ligne1.setOpaque(false);

        JLabel lblTitre = new JLabel("ÉTAT DU STOCK");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitre.setForeground(Color.WHITE);
        ligne1.add(lblTitre);

        lblDate = new JLabel();
        lblDate.setFont(new Font("Arial", Font.BOLD, 18));
        lblDate.setForeground(Color.WHITE);
        ligne1.add(lblDate);

        Timer timer = new Timer(1000, e -> {
            lblDate.setText("Date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });
        timer.start();

        gbc.gridy = 0;
        header.add(ligne1, gbc);

        add(header, BorderLayout.NORTH);
        
     // Bouton Imprimer dans le header (en bas)
        JPanel boutonsStock = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonsStock.setOpaque(false);

        JButton btnImprimer = new JButton("IMPRIMER ÉTAT DU STOCK");
        btnImprimer.setFont(new Font("Arial", Font.BOLD, 20));
        btnImprimer.setBackground(new Color(0, 150, 0));
        btnImprimer.setForeground(Color.WHITE);
        btnImprimer.addActionListener(e -> genererImageTableau("Etat_Stock_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));

        boutonsStock.add(btnImprimer);

        gbc.gridy = 1;
        header.add(boutonsStock, gbc);

        // === TABLEAU ===
        String[] colonnes = {"Référence", "Désignation", "Prix HT (DH)", "Quantité disponible"};
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    class Produit {
        String ref, designation;
        double prixHT;
        int quantite;
        Produit(String r, String d, double p, int q) {
            ref = r; designation = d; prixHT = p; quantite = q;
        }
    }
}