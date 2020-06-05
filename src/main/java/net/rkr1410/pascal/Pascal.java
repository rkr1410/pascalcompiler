package net.rkr1410.pascal;

import com.google.common.base.Strings;
import net.rkr1410.lang.ParserFactory;
import net.rkr1410.lang.backend.Backend;
import net.rkr1410.lang.backend.BackendFactory;
import net.rkr1410.lang.frontend.Parser;
import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.frontend.TokenType;
import net.rkr1410.lang.intermediate.IntermediateCode;
import net.rkr1410.lang.intermediate.SymbolTable;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageReceiver;
import net.rkr1410.util.Utils;

import java.io.BufferedReader;
import java.io.FileReader;

import static net.rkr1410.pascal.frontend.PascalTokenType.STRING;

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
                printSourceLineMessage(body);
                break;
            case PARSER_STATS:
                printParserStats(body);
                break;
            case INTERPRETER_STATS:
                printInterpreterStats(body);
                break;
            case COMPILER_STATS:
                printCompilerStats(body);
                break;
            case TOKEN:
                printTokenData(body);
                break;
            case SYNTAX_ERROR:
                printSyntaxError(body);
                break;
            default:
                throw new IllegalArgumentException("Unknown message type received: " + message.getType());
        }
    }

    void printSourceLineMessage(Object[] body) {
        int lineNumber = (Integer) body[0];
        String lineText = (String) body[1];
        System.err.printf("%03d %s\n", lineNumber, lineText);
    }

    void printParserStats(Object[] body) {
        int statementCount = (Integer) body[0];
        int syntaxErrors = (Integer) body[1];
        float elapsed = (Float) body[2];
        System.err.printf(
                "\n%,20d source lines." +
                        "\n%,20d syntax errors." +
                        "\n%,20.2f seconds total parsing time." +
                        "\n", statementCount, syntaxErrors, elapsed);
    }

    void printInterpreterStats(Object[] body) {
        int executionCount = (Integer) body[0];
        int runtimeErrors = (Integer) body[1];
        float elapsedTime = (Float) body[2];
        System.err.printf(
                "\n%,20d statements executed." +
                        "\n%,20d runtime errors." +
                        "\n%,20.2f seconds total execution time." +
                        "\n", executionCount, runtimeErrors, elapsedTime);
    }

    void printCompilerStats(Object[] body) {
        int instructionCount = (Integer) body[0];
        float elapsed_time = (Float) body[1];
        System.err.printf(
                "\n%,20d instructions generated." +
                        "\n%,20.2f seconds total code generation time." +
                        "\n", instructionCount, elapsed_time);
    }

    void printTokenData(Object[] body) {
        int line = (Integer) body[0];
        int position = (Integer) body[1];
        TokenType tokenType = (TokenType) body[2];
        String tokenText = (String) body[3];
        Object tokenValue = body[4];

        System.err.printf(">>> %-15s line=%03d, pos=%2d, text=\"%s\"", tokenType, line, position, tokenText);
        if (tokenValue != null) {
            if (tokenType == STRING) {
                tokenValue = "\"" + tokenValue + "\"";
            }
            System.err.printf(", %s", tokenValue);
        } else {
            System.err.println();
        }
    }

    private static final int PREFIX_WIDTH = 5;
    void printSyntaxError(Object[] body) {
        int lineNumber = (Integer) body[0];
        int position = (Integer) body[1];
        String tokenText = (String) body[2];
        String errorMessage = (String) body[3];

        int spaceCount = PREFIX_WIDTH + position;

        System.err.printf("%s^\n*** %s", Strings.repeat(" ", spaceCount), errorMessage);

        if (tokenText != null) {
            System.err.printf(" at[\"%s\"]\n", tokenText);
        } else {
            System.err.println();
        }
        System.err.printf("On line %s, position %s\n", lineNumber, position);
    }

}
