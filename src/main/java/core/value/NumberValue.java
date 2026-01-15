package core.value;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@lombok.Value
public class NumberValue implements Value {
    double value;


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String display() {
        return Double.toString(value);
    }
}
