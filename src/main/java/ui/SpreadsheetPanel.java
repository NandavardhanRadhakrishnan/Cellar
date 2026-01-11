package ui;

import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.Selection;
import core.grid.selection.SelectionManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ui.input.InputController;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpreadsheetPanel extends JPanel {

    static final int CELL_W = 80;
    static final int CELL_H = 25;

    Grid grid;
    Cursor cursor;
    CellEditor editor;
    SelectionManager selectionManager;
    InputController input;

    StatusBar statusBar = new StatusBar();

    {
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                input.handleKey(e);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int usableHeight = getHeight() - StatusBar.HEIGHT;
        Selection selection = selectionManager.getSelection();

        for (int r = 0; r < grid.rows; r++) {
            int y = r * CELL_H;
            if (y + CELL_H > usableHeight) break;

            for (int c = 0; c < grid.cols; c++) {
                int x = c * CELL_W;
                CellAddress addr = new CellAddress(r, c);

                if (cursor.row == r && cursor.col == c) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, CELL_W, CELL_H);
                    g.setColor(Color.WHITE);
                } else if (selection != null && selection.contains(addr)) {
                    g.setColor(new Color(128, 176, 255));
                    g.fillRect(x, y, CELL_W, CELL_H);
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELL_W, CELL_H);
                    g.setColor(Color.BLACK);
                }

                g.setColor(
                        cursor.row == r && cursor.col == c
                                ? Color.BLACK
                                : Color.GRAY
                );
                g.drawRect(x, y, CELL_W, CELL_H);

                g.setColor(Color.BLACK);
                g.drawString(
                        grid.getCell(r, c).getValue().display(),
                        x + 5,
                        y + 17
                );
            }
        }

        String statusText = baseStatusText() + selectionSuffix();

        statusBar.draw(
                g,
                getWidth(),
                getHeight(),
                input.getMode(),
                statusText
        );
    }

    private String baseStatusText() {
        return switch (input.getMode()) {
            case EDIT -> editor.valueWithCursor();
            case NAVIGATE, SELECT ->
                    Util.intToColumnLabel(cursor.col)
                            + Util.intToRowLabel(cursor.row);
        };
    }

    private String selectionSuffix() {
        if (!selectionManager.hasSelection()) return "";

        return " ("
                + Util.cellAddressToLabel(selectionManager.getAnchor())
                + ":"
                + Util.cellAddressToLabel(selectionManager.getLimit())
                + ")";
    }
}
