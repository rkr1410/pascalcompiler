package net.rkr1410.pascal.frontend;

import net.rkr1410.lang.frontend.TokenType;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h1>PascalTokenType</h1>
 *
 * <p>Pascal token types
 */
public enum PascalTokenType implements TokenType {
    /* reserved words */
    AND, ARRAY, BEGIN, CASE, CONST, DIV, DO,
    DOWNTO, ELSE, END, FILE, FOR, FUNCTION, GOTO,
    IF, IN, LABEL, MOD, NIL, NOT, OF, OR,
    PACKED, PROCEDURE, PROGRAM, RECORD, REPEAT, SET, THEN,
    TO, TYPE, UNTIL, VAR, WHILE, WITH,

    /* Special symbols */
    PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"), COLON_EQUALS(":="),
    DOT("."), COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("'"),
    EQUALS("="), NOT_EQUALS("<>"), LESS_THAN("<"), LESS_EQUALS("<="),
    GREATER_EQUALS(">="), GREATER_THAN(">"), LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACKET("["),
    RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"), UP_ARROW("^"), DOT_DOT(".."),
    IDENTIFIER, INTEGER, REAL, STRING, ERROR, END_OF_FILE;

    private static final int FIRST_RESERVED_INDEX = AND.ordinal();
    private static final int LAST_RESERVED_INDEX  = WITH.ordinal();
    private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
    private static final int LAST_SPECIAL_INDEX  = DOT_DOT.ordinal();

    /** Set of uppercase reserved words */
    private static Set<String> RESERVED_WORDS;
    /** Special symbols mapped to token types, e.g. ">=":GREATER_EQUALS*/
    private static Map<String, PascalTokenType> SPECIAL_SYMBOLS;

    static {
        RESERVED_WORDS = Arrays.asList(values())
                .subList(FIRST_RESERVED_INDEX, LAST_RESERVED_INDEX).stream()
                .map(PascalTokenType::name).collect(Collectors.toSet());
        SPECIAL_SYMBOLS = Arrays.asList(values())
                .subList(FIRST_SPECIAL_INDEX, LAST_SPECIAL_INDEX).stream()
                .collect(Collectors.toMap(PascalTokenType::getText, ptt -> ptt));
    }

    private String text;

    PascalTokenType() {
        // NOOP
    }

    PascalTokenType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


}
