package net.rkr1410.pascal.frontend.scanner;

import net.rkr1410.lang.frontend.EofToken;
import net.rkr1410.lang.frontend.Scanner;
import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.frontend.Token;

import java.io.IOException;

import static net.rkr1410.lang.frontend.Source.EOF;

public class STDPScanner extends Scanner {

    private Skipper skipper;

    /**
     * Constructor
     *
     * @param source source to use with this Sccaner
     */
    public STDPScanner(Source source) {
        super(source);
        this.skipper = new Skipper(source);
    }

    @Override
    protected Token extractToken() throws IOException {
        Token parseError = skipper.skipWhitespaceAndComments();
        if (parseError != null) {
            return parseError;
        }

        if (currentChar() == EOF) {
            return new EofToken(source);
        }

        return new Token(source);
    }


}
