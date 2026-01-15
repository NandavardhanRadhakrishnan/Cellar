package core.value;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@lombok.Value
public class StringValue implements Value {
    String value;


    @Override
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public String display() {
        return value;
    }
}
