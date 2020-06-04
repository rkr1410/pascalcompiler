package net.rkr1410.pascal;

import net.rkr1410.lang.frontend.EofToken;
import net.rkr1410.lang.frontend.Scanner;
import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.frontend.Token;

import java.io.IOException;

public class STDPScanner extends Scanner {
    /**
     * Constructor
     *
     * @param source source to use with this Sccaner
     */
    public STDPScanner(Source source) {
        super(source);
    }

    @Override
    protected Token extractToken() throws IOException {
        if (currentChar() == Source.EOF) {
            return new EofToken(source);
        }

        return new Token(source);
    }
}
