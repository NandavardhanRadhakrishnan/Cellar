package ui;

import core.grid.CellAddress;
import core.grid.Grid;
import core.value.NumberValue;
import core.value.StringValue;
import core.value.Value;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SpreadsheetPanel extends JPanel {

    private static final int CELL_W = 80;
    private static final int CELL_H = 25;

    private final Grid grid = new Grid(20, 10);
    private final Cursor cursor = new Cursor();
    private InputMode mode = InputMode.NAVIGATION;
    private final CellEditor editor = new CellEditor();

    private final StatusBar statusBar = new StatusBar();


    public SpreadsheetPanel() {
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e);
                repaint();
            }
        });
    }

    private void handleKey(KeyEvent e) {
        if (mode == InputMode.NAVIGATION) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> cursor.row--;
                case KeyEvent.VK_S -> cursor.row++;
                case KeyEvent.VK_A -> cursor.col--;
                case KeyEvent.VK_D -> cursor.col++;
                case KeyEvent.VK_ENTER -> {
                    mode = InputMode.EDIT;
                    editor.clear();

                    Value currentCellValue =
                            grid.getValue(new CellAddress(cursor.row, cursor.col));

                    if (!currentCellValue.isEmpty()) {
                        // NOTE: later replace with raw input (for formulas)
                        editor.append(currentCellValue.display());
                    }
                }
            }
        } else { // EDIT MODE
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER -> {
                    commitEditorValue();
                    mode = InputMode.NAVIGATION;
                }
                case KeyEvent.VK_ESCAPE -> {
                    mode = InputMode.NAVIGATION;
                }
                case KeyEvent.VK_BACK_SPACE -> editor.backspace();
                case KeyEvent.VK_LEFT -> editor.moveLeft();
                case KeyEvent.VK_RIGHT -> editor.moveRight();
                default -> {
                    char ch = e.getKeyChar();
                    if (!Character.isISOControl(ch)) {
                        editor.append(ch);
                    }
                }
            }
        }

    }

    private void commitEditorValue() {
        String raw = editor.value();

        if (raw.isBlank()) {
            grid.getCell(cursor.row, cursor.col).clear();
            return;
        }

        try {
            double number = Double.parseDouble(raw);
            grid.setCell(cursor.row, cursor.col, new NumberValue(number));
        } catch (NumberFormatException e) {
            grid.setCell(cursor.row, cursor.col, new StringValue(raw));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int usableHeight = getHeight() - StatusBar.HEIGHT;

        for (int r = 0; r < grid.rows; r++) {
            int y = r * CELL_H;
            if (y + CELL_H > usableHeight) break;

            for (int c = 0; c < grid.cols; c++) {
                int x = c * CELL_W;

                if (cursor.row == r && cursor.col == c) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, CELL_W, CELL_H);
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELL_W, CELL_H);
                    g.setColor(Color.BLACK);
                }

                g.drawRect(x, y, CELL_W, CELL_H);

                var cell = grid.getCell(r, c);
                String text = cell.getValue().display();

                g.drawString(text, x + 5, y + 17);
            }
        }

        String editorText =
                (mode == InputMode.EDIT)
                        ? editor.valueWithCursor()
                        : Util.intToColumnLabel(cursor.col)+":"+Util.intToRowLabel(cursor.row) ;

        statusBar.draw(
                g,
                getWidth(),
                getHeight(),
                mode,
                editorText
        );
    }
}
