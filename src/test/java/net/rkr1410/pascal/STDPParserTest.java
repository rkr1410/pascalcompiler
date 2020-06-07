package net.rkr1410.pascal;

import net.rkr1410.lang.ParserFactory;
import net.rkr1410.lang.frontend.Parser;
import net.rkr1410.lang.frontend.Source;
import net.rkr1410.lang.messages.Message;
import net.rkr1410.lang.messages.MessageReceiver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class STDPParserTest {

    @Mock BufferedReader reader;


    @Test
    void test() throws Exception {
        doReturn(
                "{This is a comment.}",
                "{This is a comment\nthat spans several\nsource lines.}",
                "Two{comments in}{a row} here",
                "{Word tokens}",
                "Hello world",
                "begin BEGIN Begin BeGiN begins",
                "{String tokens}",
                "'Hello, world.'",
                "'It''s Friday!'",
                "''",
                "' '' '' '   ''''''",
                "'This string\nspans\nsource lines.'",
                "{Bad tokens}",
                "123e99  123456789012345  1234.56E.  3.  5..  .14  314.e-2",
                "What?",
                "'String not closed",
                null).when(reader).readLine();
        Source source = new Source(reader);
        MessageReceiver receiver = new MessageReceiver() {
            @Override
            public void messageReceived(Message message) {
                System.err.println("Received message " + message);
            }
        };

        Parser parser = ParserFactory.createParser("pascal", "top-down", source);
        //source.addMessageReceiver(receiver);
        parser.addMessageReceiver(receiver);

        parser.parse();
    }
}