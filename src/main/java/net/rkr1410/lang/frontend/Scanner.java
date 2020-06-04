package net.rkr1410.lang.frontend;

import java.io.IOException;

/**
 * <h1>Scanner</h1>
 *
 * <p>A language-independent framework class.
 * To be implemented by language-specific subclasses.</p>
 */
public abstract class Scanner {

    protected Source source;
    private Token currentToken;

    /**
     * Constructor
     *
     * @param source source to use with this Scanner
     */
    public Scanner(Source source) {
        this.source = source;
    }

    /**
     * @return current token
     */
    public Token currentToken() {
        return currentToken;
    }

    /**
     * @return next token from source
     * @throws IOException if an I/O error occurs
     */
    public Token nextToken() throws IOException {
        currentToken = extractToken();
        return currentToken();
    }

    /**
     * Extract token from source.
     * To be implemented by language-specific subclasses.
     *
     * @return extracted token
     * @throws IOException if an I/O error occurs
     */
    protected abstract Token extractToken() throws IOException;

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
}
