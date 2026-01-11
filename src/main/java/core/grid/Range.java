package core.grid;

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
}
