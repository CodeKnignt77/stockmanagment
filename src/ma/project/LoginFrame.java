package ma.project;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.nio.file.*;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

    // Modern Color Scheme - Burgundy & Golden Sand
    private static final Color BURGUNDY = new Color(91, 14, 20); // #5B0E14
    private static final Color GOLDEN_SAND = new Color(241, 225, 148); // #F1E194
    private static final Color DARK_BG = new Color(20, 20, 20);
    private static final Color PANEL_BG = new Color(40, 35, 35);
    private static final Color TEXT_LIGHT = new Color(250, 250, 250);
    private static final Color TEXT_DIM = new Color(180, 180, 180);

    private static final String CONFIG_FILE = "config.txt";

    public LoginFrame() {
        this(false);
    }

    public LoginFrame(boolean compteJusteCree) {
        File f = new File(CONFIG_FILE);

        if (compteJusteCree || (f.exists() && f.length() > 0)) {
            showNormalLoginWindow();
        } else {
            showFirstLaunchWindow();
        }
    }

    // ========== FIRST LAUNCH WINDOW - MODERN DESIGN ==========
    private void showFirstLaunchWindow() {
        setTitle("StockSecure - Configuration Initiale");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Gradient Background Panel
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient from burgundy to dark
                GradientPaint gradient = new GradientPaint(
                        0, 0, BURGUNDY,
                        0, getHeight(), DARK_BG);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setBounds(0, 0, 600, 650);
        bgPanel.setLayout(null);
        add(bgPanel);

        // Main Content Panel with rounded corners
        JPanel contentPanel = createRoundedPanel(PANEL_BG, 30);
        contentPanel.setBounds(50, 60, 500, 520);
        contentPanel.setLayout(null);
        bgPanel.add(contentPanel);

        // Title
        JLabel title = new JLabel("Bienvenue");
        title.setBounds(0, 40, 500, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(GOLDEN_SAND);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(title);

        JLabel subtitle = new JLabel("Créez votre compte administrateur");
        subtitle.setBounds(0, 95, 500, 30);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(TEXT_DIM);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(subtitle);

        // Username Field
        JLabel userLabel = new JLabel("Nom d'utilisateur");
        userLabel.setBounds(60, 160, 380, 25);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_LIGHT);
        contentPanel.add(userLabel);

        JTextField userField = createModernTextField();
        userField.setBounds(60, 190, 380, 50);
        contentPanel.add(userField);

        // Password Field
        JLabel passLabel = new JLabel("Mot de passe");
        passLabel.setBounds(60, 260, 380, 25);
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(TEXT_LIGHT);
        contentPanel.add(passLabel);

        JPasswordField passField = createModernPasswordField();
        passField.setBounds(60, 290, 380, 50);
        contentPanel.add(passField);

        // Create Account Button
        JButton createBtn = createGradientButton("Créer le compte");
        createBtn.setBounds(60, 380, 380, 55);
        contentPanel.add(createBtn);

        // Button Action
        createBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                showModernDialog(this, "Veuillez remplir tous les champs", "Attention", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(CONFIG_FILE)));
                pw.println(user);
                pw.println(pass);
                pw.close();

                showModernDialog(this, "Compte créé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);

                dispose();
                new LoginFrame(true);

            } catch (Exception ex) {
                showModernDialog(this, "Erreur lors de la création du compte", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // ========== NORMAL LOGIN WINDOW - MODERN DESIGN ==========
    private void showNormalLoginWindow() {
        String[] creds = readCredentials();
        String adminUser = creds[0];
        String adminPass = creds[1];

        setTitle("StockSecure - Connexion");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Gradient Background Panel
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, BURGUNDY,
                        0, getHeight(), DARK_BG);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setBounds(0, 0, 550, 700);
        bgPanel.setLayout(null);
        add(bgPanel);

        // Login Panel
        JPanel loginPanel = createRoundedPanel(PANEL_BG, 30);
        loginPanel.setBounds(50, 120, 450, 450);
        loginPanel.setLayout(null);
        bgPanel.add(loginPanel);

        // Logo/Icon Area (decorative circle)
        JPanel logoCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, BURGUNDY, 80, 80, GOLDEN_SAND);
                g2d.setPaint(gp);
                g2d.fillOval(10, 10, 60, 60);
            }
        };
        logoCircle.setBounds(195, 40, 80, 80);
        logoCircle.setOpaque(false);
        loginPanel.add(logoCircle);

        // Title
        JLabel title = new JLabel("StockSecure");
        title.setBounds(0, 130, 450, 45);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(GOLDEN_SAND);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(title);

        JLabel subtitle = new JLabel("Connexion administrateur");
        subtitle.setBounds(0, 180, 450, 25);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(TEXT_DIM);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(subtitle);

        // Username Field
        JLabel userLabel = new JLabel("Nom d'utilisateur");
        userLabel.setBounds(50, 230, 350, 25);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_LIGHT);
        loginPanel.add(userLabel);

        JTextField userField = createModernTextField();
        userField.setBounds(50, 260, 350, 50);
        loginPanel.add(userField);

        // Password Field
        JLabel passLabel = new JLabel("Mot de passe");
        passLabel.setBounds(50, 325, 350, 25);
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(TEXT_LIGHT);
        loginPanel.add(passLabel);

        JPasswordField passField = createModernPasswordField();
        passField.setBounds(50, 355, 350, 50);
        loginPanel.add(passField);

        // Login Button
        JButton loginBtn = createGradientButton("Se connecter");
        loginBtn.setBounds(50, 435, 350, 55);
        loginPanel.add(loginBtn);

        // Login Action
        loginBtn.addActionListener(e -> {
            if (userField.getText().equals(adminUser) && new String(passField.getPassword()).equals(adminPass)) {
                dispose();
                new DashboardFrame();
            } else {
                showModernDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });

        // Enter key support
        passField.addActionListener(e -> loginBtn.doClick());

        setVisible(true);
    }

    // ========== CUSTOM UI COMPONENTS ==========

    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBackground(new Color(50, 45, 45));
        field.setForeground(TEXT_LIGHT);
        field.setCaretColor(GOLDEN_SAND);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(80, 70, 70)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(15, GOLDEN_SAND),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(15, new Color(80, 70, 70)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            }
        });

        return field;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBackground(new Color(50, 45, 45));
        field.setForeground(TEXT_LIGHT);
        field.setCaretColor(GOLDEN_SAND);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(80, 70, 70)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(15, GOLDEN_SAND),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(15, new Color(80, 70, 70)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            }
        });

        return field;
    }

    private JButton createGradientButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gp = new GradientPaint(
                        0, 0, BURGUNDY,
                        getWidth(), getHeight(), GOLDEN_SAND);
                g2d.setPaint(gp);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));

                // Text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2d.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 2);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(new Font("Segoe UI", Font.BOLD, 19));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(new Font("Segoe UI", Font.BOLD, 18));
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

    // Rounded Border class
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }

    private void showModernDialog(Component parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
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