package core.grid.selection;

import core.grid.CellAddress;
import core.grid.Range;

public record RangeSelection(Range range) implements Selection {
    @Override
    public boolean contains(CellAddress address) {
        return range.contains(address);
    }

    @Override
    public boolean contains(int row, int col) {
        return contains(new CellAddress(row,col));
    }

    @Override
    public Iterable<CellAddress> getSelectedCellAddresses() {
        return range.getAllAddresses();
    }

    @Override
    public Iterable<Iterable<CellAddress>> getRows() {
        return range.getRows();
    }
}
