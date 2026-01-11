package core.value;

public record StringValue(String value) implements Value {


    @Override
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public String display() {
        return value;
    }
}
