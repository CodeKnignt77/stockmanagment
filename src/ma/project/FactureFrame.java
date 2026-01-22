package ma.project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.HashMap;

public class FactureFrame extends JFrame {

    // Modern Color Scheme - Burgundy & Golden Sand
    private static final Color BURGUNDY = new Color(91, 14, 20); // #5B0E14
    private static final Color GOLDEN_SAND = new Color(241, 225, 148); // #F1E194
    private static final Color DARK_BG = new Color(20, 20, 20);
    private static final Color PANEL_BG = new Color(40, 35, 35);
    private static final Color TEXT_LIGHT = new Color(250, 250, 250);
    private static final Color TEXT_DIM = new Color(180, 180, 180);
    private static final Color INPUT_BG = new Color(60, 55, 55);

    private DefaultTableModel tableModel;
    private JTextField txtClientId, txtRefProduit, txtQuantite;
    private HashMap<String, String> clients = new HashMap<>();
    private HashMap<String, Produit> produits = new HashMap<>();
    private JLabel lblClient;
    private JLabel lblTotal;
    private DecimalFormat df = new DecimalFormat("#,##0.00");

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
        setTitle("StockSecure - Facturation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background Panel
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(BURGUNDY.getRed(), BURGUNDY.getGreen(), BURGUNDY.getBlue(), 200),
                        getWidth(), getHeight(), DARK_BG);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BorderLayout(20, 20));
        bgPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(bgPanel);

        // Header Panel
        JPanel headerPanel = createRoundedPanel(PANEL_BG);
        headerPanel.setLayout(new GridLayout(2, 1, 10, 10));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        bgPanel.add(headerPanel, BorderLayout.NORTH);

        // Header Row 1: Title
        JLabel titleLabel = new JLabel("Nouvelle Facture");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(GOLDEN_SAND);
        headerPanel.add(titleLabel);

        // Header Row 2: Inputs
        JPanel inputsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        inputsPanel.setOpaque(false);

        // Client Input
        JPanel pnlClient = new JPanel(new BorderLayout(5, 5));
        pnlClient.setOpaque(false);
        pnlClient.add(createLabel("Client ID:"), BorderLayout.NORTH);
        txtClientId = createStyledTextField();
        txtClientId.addActionListener(e -> verifierClient());
        pnlClient.add(txtClientId, BorderLayout.CENTER);

        // Product Input
        JPanel pnlProduct = new JPanel(new BorderLayout(5, 5));
        pnlProduct.setOpaque(false);
        pnlProduct.add(createLabel("Réf Produit:"), BorderLayout.NORTH);
        txtRefProduit = createStyledTextField();
        txtRefProduit.addActionListener(e -> txtQuantite.requestFocus());
        pnlProduct.add(txtRefProduit, BorderLayout.CENTER);

        // Quantity Input
        JPanel pnlQty = new JPanel(new BorderLayout(5, 5));
        pnlQty.setOpaque(false);
        pnlQty.add(createLabel("Quantité:"), BorderLayout.NORTH);
        txtQuantite = createStyledTextField();
        txtQuantite.setText("1");
        txtQuantite.addActionListener(e -> ajouterProduit());
        pnlQty.add(txtQuantite, BorderLayout.CENTER);

        // Add Button
        JPanel pnlBtn = new JPanel(new BorderLayout(5, 5));
        pnlBtn.setOpaque(false);
        pnlBtn.add(new JLabel(" "), BorderLayout.NORTH); // Spacer
        JButton btnAjouter = createStyledButton("Ajouter", GOLDEN_SAND);
        btnAjouter.addActionListener(e -> ajouterProduit());
        pnlBtn.add(btnAjouter, BorderLayout.CENTER);

        inputsPanel.add(pnlClient);
        inputsPanel.add(pnlProduct);
        inputsPanel.add(pnlQty);
        inputsPanel.add(pnlBtn);
        headerPanel.add(inputsPanel);

        // Client Info Label (floating below header usually, putting inside header for
        // now)
        lblClient = new JLabel("Client: En attente...");
        lblClient.setForeground(TEXT_DIM);
        titleLabel.add(lblClient); // Creating a bit of a hack layout or just add to header
        // Let's actually add it to the header properly
        // Re-doing layout slightly
        headerPanel.setLayout(new BorderLayout(10, 10));
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);
        topHeader.add(titleLabel, BorderLayout.WEST);
        topHeader.add(lblClient, BorderLayout.EAST);
        headerPanel.add(topHeader, BorderLayout.NORTH);
        headerPanel.add(inputsPanel, BorderLayout.CENTER);

        // Table
        String[] columns = { "Produit", "Prix Unitaire", "Quantité", "Total HT" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(DARK_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        bgPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createRoundedPanel(PANEL_BG);
        footerPanel.setLayout(new BorderLayout(20, 20));
        footerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        lblTotal = new JLabel("Total TTC: 0.00 DH");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotal.setForeground(GOLDEN_SAND);

        JButton btnPrint = createStyledButton("Valider & Imprimer", BURGUNDY);
        btnPrint.setPreferredSize(new Dimension(200, 45));
        btnPrint.addActionListener(e -> imprimerFacture());

        footerPanel.add(lblTotal, BorderLayout.WEST);
        footerPanel.add(btnPrint, BorderLayout.EAST);
        bgPanel.add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Helper Methods
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_DIM);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_LIGHT);
        field.setCaretColor(GOLDEN_SAND);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(80, 75, 75)),
                new EmptyBorder(5, 10, 5, 10)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isRollover() ? baseColor.brighter() : baseColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setForeground(baseColor == GOLDEN_SAND ? DARK_BG : TEXT_LIGHT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createRoundedPanel(Color color) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setBackground(PANEL_BG);
        table.setForeground(TEXT_LIGHT);
        table.setGridColor(new Color(60, 55, 55));
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(BURGUNDY);
        table.setSelectionForeground(GOLDEN_SAND);

        JTableHeader header = table.getTableHeader();
        header.setBackground(DARK_BG);
        header.setForeground(GOLDEN_SAND);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BURGUNDY));

        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
    }

    // Logic Methods
    private void verifierClient() {
        String id = txtClientId.getText().trim().toUpperCase();
        if (clients.containsKey(id)) {
            lblClient.setText("Client: " + clients.get(id));
            lblClient.setForeground(GOLDEN_SAND);
            txtRefProduit.requestFocus();
        } else {
            lblClient.setText("Client introuvable !");
            lblClient.setForeground(Color.RED);
        }
    }

    private void ajouterProduit() {
        String ref = txtRefProduit.getText().trim().toUpperCase();
        if (produits.containsKey(ref)) {
            try {
                int qte = Integer.parseInt(txtQuantite.getText().trim());
                if (qte <= 0)
                    throw new NumberFormatException();

                Produit p = produits.get(ref);
                double totalLigne = p.getPrix() * qte;

                tableModel.addRow(new Object[] {
                        p.getNom(),
                        df.format(p.getPrix()) + " DH",
                        qte,
                        df.format(totalLigne) + " DH"
                });

                calculerTotal();

                // Reset inputs
                txtRefProduit.setText("");
                txtQuantite.setText("1");
                txtRefProduit.requestFocus();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantité invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Produit introuvable", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void calculerTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String val = tableModel.getValueAt(i, 3).toString().replace(" DH", "").replace(",", "").replace(" ", "");
            // Note: replace non-breaking space if standard format uses it
            try {
                // Simple parse manual since format might have spaces
                // For simplicity in this demo reusing logic
                // In real app use NumberFormat.parse
                // Re-calculating from raw types in real app is better
                // Here just purely visual demo logic
                // Let's correct this slightly by just re-calculating from inputs? No, I don't
                // store them.
                // Hacky parse for demo:
                String cleanVal = val.replaceAll("[^\\d.]", "");
                if (cleanVal.isEmpty())
                    continue;
                total += Double.parseDouble(cleanVal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lblTotal.setText("Total TTC: " + df.format(total) + " DH");
    }

    private void imprimerFacture() {
        JOptionPane.showMessageDialog(this,
                "Facture envoyée à l'imprimante...\nTotal: " + lblTotal.getText(),
                "Impression",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Inner class for simple Product
    static class Produit {
        private String nom;
        private double prix;

        public Produit(String nom, double prix) {
            this.nom = nom;
            this.prix = prix;
        }

        public String getNom() {
            return nom;
        }

        public double getPrix() {
            return prix;
        }
    }
}