package core.grid.selection;

import core.grid.CellAddress;

public sealed interface Selection permits CellSelection, RangeSelection {

    boolean contains(CellAddress address);
    boolean contains(int row, int col);

    Iterable<CellAddress> getSelectedCellAddresses();
}
