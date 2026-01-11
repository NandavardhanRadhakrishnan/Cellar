package core.grid;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Range {

    private final int r1, c1, r2, c2;

    public Range(CellAddress a, CellAddress b) {
        this.r1 = Math.min(a.getRow(), b.getRow());
        this.c1 = Math.min(a.getCol(), b.getCol());
        this.r2 = Math.max(a.getRow(), b.getRow());
        this.c2 = Math.max(a.getCol(), b.getCol());
    }

    public boolean contains(CellAddress addr) {
        return addr.getRow() >= r1 && addr.getRow() <= r2
                && addr.getCol() >= c1 && addr.getCol() <= c2;
    }

    public boolean contains(int row, int col) {
        return row >= r1 && row <= r2
                && col >= c1 && col <= c2;
    }

    public Iterable<CellAddress> getAllAddresses() {
        return () -> new Iterator<>() {

            private int row = r1;
            private int col = c1;

            @Override
            public boolean hasNext() {
                return row <= r2 && col <= c2;
            }

            @Override
            public CellAddress next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }

                CellAddress cellAddress = new CellAddress(row, col);

                col++;

                if (col > c2) {
                    col = c1;
                    row++;
                }

                return cellAddress;
            }
        };
    }
}
