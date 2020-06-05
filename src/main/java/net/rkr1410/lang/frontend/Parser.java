package net.rkr1410.lang.frontend;

import net.rkr1410.lang.intermediate.IntermediateCode;
import net.rkr1410.lang.intermediate.SymbolTable;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageHelper;
import net.rkr1410.lang.messages.MessageProducer;
import net.rkr1410.lang.messages.MessageReceiver;

/**
 * <h1>Parser</h1>
 *
 * <p>A language-independent framework class.
 * To be implemented by language-specific subclasses.</p>
 */
public abstract class Parser<T extends ErrorCode> implements MessageProducer {

    protected SymbolTable symbolTable;
    protected IntermediateCode intermediateCode;
    protected Scanner scanner;
    protected MessageHelper messageHelper;
    protected ErrorHandler<T> errorHandler;

    /**
     * Constructor
     * @param scanner scanner to use with this parser
     */
    protected Parser(Scanner scanner) {
        this.scanner = scanner;
        this.messageHelper = new MessageHelper();
    }

    public void setErrorHandler(ErrorHandler<T> errorHandler) {
        this.errorHandler = errorHandler;
        this.errorHandler.addMessageReceiver(this::sendMessage);
    }

    /**
     * Parse source to generate intermediate code and a symbol table.
     * For implementation by language-specific parsers.
     *
     * @throws Exception when exception during parsing occurs
     */
    //TODO Exception? Maybe narrow the type
    public abstract void parse() throws Exception;

    /**
     * Gets number of errors which occurred during parsing.
     * For implementation by language-specific parsers.
     *
     * @return error count
     */
    public abstract int getErrorCount();

    /**
     * Forwards the call to scanner's <code>currentToken()</code>
     *
     * @return the current token
     */
    public Token currentToken() {
        return scanner.currentToken();
    }

    /**
     * Forwards the call to scanner's <code>nextToken()</code>
     *
     * @return the next token
     * @throws Exception if an error occurred
     */
    //TODO Exception? Maybe narrow the type
    public Token nextToken() throws Exception {
        return scanner.nextToken();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public IntermediateCode getIntermediateCode() {
        return intermediateCode;
    }

    @Override
    public MessageHelper getMessageHelper() {
        return messageHelper;
    }
}
