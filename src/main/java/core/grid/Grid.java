package core.grid;

import core.eval.Evaluator;
import core.eval.GridEvalContext;
import core.value.StringValue;
import core.value.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Grid {

    private Map<CellAddress, Cell> cells = new HashMap<>();
    public final int rows;
    public final int cols;

    public Cell getCell(CellAddress address) {
        return cells.computeIfAbsent(address, addr -> new Cell(new StringValue("")));
    }

    public Cell getCell(int row, int col){
        return getCell(new CellAddress(row, col));
    }

    public void setCell(CellAddress address, Value value){
        getCell(address).setRaw(value);
    }

    public void setCell(int row, int col, Value value){
        setCell(new CellAddress(row, col), value);
    }

    public Value getValue(CellAddress address){
        return getCell(address).getValue();
    }

    public void clearComputedValues(){
        cells.values().forEach(cell -> cell.setComputed(null));
    }

    public void recalculateAll(Evaluator evaluator) {
        clearComputedValues();
        GridEvalContext ctx = new GridEvalContext(this, evaluator);
        for (CellAddress cellAddress: cells.keySet()){
            ctx.resolve(cellAddress);
        }
    }
}
