package ma.project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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

    // Warm Earth Tone Color Scheme
    private static final Color PRIMARY_COLOR = new Color(102, 76, 54); // #664C36 Rich Brown
    private static final Color SECONDARY_COLOR = new Color(51, 28, 8); // #331C08 Dark Brown
    private static final Color BG_COLOR = new Color(255, 211, 172); // #FFD3AC Light Cream
    private static final Color SUCCESS_COLOR = new Color(88, 129, 87); // Earthy Green
    private static final Color DANGER_COLOR = new Color(153, 51, 0); // Warm Red-Brown

    public EtatStockFrame() {
        try {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            initUI();
            chargerProduits();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ma/project/Design sans titre.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icône non trouvée");
        }
    }

    private void initUI() {
        setTitle("StockSecure - État du Stock");
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

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel lblIcon = new JLabel("📊");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JLabel lblTitre = new JLabel("ÉTAT DU STOCK");
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        header.add(titlePanel, gbc);

        // Print Button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        JButton btnImprimer = createStyledButton("IMPRIMER ÉTAT", SUCCESS_COLOR);
        btnImprimer.addActionListener(
                e -> genererImageTableau("Etat_Stock_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));

        btnPanel.add(btnImprimer);

        gbc.gridy = 1;
        header.add(btnPanel, gbc);

        add(header, BorderLayout.NORTH);

        // === TABLE ===
        String[] colonnes = { "Référence", "Désignation", "Prix HT", "État Stock" };
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(
                new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 45));

        // Custom Renderer for Stock Column
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if (status != null && status.contains("Rupture")) {
                    c.setForeground(DANGER_COLOR);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (status != null && status.contains("faible")) {
                    c.setForeground(new Color(255, 140, 0)); // Orange
                } else {
                    c.setForeground(SUCCESS_COLOR);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG_COLOR);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        setVisible(true);
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
                if (ligne.trim().isEmpty())
                    continue;
                String[] p = ligne.split("\\|");
                if (p.length < 4)
                    continue;
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

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        for (Produit p : listeProduits) {
            String stockStatus;
            if (p.quantite <= 0)
                stockStatus = "Rupture de stock";
            else if (p.quantite < 10)
                stockStatus = "Stock faible (" + p.quantite + ")";
            else
                stockStatus = p.quantite + " unité(s)";

            tableModel.addRow(new Object[] { p.ref, p.designation, String.format("%.2f DH", p.prixHT), stockStatus });
        }
    }

    class Produit {
        String ref, designation;
        double prixHT;
        int quantite;

        Produit(String r, String d, double p, int q) {
            ref = r;
            designation = d;
            prixHT = p;
            quantite = q;
        }
    }
}