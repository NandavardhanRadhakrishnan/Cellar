package core.formula.parser;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Lexer {

    private final String input;
    private int pos = 0;


    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            char c = peek();

            if (Character.isWhitespace(c)) {
                advance();
                continue;
            }

            if (Character.isDigit(c)) {
                tokens.add(number());
                continue;
            }

            if (Character.isLetter(c)) {
                tokens.add(cellRef());
                continue;
            }

            switch (c) {
                case '+' -> tokens.add(simple(TokenType.PLUS));
                case '-' -> tokens.add(simple(TokenType.MINUS));
                case '*' -> tokens.add(simple(TokenType.STAR));
                case '/' -> tokens.add(simple(TokenType.SLASH));
                case '(' -> tokens.add(simple(TokenType.LPAREN));
                case ')' -> tokens.add(simple(TokenType.RPAREN));
                default -> throw new RuntimeException("Unexpected char: " + c);
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private Token simple(TokenType type) {
        char c = advance();
        return new Token(type, String.valueOf(c));
    }

    private Token number() {
        int start = pos;
        while (!isAtEnd() && (Character.isDigit(peek()) || peek() == '.')) {
            advance();
        }
        return new Token(TokenType.NUMBER, input.substring(start, pos));
    }

    private Token cellRef() {
        int start = pos;
        while (!isAtEnd() && Character.isLetter(peek())) advance();
        while (!isAtEnd() && Character.isDigit(peek())) advance();
        return new Token(TokenType.CELL, input.substring(start, pos));
    }

    private char advance() {
        return input.charAt(pos++);
    }

    private char peek() {
        return input.charAt(pos);
    }

    private boolean isAtEnd() {
        return pos >= input.length();
    }

}
