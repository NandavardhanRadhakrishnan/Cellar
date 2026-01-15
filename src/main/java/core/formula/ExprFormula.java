package core.formula;

import core.eval.EvalContext;
import core.eval.Evaluator;
import core.formula.ast.Expr;
import core.grid.CellAddress;
import core.value.Value;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class ExprFormula implements Formula {

    private final Expr expr;
    private final Evaluator evaluator;

    @Override
    public Value evaluate(EvalContext ctx) {
        return evaluator.evaluate(expr, ctx);
    }

    @Override
    public Set<CellAddress> dependencies() {
        return expr.dependencies();
    }
}
