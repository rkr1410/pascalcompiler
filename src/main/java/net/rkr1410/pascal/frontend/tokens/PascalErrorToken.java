package net.rkr1410.pascal.frontend.tokens;

import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.frontend.Token;
import net.rkr1410.pascal.frontend.PascalErrorCode;
import net.rkr1410.pascal.frontend.PascalTokenType;

import java.io.IOException;

/**
 * <h1>PascalErrorToken</h1>
 *
 * A Pascal error token
 */
public class PascalErrorToken extends Token {

    /**
     * Constructor
     * <p>
     * Extracts a token from <code>Source</code>
     *
     * @param source source to fetch token characters from
     * @throws IOException if an I/O error occurs
     */
    public PascalErrorToken(Source source, PascalErrorCode errorCode, String tokenText) throws IOException {
        super(source);
        this.text = tokenText;
        this.tokenType = PascalTokenType.ERROR;
        this.value = errorCode;
    }

    /**
     * A no-op, doesn't consume any characters
     *
     * @throws IOException if an I/O exception occurs
     */
    @Override
    protected void extract() throws IOException {
        // NOOP
    }
}
