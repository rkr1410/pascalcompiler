package net.rkr1410.pascal.frontend.tokens;

import net.rkr1410.lang.frontend.Source;
import net.rkr1410.pascal.frontend.PascalTokenType;

import java.io.IOException;

public class PascalWordToken extends PascalToken {

    /**
     * Constructor
     *
     * @param source source to fetch token characters from
     * @throws IOException if an I/O error occurs
     */
    public PascalWordToken(Source source) throws IOException {
        super(source);
    }

    /**
     * Extract a Pascal word from the source
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void extract() throws IOException {
        StringBuilder sb = new StringBuilder();
        char c = currentChar();

        while (Character.isLetterOrDigit(c)) {
            sb.append(c);
            c = nextChar();
        }

        this.text = sb.toString();
        this.tokenType = PascalTokenType.isReservedWord(this.text)
                ? PascalTokenType.valueOf(this.text.toUpperCase())
                : PascalTokenType.IDENTIFIER;
    }
}
