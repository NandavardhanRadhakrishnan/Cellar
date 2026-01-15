package core.formula;

import core.eval.Evaluator;
import core.formula.ast.Expr;
import core.formula.parser.FormulaParser;
import core.formula.parser.Lexer;
import core.formula.parser.Token;
import core.value.*;

import java.util.List;

public final class FormulaEngine {

    private final Evaluator evaluator = new Evaluator();

    public Value parseValue(String input) {

        if (input == null || input.isBlank()) {
            return null;
        }

        if (input.startsWith("=")) {
            String src = input.substring(1);
            List<Token> tokens = new Lexer(src).tokenize();
            Expr expr = new FormulaParser(tokens).parse();
            Formula formula = new ExprFormula(expr, evaluator);
            return new FormulaValue(input, formula);
        }

        // try number
        try {
            return new NumberValue(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            return new StringValue(input);
        }
    }

    public Evaluator evaluator() {
        return evaluator;
    }
}
