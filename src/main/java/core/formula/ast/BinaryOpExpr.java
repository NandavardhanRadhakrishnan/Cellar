package core.formula.ast;

import core.formula.op.BinaryOp;
import core.grid.CellAddress;

import java.util.HashSet;
import java.util.Set;

public record BinaryOpExpr(
        Expr left, BinaryOp op, Expr right
) implements Expr {

    @Override
    public Set<CellAddress> dependencies() {
        Set<CellAddress> deps = new HashSet<>();
        deps.addAll(left.dependencies());
        deps.addAll(right.dependencies());
        return deps;
    }
}
