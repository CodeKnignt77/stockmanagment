package ma.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class DashboardFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Modern Color Scheme - Burgundy & Golden Sand
    private static final Color BURGUNDY = new Color(91, 14, 20); // #5B0E14
    private static final Color GOLDEN_SAND = new Color(241, 225, 148); // #F1E194
    private static final Color DARK_BG = new Color(20, 20, 20);
    private static final Color PANEL_BG = new Color(40, 35, 35);
    private static final Color CARD_BG = new Color(50, 45, 45);
    private static final Color TEXT_LIGHT = new Color(250, 250, 250);
    private static final Color TEXT_DIM = new Color(180, 180, 180);

    public DashboardFrame() {
        setTitle("StockSecure - Tableau de Bord");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        // Gradient Background Panel
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Complex gradient from burgundy through dark to golden hint
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(BURGUNDY.getRed(), BURGUNDY.getGreen(), BURGUNDY.getBlue(), 200),
                        getWidth(), getHeight(), DARK_BG);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Subtle radial effect
                g2d.setColor(new Color(GOLDEN_SAND.getRed(), GOLDEN_SAND.getGreen(), GOLDEN_SAND.getBlue(), 15));
                g2d.fillOval(getWidth() - 300, -100, 500, 500);
            }
        };
        bgPanel.setBounds(0, 0, 1100, 750);
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // Header Panel
        JPanel headerPanel = createRoundedPanel(PANEL_BG, 20);
        headerPanel.setBounds(30, 30, 1040, 100);
        headerPanel.setLayout(null);
        bgPanel.add(headerPanel);

        // Logo/Icon in header
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, BURGUNDY, 60, 60, GOLDEN_SAND);
                g2d.setPaint(gp);
                g2d.fillOval(5, 5, 50, 50);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
                g2d.drawString("SS", 17, 38);
            }
        };
        logoPanel.setBounds(30, 25, 60, 60);
        logoPanel.setOpaque(false);
        headerPanel.add(logoPanel);

        // Title
        JLabel title = new JLabel("StockSecure");
        title.setBounds(100, 20, 400, 35);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(GOLDEN_SAND);
        headerPanel.add(title);

        JLabel subtitle = new JLabel("Gestion d'entreprise professionnelle");
        subtitle.setBounds(100, 55, 400, 25);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(TEXT_DIM);
        headerPanel.add(subtitle);

        // Logout Button in Header
        JButton logoutBtn = createSmallButton("Déconnexion", BURGUNDY);
        logoutBtn.setBounds(880, 30, 140, 45);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        headerPanel.add(logoutBtn);

        // Main Content Area Label
        JLabel sectionTitle = new JLabel("Modules de gestion");
        sectionTitle.setBounds(50, 150, 400, 35);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sectionTitle.setForeground(TEXT_LIGHT);
        bgPanel.add(sectionTitle);

        // Create Card-Style Buttons in Grid Layout
        int startX = 50;
        int startY = 210;
        int cardWidth = 320;
        int cardHeight = 130;
        int gapX = 30;
        int gapY = 30;

        // Row 1
        JPanel clientCard = createModuleCard("Gestion clients",
                "Gérer vos clients et contacts", "👥", 0);
        clientCard.setBounds(startX, startY, cardWidth, cardHeight);
        clientCard.addMouseListener(createCardClickListener(
                () -> JOptionPane.showMessageDialog(this, "Module Clients - Bientôt disponible")));
        bgPanel.add(clientCard);

        JPanel productCard = createModuleCard("Gestion produits",
                "Stock et catalogue produits", "📦", 1);
        productCard.setBounds(startX + cardWidth + gapX, startY, cardWidth, cardHeight);
        productCard.addMouseListener(createCardClickListener(
                () -> JOptionPane.showMessageDialog(this, "Module Produits - Bientôt disponible")));
        bgPanel.add(productCard);

        JPanel invoiceCard = createModuleCard("Nouvelle facture",
                "Créer et gérer les factures", "🧾", 2);
        invoiceCard.setBounds(startX + (cardWidth + gapX) * 2, startY, cardWidth, cardHeight);
        invoiceCard.addMouseListener(createCardClickListener(() -> new FactureFrame()));
        bgPanel.add(invoiceCard);

        // Row 2
        JPanel stockCard = createModuleCard("État du stock",
                "Visualiser l'inventaire", "📊", 3);
        stockCard.setBounds(startX, startY + cardHeight + gapY, cardWidth, cardHeight);
        stockCard.addMouseListener(
                createCardClickListener(() -> JOptionPane.showMessageDialog(this, "État du stock affiché")));
        bgPanel.add(stockCard);

        JPanel cryptoCard = createModuleCard("Démo crypto",
                "Attaques cryptographiques", "🔐", 4);
        cryptoCard.setBounds(startX + cardWidth + gapX, startY + cardHeight + gapY, cardWidth, cardHeight);
        cryptoCard.addMouseListener(createCardClickListener(() -> JOptionPane.showMessageDialog(this,
                "Démonstration des attaques Coppersmith & Boneh-Durfee\n" +
                        "sur RSA 4096-bits implémenté depuis zéro en Java pur",
                "Module Cryptographie Avancée",
                JOptionPane.INFORMATION_MESSAGE)));
        bgPanel.add(cryptoCard);

        // Footer info
        JLabel footer = new JLabel("v1.0 | © 2024 StockSecure");
        footer.setBounds(0, 700, 1100, 30);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(TEXT_DIM);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        bgPanel.add(footer);

        setVisible(true);
    }

    // ========== CUSTOM UI COMPONENTS ==========

    private JPanel createModuleCard(String title, String description, String icon, int index) {
        JPanel card = new JPanel() {
            private boolean hover = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background with subtle gradient
                Color baseColor = hover ? new Color(65, 60, 60) : CARD_BG;
                GradientPaint gp = new GradientPaint(
                        0, 0, baseColor,
                        getWidth(), getHeight(),
                        new Color(baseColor.getRed() - 10, baseColor.getGreen() - 10, baseColor.getBlue() - 10));
                g2d.setPaint(gp);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));

                // Border glow on hover
                if (hover) {
                    g2d.setColor(new Color(GOLDEN_SAND.getRed(), GOLDEN_SAND.getGreen(), GOLDEN_SAND.getBlue(), 100));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 25, 25));
                }
            }
        };

        card.setLayout(null);
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).putClientProperty("hover", true);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).putClientProperty("hover", false);
                card.repaint();
            }
        });

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(25, 25, 60, 60);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        card.add(iconLabel);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setBounds(25, 90, 270, 25);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(GOLDEN_SAND);
        card.add(titleLabel);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setBounds(25, 112, 270, 20);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_DIM);
        card.add(descLabel);

        return card;
    }

    private MouseAdapter createCardClickListener(Runnable action) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JPanel card = (JPanel) e.getSource();
                card.putClientProperty("hover", true);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JPanel card = (JPanel) e.getSource();
                card.putClientProperty("hover", false);
                card.repaint();
            }
        };
    }

    private JButton createSmallButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            private boolean hover = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = hover ? baseColor.brighter() : baseColor;
                g2d.setColor(bgColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2d.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 2);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton) e.getSource()).putClientProperty("hover", true);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton) e.getSource()).putClientProperty("hover", false);
                button.repaint();
            }
        });

        return button;
    }

    private JPanel createRoundedPanel(Color bgColor, int radius) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(bgColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            }
        };
    }
}