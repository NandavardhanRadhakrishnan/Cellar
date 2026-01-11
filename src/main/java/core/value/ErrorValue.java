package core.value;

public record ErrorValue(String message) implements Value {

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String display() {
        return "#ERR: " + message;
    }
}
