package net.rkr1410.pascal.frontend;

import net.rkr1410.lang.frontend.*;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageProducer;
import net.rkr1410.lang.messages.MessageType;

import java.io.IOException;

import static net.rkr1410.pascal.frontend.PascalTokenType.ERROR;

/**
 * <h1>STDPParser</h1>
 * <p>
 * A simple top-down Pascal parser.
 */
public class STDPParser extends Parser<PascalErrorCode> implements MessageProducer {
    /**
     * Constructor
     *
     * @param scanner scanner to use with this parser
     */
    public STDPParser(Scanner scanner) {
        super(scanner);
        errorHandler = new PascalErrorHandler();
    }

    /**
     * Parse a Pascal program {@link net.rkr1410.lang.frontend.Source} and generate symbol table
     * and intermediate code
     *
     * @throws Exception
     */
    @Override
    public void parse() throws Exception {
        try {
            readTokens();
        } catch (IOException e) {
            //todo implement
            //errorHandler.abortTranslation(IO_ERROR, this);
            e.printStackTrace();
        }
    }

    void readTokens() throws Exception {
        long startTiMeMillis = System.currentTimeMillis();
        Token token;

        while (!((token = nextToken()) instanceof EofToken)) {
            TokenType tokenType = token.getTokenType();

            if (tokenType != ERROR) {
                sendMessage(new Message(MessageType.TOKEN, new Object[]{
                        token.getLineNumber(),
                        token.getLinePosition(),
                        tokenType,
                        token.getText(),
                        token.getValue()
                }));
            } else {
                errorHandler.flag(token, (PascalErrorCode) token.getValue());
            }
        }

        float elapsed = (System.currentTimeMillis() - startTiMeMillis) / 1000f;
        sendMessage(new Message(MessageType.PARSER_STATS, new Object[]{
                token.getLineNumber(),
                getErrorCount(),
                elapsed
        }));
    }

    @Override
    public int getErrorCount() {
        return errorHandler.getErrorCount();
    }
}
