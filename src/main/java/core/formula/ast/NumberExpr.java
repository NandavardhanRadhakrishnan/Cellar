package core.formula.ast;

import core.grid.CellAddress;

import java.util.Set;

public record NumberExpr(
        double value
) implements Expr {

    @Override
    public Set<CellAddress> dependencies() {
        return Set.of();
    }
}
