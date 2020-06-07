package net.rkr1410.pascal.frontend.tokens;

import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.frontend.Token;

import java.io.IOException;

public class PascalToken extends Token {

    /**
     * Constructor
     *
     * @param source source to fetch token characters from
     * @throws IOException if an I/O error occurs
     */
    public PascalToken(Source source) throws IOException {
        super(source);
    }
}
