package core.grid;

import core.value.EmptyValue;
import core.value.FormulaValue;
import core.value.Value;
import lombok.*;

@Data
@AllArgsConstructor
public class Cell {
    private Value raw;
    private Value computed;


    public Cell(Value raw) {
        this.raw = raw;
    }

    public Value getValue() {
        if (raw instanceof FormulaValue){
            return computed;
        }
        return raw;
    }

    public void setRaw(Value raw) {
        this.raw = raw;
        this.computed = null;
    }

    public void clear(){
        raw = new EmptyValue();
        computed = null;
    }
}
