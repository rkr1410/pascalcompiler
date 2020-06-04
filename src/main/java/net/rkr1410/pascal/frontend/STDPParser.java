package net.rkr1410.pascal.frontend;

import net.rkr1410.lang.frontend.EofToken;
import net.rkr1410.lang.frontend.Parser;
import net.rkr1410.lang.frontend.Scanner;
import net.rkr1410.lang.frontend.Token;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageHelper;
import net.rkr1410.lang.messages.MessageProducer;
import net.rkr1410.lang.messages.MessageType;

public class STDPParser extends Parser implements MessageProducer {


    /**
     * Constructor
     *
     * @param scanner scanner to use with this parser
     */
    public STDPParser(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void parse() throws Exception {
        Token token;

        long start = System.currentTimeMillis();

        while (!((token = nextToken()) instanceof EofToken));

        float elapsed = (System.currentTimeMillis() - start) / 1000f;

        sendMessage(new Message(MessageType.PARSER_STATS, new Object[] {token.getLineNumber(), getErrorCount(), elapsed}));
    }

    @Override
    public int getErrorCount() {
        return 0;
    }
}
