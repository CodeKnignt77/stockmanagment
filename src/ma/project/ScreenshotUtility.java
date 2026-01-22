package ma.project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class ScreenshotUtility {
    public static void captureFrame(JFrame frame, String fileName) {
        try {
            // Ensure the frame is laid out
            frame.addNotify();
            frame.validate();

            // For frames not yet visible, we might need a fixed size or packed size
            // If it has no size, pack it
            if (frame.getWidth() == 0 || frame.getHeight() == 0) {
                frame.pack();
            }

            BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Apply high quality rendering hints
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            frame.paintAll(g2d);
            g2d.dispose();

            File directory = new File("screenshots");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File outputFile = new File(directory, fileName + ".png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Screenshot saved: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error capturing screenshot for " + fileName + ": " + e.getMessage());
        }
    }
}
