package ma.project;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class LoginFrame extends JFrame {

    private static final String CONFIG_FILE = "config.txt";

    public LoginFrame() {
        this(false);
    }

    public LoginFrame(boolean compteJusteCree) {
        File config = new File(CONFIG_FILE);
        if (compteJusteCree || (config.exists() && config.length() > 0)) {
            showNormalLoginWindow();
        } else {
            showFirstLaunchWindow();
        }
    }

    // ==================================================================
    // Première utilisation : création du compte admin + génération clé RSA dérivée du mot de passe
    // ==================================================================
    private void showFirstLaunchWindow() {
        setTitle("StockSecure - Première utilisation");
        setSize(520, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JLabel titre = new JLabel("Créez le compte administrateur de votre entreprise");
        titre.setBounds(40, 30, 460, 40);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre);

        JLabel l1 = new JLabel("Nom d'utilisateur :");
        l1.setBounds(50, 100, 400, 30);
        JTextField userField = new JTextField();
        userField.setBounds(50, 135, 400, 40);
        add(l1);
        add(userField);

        JLabel l2 = new JLabel("Mot de passe (il servira aussi à générer la clé RSA) :");
        l2.setBounds(50, 190, 450, 30);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(50, 225, 400, 40);
        add(l2);
        add(passField);

        JButton btn = new JButton("Enregistrer et continuer");
        btn.setBounds(100, 310, 300, 50);
        btn.setBackground(new Color(0, 150, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        add(btn);

        btn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Les deux champs sont obligatoires !");
                return;
            }

            // Sauvegarde du compte admin
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(CONFIG_FILE)))) {
                pw.println(user);
                pw.println(pass);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde du compte !");
                return;
            }

            // Génération de la clé RSA DÉRIVÉE DU MOT DE PASSE
            JOptionPane.showMessageDialog(this, "Génération de la clé RSA 2048-bits à partir de votre mot de passe... (3-5 secondes)");
            RSAUtil rsa = new RSAUtil(pass);  // ← clé déterministe à partir du mot de passe

            JOptionPane.showMessageDialog(this, 
                "Compte administrateur créé avec succès !\n" +
                "Clé RSA générée à partir de votre mot de passe.\n" +
                "Vos données seront chiffrées de façon sécurisée.",
                "Sécurité activée", JOptionPane.INFORMATION_MESSAGE);

            // Passage au login normal
            SwingUtilities.invokeLater(() -> {
                dispose();
                new LoginFrame(true);
            });
        });

        setVisible(true);
    }

    // ==================================================================
    // Login normal : régénération de la clé RSA à partir du mot de passe saisi
    // ==================================================================
    private void showNormalLoginWindow() {
        setTitle("StockSecure - Connexion");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JLabel titre = new JLabel("Connexion Administrateur");
        titre.setBounds(80, 20, 350, 50);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setForeground(new Color(0, 80, 160));
        add(titre);

        JLabel lu = new JLabel("Utilisateur :");
        lu.setBounds(80, 100, 120, 30);
        JTextField userField = new JTextField();
        userField.setBounds(200, 100, 170, 30);
        add(lu);
        add(userField);

        JLabel lp = new JLabel("Mot de passe :");
        lp.setBounds(80, 150, 120, 30);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(200, 150, 170, 30);
        add(lp);
        add(passField);

        JButton btn = new JButton("Se connecter →");
        btn.setBounds(140, 220, 170, 45);
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        add(btn);

        String[] creds = readCredentials();
        String adminUser = creds[0];
        String adminPass = creds[1];

        btn.addActionListener(e -> {
            String enteredUser = userField.getText();
            String enteredPass = new String(passField.getPassword());

            if (enteredUser.equals(adminUser) && enteredPass.equals(adminPass)) {
                // Régénération de la clé RSA à partir du mot de passe correct
                RSAUtil rsa = new RSAUtil(enteredPass);

                JOptionPane.showMessageDialog(this, "Connexion réussie ! Clé RSA régénérée.");
                dispose();
                new DashboardFrame(rsa);  // ← on passe la clé RSA au Dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private String[] readCredentials() {
        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
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