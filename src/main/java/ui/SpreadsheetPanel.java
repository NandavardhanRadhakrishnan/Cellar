package ui;

import core.formula.FormulaEngine;
import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.Selection;
import core.grid.selection.SelectionManager;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.WindowSizeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ui.input.InputController;
import util.Util;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpreadsheetPanel implements Model {

  static final int CELL_W = 12;

  final Grid grid;
  final Cursor cursor;
  final CellEditor editor;
  final SelectionManager selectionManager;
  final InputController input;
  final FormulaEngine formulaEngine;

  final StatusBar statusBar = new StatusBar();

  int width = 80;
  int height = 24;

  @Override
  public Command init() {
    return null;
  }

  @Override
  public UpdateResult<? extends Model> update(Message msg) {
    if (msg instanceof WindowSizeMessage wsm) {
        this.width = wsm.width();
        this.height = wsm.height();
        return UpdateResult.from(this);
    }
    
    if (msg instanceof KeyPressMessage keyPressMessage) {
        if (keyPressMessage.key().equals("ctrl+c")) {
            return UpdateResult.from(this, QuitMessage::new);
        }
        
        input.handleKey(keyPressMessage);
        return UpdateResult.from(this);
    }
    
    return UpdateResult.from(this);
  }

  @Override
  public String view() {
    StringBuilder sb = new StringBuilder();
    
    int rowHeaderWidth = 4;
    int usableHeight = height - 1; // 1 row for status bar
    // We need 1 row for column headers and its divider
    int maxRows = Math.min((usableHeight - 2) / 2, grid.rows);
    if (maxRows < 0) maxRows = 0;
    
    Selection selection = selectionManager.getSelection();

    // Build horizontal divider
    StringBuilder divSb = new StringBuilder();
    divSb.append(ThemeManager.getGridBorder());
    divSb.append("+").append("-".repeat(rowHeaderWidth)).append("+");
    for (int c = 0; c < grid.cols; c++) {
        divSb.append("-".repeat(CELL_W)).append("+");
    }
    divSb.append(ThemeManager.getReset()).append("\n");
    String hDivider = divSb.toString();

    if (maxRows > 0) {
        // Render Column Headers
        sb.append(hDivider);
        sb.append(ThemeManager.getGridBorder()).append("|").append(" ".repeat(rowHeaderWidth)).append("|");
        for (int c = 0; c < grid.cols; c++) {
            String colLabel = Util.intToColumnLabel(c);
            int pad = (CELL_W - colLabel.length()) / 2;
            String formatted = String.format("%-" + CELL_W + "s", " ".repeat(pad) + colLabel);
            sb.append(formatted).append("|");
        }
        sb.append(ThemeManager.getReset()).append("\n");
        sb.append(hDivider);
    }

    for (int r = 0; r < maxRows; r++) {
      String rowLabel = Util.intToRowLabel(r);
      int rowPad = (rowHeaderWidth - rowLabel.length()) / 2;
      String formattedRowLabel = String.format("%-" + rowHeaderWidth + "s", " ".repeat(rowPad) + rowLabel);
      
      sb.append(ThemeManager.getGridBorder()).append("|").append(formattedRowLabel).append("|").append(ThemeManager.getReset());

      for (int c = 0; c < grid.cols; c++) {
        CellAddress addr = new CellAddress(r, c);

        boolean isCursor = cursor.row == r && cursor.col == c;
        boolean isSelected = selection != null && selection.contains(addr);

        String bg = ThemeManager.getCellBackground(isCursor, isSelected);
        String fg = ThemeManager.getCellText(isSelected);
        
        String val = grid.getCell(r, c).getValue().display();
        
        // Pad or truncate to CELL_W
        if (val.length() > CELL_W) {
            val = val.substring(0, CELL_W);
        }
        
        String content;
        if (isCursor) {
            // Use special markers for cursor: bold brackets
            if (val.length() > CELL_W - 2) {
                val = val.substring(0, CELL_W - 2);
            }
            content = ThemeManager.getCursorBorder() + ThemeManager.getBold() + "[" + ThemeManager.getBoldOff() + ThemeManager.getReset() + 
                      bg + fg + String.format("%-" + (CELL_W - 2) + "s", val) + ThemeManager.getReset() + 
                      ThemeManager.getCursorBorder() + ThemeManager.getBold() + "]" + ThemeManager.getBoldOff() + ThemeManager.getReset();
        } else {
            content = bg + fg + String.format("%-" + CELL_W + "s", val) + ThemeManager.getReset();
        }
        
        sb.append(content).append(ThemeManager.getGridBorder()).append("|").append(ThemeManager.getReset());
      }
      sb.append("\n");
      sb.append(hDivider);
    }
    // Fill remaining rows if needed
    int linesUsed = maxRows > 0 ? 2 + (maxRows * 2) : 0;
    for (int r = linesUsed; r < usableHeight; r++) {
        sb.append("\n");
    }

    String statusText = baseStatusText() + selectionSuffix();
    sb.append(statusBar.render(width, input.getMode(), statusText));

    return sb.toString();
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
