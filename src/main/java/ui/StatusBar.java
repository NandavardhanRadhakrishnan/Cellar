package ui;

import ui.input.InputMode;

import java.awt.*;

public class StatusBar {

    public static final int HEIGHT = 30;

    private static final int MODE_BOX_WIDTH = 40;

    public void draw(
            Graphics g,
            int panelWidth,
            int panelHeight,
            InputMode mode,
            String editorText
    ) {
        int y = panelHeight - HEIGHT;

        // Background of entire bar
        g.setColor(new Color(235, 235, 235));
        g.fillRect(0, y, panelWidth, HEIGHT);

        // Top border of bar
        g.setColor(Color.GRAY);
        g.drawLine(0, y, panelWidth, y);

        drawModeBox(g, mode, y);
        drawEditorText(g, editorText, y);
    }

    private void drawModeBox(Graphics g, InputMode mode, int barY) {

        // Box: flush left, full height
        g.setColor(mode.getColor());
        g.fillRect(
                0,
                barY,
                MODE_BOX_WIDTH,
                HEIGHT
        );

        // Right border of the mode box
        g.setColor(Color.BLACK);
        g.drawLine(
                MODE_BOX_WIDTH,
                barY,
                MODE_BOX_WIDTH,
                barY + HEIGHT
        );

        // Text centered vertically & horizontally in the box
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(mode.getLabel());
        int textAscent = fm.getAscent();

        int textX = (MODE_BOX_WIDTH - textWidth) / 2;
        int textY = barY + (HEIGHT + textAscent) / 2 - 2;

        g.drawString(mode.getLabel(), textX, textY);
    }

    private void drawEditorText(Graphics g, String text, int barY) {
        g.setColor(Color.BLACK);

        g.drawString(
                text,
                MODE_BOX_WIDTH + 10,
                barY + 20
        );
    }
}
