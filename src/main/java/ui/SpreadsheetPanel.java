package ui;

import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.Selection;
import core.grid.selection.SelectionManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ui.input.InputController;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpreadsheetPanel extends JPanel {

    static final int CELL_W = 80;
    static final int CELL_H = 25;

    final Grid grid = new Grid(20, 10);
    final Cursor cursor = new Cursor();
    final CellEditor editor = new CellEditor();
    final SelectionManager selectionManager = new SelectionManager();
    final StatusBar statusBar = new StatusBar();

    final InputController input;

    public SpreadsheetPanel() {
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        setFocusable(true);
        requestFocusInWindow();

        input = new InputController(
                grid,
                cursor,
                editor,
                selectionManager
        );

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

                if (cursor.row == r && cursor.col == c) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, CELL_W, CELL_H);
                    g.setColor(Color.WHITE);
                } else if (selection!= null && selection.contains(new CellAddress(r, c))){
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
                String text = grid.getCell(r, c).getValue().display();
                g.drawString(text, x + 5, y + 17);
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
            case EDIT ->
                    editor.valueWithCursor();

            case NAVIGATE, SELECT ->
                    Util.intToColumnLabel(cursor.col)
                    +
                    Util.intToRowLabel(cursor.row);
        };
    }

    private String selectionSuffix() {
        if (!selectionManager.hasSelection()) {
            return "";
        }

        return " ("
                + Util.cellAddressToLabel(selectionManager.getAnchor())
                + ":"
                + Util.cellAddressToLabel(selectionManager.getLimit())
                + ")";
    }


}
