package net.rkr1410.lang.backend;

import net.rkr1410.lang.backend.compiler.CodeGenerator;
import net.rkr1410.lang.backend.interpreter.Interpreter;

/**
 * <h1>BackendFactory</h1>
 *
 * <p>Factory class to create compiler and interpreter components
 */
public class BackendFactory {
    public static final String COMMAND_COMPILER = "COMPILE";
    public static final String COMMAND_EXECUTE = "EXECUTE";

    /**
     * Create a compiler or an interpreter back end component.
     *
     * @param operation either "compile" or "execute"
     * @return a compiler or an interpreter back end component.
     */
    public static Backend createBackend(String operation) {
        switch (operation == null ? "" : operation.toUpperCase()) {
            case COMMAND_COMPILER:
                return new CodeGenerator();
            case COMMAND_EXECUTE:
                return new Interpreter();
            default:
                throw new IllegalArgumentException("Unknown command: " + operation);
        }
    }
}
