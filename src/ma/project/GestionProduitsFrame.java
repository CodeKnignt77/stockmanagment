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

public class GestionProduitsFrame extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtDesignation, txtPrixHT, txtQuantite;
    private JLabel lblRef, lblDate;
    private List<Produit> listeProduits = new ArrayList<>();

    public GestionProduitsFrame() {
        initUI();          // UI d'abord
        chargerProduits(); // données ensuite
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
            // Exemples par défaut avec quantité
            listeProduits.add(new Produit("P101", "Samsung Galaxy S24", 10200.00, 5));
            listeProduits.add(new Produit("P202", "Sac de ciment 50kg", 102.00, 50));
            listeProduits.add(new Produit("P303", "Huile d'olive 5L", 144.00, 20));
        }
        rafraichirTableau();
    }

    private void sauvegarderProduits() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("produits.txt")))) {
            for (Produit p : listeProduits) {
                pw.println(p.ref + "|" + p.designation + "|" + p.prixHT + "|" + p.quantite);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde des produits !");
        }
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        for (Produit p : listeProduits) {
            tableModel.addRow(new Object[]{p.ref, p.designation, p.prixHT, p.quantite});
        }
    }

    private void initUI() {
        setTitle("StockSecure - Gestion des Produits");
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

        // Ligne 1 : Titre + Date live (à gauche)
        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        ligne1.setOpaque(false);

        JLabel lblTitre = new JLabel("GESTION DES PRODUITS");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 28));
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

        // Ligne 2 : Formulaire produit
        JPanel form = new JPanel(new GridLayout(1, 8, 20, 15));
        form.setOpaque(false);

        // Référence
        JLabel lblRefLabel = new JLabel("Référence :");
        lblRefLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblRefLabel.setForeground(Color.WHITE);
        form.add(lblRefLabel);

        lblRef = new JLabel("");
        lblRef.setFont(new Font("Arial", Font.BOLD, 20));
        lblRef.setForeground(Color.YELLOW);
        form.add(lblRef);

        // Désignation
        JLabel lblDesLabel = new JLabel("Désignation :");
        lblDesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblDesLabel.setForeground(Color.WHITE);
        form.add(lblDesLabel);

        txtDesignation = new JTextField(30);
        txtDesignation.setFont(new Font("Arial", Font.PLAIN, 18));
        form.add(txtDesignation);

        // Prix HT
        JLabel lblPrixLabel = new JLabel("Prix HT (DH) :");
        lblPrixLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblPrixLabel.setForeground(Color.WHITE);
        form.add(lblPrixLabel);

        txtPrixHT = new JTextField(12);
        txtPrixHT.setFont(new Font("Arial", Font.PLAIN, 18));
        form.add(txtPrixHT);

        // Quantité
        JLabel lblQuantiteLabel = new JLabel("Quantité :");
        lblQuantiteLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblQuantiteLabel.setForeground(Color.WHITE);
        form.add(lblQuantiteLabel);

        txtQuantite = new JTextField(10);
        txtQuantite.setFont(new Font("Arial", Font.PLAIN, 18));
        form.add(txtQuantite);

        gbc.gridy = 1;
        header.add(form, gbc);

        // Boutons
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        boutons.setOpaque(false);

        JButton btnAjouter = new JButton("AJOUTER PRODUIT");
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 18));
        btnAjouter.setBackground(new Color(0, 150, 0));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterProduit());

        JButton btnModifier = new JButton("MODIFIER PRODUIT");
        btnModifier.setFont(new Font("Arial", Font.BOLD, 18));
        btnModifier.setBackground(new Color(0, 120, 215));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.addActionListener(e -> modifierProduit());

        JButton btnSupprimer = new JButton("SUPPRIMER PRODUIT");
        btnSupprimer.setFont(new Font("Arial", Font.BOLD, 18));
        btnSupprimer.setBackground(Color.RED);
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerProduit());

        JButton btnVider = new JButton("VIDER CHAMPS");
        btnVider.setFont(new Font("Arial", Font.BOLD, 18));
        btnVider.addActionListener(e -> viderChamps());
        JButton btnImprimer = new JButton("IMPRIMER LISTE");
        btnImprimer.setFont(new Font("Arial", Font.BOLD, 18));
        btnImprimer.setBackground(new Color(0, 150, 0));
        btnImprimer.setForeground(Color.WHITE);
        btnImprimer.addActionListener(e -> genererImageTableau("Liste_Produits_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));

        boutons.add(btnImprimer);
        boutons.add(btnAjouter);
        boutons.add(btnModifier);
        boutons.add(btnSupprimer);
        boutons.add(btnVider);

        gbc.gridy = 2;
        header.add(boutons, gbc);

        add(header, BorderLayout.NORTH);

        // === TABLEAU ===
        String[] colonnes = {"Référence", "Désignation", "Prix HT (DH)", "Quantité disponible"};
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getSelectionModel().addListSelectionListener(e -> selectionnerProduit());
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void ajouterProduit() {
        String designation = txtDesignation.getText().trim();
        String prixStr = txtPrixHT.getText().trim();
        String quantiteStr = txtQuantite.getText().trim();

        if (designation.isEmpty() || prixStr.isEmpty() || quantiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires !");
            return;
        }

        double prixHT;
        int quantite;
        try {
            prixHT = Double.parseDouble(prixStr);
            quantite = Integer.parseInt(quantiteStr);
            if (prixHT < 0 || quantite < 0) throw new Exception();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Prix ou quantité invalide !");
            return;
        }

        String ref = genererNouvelleRef();
        Produit nouveau = new Produit(ref, designation, prixHT, quantite);
        listeProduits.add(nouveau);
        rafraichirTableau();
        sauvegarderProduits();
        viderChamps();
        lblRef.setText(ref);
        JOptionPane.showMessageDialog(this, "Produit ajouté ! Référence : " + ref);
    }

    private void modifierProduit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit à modifier !");
            return;
        }

        String ref = (String) tableModel.getValueAt(row, 0);
        String designation = txtDesignation.getText().trim();
        String prixStr = txtPrixHT.getText().trim();
        String quantiteStr = txtQuantite.getText().trim();

        if (designation.isEmpty() || prixStr.isEmpty() || quantiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires !");
            return;
        }

        double prixHT;
        int quantite;
        try {
            prixHT = Double.parseDouble(prixStr);
            quantite = Integer.parseInt(quantiteStr);
            if (prixHT < 0 || quantite < 0) throw new Exception();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Prix ou quantité invalide !");
            return;
        }

        listeProduits.removeIf(p -> p.ref.equals(ref));
        listeProduits.add(new Produit(ref, designation, prixHT, quantite));
        rafraichirTableau();
        sauvegarderProduits();
        JOptionPane.showMessageDialog(this, "Produit modifié !");
    }

    private void supprimerProduit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit à supprimer !");
            return;
        }

        String ref = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le produit " + ref + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            listeProduits.removeIf(p -> p.ref.equals(ref));
            rafraichirTableau();
            sauvegarderProduits();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Produit supprimé !");
        }
    }

    private void selectionnerProduit() {
        int row = table.getSelectedRow();
        if (row != -1) {
            lblRef.setText((String) tableModel.getValueAt(row, 0));
            txtDesignation.setText((String) tableModel.getValueAt(row, 1));
            txtPrixHT.setText(tableModel.getValueAt(row, 2).toString());
            txtQuantite.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    private void viderChamps() {
        lblRef.setText("");
        txtDesignation.setText("");
        txtPrixHT.setText("");
        txtQuantite.setText("");
    }

    private String genererNouvelleRef() {
        int max = 0;
        for (Produit p : listeProduits) {
            if (p.ref.startsWith("P")) {
                try {
                    int num = Integer.parseInt(p.ref.substring(1));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "P" + String.format("%03d", max + 1);
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