package net.rkr1410.pascal.frontend.tokens;

import net.rkr1410.lang.frontend.Source;
import net.rkr1410.pascal.frontend.PascalErrorCode;
import net.rkr1410.pascal.frontend.PascalTokenType;
import sun.text.normalizer.UCharacter;

import java.io.IOException;

import static net.rkr1410.lang.frontend.Source.EOF;

public class PascalStringToken extends PascalToken {

    /**
     * Constructor
     *
     * @param source source to fetch token characters from
     * @throws IOException if an I/O error occurs
     */
    public PascalStringToken(Source source) throws IOException {
        super(source);
    }

    /**
     * Extract a string from the source
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void extract() throws IOException {
        StringBuilder textBuffer = new StringBuilder("'");
        StringBuilder valueBuffer = new StringBuilder();
        // eat starting quote
        nextChar();

        do  {
            appendAnyRegularCharacter(valueBuffer, textBuffer);
            appendAnyEscapedQuotes(valueBuffer, textBuffer);
        } while (currentChar() != '\'' && currentChar() != EOF);

        if (currentChar() == '\'') {
            finalizeStringRead(valueBuffer, textBuffer);
        } else {
            tokenType = PascalTokenType.ERROR;
            value = PascalErrorCode.UNEXPECTED_EOF;
        }

        text = textBuffer.toString();
    }

    private void finalizeStringRead(StringBuilder valueBuffer, StringBuilder textBuffer) throws IOException {
        nextChar();
        textBuffer.append('\'');
        tokenType = PascalTokenType.STRING;
        value = valueBuffer.toString();
    }

    private void appendAnyRegularCharacter(StringBuilder valueBuffer, StringBuilder textBuffer) throws IOException {
        char c = anyWhitespaceToSingleSpace();

        if (c != '\'' && c != EOF) {
            textBuffer.append(c);
            valueBuffer.append(c);
            nextChar();
        }
    }

    private char anyWhitespaceToSingleSpace() throws IOException {
        char c = currentChar();
        if (Character.isWhitespace(c)) {
            return ' ';
        }
        return c;
    }

    private void appendAnyEscapedQuotes(StringBuilder valueBuffer, StringBuilder textBuffer) throws IOException {
        if (currentChar() == '\'') {
            while (currentChar() == '\'' && peekChar() == '\'') {
                textBuffer.append("''");
                valueBuffer.append("'");
                nextChar();
                nextChar();
            }
        }
    }
}
