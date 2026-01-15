package core.value;

import core.formula.Formula;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@lombok.Value
public class FormulaValue implements Value {

    private final String source;
    private final Formula formula;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String display() {
        return source;
    }
}
