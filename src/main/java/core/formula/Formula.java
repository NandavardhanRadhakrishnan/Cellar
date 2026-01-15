package core.formula;

import core.eval.EvalContext;
import core.grid.CellAddress;
import core.value.Value;

import java.util.Set;

public interface Formula {
    Value evaluate(EvalContext ctx);
    Set<CellAddress> dependencies();
}
