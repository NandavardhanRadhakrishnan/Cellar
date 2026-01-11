package core.grid.selection;

import core.grid.CellAddress;
import core.grid.Range;
import lombok.Getter;

@Getter
public class SelectionManager {

    private CellAddress anchor;
    private CellAddress limit;
    private Selection selection;

    public void startSelection(CellAddress start) {
        this.anchor = start;
        this.limit = start;
        this.selection = new CellSelection(start);
    }

    public void update(CellAddress cursor) {
        this.limit = cursor;

        if (anchor.equals(cursor)) {
            this.selection = new CellSelection(anchor);
        } else {
            this.selection = new RangeSelection(
                    new Range(anchor, cursor)
            );
        }
    }

    public boolean hasSelection() {
        return selection != null;
    }

    public boolean isSingleCell() {
        return anchor != null && anchor.equals(limit);
    }

    public void clear() {
        anchor = null;
        limit = null;
        selection = null;
    }
}
