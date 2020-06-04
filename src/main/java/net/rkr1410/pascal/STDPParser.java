package net.rkr1410.pascal;

import net.rkr1410.lang.frontend.EofToken;
import net.rkr1410.lang.frontend.Parser;
import net.rkr1410.lang.frontend.Scanner;
import net.rkr1410.lang.frontend.Token;

public class STDPParser extends Parser {
    /**
     * Constructor
     *
     * @param scanner scanner to use with this parser
     */
    protected STDPParser(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void parse() throws Exception {
        Token token;

        long start = System.currentTimeMillis();

        while (!((token = nextToken()) instanceof EofToken));

        float elapsed = (System.currentTimeMillis() - start) / 1000f;
        System.err.printf("Found %s errors\n", getErrorCount());
        System.err.printf("Parsed %s lines in %.3f seconds\n", token.getLineNumber(), elapsed);
    }

    @Override
    public int getErrorCount() {
        return 0;
    }
}
