package net.rkr1410.lang.frontend;

import java.io.IOException;

/**
 * <h1>EofToken</h1>
 *
 * <p>A generic end-of-file token
 */
public class EofToken extends Token implements TokenType {

    /**
     * Constructor
     * <p>
     * Extracts a token from <code>Source</code>
     *
     * @param source source to fetch token characters from
     * @throws IOException if an I/O error occurs
     */
    public EofToken(Source source) throws IOException {
        super(source);
    }

    /**
     * A no-op. Does not consume any characters from source.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void extract() throws IOException {
        // NOOP
    }
}
