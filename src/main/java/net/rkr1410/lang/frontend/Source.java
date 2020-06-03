package net.rkr1410.lang.frontend;

import net.rkr1410.util.IfElseChain;
import net.rkr1410.util.LearningExperience;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static net.rkr1410.util.RiskType.TOO_SPECIFIC;
import static net.rkr1410.util.Utils.sneakThrow;

/**
 * <h1>Source</h1>
 *
 * <p>A framework class representing the source program</p>
 */
public class Source {
    private static final int BEFORE_START_OF_FILE = -2;
    private static final int BEFORE_START_OF_LINE = -1;

    public static final char EOL = '\n';
    public static final char EOF = (char) 0;

    private BufferedReader reader;
    private String line;
    private int lineNumber;
    private int lineOffset;

    /**
     * Constructor
     *
     * @param reader the reader for source program
     * @throws IOException if an I/O error occurred
     */
    public Source(BufferedReader reader) throws IOException {
        this.reader = reader;
        this.lineNumber = 0;
        this.lineOffset = BEFORE_START_OF_FILE;
    }

    /**
     * Returns character from source at current position.
     *
     * @return source char at current position
     * @throws Exception if an error occurs
     */
    public char currentChar() throws Exception {
        return new IfElseChain<Character>()
                .ifThen(this::outsideLineBounds, sneakily(this::readLineAndNextCharacter))
                .ifThen(this::atEndOfFile, () -> EOF)
                .ifThen(this::atEndOfLine, () -> EOL)
                .elseDefault(() -> line.charAt(lineOffset));
    }

    /**
     * Returns next character from source. Current one is consumed
     * (calls to <code>currentChar()</code> will no longer return it,
     * returning this one instead)
     *
     * @return next source character
     * @throws Exception if an error occurs
     */
    public char nextChar() throws Exception {
        ++lineOffset;
        return currentChar();
    }

    /**
     * Returns next character from source. Current one is <b>not</b> consumed
     * (calls to <code>currentChar()</code> will still return it)
     *
     * @return next source character
     * @throws Exception if an error occurs
     */
    public char peekChar() throws Exception {
        if (outsideLineBounds()) {
            readLineAndNextCharacter();
        }
        if (line == null) {
            return EOF;
        }

        int nextPos = lineOffset + 1;
        return nextPos < line.length() ? line.charAt(nextPos) : EOL;
    }

    /**
     * Reads next line from source
     *
     * @throws IOException if an I/O error occurs
     */
    public void readLine() throws IOException {
        line = reader.readLine();
        lineOffset = BEFORE_START_OF_LINE;

        if (line != null) {
            ++lineNumber;
        }
    }

    /**
     * Closes source
     *
     * @throws Exception if an error occurs
     */
    public void close() throws Exception {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private char readLineAndNextCharacter() throws Exception {
        readLine();
        return nextChar();
    }

    private boolean atEndOfFile() {
        return line == null;
    }

    private boolean atEndOfLine() {
        return lineOffset == BEFORE_START_OF_LINE|| lineOffset == line.length();
    }

    private boolean outsideLineBounds() {
        return haveNotStartedReadingYet() || movedBeyondCurrentLineEnd();
    }

    private boolean haveNotStartedReadingYet() {
        return lineOffset == BEFORE_START_OF_FILE;
    }

    private boolean movedBeyondCurrentLineEnd() {
        return line != null && lineOffset > line.length();
    }

    @LearningExperience(type = TOO_SPECIFIC)
    private static <V> Supplier<V> sneakily(Callable<V> s) {
        return () -> {
            try {
                return s.call();
            } catch (Exception e) {
                sneakThrow(e);
            }
            // this should never happen as by this moment we have either returned callable's result or threw
            return (V) null;
        };
    }
}