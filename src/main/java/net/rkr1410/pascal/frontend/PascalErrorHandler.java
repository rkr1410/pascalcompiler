package net.rkr1410.pascal.frontend;

import net.rkr1410.lang.frontend.ErrorHandler;
import net.rkr1410.lang.frontend.Token;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageHelper;
import net.rkr1410.lang.messages.MessageProducer;

import static net.rkr1410.lang.messages.MessageType.SYNTAX_ERROR;
import static net.rkr1410.pascal.frontend.PascalErrorCode.TOO_MANY_ERRORS;

/**
 * <h1>PascalErrorHandler</h1>
 *
 * <p>Handler for Pascal syntax errors
 */
public class PascalErrorHandler extends ErrorHandler<PascalErrorCode> implements MessageProducer {
    private static final int MAX_ERRORS = 15;

    private int errorCount;
    private MessageHelper messageHelper;

    public PascalErrorHandler() {
        errorCount = 0;
        messageHelper = new MessageHelper();
    }

    @Override
    public void flag(Token token, PascalErrorCode errorCode) {
        sendMessage(new Message(SYNTAX_ERROR, new Object[]{
                token.getLineNumber(),
                token.getLinePosition(),
                token.getText(),
                errorCode.toString()
        }));

        if (++errorCount > MAX_ERRORS) {
            abortTranslation(TOO_MANY_ERRORS);
        }
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public void abortTranslation(PascalErrorCode code) {
        sendMessage(new Message(SYNTAX_ERROR, new Object[]{
                0, 0, "", "FATAL_ERROR: " + code
        }));
        System.exit(code.getStatus());
    }

    @Override
    public MessageHelper getMessageHelper() {
        return messageHelper;
    }
}
