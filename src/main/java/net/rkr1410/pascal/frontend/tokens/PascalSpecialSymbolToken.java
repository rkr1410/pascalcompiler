package net.rkr1410.pascal.frontend.tokens;

import net.rkr1410.lang.frontend.Source;
import net.rkr1410.pascal.frontend.PascalErrorCode;
import net.rkr1410.pascal.frontend.PascalTokenType;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PascalSpecialSymbolToken extends PascalToken {

    /**
     * Constructor
     *
     * @param source source to fetch token characters from
     * @throws IOException if an I/O error occurs
     */
    public PascalSpecialSymbolToken(Source source) throws IOException {
        super(source);
    }

    /**
     * Extract a Pascal special symbol from the source
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void extract() throws IOException {
        String charAsString = Character.toString(currentChar());
        this.text = charAsString;

        if (PascalTokenType.isExclusivelySingleCharacterSpecialSymbol(currentChar())) {
            createSingleCharacterToken(charAsString);
        } else {
            createPossiblyTwoCharacterToken();
        }
    }

    private void createSingleCharacterToken(String charAsString) throws IOException {
        this.tokenType = PascalTokenType.getSpecialSymbol(charAsString);
        nextChar();
    }

    private void createPossiblyTwoCharacterToken() throws IOException {
        boolean symbolFound = findSymbolFor(':', '=')
                || findSymbolFor('>', '=')
                || findSymbolFor('<', '=', '>')
                || findSymbolFor('.', '.');

        if (symbolFound) {
            this.tokenType = PascalTokenType.getSpecialSymbol(this.text);
        } else {
            this.tokenType = PascalTokenType.ERROR;
            this.value = PascalErrorCode.INVALID_CHARACTER;
            nextChar();
        }
    }

    private boolean findSymbolFor(char symbolInitialChar, Character... possibleFollowingChars) throws IOException {
        Set<Character> options = new HashSet<>(Arrays.asList(possibleFollowingChars));
        if (currentChar() == symbolInitialChar) {
            char c = nextChar();
            if (options.contains(c)) {
                this.text += Character.toString(c);
                nextChar();
            }
            return true;
        }
        return false;
    }
}
