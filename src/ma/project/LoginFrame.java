package ma.project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.nio.file.*;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
    
    private static final String CONFIG_FILE = "config.txt";
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SECONDARY_COLOR = new Color(48, 63, 159);
    private static final Color SUCCESS_COLOR = new Color(56, 142, 60);
    private static final Color BG_COLOR = new Color(245, 247, 250);

    public LoginFrame() {
        this(false);
     // Chargement de l'icône de l'application
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ma/project/Design sans titre.png"));
            // Ou si tu l'as mis directement dans src : new ImageIcon("Design sans titre.png")
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Si l'icône n'est pas trouvée, on continue sans
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
            // Ou si tu l'as mis directement dans src : new ImageIcon("Design sans titre.png")
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Si l'icône n'est pas trouvée, on continue sans
            System.out.println("Icône non trouvée");
        }
    }

    // Écran première utilisation 
    private void showFirstLaunchWindow() {
        setTitle("StockSecure - Première utilisation");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // Header avec gradient
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
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JLabel iconLabel = new JLabel("🔐", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titre = new JLabel("Bienvenue dans StockSecure");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(new EmptyBorder(10, 0, 5, 0));
        
        JLabel subtitle = new JLabel("Créez le compte administrateur de votre entreprise");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(255, 255, 255, 220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(iconLabel);
        header.add(titre);
        header.add(subtitle);
        add(header, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BG_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Champ utilisateur
        JLabel l1 = new JLabel("Nom d'utilisateur");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l1.setForeground(new Color(80, 80, 80));
        l1.setAlignmentX(Component.LEFT_ALIGNMENT);
        l1.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        JTextField userField = createStyledTextField();
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        userField.setPreferredSize(new Dimension(0, 45));
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Champ mot de passe
        JLabel l2 = new JLabel("Mot de passe");
        l2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l2.setForeground(new Color(80, 80, 80));
        l2.setAlignmentX(Component.LEFT_ALIGNMENT);
        l2.setBorder(new EmptyBorder(20, 0, 8, 0));
        
        JPasswordField passField = createStyledPasswordField();
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setPreferredSize(new Dimension(0, 45));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Bouton
        JButton btn = createStyledButton("Enregistrer et continuer →", SUCCESS_COLOR);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setPreferredSize(new Dimension(0, 50));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setBorder(new EmptyBorder(30, 0, 0, 0));

        centerPanel.add(l1);
        centerPanel.add(userField);
        centerPanel.add(l2);
        centerPanel.add(passField);
        centerPanel.add(btn);
        add(centerPanel, BorderLayout.CENTER);

        // Action du bouton
        btn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                showError("Les deux champs sont obligatoires !");
                return;
            }

            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(CONFIG_FILE)));
                pw.println(user);
                pw.println(pass);
                pw.close();

                showSuccess("Compte administrateur créé avec succès !");
                dispose();
                new LoginFrame(true);
            } catch (Exception ex) {
                showError("Impossible d'enregistrer le fichier config.txt\nVérifiez les droits d'écriture.");
            }
        });

        setVisible(true);
    }

    // Login normal
    private void showNormalLoginWindow() {
        String[] creds = readCredentials();
        String adminUser = creds[0];
        String adminPass = creds[1];

        setTitle("StockSecure - Connexion");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // Header
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
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(50, 40, 50, 40));
        
        JLabel iconLabel = new JLabel("🔑", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titre = new JLabel("Connexion Administrateur");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        header.add(iconLabel);
        header.add(titre);
        add(header, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BG_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Champ utilisateur
        JLabel lu = new JLabel("Utilisateur");
        lu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lu.setForeground(new Color(80, 80, 80));
        lu.setAlignmentX(Component.LEFT_ALIGNMENT);
        lu.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        JTextField uf = createStyledTextField();
        uf.setAlignmentX(Component.LEFT_ALIGNMENT);
        uf.setPreferredSize(new Dimension(0, 45));
        uf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Champ mot de passe
        JLabel lp = new JLabel("Mot de passe");
        lp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lp.setForeground(new Color(80, 80, 80));
        lp.setAlignmentX(Component.LEFT_ALIGNMENT);
        lp.setBorder(new EmptyBorder(20, 0, 8, 0));
        
        JPasswordField pf = createStyledPasswordField();
        pf.setAlignmentX(Component.LEFT_ALIGNMENT);
        pf.setPreferredSize(new Dimension(0, 45));
        pf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Bouton
        JButton btn = createStyledButton("Se connecter →", PRIMARY_COLOR);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setPreferredSize(new Dimension(0, 50));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setBorder(new EmptyBorder(30, 0, 0, 0));

        centerPanel.add(lu);
        centerPanel.add(uf);
        centerPanel.add(lp);
        centerPanel.add(pf);
        centerPanel.add(btn);
        add(centerPanel, BorderLayout.CENTER);

        btn.addActionListener(e -> {
            if (uf.getText().equals(adminUser) && new String(pf.getPassword()).equals(adminPass)) {
                dispose();
                new DashboardFrame();
            } else {
                showError("Identifiants incorrects !");
                pf.setText("");
            }
        });

        setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2),
                    new EmptyBorder(11, 14, 11, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2),
                    new EmptyBorder(11, 14, 11, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private String[] readCredentials() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CONFIG_FILE))) {
            String user = br.readLine();
            String pass = br.readLine();
            return new String[]{user, pass};
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fichier de configuration corrompu.\nSupprimez config.txt et relancez.");
            System.exit(0);
            return new String[]{"", ""};
        }
    }
}
