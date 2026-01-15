package core.eval;

import core.grid.CellAddress;
import core.value.Value;

public interface EvalContext {
    Value resolve(CellAddress address);
}
