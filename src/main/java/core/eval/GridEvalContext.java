package core.eval;

import core.grid.Cell;
import core.grid.CellAddress;
import core.grid.Grid;
import core.value.EmptyValue;
import core.value.ErrorValue;
import core.value.FormulaValue;
import core.value.Value;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class GridEvalContext implements EvalContext {

    private final Grid grid;
    private final Evaluator evaluator;
    private final Set<CellAddress> visiting = new HashSet<>();

    @Override
    public Value resolve(CellAddress address) {

        if (visiting.contains(address)) {
            return new ErrorValue("#CYCLE!");
        }

        Cell cell = grid.getCell(address);
        if (cell == null) {
            return new EmptyValue();
        }

        Value raw = cell.getRaw();
        if (raw == null || raw instanceof EmptyValue) {
            return new EmptyValue();
        }

        if (!(raw instanceof FormulaValue formula)) {
            return raw;
        }

        if (cell.getComputed() != null) {
            return cell.getComputed();
        }

        visiting.add(address);
        Value result = formula.getFormula().evaluate(this);
        visiting.remove(address);

        cell.setComputed(result);
        return result;
    }

}
