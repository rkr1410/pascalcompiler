package net.rkr1410.pascal;

import net.rkr1410.lang.ParserFactory;
import net.rkr1410.lang.backend.Backend;
import net.rkr1410.lang.backend.BackendFactory;
import net.rkr1410.lang.frontend.Parser;
import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.intermediate.IntermediateCode;
import net.rkr1410.lang.intermediate.SymbolTable;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageReceiver;
import net.rkr1410.util.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * <h1>Pascal</h1>
 * <p>
 * Compile or interpret a Pascal program
 */
public class Pascal implements MessageReceiver {
    private Parser parser;
    private Source source;
    private IntermediateCode intermediateCode;
    private SymbolTable symbolTable;
    private Backend backend;

    public Pascal(String operation, String filePath, String flags) {
        //todo: flags

        try {
            source = new Source(new BufferedReader(new FileReader(filePath)));
            parser = ParserFactory.createParser("pascal", "top-down", source);
            backend = BackendFactory.createBackend(operation);

            source.addMessageReceiver(this);
            parser.addMessageReceiver(this);
            backend.addMessageReceiver(this);
        } catch (Exception e) {
            Utils.sneakThrow(e);
        }
    }

    void parse() {
        try {
            parser.parse();
            source.close();

            intermediateCode = parser.getIntermediateCode();
            symbolTable = parser.getSymbolTable();
        } catch (Exception e) {
            Utils.sneakThrow(e);
        }
    }

    void run() {
        try {
            backend.process(intermediateCode, symbolTable);
        } catch (Exception e) {
            Utils.sneakThrow(e);
        }
    }

    static void wrapErrors(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            System.err.println("----==[ internal error ]==----");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            //Pascal pascal = new Pascal(args[0], args[1], "");
            Pascal pascal = new Pascal("execute", "c:\\stuff\\java\\pascal\\src\\main\\resources\\hello.pas", "");
            wrapErrors(pascal::parse);
            wrapErrors(pascal::run);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: Pascal [execute|compile] pathToPascalSource");
        }
    }

    @Override
    public void messageReceived(Message message) {
        //TODO: better message types, break down listeners
        Object[] body = (Object[]) message.getBody();
        switch (message.getType()) {
            case SOURCE_LINE:
                int lineNumber = (Integer) body[0];
                String lineText = (String) body[1];
                System.err.printf("%03d %s\n", lineNumber, lineText);
                break;
            case PARSER_STATS:
                int statementCount = (Integer) body[0];
                int syntaxErrors = (Integer) body[1];
                float elapsed = (Float) body[2];
                System.err.printf(
                        "\n%,20d source lines." +
                                "\n%,20d syntax errors." +
                                "\n%,20.2f seconds total parsing time." +
                                "\n", statementCount, syntaxErrors, elapsed);
                break;
            case INTERPRETER_STATS:
                int executionCount = (Integer) body[0];
                int runtimeErrors = (Integer) body[1];
                float elapsedTime = (Float) body[2];
                System.err.printf(
                        "\n%,20d statements executed." +
                                "\n%,20d runtime errors." +
                                "\n%,20.2f seconds total execution time." +
                                "\n", executionCount, runtimeErrors, elapsedTime);
                break;
            case COMPILER_STATS:
                int instructionCount = (Integer) body[0];
                float elapsed_time = (Float) body[1];
                System.err.printf(
                        "\n%,20d instructions generated." +
                                "\n%,20.2f seconds total code generation time." +
                                "\n", instructionCount, elapsed_time);
                break;
            default:
                throw new IllegalArgumentException("Unknown message type received: " + message.getType());
        }
    }
}
