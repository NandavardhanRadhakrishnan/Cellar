package core.grid;

import lombok.Data;

import java.util.Objects;

@Data
public final class CellAddress {

    private final int row;
    private final int col;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellAddress other)) return false;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode(){
        return Objects.hash(row, col);
    }

    @Override
    public String toString(){
        return "(" + row + ", " + col + ")";
    }

}
