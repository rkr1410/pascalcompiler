package net.rkr1410.pascal.frontend.scanner;

import net.rkr1410.lang.frontend.EofToken;
import net.rkr1410.lang.frontend.Scanner;
import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.frontend.Token;
import net.rkr1410.pascal.frontend.PascalErrorCode;
import net.rkr1410.pascal.frontend.PascalTokenType;
import net.rkr1410.pascal.frontend.tokens.PascalErrorToken;
import net.rkr1410.pascal.frontend.tokens.PascalStringToken;
import net.rkr1410.pascal.frontend.tokens.PascalWordToken;

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

        Token token;
        char c = currentChar();
        if (c == EOF) {
            token = new EofToken(source);
        } else if (Character.isLetter(c)) {
            token = new PascalWordToken(source);
/*
        } else if (Character.isDigit(c)) {
            // token = new PascalNumberToken(source);

 */
        } else if (c == '\'') {
            token = new PascalStringToken(source);
        }
        //todo it's wrong, keys there are multi-char strings
        //else if (PascalTokenType.isSpecialSymbol()) {
        else {
            nextChar();
            token = new PascalErrorToken(source, PascalErrorCode.INVALID_CHARACTER, Character.toString(c));
        }

        //todo or not? extracting token should put as at next char already?
        //nextChar(); // consume character

        return token;
    }


}
