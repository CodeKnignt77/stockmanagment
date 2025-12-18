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

public class GestionClientsFrame extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtNom, txtTelephone, txtAdresse, txtIce;
    private JLabel lblId, lblDate;
    private List<Client> listeClients = new ArrayList<>();

    public GestionClientsFrame() {
        try {
            
            initUI();
            chargerClients();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur critique lors de l'ouverture du module Clients :\n" + ex.getMessage());
        }
    }
    
    private void genererImageRapport(String titreModule) {
        int width = 800;
        int rowHeight = 40;
        int headerHeight = 250;
        int footerHeight = 100;
        int tableHeight = tableModel.getRowCount() * rowHeight + 50; // en-tête tableau + lignes
        int height = headerHeight + tableHeight + footerHeight;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        int y = 60;

        // === TITRE CENTRE ===
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g.getFontMetrics();
        int titreWidth = fm.stringWidth(titreModule);
        g.drawString(titreModule, (width - titreWidth) / 2, y);

        y += 80;

        // === LOGO CENTRE ===
        try {
            BufferedImage logo = ImageIO.read(new File("logo.png"));
            int logoWidth = 160;
            int logoHeight = 160;
            g.drawImage(logo, (width - logoWidth) / 2, y - 40, logoWidth, logoHeight, null);
        } catch (Exception e) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String text = "StockSecure Maroc";
            int textWidth = fm.stringWidth(text);
            g.drawString(text, (width - textWidth) / 2, y + 60);
        }

        y += 140;

        // === DATE À GAUCHE ===
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), 50, y);

        y += 60;

        // === TABLEAU ===
        int tableLeft = 50;
        int tableWidth = width - 100;
        int colCount = tableModel.getColumnCount();
        int[] colWidths = new int[colCount];
        // Ajuste les largeurs selon tes colonnes
        if (titreModule.contains("CLIENTS")) {
            colWidths = new int[]{100, 200, 150, 200, 150}; // ID, Nom, Tel, Adresse, ICE
        } else if (titreModule.contains("PRODUITS")) {
            colWidths = new int[]{100, 300, 150, 150}; // Ref, Designation, Prix, Quantité
        } else { // Stock
            colWidths = new int[]{100, 300, 150, 150}; // Ref, Designation, Prix, Quantité
        }

        // En-tête tableau
        g.setColor(new Color(0, 102, 204));
        g.fillRect(tableLeft, y, tableWidth, 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        int x = tableLeft + 20;
        for (int i = 0; i < colCount; i++) {
            g.drawString(tableModel.getColumnName(i), x, y + 25);
            x += colWidths[i];
        }

        y += 50;

        // Lignes du tableau
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            x = tableLeft + 20;
            for (int col = 0; col < colCount; col++) {
                Object value = tableModel.getValueAt(row, col);
                String text = value != null ? value.toString() : "";
                g.drawString(text, x, y + 25);
                x += colWidths[col];
            }
            // Bordures horizontales
            g.drawLine(tableLeft, y + 40, tableLeft + tableWidth, y + 40);
            y += rowHeight;
        }

        // Bordure extérieure du tableau
        g.drawRect(tableLeft, y - tableHeight, tableWidth, tableHeight);

        // === PIED DE PAGE ===
        y += 40;
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.GRAY);
        g.drawString("Généré par StockSecure - © 2025", 50, y);
        String compteur = "Nombre d'éléments : " + tableModel.getRowCount();
        fm = g.getFontMetrics();
        int compteurWidth = fm.stringWidth(compteur);
        g.drawString(compteur, width - 50 - compteurWidth, y);

        g.dispose();

        String fichier = titreModule.replace(" ", "_") + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
        try {
            ImageIO.write(img, "png", new File(fichier));
            JOptionPane.showMessageDialog(this, "Rapport généré avec succès :\n" + fichier);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du rapport !");
        }
    }

    private void chargerClients() {
        listeClients.clear();
        File file = new File("clients.txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    if (ligne.trim().isEmpty()) continue;
                    String[] p = ligne.split("\\|");
                    if (p.length >= 5) {
                        listeClients.add(new Client(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur lecture clients.txt : " + e.getMessage());
            }
        }
        // Exemples par défaut si vide
        if (listeClients.isEmpty()) {
            listeClients.add(new Client("C001", "Mohammed Benali", "0661234567", "Casablanca", "001234567890123"));
            listeClients.add(new Client("C002", "SARL ImportExport", "0522888999", "Rabat", "002345678901234"));
        }
        rafraichirTableau();
    }

    private void sauvegarderClients() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("clients.txt")))) {
            for (Client c : listeClients) {
                pw.println(c.id + "|" + c.nom + "|" + c.telephone + "|" + c.adresse + "|" + c.ice);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur sauvegarde clients !");
        }
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        for (Client c : listeClients) {
            tableModel.addRow(new Object[]{c.id, c.nom, c.telephone, c.adresse, c.ice});
        }
    }

    private void initUI() {
        setTitle("StockSecure - Gestion des Clients");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

     // === HEADER ===
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(0, 102, 204));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Ligne 1 : Titre + Date live
        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        ligne1.setOpaque(false);

        JLabel lblTitre = new JLabel("GESTION DES CLIENTS");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitre.setForeground(Color.WHITE);
        ligne1.add(lblTitre, BorderLayout.WEST);

        lblDate = new JLabel();
        lblDate.setFont(new Font("Arial", Font.BOLD, 18));
        lblDate.setForeground(Color.WHITE);
        ligne1.add(lblDate, BorderLayout.EAST);

        Timer timer = new Timer(1000, e -> {
            lblDate.setText("Date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });
        timer.start();

        gbc.gridy = 0;
        header.add(ligne1, gbc);

        // Ligne 2 : Formulaire
        JPanel form = new JPanel(new GridLayout(3, 5, 20, 15));
        form.setOpaque(false);

        // ID Client
        JLabel lblIdLabel = new JLabel("ID Client :");
        lblIdLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblIdLabel.setForeground(Color.WHITE);
        form.add(lblIdLabel);

        lblId = new JLabel("");
        lblId.setFont(new Font("Arial", Font.BOLD, 20));
        lblId.setForeground(Color.YELLOW);
        form.add(lblId);

        // Nom
        JLabel lblNomLabel = new JLabel("Nom :");
        lblNomLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblNomLabel.setForeground(Color.WHITE);
        form.add(lblNomLabel);

        txtNom = new JTextField(25);
        txtNom.setFont(new Font("Arial", Font.PLAIN, 18));
        form.add(txtNom);

        // Téléphone
        JLabel lblTelLabel = new JLabel("Téléphone :");
        lblTelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblTelLabel.setForeground(Color.WHITE);
        form.add(lblTelLabel);

        txtTelephone = new JTextField(15);
        txtTelephone.setFont(new Font("Arial", Font.PLAIN, 18));
        form.add(txtTelephone);

        // Adresse
        JLabel lblAdresseLabel = new JLabel("Adresse :");
        lblAdresseLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblAdresseLabel.setForeground(Color.WHITE);
        form.add(lblAdresseLabel);

        txtAdresse = new JTextField(30);
        txtAdresse.setFont(new Font("Arial", Font.PLAIN, 18));
        form.add(txtAdresse);

        // ICE
        JLabel lblIceLabel = new JLabel("ICE :");
        lblIceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblIceLabel.setForeground(Color.WHITE);
        form.add(lblIceLabel);

        txtIce = new JTextField(15);
        txtIce.setFont(new Font("Arial", Font.PLAIN, 18));
        form.add(txtIce);

        gbc.gridy = 1;
        header.add(form, gbc);

        // Boutons
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        boutons.setOpaque(false);

        JButton btnAjouter = new JButton("AJOUTER CLIENT");
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 18));
        btnAjouter.setBackground(new Color(0, 150, 0));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterClient());

        JButton btnModifier = new JButton("MODIFIER CLIENT");
        btnModifier.setFont(new Font("Arial", Font.BOLD, 18));
        btnModifier.setBackground(new Color(0, 120, 215));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.addActionListener(e -> modifierClient());

        JButton btnSupprimer = new JButton("SUPPRIMER CLIENT");
        btnSupprimer.setFont(new Font("Arial", Font.BOLD, 18));
        btnSupprimer.setBackground(Color.RED);
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerClient());

        JButton btnVider = new JButton("VIDER CHAMPS");
        btnVider.setFont(new Font("Arial", Font.BOLD, 18));
        btnVider.addActionListener(e -> viderChamps());
        JButton btnImprimer = new JButton("IMPRIMER LISTE");
        btnImprimer.setFont(new Font("Arial", Font.BOLD, 18));
        btnImprimer.setBackground(new Color(0, 150, 0));
        btnImprimer.setForeground(Color.WHITE);
        btnImprimer.addActionListener(e -> genererImageTableau("Liste_Clients_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));

        boutons.add(btnImprimer);
        boutons.add(btnAjouter);
        boutons.add(btnModifier);
        boutons.add(btnSupprimer);
        boutons.add(btnVider);

        gbc.gridy = 2;
        header.add(boutons, gbc);

        add(header, BorderLayout.NORTH);

        // TABLEAU
        String[] colonnes = {"ID Client", "Nom", "Téléphone", "Adresse", "ICE"};
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getSelectionModel().addListSelectionListener(e -> selectionnerClient());
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void ajouterClient() {
        String nom = txtNom.getText().trim();
        String tel = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String ice = txtIce.getText().trim();

        if (nom.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom et téléphone obligatoires !");
            return;
        }

        String id = genererNouvelId();
        Client nouveau = new Client(id, nom, tel, adresse, ice);
        listeClients.add(nouveau);
        rafraichirTableau();
        sauvegarderClients();
        viderChamps();
        lblId.setText(id);
        JOptionPane.showMessageDialog(this, "Client ajouté ! ID : " + id);
    }

    private void modifierClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client !");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        String nom = txtNom.getText().trim();
        String tel = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String ice = txtIce.getText().trim();

        if (nom.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom et téléphone obligatoires !");
            return;
        }

        listeClients.removeIf(c -> c.id.equals(id));
        listeClients.add(new Client(id, nom, tel, adresse, ice));
        rafraichirTableau();
        sauvegarderClients();
        JOptionPane.showMessageDialog(this, "Client modifié !");
    }

    private void supprimerClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client !");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le client " + id + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            listeClients.removeIf(c -> c.id.equals(id));
            rafraichirTableau();
            sauvegarderClients();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Client supprimé !");
        }
    }

    private void selectionnerClient() {
        int row = table.getSelectedRow();
        if (row != -1) {
            lblId.setText((String) tableModel.getValueAt(row, 0));
            txtNom.setText((String) tableModel.getValueAt(row, 1));
            txtTelephone.setText((String) tableModel.getValueAt(row, 2));
            txtAdresse.setText((String) tableModel.getValueAt(row, 3));
            txtIce.setText((String) tableModel.getValueAt(row, 4));
        }
    }

    private void viderChamps() {
        lblId.setText("");
        txtNom.setText("");
        txtTelephone.setText("");
        txtAdresse.setText("");
        txtIce.setText("");
    }

    private String genererNouvelId() {
        int max = 0;
        for (Client c : listeClients) {
            if (c.id.startsWith("C")) {
                try {
                    int num = Integer.parseInt(c.id.substring(1));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "C" + String.format("%03d", max + 1);
    }

    class Client {
        String id, nom, telephone, adresse, ice;
        Client(String i, String n, String t, String a, String ic) {
            id = i; nom = n; telephone = t; adresse = a; ice = ic;
        }
    }
    private void genererImageTableau(String titreFichier) {
        // Capture simple du tableau
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
}