package core.eval;

import core.formula.ast.BinaryOpExpr;
import core.formula.ast.CellRefExpr;
import core.formula.ast.Expr;
import core.formula.ast.NumberExpr;
import core.formula.op.BinaryOp;
import core.value.ErrorValue;
import core.value.NumberValue;
import core.value.Value;

public final class Evaluator {

    public Value evaluate(Expr expr, EvalContext ctx) {

        if (expr instanceof NumberExpr n){
            return new NumberValue(n.value());
        }

        if (expr instanceof CellRefExpr c){
            return ctx.resolve(c.cellAddress());
        }

        if (expr instanceof BinaryOpExpr b) {
            Value left  = evaluate(b.left(), ctx);
            Value right = evaluate(b.right(), ctx);
            return applyBinary(b.op(), left, right);
        }

        throw new IllegalStateException("Unknown expression: " + expr);
    }

    private Value applyBinary(BinaryOp op, Value l, Value r) {
        if (l instanceof ErrorValue) return l;
        if (r instanceof ErrorValue) return r;
        if (!(l instanceof NumberValue ln) ||
                !(r instanceof NumberValue rn)) {
            return new ErrorValue("#VALUE!");
        }

        double a = ln.getValue();
        double b = rn.getValue();

        return switch (op) {
            case ADD -> new NumberValue(a + b);
            case SUB -> new NumberValue(a - b);
            case MUL -> new NumberValue(a * b);
            case DIV -> b == 0
                    ? new ErrorValue("#DIV/0!")
                    : new NumberValue(a / b);
        };
    }
}
