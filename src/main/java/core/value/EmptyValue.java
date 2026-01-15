package core.value;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@lombok.Value
public final class EmptyValue implements Value {

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String display() {
        return "";
    }
}
