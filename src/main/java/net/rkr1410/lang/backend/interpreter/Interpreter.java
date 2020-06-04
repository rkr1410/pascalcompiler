package net.rkr1410.lang.backend.interpreter;

import net.rkr1410.lang.backend.Backend;
import net.rkr1410.lang.intermediate.IntermediateCode;
import net.rkr1410.lang.intermediate.SymbolTable;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageType;

/**
 * Executing interpreter
 */
public class Interpreter extends Backend {

    /**
     * Process the intermediate code and symbol table obtained from parser to execute
     * source program
     *
     * @param intermediateCode intermediate code
     * @param symbolTable symbol table
     * @throws Exception if an error occurs
     */
    @Override
    public void process(IntermediateCode intermediateCode, SymbolTable symbolTable) throws Exception {
        long start = System.currentTimeMillis();
        float elapsed = (System.currentTimeMillis() - start) / 1000f;
        int executionCount = 0;
        int runtimeErrors = 0;

        sendMessage(new Message(MessageType.INTERPRETER_STATS, new Object[] {executionCount, runtimeErrors, elapsed}));
    }
}
