package core.formula.op;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BinaryOp {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/");

    private final String symbol;
}
