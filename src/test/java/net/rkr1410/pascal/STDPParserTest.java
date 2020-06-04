package net.rkr1410.pascal;

import net.rkr1410.lang.ParserFactory;
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
        doReturn("abcde", "fg", "hij", null).when(reader).readLine();
        Source source = new Source(reader);
        source.addMessageReceiver(new MessageReceiver() {
            @Override
            public void messageReceived(Message message) {
                System.err.println("Received message " + message);
            }
        });

        ParserFactory.createParser("pascal", "top-down", source)
                .parse();
    }
}