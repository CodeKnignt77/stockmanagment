package ma.project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FactureFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTextField txtClientId, txtRefProduit, txtQuantite;
    private HashMap<String, String> clients = new HashMap<>();
    private HashMap<String, Produit> produits = new HashMap<>();
    private JLabel lblClient;
    private JLabel lblTotal;
    private JLabel lblNumFacture;
    private int numeroFacture = 0;
    
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SUCCESS_COLOR = new Color(56, 142, 60);
    private static final Color BG_COLOR = new Color(250, 250, 252);
    private static final Color HEADER_COLOR = new Color(30, 136, 229);

    public FactureFrame() {
        chargerDonnees();
        incrementerNumeroFacture();
        initUI();
    }

    private void chargerDonnees() {
        clients.put("C001", "Mohammed Benali - Casablanca");
        clients.put("C002", "SARL ImportExport - Rabat");
        clients.put("C003", "Pharmacie Al Massira - Marrakech");

        produits.put("P101", new Produit("Samsung Galaxy S24", 10200.00));
        produits.put("P202", new Produit("Sac de ciment 50kg", 102.00));
        produits.put("P303", new Produit("Huile d'olive 5L", 144.00));
        produits.put("P404", new Produit("Laptop Dell XPS", 15000.00));
        produits.put("P505", new Produit("Pack eau 12x1.5L", 54.00));
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
        setTitle("StockSecure - Facturation Rapide");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // Header moderne
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // Tableau avec style
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // Panel de saisie et total
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setPaint(new GradientPaint(0, 0, HEADER_COLOR, 0, getHeight(), PRIMARY_COLOR));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel iconLabel = new JLabel("🧾");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
        JLabel title = new JLabel("Facturation Rapide");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(iconLabel);
        titlePanel.add(Box.createHorizontalStrut(10));
        titlePanel.add(title);
        header.add(titlePanel);

        // Champs de saisie
        JPanel inputPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Client ID
        JPanel clientIdPanel = createInputField("Client ID", txtClientId = createStyledTextField());
        txtClientId.addActionListener(e -> verifierClient());
        inputPanel.add(clientIdPanel);

        // Client Name
        JPanel clientNamePanel = new JPanel(new BorderLayout());
        clientNamePanel.setOpaque(false);
        JLabel clientLabel = new JLabel("Client :");
        clientLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        clientLabel.setForeground(new Color(255, 255, 255, 220));
        lblClient = new JLabel("Entrez un ID client...");
        lblClient.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblClient.setForeground(new Color(255, 255, 200));
        clientNamePanel.add(clientLabel, BorderLayout.NORTH);
        clientNamePanel.add(lblClient, BorderLayout.CENTER);
        inputPanel.add(clientNamePanel);

        // Référence Produit
        JPanel refPanel = createInputField("Réf Produit", txtRefProduit = createStyledTextField());
        txtRefProduit.addActionListener(e -> txtQuantite.requestFocus());
        inputPanel.add(refPanel);

        // Espace vide pour alignement
        inputPanel.add(new JPanel());
        inputPanel.add(new JPanel());
        inputPanel.add(new JPanel());

        header.add(inputPanel);
        return header;
    }

    private JPanel createInputField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label + " :");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(255, 255, 255, 220));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(new Color(255, 255, 255, 240));
        field.setOpaque(true);
        return field;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        tableModel = new DefaultTableModel(new String[]{"Produit", "PU HT", "Qté", "Total HT"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Header du tableau
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        scrollPane.setBackground(BG_COLOR);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 20, 20));

        // Panel de saisie
        JPanel saisiePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        saisiePanel.setBackground(BG_COLOR);
        saisiePanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel qteLabel = new JLabel("Quantité :");
        qteLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        qteLabel.setForeground(new Color(80, 80, 80));

        txtQuantite = new JTextField("1", 8);
        txtQuantite.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtQuantite.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2),
            new EmptyBorder(8, 12, 8, 12)
        ));
        txtQuantite.addActionListener(e -> ajouterProduit());

        JButton btnAjouter = createStyledButton("➕ AJOUTER PRODUIT", PRIMARY_COLOR);
        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAjouter.setPreferredSize(new Dimension(200, 45));
        btnAjouter.addActionListener(e -> ajouterProduit());

        saisiePanel.add(qteLabel);
        saisiePanel.add(txtQuantite);
        saisiePanel.add(Box.createHorizontalStrut(20));
        saisiePanel.add(btnAjouter);

        // Panel total et impression
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(BG_COLOR);
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SUCCESS_COLOR, 2, true),
            new EmptyBorder(15, 20, 15, 20)
        ));

        lblTotal = new JLabel("TOTAL TTC : 0.00 DH");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotal.setForeground(SUCCESS_COLOR);
        totalPanel.add(lblTotal, BorderLayout.WEST);

        JButton btnPDF = createStyledButton("🖨️ IMPRIMER FACTURE", SUCCESS_COLOR);
        btnPDF.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPDF.setPreferredSize(new Dimension(250, 50));
        btnPDF.addActionListener(e -> genererPDFAvecLogo());
        totalPanel.add(btnPDF, BorderLayout.EAST);

        panel.add(saisiePanel, BorderLayout.NORTH);
        panel.add(totalPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }

    private void verifierClient() {
        String id = txtClientId.getText().toUpperCase().trim();
        String nom = clients.get(id);
        if (nom != null) {
            lblClient.setText(nom);
            lblClient.setForeground(new Color(144, 238, 144));
            txtRefProduit.requestFocus();
        } else {
            lblClient.setText("❌ Client inconnu !");
            lblClient.setForeground(new Color(255, 150, 150));
        }
    }

    private void ajouterProduit() {
        String ref = txtRefProduit.getText().toUpperCase().trim();
        Produit p = produits.get(ref);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "❌ Produit non trouvé !\nRéférences disponibles: P101, P202, P303, P404, P505", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int qte = 1;
        try { 
            qte = Integer.parseInt(txtQuantite.getText()); 
        } catch (Exception ignored) {}
        if (qte < 1) qte = 1;

        double totalLigne = p.prixHT * qte;
        tableModel.addRow(new Object[]{p.nom, String.format("%.2f", p.prixHT), qte, String.format("%.2f", totalLigne)});

        // Calculer le total TTC
        double totalHT = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            totalHT += Double.parseDouble(tableModel.getValueAt(i, 3).toString());
        }
        double totalTTC = totalHT * 1.20;
        lblTotal.setText(String.format("TOTAL TTC : %.2f DH", totalTTC));

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
            BufferedImage logo = ImageIO.read(new File("logo.JPEG"));
            g.drawImage(logo, 220, 80, 160, 160, null);
        } catch (Exception e) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("StockSecure Maroc", 220, 180);
        }

        lblNumFacture = new JLabel("Facture N° F" + new SimpleDateFormat("yyyy").format(new Date()) + "-" + String.format("%04d", numeroFacture));
        lblNumFacture.setFont(new Font("Arial", Font.BOLD, 24));
        lblNumFacture.setForeground(Color.WHITE);
        
        
        g.drawString(lblNumFacture.getText(), 50, 280);
        g.drawString("Client : " + lblClient.getText(), 50, 310);
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
        g.drawString(lblTotal.getText(), 200, y + 50);

        g.dispose();

        try {
            ImageIO.write(img, "png", new File("Facture_F" + numeroFacture + ".png"));
            JOptionPane.showMessageDialog(this, "Facture générée : Facture_F" + numeroFacture + ".png");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du PDF");
        }
    }

    class Produit {
        String nom;
        double prixHT;
        Produit(String n, double p) { nom = n; prixHT = p; }
    }
}
