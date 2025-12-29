package ma.project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestionClientsFrame extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtNom, txtTelephone, txtAdresse, txtIce;
    private JLabel lblId, lblDate;
    private List<Client> listeClients = new ArrayList<>();
    
    // Constants from DashboardFrame
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SECONDARY_COLOR = new Color(48, 63, 159);
    private static final Color BG_COLOR = new Color(250, 250, 252);
    private static final Color SUCCESS_COLOR = new Color(56, 142, 60);
    private static final Color DANGER_COLOR = new Color(211, 47, 47);

    public GestionClientsFrame() {
        try {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            
            initUI();
            chargerClients();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ma/project/Design sans titre.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icône non trouvée");
        }
    }

    private void initUI() {
        setTitle("StockSecure - Gestion des Clients");
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
        
        // -- Title Section --
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel lblIcon = new JLabel("👥");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel lblTitre = new JLabel("GESTION DES CLIENTS");
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

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        header.add(titlePanel, gbc);

        // -- Form Section --
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(8, 10, 8, 10);
        fgbc.anchor = GridBagConstraints.WEST;
        
        // Row 1
        fgbc.gridx = 0; fgbc.gridy = 0;
        formPanel.add(createLabel("ID Client :"), fgbc);
        
        lblId = new JLabel("---");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblId.setForeground(new Color(255, 215, 0)); // Gold
        fgbc.gridx = 1; 
        formPanel.add(lblId, fgbc);
        
        fgbc.gridx = 2;
        formPanel.add(createLabel("Nom complet :"), fgbc);       
        txtNom = createStyledTextField(20);
        fgbc.gridx = 3;
        formPanel.add(txtNom, fgbc);
        
        fgbc.gridx = 4;
        formPanel.add(createLabel("Téléphone :"), fgbc);
        txtTelephone = createStyledTextField(15);
        fgbc.gridx = 5;
        formPanel.add(txtTelephone, fgbc);

        // Row 2
        fgbc.gridx = 0; fgbc.gridy = 1;
        formPanel.add(createLabel("Adresse :"), fgbc);
        txtAdresse = createStyledTextField(20);
        fgbc.gridx = 1; fgbc.gridwidth = 3; fgbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtAdresse, fgbc);
        fgbc.fill = GridBagConstraints.NONE; fgbc.gridwidth = 1; 
        
        fgbc.gridx = 4;
        formPanel.add(createLabel("ICE :"), fgbc);
        txtIce = createStyledTextField(15);
        fgbc.gridx = 5;
        formPanel.add(txtIce, fgbc);

        gbc.gridy = 1;
        header.add(formPanel, gbc);

        // -- Button Section --
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton btnAdd = createStyledButton("AJOUTER", SUCCESS_COLOR);
        btnAdd.addActionListener(e -> ajouterClient());
        
        JButton btnEdit = createStyledButton("MODIFIER", PRIMARY_COLOR);
        btnEdit.addActionListener(e -> modifierClient());

        JButton btnDel = createStyledButton("SUPPRIMER", DANGER_COLOR);
        btnDel.addActionListener(e -> supprimerClient());

        JButton btnKlear = createStyledButton("VIDER", new Color(117, 117, 117)); // Grey
        btnKlear.addActionListener(e -> viderChamps());
        
        JButton btnPrint = createStyledButton("IMPRIMER", new Color(156, 39, 176)); // Purple
        btnPrint.addActionListener(e -> genererImageTableau("Liste_Clients_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDel);
        btnPanel.add(btnKlear);
        btnPanel.add(btnPrint);

        gbc.gridy = 2;
        header.add(btnPanel, gbc);

        add(header, BorderLayout.NORTH);

        // === TABLE SECTION ===
        String[] columns = {"ID Client", "Nom", "Téléphone", "Adresse", "ICE"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 45));
        
        table.getSelectionModel().addListSelectionListener(e -> selectionnerClient());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG_COLOR);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    // === HELPER METHODS ===

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(255, 255, 255, 220));
        return lbl;
    }

    private JTextField createStyledTextField(int cols) {
        JTextField txt = new JTextField(cols);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1), 
            new EmptyBorder(8, 10, 8, 10)));
        txt.setBackground(new Color(255, 255, 255, 240));
        return txt;
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

    private void chargerClients() {
        listeClients.clear();
        File file = new File("clients.txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    if (ligne.trim().isEmpty()) continue;
                    String[] p = ligne.split("\\|");
                    if (p.length >= 5) {
                        listeClients.add(new Client(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur lecture clients.txt : " + e.getMessage());
            }
        }
        if (listeClients.isEmpty()) {
            listeClients.add(new Client("C001", "Mohammed Benali", "0661234567", "Casablanca", "001234567890123"));
            listeClients.add(new Client("C002", "SARL ImportExport", "0522888999", "Rabat", "002345678901234"));
        }
        rafraichirTableau();
    }

    private void sauvegarderClients() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("clients.txt")))) {
            for (Client c : listeClients) {
                pw.println(c.id + "|" + c.nom + "|" + c.telephone + "|" + c.adresse + "|" + c.ice);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur sauvegarde clients !");
        }
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        for (Client c : listeClients) {
            tableModel.addRow(new Object[]{c.id, c.nom, c.telephone, c.adresse, c.ice});
        }
    }

    private void ajouterClient() {
        String nom = txtNom.getText().trim();
        String tel = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String ice = txtIce.getText().trim();

        if (nom.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom et téléphone obligatoires !");
            return;
        }

        String id = genererNouvelId();
        Client nouveau = new Client(id, nom, tel, adresse, ice);
        listeClients.add(nouveau);
        rafraichirTableau();
        sauvegarderClients();
        viderChamps();
        lblId.setText(id);
        JOptionPane.showMessageDialog(this, "Client ajouté ! ID : " + id);
    }

    private void modifierClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client !");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        String nom = txtNom.getText().trim();
        String tel = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String ice = txtIce.getText().trim();

        if (nom.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom et téléphone obligatoires !");
            return;
        }

        listeClients.removeIf(c -> c.id.equals(id));
        listeClients.add(new Client(id, nom, tel, adresse, ice));
        rafraichirTableau();
        sauvegarderClients();
        JOptionPane.showMessageDialog(this, "Client modifié !");
    }

    private void supprimerClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client !");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le client " + id + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            listeClients.removeIf(c -> c.id.equals(id));
            rafraichirTableau();
            sauvegarderClients();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Client supprimé !");
        }
    }

    private void selectionnerClient() {
        int row = table.getSelectedRow();
        if (row != -1) {
            lblId.setText((String) tableModel.getValueAt(row, 0));
            txtNom.setText((String) tableModel.getValueAt(row, 1));
            txtTelephone.setText((String) tableModel.getValueAt(row, 2));
            txtAdresse.setText((String) tableModel.getValueAt(row, 3));
            txtIce.setText((String) tableModel.getValueAt(row, 4));
        }
    }

    private void viderChamps() {
        lblId.setText("");
        txtNom.setText("");
        txtTelephone.setText("");
        txtAdresse.setText("");
        txtIce.setText("");
    }

    private String genererNouvelId() {
        int max = 0;
        for (Client c : listeClients) {
            if (c.id.startsWith("C")) {
                try {
                    int num = Integer.parseInt(c.id.substring(1));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "C" + String.format("%03d", max + 1);
    }

    class Client {
        String id, nom, telephone, adresse, ice;
        Client(String i, String n, String t, String a, String ic) {
            id = i; nom = n; telephone = t; adresse = a; ice = ic;
        }
    }
}