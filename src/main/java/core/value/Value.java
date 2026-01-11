package core.value;

public sealed interface Value permits NumberValue, StringValue, ErrorValue, EmptyValue {

    boolean isEmpty();

    String display();
}
