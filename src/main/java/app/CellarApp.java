package app;

import javax.swing.*;
import ui.SpreadsheetPanel;

public class CellarApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cellar");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);

            frame.add(new SpreadsheetPanel());
            frame.setVisible(true);
        });
    }
}
