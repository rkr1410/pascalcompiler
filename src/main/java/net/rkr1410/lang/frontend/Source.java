package net.rkr1410.lang.frontend;

import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageHelper;
import net.rkr1410.lang.messages.MessageProducer;
import net.rkr1410.lang.messages.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * <h1>Source</h1>
 *
 * <p>A framework class representing the source program</p>
 */
public class Source implements MessageProducer {
    public static final char EOL = '\n';
    public static final char EOF = (char) 0;

    private MessageHelper messageHelper;

    private BufferedReader reader;
    private String line;
    private int lineNumber;
    private int linePosition;
    private boolean initialized;

    /**
     * Constructor
     *
     * @param reader the reader for source program
     */
    public Source(BufferedReader reader) {
        this.reader = Objects.requireNonNull(reader, "Reader cannot be null");
        this.lineNumber = 0;
        this.messageHelper = new MessageHelper();
    }

    /**
     * Returns next character from source.
     * Current one is consumed.
     *
     * @return next source character
     * @throws IOException if an I/O error occurred
     */
    public char nextChar() throws IOException {
        ensureAtLeastOneLineWasRead();
        if (sourceLineAvailable()) {
            if (linePosition < line.length()) {
                ++linePosition;
            } else {
                readLine();
            }
        }
        return currentChar();
    }

    /**
     * Returns character from source at current position.
     *
     * @return source char at current position
     * @throws IOException if an I/O error occurred
     */
    public char currentChar() throws IOException {
        ensureAtLeastOneLineWasRead();
        return charAtOffset(0);
    }

    /**
     * Returns next character from source.
     * Current one is not consumed.
     *
     * @return next source character
     * @throws IOException if an I/O error occurred
     */
    public char peekChar() throws IOException {
        ensureAtLeastOneLineWasRead();
        return charAtOffset(1);
    }

    /**
     * Reads next line from source
     *
     * @throws IOException if an I/O error occurs
     */
    public void readLine() throws IOException {
        line = reader.readLine();

        if (line != null) {
            linePosition = 0;
            lineNumber++;
            sendSourceLineMessage();
        }
        initialized = true;
    }

    /**
     * @return current line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return current lie offset
     */
    public int getLinePosition() {
        return linePosition;
    }

    /**
     * Closes source
     *
     * @throws IOException if an I/O error occurred
     */
    public void close() throws IOException {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private char charAtOffset(int offset) {
        if (!sourceLineAvailable()) {
            return EOF;
        } else if (offsetOutsideLineBounds(offset)) {
            return EOL;
        } else {
            return line.charAt(linePosition + offset);
        }
    }

    private boolean sourceLineAvailable() {
        return line != null;
    }

    private boolean offsetOutsideLineBounds(int offset) {
        return linePosition + offset >= line.length();
    }

    private void ensureAtLeastOneLineWasRead() throws IOException {
        if (!initialized) {
            readLine();
        }
    }

    private void sendSourceLineMessage() {
        sendMessage(new Message(MessageType.SOURCE_LINE, new Object[] { lineNumber, line }));
    }

    @Override
    public MessageHelper getMessageHelper() {
        return messageHelper;
    }
}