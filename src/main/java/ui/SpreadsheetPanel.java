package ui;

import core.formula.FormulaEngine;
import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.Selection;
import core.grid.selection.SelectionManager;
import java.awt.*;
import java.awt.BasicStroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ui.input.InputController;
import util.Util;

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
  FormulaEngine formulaEngine;

  StatusBar statusBar = new StatusBar();

  {
    setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
    setBackground(ThemeManager.getPanelBackground());
    setFocusable(true);
    requestFocusInWindow();

    addKeyListener(
      new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          input.handleKey(e);
          // Update background in case theme was toggled
          setBackground(ThemeManager.getPanelBackground());
          grid.recalculateAll(formulaEngine.evaluator());
          repaint();
        }
      }
    );
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

        boolean isCursor = cursor.row == r && cursor.col == c;
        boolean isSelected = selection != null && selection.contains(addr);

        // Background color based on selection/cursor state
        g.setColor(ThemeManager.getCellBackground(isCursor, isSelected));
        g.fillRect(x, y, CELL_W, CELL_H);

        // Cell border - thicker and colored for cursor cell
        if (isCursor) {
          g.setColor(ThemeManager.getCursorBorder());
          ((Graphics2D) g).setStroke(new BasicStroke(2));
        } else {
          g.setColor(ThemeManager.getGridBorder());
          ((Graphics2D) g).setStroke(new BasicStroke(1));
        }
        g.drawRect(x, y, CELL_W, CELL_H);

        // Text color based on theme
        g.setColor(ThemeManager.getCellText(isSelected));
        g.drawString(grid.getCell(r, c).getValue().display(), x + 5, y + 17);
      }
    }

    String statusText = baseStatusText() + selectionSuffix();

    statusBar.draw(g, getWidth(), getHeight(), input.getMode(), statusText);
  }

  private String baseStatusText() {
    return switch (input.getMode()) {
      case EDIT -> editor.valueWithCursor();
      case NAVIGATE, SELECT -> Util.intToColumnLabel(cursor.col) +
      Util.intToRowLabel(cursor.row);
    };
  }

  private String selectionSuffix() {
    if (!selectionManager.hasSelection()) return "";

    return (
      " (" +
      Util.cellAddressToLabel(selectionManager.getAnchor()) +
      ":" +
      Util.cellAddressToLabel(selectionManager.getLimit()) +
      ")"
    );
  }
}
