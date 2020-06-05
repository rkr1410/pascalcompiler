package net.rkr1410.lang.frontend;

import java.io.IOException;

/**
 * <h1>Token</h1>
 *
 * <p>Represents a token returned by {@link Scanner}
 */
public class Token {
    protected TokenType tokenType;
    protected String text;
    protected Object value;

    protected Source source;
    protected int lineNumber;
    protected int linePosition;

    /**
     * Constructor
     *
     * Extracts a token from <code>Source</code>
     *
     * @param source source to fetch token characters from
     * @throws IOException if an I/O error occurs
     */
    public Token(Source source) throws IOException {
        this.source = source;
        this.lineNumber = source.getLineNumber();
        this.linePosition = source.getLinePosition();

        extract();
    }

    /**
     * Default implementation, extracting single-character tokens. To be overrided
     * by subclasses to construct language-specific tokens. After extraction, the current
     * source position will be one charavter after the token.
     *
     * @throws IOException if an I/O error occurs
     */
    protected void extract() throws IOException {
        text = Character.toString(currentChar());
        value = null;
        nextChar();
    }

    /**
     * Returns character from source at current position, forwards
     * the call to <code>Source</code>.
     *
     * @return current character from the source
     * @throws IOException if an I/O error occurs
     */
    protected char currentChar() throws IOException {
        return source.currentChar();
    }

    /**
     * Returns next character from source, forwards
     * the call to <code>Source</code>.
     *
     * @return next character from the source, after moving forward
     * @throws IOException if an I/O error occurs
     */
    protected char nextChar() throws IOException {
        return source.nextChar();
    }

    /**
     * @return number of source line which this token was extracted from
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return source line offset at which this token was extracted
     */
    public int getLinePosition() {
        return linePosition;
    }

    /**
     * @return text from which this token was constructed
     */
    public String getText() {
        return text;
    }

    /**
     * @return TokenType of this token
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Returns value associated with this token.
     * <p>Tokens can have an associated value, e.g. an error token can have
     * an error code.
     *
     * @return token value
     */
    public Object getValue() {
        return value;
    }
}
