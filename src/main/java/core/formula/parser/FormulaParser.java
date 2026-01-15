package core.formula.parser;

import core.formula.ast.*;
import core.formula.op.BinaryOp;
import lombok.RequiredArgsConstructor;
import util.Util;

import java.util.List;

//expression  → term ((+ | -) term)*
//term        → factor ((* | /) factor)*
//factor      → NUMBER
//            | CELL_REF
//            | "(" expression ")"


@RequiredArgsConstructor
public final class FormulaParser {

    private final List<Token> tokens;
    private int current = 0;


    public Expr parse() {
        Expr expr = expression();
        consume(TokenType.EOF, "Unexpected input after expression");
        return expr;
    }

    // expression → term ((+ | -) term)*
    private Expr expression() {
        Expr expr = term();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token op = previous();
            Expr right = term();
            expr = new BinaryOpExpr(expr, toBinaryOp(op), right);
        }

        return expr;
    }

    // term → factor ((* | /) factor)*
    private Expr term() {
        Expr expr = factor();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token op = previous();
            Expr right = factor();
            expr = new BinaryOpExpr(expr, toBinaryOp(op), right);
        }

        return expr;
    }

    // factor → NUMBER | CELL | "(" expression ")"
    private Expr factor() {
        if (match(TokenType.NUMBER)) {
            return new NumberExpr(Double.parseDouble(previous().lexeme()));
        }

        if (match(TokenType.CELL)) {
            return new CellRefExpr(Util.labelToCellAddress(previous().lexeme()));
        }

        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Expected ')'");
            return expr;
        }

        throw error("Expected expression");
    }


    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        return peek().type() == type;
    }

    private Token advance() {
        return tokens.get(current++);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private void consume(TokenType type, String msg) {
        if (check(type)) {
            advance();
            return;
        }
        throw error(msg);
    }

    private RuntimeException error(String msg) {
        return new RuntimeException(msg + " at token " + peek());
    }

    private BinaryOp toBinaryOp(Token token) {
        return switch (token.type()) {
            case PLUS  -> BinaryOp.ADD;
            case MINUS -> BinaryOp.SUB;
            case STAR  -> BinaryOp.MUL;
            case SLASH -> BinaryOp.DIV;
            default -> throw new IllegalStateException("Invalid operator");
        };
    }
}
