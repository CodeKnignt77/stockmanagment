package ma.project;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("StockSecure - Tableau de bord");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Titre principal
        JLabel title = new JLabel("StockSecure - Gestion Entreprise");
        title.setBounds(0, 20, 900, 60);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(0, 80, 160));
        add(title);

        // Boutons gauche
        JButton btnClients = createButton("Gestion Clients", 100, 120);
        JButton btnProduits = createButton("Gestion Produits", 100, 200);
        JButton btnFactures = createButton("Nouvelle Facture", 100, 280);
        JButton btnStock = createButton("État du Stock", 100, 360);

        // Boutons droite
        JButton btnAttaques = createButton("Démonstration Attaques Crypto", 500, 120);
        JButton btnDeconnexion = createButton("Déconnexion", 500, 360);

        // Liaisons
        btnClients.addActionListener(e -> new GestionClientsFrame());
        btnProduits.addActionListener(e -> new GestionProduitsFrame());
        btnFactures.addActionListener(e -> new FactureFrame());
        btnStock.addActionListener(e -> new EtatStockFrame());
        btnAttaques.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Démonstration des attaques Coppersmith & Boneh-Durfee\n" +
            "sur RSA 4096-bits implémenté depuis zéro en Java pur",
            "Module Cryptographie Avancée", JOptionPane.INFORMATION_MESSAGE));
        
        // Déconnexion retourne au Login
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    private JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 300, 60);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btn);
        return btn;
    }
}