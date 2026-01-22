package ma.project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.nio.file.*;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

    private static final String CONFIG_FILE = "config.txt";

    // Warm Earth Tone Color Scheme
    private static final Color PRIMARY_COLOR = new Color(102, 76, 54); // #664C36 Rich Brown
    private static final Color SECONDARY_COLOR = new Color(51, 28, 8); // #331C08 Dark Brown
    private static final Color ACCENT_COLOR = new Color(204, 190, 177); // #CCBEB1 Warm Beige
    private static final Color SUCCESS_COLOR = new Color(88, 129, 87); // Earthy Green
    private static final Color BG_COLOR = new Color(255, 211, 172); // #FFD3AC Light Cream
    private static final Color CARD_BG = new Color(245, 235, 220); // Warm card background

    public LoginFrame() {
        this(false);
        // Chargement de l'icône de l'application
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ma/project/Design sans titre.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icône non trouvée");
        }
    }

    public LoginFrame(boolean compteJusteCree) {
        File f = new File(CONFIG_FILE);

        if (compteJusteCree || (f.exists() && f.length() > 0)) {
            showNormalLoginWindow();
        } else {
            showFirstLaunchWindow();
        }
        // Chargement de l'icône de l'application
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ma/project/Design sans titre.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icône non trouvée");
        }
    }

    // ========== FIRST LAUNCH - CLEAN DESIGN ==========
    private void showFirstLaunchWindow() {
        setTitle("StockSecure - Configuration");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Subtle gradient from cream to lighter cream
                GradientPaint gradient = new GradientPaint(
                        0, 0, BG_COLOR,
                        0, getHeight(), new Color(255, 220, 185));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Brown Header Bar (like in mockup)
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
        header.setPreferredSize(new Dimension(550, 80));
        header.setOpaque(false);
        add(header, BorderLayout.NORTH);

        // Center container
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false); // Transparent to show gradient
        centerContainer.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Login Card (like mockup)
        JPanel card = createRoundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Lock Icon
        JLabel lockIcon = new JLabel("🔐");
        lockIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lockIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lockIcon.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Title
        JLabel title = new JLabel("StockSecure");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(SECONDARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 8, 0));

        // Subtitle
        JLabel subtitle = new JLabel("Créez votre compte administrateur");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Username Input
        JTextField userField = createCleanTextField("👤  Nom d'utilisateur");
        userField.setMaximumSize(new Dimension(350, 45));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password Input
        JPasswordField passField = createCleanPasswordField("🔒  Mot de passe");
        passField.setMaximumSize(new Dimension(350, 45));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passField.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Create Button
        JButton createBtn = createModernButton("Créer le compte", SUCCESS_COLOR);
        createBtn.setMaximumSize(new Dimension(350, 45));
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtn.setBorder(new EmptyBorder(25, 0, 0, 0));

        // Add components
        card.add(lockIcon);
        card.add(title);
        card.add(subtitle);
        card.add(userField);
        card.add(passField);
        card.add(createBtn);

        centerContainer.add(card);
        add(centerContainer, BorderLayout.CENTER);

        // Button action
        createBtn.addActionListener(e -> {
            String user = userField.getText().trim().replace("👤  ", "").replace("Nom d'utilisateur", "");
            String pass = new String(passField.getPassword());

            if (user.isEmpty() || pass.isEmpty() || user.equals("Nom d'utilisateur")) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Attention",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(CONFIG_FILE)));
                pw.println(user);
                pw.println(pass);
                pw.close();

                JOptionPane.showMessageDialog(this, "Compte créé avec succès!", "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFrame(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // ========== NORMAL LOGIN - CLEAN DESIGN ==========
    private void showNormalLoginWindow() {
        String[] creds = readCredentials();
        String adminUser = creds[0];
        String adminPass = creds[1];

        setTitle("StockSecure - Connexion");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Subtle gradient from cream to lighter cream
                GradientPaint gradient = new GradientPaint(
                        0, 0, BG_COLOR,
                        0, getHeight(), new Color(255, 220, 185));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Brown Header Bar
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
        header.setPreferredSize(new Dimension(550, 80));
        header.setOpaque(false);
        add(header, BorderLayout.NORTH);

        // Center container
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false); // Transparent to show gradient
        centerContainer.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Login Card
        JPanel card = createRoundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Lock Icon
        JLabel lockIcon = new JLabel("🔐");
        lockIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lockIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lockIcon.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Title
        JLabel title = new JLabel("StockSecure");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(SECONDARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Username Input
        JTextField userField = createCleanTextField("👤  Nom d'utilisateur");
        userField.setMaximumSize(new Dimension(350, 45));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password Input
        JPasswordField passField = createCleanPasswordField("🔒  Mot de passe");
        passField.setMaximumSize(new Dimension(350, 45));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passField.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Login Button
        JButton loginBtn = createModernButton("Connexion", SUCCESS_COLOR);
        loginBtn.setMaximumSize(new Dimension(350, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBorder(new EmptyBorder(25, 0, 0, 0));

        // Links
        JLabel forgotLink = new JLabel("<html><u>Mot de passe oublié?</u></html>");
        forgotLink.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        forgotLink.setForeground(new Color(100, 100, 100));
        forgotLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLink.setBorder(new EmptyBorder(15, 0, 5, 0));

        JLabel createLink = new JLabel("<html><u>Créer un compte</u></html>");
        createLink.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        createLink.setForeground(new Color(100, 100, 100));
        createLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        createLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add components
        card.add(lockIcon);
        card.add(title);
        card.add(userField);
        card.add(passField);
        card.add(loginBtn);
        card.add(forgotLink);
        card.add(createLink);

        centerContainer.add(card);
        add(centerContainer, BorderLayout.CENTER);

        // Login Action
        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim().replace("👤  ", "").replace("Nom d'utilisateur", "");
            String pass = new String(passField.getPassword());

            if (user.equals(adminUser) && pass.equals(adminPass)) {
                dispose();
                new DashboardFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });

        passField.addActionListener(e -> loginBtn.doClick());

        setVisible(true);
    }

    // ========== MODERN UI COMPONENTS ==========

    private JPanel createRoundedCard() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BG);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));

                // Subtle border
                g2d.setColor(ACCENT_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 25, 25));
                g2d.dispose();
            }
        };
    }

    private JTextField createCleanTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(150, 150, 150));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(12, 15, 12, 15)));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(SECONDARY_COLOR);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY_COLOR, 2, true),
                        new EmptyBorder(12, 15, 12, 15)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(150, 150, 150));
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(200, 200, 200), 1, true),
                        new EmptyBorder(12, 15, 12, 15)));
            }
        });

        return field;
    }

    private JPasswordField createCleanPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(150, 150, 150));
        field.setBackground(Color.WHITE);
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(12, 15, 12, 15)));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('●');
                    field.setForeground(SECONDARY_COLOR);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY_COLOR, 2, true),
                        new EmptyBorder(12, 15, 12, 15)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                    field.setForeground(new Color(150, 150, 150));
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(200, 200, 200), 1, true),
                        new EmptyBorder(12, 15, 12, 15)));
            }
        });

        return field;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }

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

        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(350, 45));

        return btn;
    }

    private String[] readCredentials() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CONFIG_FILE))) {
            String user = br.readLine();
            String pass = br.readLine();
            return new String[] { user, pass };
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Fichier de configuration corrompu.\\nSupprimez config.txt et relancez.");
            System.exit(0);
            return new String[] { "", "" };
        }
    }
}
