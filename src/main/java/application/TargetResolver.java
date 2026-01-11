package application;

import core.grid.Cell;
import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.Selection;
import core.grid.selection.SelectionManager;
import lombok.Data;
import ui.Cursor;

import java.util.function.Consumer;

@Data
public final class TargetResolver {

    private final Grid grid;
    private final Cursor cursor;
    private final SelectionManager selectionManager;

    public void forEachCell(Consumer<Cell> action) {
        if (selectionManager.hasSelection()){
            Selection selection = selectionManager.getSelection();
            for (CellAddress cellAddress: selection.getSelectedCellAddresses()){
                action.accept(grid.getCell(cellAddress));
            }
        } else {
            action.accept(grid.getCell(cursor.row, cursor.col));
        }
    }

    public CellAddress primaryCell(){
        return selectionManager.hasSelection()
                ? selectionManager.getAnchor()
                : new CellAddress(cursor.row, cursor.col);
    }
}
