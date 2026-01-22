package ma.project;

import javax.swing.*;

public class ScreenshotRunner {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        System.out.println("Starting master branch screenshot generation...");

        // 1. Login Frame
        System.out.println("Capturing LoginFrame...");
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        ScreenshotUtility.captureFrame(loginFrame, "master_login_screen");
        loginFrame.dispose();

        // 2. Dashboard Frame
        System.out.println("Capturing DashboardFrame...");
        DashboardFrame dashboardFrame = new DashboardFrame();
        dashboardFrame.setVisible(true);
        ScreenshotUtility.captureFrame(dashboardFrame, "master_dashboard_screen");
        dashboardFrame.dispose();

        // 3. Facture Frame
        System.out.println("Capturing FactureFrame...");
        FactureFrame factureFrame = new FactureFrame();
        factureFrame.setVisible(true);
        ScreenshotUtility.captureFrame(factureFrame, "master_facture_screen");
        factureFrame.dispose();

        System.out.println("Screenshots generated for available frames in 'screenshots/'.");
        System.exit(0);
    }
}
