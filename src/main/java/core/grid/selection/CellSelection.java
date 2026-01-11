package core.grid.selection;

import core.grid.CellAddress;

import java.util.Collections;
import java.util.List;

public record CellSelection(CellAddress cellAddress) implements Selection {
    @Override
    public boolean contains(CellAddress address) {
        return cellAddress.equals(address);
    }

    @Override
    public boolean contains(int row, int col) {
        return contains(new CellAddress(row, col));
    }

    @Override
    public Iterable<CellAddress> getSelectedCellAddresses() {
        return Collections.singleton(cellAddress);
    }

}
