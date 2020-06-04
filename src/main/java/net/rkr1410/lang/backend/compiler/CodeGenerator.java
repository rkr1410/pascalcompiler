package net.rkr1410.lang.backend.compiler;

import net.rkr1410.lang.backend.AbstractBackend;
import net.rkr1410.lang.intermediate.IntermediateCode;
import net.rkr1410.lang.intermediate.SymbolTable;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageType;

/**
 * Compiler
 */
public class CodeGenerator extends AbstractBackend {

    /**
     * Process the intermediate code and symbol table obtained from parser to generate
     * machine-language instructions
     *
     * @param intermediateCode intermediate code
     * @param symbolTable symbol table
     * @throws Exception if an error occurs
     */
    @Override
    public void process(IntermediateCode intermediateCode, SymbolTable symbolTable) throws Exception {
        long start = System.currentTimeMillis();
        float elapsed = (System.currentTimeMillis() - start) / 1000f;
        int instructionCount = 0;

        sendMessage(new Message(MessageType.COMPILER_STATS, new Object[] {instructionCount, elapsed}));
    }
}
