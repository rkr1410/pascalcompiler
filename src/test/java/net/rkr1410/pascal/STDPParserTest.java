package net.rkr1410.pascal;

import net.rkr1410.lang.frontend.Source;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class STDPParserTest {

    @Mock BufferedReader reader;


    @Test
    void test() throws Exception {
        doReturn("abcde", "fg", "hij", null).when(reader).readLine();
        STDPParser p = new STDPParser(new STDPScanner(new Source(reader)));

        p.parse();
    }
}