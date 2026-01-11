package core.grid;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Range {
    private final CellAddress start;
    private final CellAddress end;

    public List<CellAddress> cells() {
        List<CellAddress> cellAddresses = new ArrayList<>();
        for (int r = start.getRow(); r < end.getRow(); r++) {
            for (int c = start.getCol(); c < end.getCol(); c++) {
                cellAddresses.add(new CellAddress(r,c));
            }
        }
        return cellAddresses;
    }
}
