package ma.project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SECONDARY_COLOR = new Color(48, 63, 159);
    private static final Color ACCENT_COLOR = new Color(0, 150, 136);
    private static final Color DANGER_COLOR = new Color(211, 47, 47);
    private static final Color SUCCESS_COLOR = new Color(56, 142, 60);
    private static final Color BG_COLOR = new Color(250, 250, 252);
    private static final Color CARD_COLOR = Color.WHITE;

    public DashboardFrame() {
        setTitle("StockSecure - Tableau de bord");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // Header avec gradient
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // Panel principal avec grille de cartes
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = createFooter();
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setPaint(new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 120));
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Titre avec icône
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel title = new JLabel("StockSecure");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Gestion Professionnelle des Stocks & Facturation");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(iconLabel);
        titlePanel.add(Box.createHorizontalStrut(15));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);
        titlePanel.add(textPanel);

        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Ligne 1
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(createCard("👥", "Gestion Clients", "Gérez vos clients et leurs informations", PRIMARY_COLOR, e ->new GestionClientsFrame() ), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createCard("📦", "Gestion Produits", "Catalogue et gestion des stocks", ACCENT_COLOR,
        		e -> new GestionProduitsFrame()), gbc);
        
        gbc.gridx = 2;
        mainPanel.add(createCard("🧾", "Nouvelle Facture", "Créer une facture rapidement", SUCCESS_COLOR,
            e -> new FactureFrame()), gbc);

        // Ligne 2
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(createCard("📊", "État du Stock", "Consulter les niveaux de stock", new Color(255, 152, 0),
            e -> new EtatStockFrame()), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createCard("🔐", "Démo Cryptographie", "RSA 4096 bits & Attaques", new Color(156, 39, 176),
            e -> JOptionPane.showMessageDialog(this,
                "Démonstration des attaques Coppersmith & Boneh-Durfee\n" +
                "sur RSA 4096-bits implémenté depuis zéro en Java pur",
                "Module Cryptographie Avancée",
                JOptionPane.INFORMATION_MESSAGE)), gbc);
        
        gbc.gridx = 2;
        mainPanel.add(createCard("🚪", "Déconnexion", "Quitter la session", DANGER_COLOR,
            e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir vous déconnecter ?",
                    "Déconnexion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginFrame();
                }
            }), gbc);

        return mainPanel;
    }

    private JPanel createCard(String icon, String title, String description, Color color, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30), 2, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(280, 200));
        card.setMaximumSize(new Dimension(280, 200));

        // Icône
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Titre
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 8, 0));

        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(titleLabel);
        card.add(descLabel);

        // Effet hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(color, 3, true),
                    new EmptyBorder(25, 25, 25, 25)
                ));
                card.setLocation(card.getX(), card.getY() - 5);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30), 2, true),
                    new EmptyBorder(25, 25, 25, 25)
                ));
                card.setLocation(card.getX(), card.getY() + 5);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new java.awt.event.ActionEvent(card, 0, ""));
            }
        });

        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(240, 240, 240));
        footer.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        JLabel footerText = new JLabel("© 2025 StockSecure - Développé par CHAOUKI YASSER | Version 1.0");
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerText.setForeground(new Color(120, 120, 120));
        
        footer.add(footerText);
        return footer;
    }
}
