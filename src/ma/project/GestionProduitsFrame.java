package ma.project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

    // Constants from DashboardFrame
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SECONDARY_COLOR = new Color(48, 63, 159);
    private static final Color BG_COLOR = new Color(250, 250, 252);
    private static final Color SUCCESS_COLOR = new Color(56, 142, 60);
    private static final Color DANGER_COLOR = new Color(211, 47, 47);
    private static final Color ACCENT_COLOR = new Color(0, 150, 136);

    public GestionProduitsFrame() {
        try {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            initUI(); 
            chargerProduits();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ma/project/Design sans titre.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icône non trouvée");
        }
    }

    private void initUI() {
        setTitle("StockSecure - Gestion des Produits");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // === HEADER WITH GRADIENT ===
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setPaint(new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        header.setLayout(new GridBagLayout());
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // -- Title --
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel lblIcon = new JLabel("📦");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JLabel lblTitre = new JLabel("GESTION DES PRODUITS");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(Color.WHITE);
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(lblTitre, BorderLayout.NORTH);
        
        lblDate = new JLabel();
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDate.setForeground(new Color(255, 255, 255, 200));
        textPanel.add(lblDate, BorderLayout.SOUTH);
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(lblIcon);
        leftPanel.add(textPanel);
        
        titlePanel.add(leftPanel, BorderLayout.WEST);

        Timer timer = new Timer(1000, e -> {
            lblDate.setText(new SimpleDateFormat("dd MMMM yyyy - HH:mm:ss").format(new Date()));
        });
        timer.start();

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        header.add(titlePanel, gbc);

        // -- Form --
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(8, 10, 8, 10);
        fgbc.anchor = GridBagConstraints.WEST;

        // REF
        fgbc.gridx = 0; fgbc.gridy = 0;
        formPanel.add(createLabel("Référence :"), fgbc);
        
        lblRef = new JLabel("---");
        lblRef.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRef.setForeground(new Color(255, 215, 0)); // Gold
        fgbc.gridx = 1;
        formPanel.add(lblRef, fgbc);

        // DES
        fgbc.gridx = 2;
        formPanel.add(createLabel("Désignation :"), fgbc);
        txtDesignation = createStyledTextField(20);
        fgbc.gridx = 3;
        formPanel.add(txtDesignation, fgbc);

        // PRIX
        fgbc.gridx = 0; fgbc.gridy = 1;
        formPanel.add(createLabel("Prix HT (DH) :"), fgbc);
        txtPrixHT = createStyledTextField(10);
        fgbc.gridx = 1;
        formPanel.add(txtPrixHT, fgbc);

        // QTE
        fgbc.gridx = 2; fgbc.gridy = 1;
        formPanel.add(createLabel("Quantité :"), fgbc);
        txtQuantite = createStyledTextField(10);
        fgbc.gridx = 3;
        formPanel.add(txtQuantite, fgbc);
        
        gbc.gridy = 1;
        header.add(formPanel, gbc);

        // -- Buttons --
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton btnAdd = createStyledButton("AJOUTER", SUCCESS_COLOR);
        btnAdd.addActionListener(e -> ajouterProduit());
        
        JButton btnEdit = createStyledButton("MODIFIER", PRIMARY_COLOR);
        btnEdit.addActionListener(e -> modifierProduit());

        JButton btnDel = createStyledButton("SUPPRIMER", DANGER_COLOR);
        btnDel.addActionListener(e -> supprimerProduit());

        JButton btnKlear = createStyledButton("VIDER", new Color(117, 117, 117));
        btnKlear.addActionListener(e -> viderChamps());
        
        JButton btnPrint = createStyledButton("IMPRIMER", new Color(156, 39, 176));
        btnPrint.addActionListener(e -> genererImageTableau("Liste_Produits_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDel);
        btnPanel.add(btnKlear);
        btnPanel.add(btnPrint);

        gbc.gridy = 2;
        header.add(btnPanel, gbc);

        add(header, BorderLayout.NORTH);

        // === TABLE ===
        String[] colonnes = {"Référence", "Désignation", "Prix HT (DH)", "Quantité disponible"};
        tableModel = new DefaultTableModel(colonnes, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 45));
        
        table.getSelectionModel().addListSelectionListener(e -> selectionnerProduit());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG_COLOR);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);

        setVisible(true);
    }
    
    // === HELPERS ===
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(255, 255, 255, 220));
        return lbl;
    }

    private JTextField createStyledTextField(int cols) {
        JTextField txt = new JTextField(cols);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1), 
            new EmptyBorder(8, 10, 8, 10)));
        txt.setBackground(new Color(255, 255, 255, 240));
        return txt;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void genererImageTableau(String titreFichier) {
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
            if (listeProduits.isEmpty()) {
                listeProduits.add(new Produit("P101", "Samsung Galaxy S24", 10200.00, 5));
                listeProduits.add(new Produit("P202", "Sac de ciment 50kg", 102.00, 50));
                listeProduits.add(new Produit("P303", "Huile d'olive 5L", 144.00, 20));
            }
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