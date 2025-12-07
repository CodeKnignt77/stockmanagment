package ma.project;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.nio.file.*;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
    

    private static final String CONFIG_FILE = "config.txt";

    public LoginFrame() {
        this(false);   // false = pas en mode "je viens de créer le compte"
    }

    public LoginFrame(boolean compteJusteCree) {
        File f = new File(CONFIG_FILE);
        
        if (compteJusteCree || (f.exists() && f.length() > 0)) {
            showNormalLoginWindow();
        } else {
            showFirstLaunchWindow();
        }
    }

    private boolean configExists() {
        try {
            return Files.exists(Paths.get(CONFIG_FILE));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean configIsEmpty() {
        try {
            return Files.size(Paths.get(CONFIG_FILE)) == 0;
        } catch (Exception e) {
            return true;   // si erreur = on traite comme vide
        }
    }

    // Écran première utilisation 
    private void showFirstLaunchWindow() {
        setTitle("StockSecure - Première utilisation");
        setSize(520, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

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

        JLabel l2 = new JLabel("Mot de passe :");
        l2.setBounds(50, 190, 400, 30);
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

        // ←←← LA PARTIE IMPORTANTE : ÉCRITURE FIABLE + PAS DE BOUCLE ←←←
        btn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Les deux champs sont obligatoires !");
                return;
            }

            try {
                // Écriture 100 % sûre avec fermeture immédiate
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(CONFIG_FILE)));
                pw.println(user);
                pw.println(pass);
                pw.close();   // ← très important !

                JOptionPane.showMessageDialog(this, 
                    "Compte administrateur créé avec succès !", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                dispose();                    // ferme la fenêtre de création
                new LoginFrame(true);         // ← true = on sait qu’on vient de créer le compte

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Impossible d'enregistrer le fichier config.txt\nVérifiez les droits d'écriture.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
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
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        
        JLabel titre = new JLabel("Connexion Administrateur");
        titre.setBounds(80, 20, 350, 50);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setForeground(new Color(0, 80, 160));
        add(titre);

        JLabel lu = new JLabel("Utilisateur :");
        lu.setBounds(80, 100, 120, 30);
        JTextField uf = new JTextField();
        uf.setBounds(200, 100, 170, 30);
        add(lu); add(uf);

        JLabel lp = new JLabel("Mot de passe :");
        lp.setBounds(80, 150, 120, 30);
        JPasswordField pf = new JPasswordField();
        pf.setBounds(200, 150, 170, 30);
        add(lp); add(pf);

        JButton btn = new JButton("Se connecter →");
        btn.setBounds(140, 220, 170, 45);
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        add(btn);

        btn.addActionListener(e -> {
            if (uf.getText().equals(adminUser) && new String(pf.getPassword()).equals(adminPass)) {
                dispose();
                new DashboardFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
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