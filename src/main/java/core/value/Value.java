package core.value;

public sealed interface Value permits NumberValue, StringValue, ErrorValue, EmptyValue, FormulaValue {

    boolean isEmpty();

    String display();
}
