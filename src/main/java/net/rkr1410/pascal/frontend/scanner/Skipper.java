package net.rkr1410.pascal.frontend.scanner;

import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.frontend.Token;
import net.rkr1410.pascal.frontend.PascalErrorCode;
import net.rkr1410.pascal.frontend.tokens.PascalErrorToken;

import java.io.IOException;
import java.util.Objects;

import static net.rkr1410.lang.frontend.Source.EOF;

public class Skipper {

    enum SkipCommentOutcome {
        COMMENT_SKIPPED, COMMENT_NOT_FOUND, MISSING_CLOSING_BRACE
    }

    private Source source;

    Skipper(Source source) {
        this.source = Objects.requireNonNull(source);
    }

    Token skipWhitespaceAndComments() throws IOException {
        while (isWhitespaceOrComment()) {
            switch (skipComment()) {
                // this is whitespace, skip it and continue loop
                case COMMENT_NOT_FOUND:
                    source.nextChar();
                    break;
                // Comment was opened, but never closed
                case MISSING_CLOSING_BRACE:
                    return new PascalErrorToken(source, PascalErrorCode.UNEXPECTED_EOF, "");
                // Skipped comment, continue loop
                case COMMENT_SKIPPED:
                default:
                    // NOOP
            }
        }
        return null;
    }

    private boolean isWhitespaceOrComment() throws IOException {
        char c = source.currentChar();
        return Character.isWhitespace(c) || isCommentStart(c);
    }

    private SkipCommentOutcome skipComment() throws IOException {//}, ParseException {
        char c = source.currentChar();
        if (isCommentStart(c)) {
            do {
                c = source.nextChar();
            } while (!isCommentEnd(c) && c != EOF);

            if (isCommentEnd(c)) {
                source.nextChar(); // eat closing brace
            } else {
                return SkipCommentOutcome.MISSING_CLOSING_BRACE;
            }
            return SkipCommentOutcome.COMMENT_SKIPPED;
        }
        return SkipCommentOutcome.COMMENT_NOT_FOUND;
    }

    private boolean isCommentStart(char c) {
        return c == '{';
    }

    private boolean isCommentEnd(char c) {
        return c == '}';
    }
}
