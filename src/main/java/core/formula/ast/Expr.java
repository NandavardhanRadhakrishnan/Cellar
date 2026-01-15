package core.formula.ast;

import core.grid.CellAddress;

import java.util.Set;

public sealed interface Expr permits NumberExpr, CellRefExpr, BinaryOpExpr {

    Set<CellAddress> dependencies();
}
