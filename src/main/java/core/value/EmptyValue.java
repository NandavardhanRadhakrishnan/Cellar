package core.value;

public record EmptyValue() implements Value {
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String display() {
        return "";
    }
}
