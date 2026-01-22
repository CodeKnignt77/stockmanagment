package ma.project;

import javax.swing.*;

public class ScreenshotRunner {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        System.out.println("Starting real screenshot generation...");

        // 1. Login Frame
        System.out.println("Capturing LoginFrame...");
        LoginFrame loginFrame = new LoginFrame(true); // showNormalLoginWindow
        loginFrame.setVisible(true);
        ScreenshotUtility.captureFrame(loginFrame, "real_login_screen");
        loginFrame.dispose();

        // 2. Dashboard Frame
        System.out.println("Capturing DashboardFrame...");
        DashboardFrame dashboardFrame = new DashboardFrame();
        dashboardFrame.setVisible(true);
        ScreenshotUtility.captureFrame(dashboardFrame, "real_dashboard_screen");
        dashboardFrame.dispose();

        // 3. Gestion Clients Frame
        System.out.println("Capturing GestionClientsFrame...");
        GestionClientsFrame clientsFrame = new GestionClientsFrame();
        clientsFrame.setVisible(true);
        ScreenshotUtility.captureFrame(clientsFrame, "real_clients_screen");
        clientsFrame.dispose();

        // 4. Gestion Produits Frame
        System.out.println("Capturing GestionProduitsFrame...");
        GestionProduitsFrame produitsFrame = new GestionProduitsFrame();
        produitsFrame.setVisible(true);
        ScreenshotUtility.captureFrame(produitsFrame, "real_produits_screen");
        produitsFrame.dispose();

        // 5. Facture Frame
        System.out.println("Capturing FactureFrame...");
        FactureFrame factureFrame = new FactureFrame();
        factureFrame.setVisible(true);
        ScreenshotUtility.captureFrame(factureFrame, "real_facture_screen");
        factureFrame.dispose();

        // 6. Etat Stock Frame
        System.out.println("Capturing EtatStockFrame...");
        EtatStockFrame stockFrame = new EtatStockFrame();
        stockFrame.setVisible(true);
        ScreenshotUtility.captureFrame(stockFrame, "real_stock_screen");
        stockFrame.dispose();

        System.out.println("All screenshots generated successfully in 'screenshots/' directory.");
        System.exit(0);
    }
}
