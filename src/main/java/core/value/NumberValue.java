package core.value;

public record NumberValue(double value) implements Value {

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String display() {
        return Double.toString(value);
    }
}
