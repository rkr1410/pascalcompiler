package net.rkr1410.lang.backend;

import net.rkr1410.lang.intermediate.IntermediateCode;
import net.rkr1410.lang.intermediate.SymbolTable;
import net.rkr1410.lang.messages.MessageHelper;
import net.rkr1410.lang.messages.MessageProducer;

public abstract class Backend implements MessageProducer {

    protected SymbolTable symbolTable;
    protected IntermediateCode intermediateCode;

    private MessageHelper messageHelper;

    public Backend() {
        this.messageHelper = new MessageHelper();
    }

    /**
     * Process the intermediate code generated by parser.
     * To be implemented by compiler and interpreter subclasses.
     *
     * @param intermediateCode intermediate code
     * @param symbolTable symbol table
     * @throws Exception if an error occurse during compilation/execution
     */
    public abstract void process(IntermediateCode intermediateCode, SymbolTable symbolTable) throws Exception;

    @Override
    public MessageHelper getMessageHelper() {
        return messageHelper;
    }
}
