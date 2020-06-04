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

    public char currentChar() throws IOException {
        return source.currentChar();
    }

    public char nextChar() throws IOException {
        return source.nextChar();
    }
}
