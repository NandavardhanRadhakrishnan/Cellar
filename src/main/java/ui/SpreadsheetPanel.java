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
    
    int usableHeight = height - 1; // 1 row for status bar
    int maxRows = Math.min((usableHeight - 1) / 2, grid.rows);
    if (maxRows < 0) maxRows = 0;
    
    Selection selection = selectionManager.getSelection();

    // Build horizontal divider
    StringBuilder divSb = new StringBuilder();
    divSb.append(ThemeManager.getGridBorder());
    divSb.append("+");
    for (int c = 0; c < grid.cols; c++) {
        divSb.append("-".repeat(CELL_W)).append("+");
    }
    divSb.append(ThemeManager.getReset()).append("\n");
    String hDivider = divSb.toString();

    if (maxRows > 0) {
        sb.append(hDivider);
    }

    for (int r = 0; r < maxRows; r++) {
      for (int c = 0; c < grid.cols; c++) {
        CellAddress addr = new CellAddress(r, c);

        boolean isCursor = cursor.row == r && cursor.col == c;
        boolean isNextCursor = cursor.row == r && cursor.col == c + 1;
        boolean isSelected = selection != null && selection.contains(addr);

        if (c == 0) {
            if (isCursor) {
                sb.append(ThemeManager.getCursorBorder()).append("[").append(ThemeManager.getReset());
            } else {
                sb.append(ThemeManager.getGridBorder()).append("|").append(ThemeManager.getReset());
            }
        }

        String bg = ThemeManager.getCellBackground(isCursor, isSelected);
        String fg = ThemeManager.getCellText(isSelected);
        
        String val = grid.getCell(r, c).getValue().display();
        
        // Pad or truncate to CELL_W - 2
        if (val.length() > CELL_W - 2) {
            val = val.substring(0, CELL_W - 2);
        }
        
        String valPadded = String.format("%-" + (CELL_W - 2) + "s", val);
        
        sb.append(bg).append(fg).append(" ").append(valPadded).append(" ").append(ThemeManager.getReset());
        
        // Right border logic
        if (isCursor) {
            sb.append(ThemeManager.getCursorBorder()).append("]").append(ThemeManager.getReset());
        } else if (isNextCursor) {
            sb.append(ThemeManager.getCursorBorder()).append("[").append(ThemeManager.getReset());
        } else {
            sb.append(ThemeManager.getGridBorder()).append("|").append(ThemeManager.getReset());
        }
      }
      sb.append("\n");
      sb.append(hDivider);
    }
    // Fill remaining rows if needed
    int linesUsed = maxRows > 0 ? 1 + (maxRows * 2) : 0;
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
