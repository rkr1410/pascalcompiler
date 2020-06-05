package net.rkr1410.lang.frontend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class SourceTest {

    /*
    positions:    0 1 2 3  4 5  6
    default data: T e s \n t \n \000
     */
    private static final String[] DEFAULT_DATA = new String[]{"Tes", "t", null};
    private static final char EOL = '\n';
    private static final char EOF = (char) 0;

    @Mock BufferedReader reader;
    Source source;

    @Test
    void readerCannotBeNull() {
        assertThatThrownBy(() -> new Source(null))
                .isInstanceOf(NullPointerException.class);
    }

/**
                               ┌─┐┬ ┬┬─┐┬─┐┌─┐┌┐┌┌┬┐┌─┐┬ ┬┌─┐┬─┐
                               │  │ │├┬┘├┬┘├┤ │││ │ │  ├─┤├─┤├┬┘
                               └─┘└─┘┴└─┴└─└─┘┘└┘ ┴ └─┘┴ ┴┴ ┴┴└─
                      A set of tests verifying currentChar() method results
 */

    @Test
    void nullDataAndNothingReadYet_currentChar_EOFIsReturned() throws Exception {
        doReturn(null).when(reader).readLine();
        initSource();

        char c = source.currentChar();
        assertThat(c).isEqualTo(EOF);
    }

    @Test
    void emptyDataAndNothingReadYet_currentChar_EOLIsReturned() throws Exception {
        doReturn("").when(reader).readLine();
        initSource();

        char c = source.currentChar();
        assertThat(c).isEqualTo(EOL);
    }

    @Test
    void someDataAndNothingReadYet_currentChar_firstCharIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        char c = source.currentChar();
        assertThat(c).isEqualTo(DEFAULT_DATA[0].charAt(0));
    }

    @Test
    void someDataAndSingleCharRead_currentChar_secondCharIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        source.nextChar();
        char c = source.currentChar();
        assertThat(c).isEqualTo(DEFAULT_DATA[0].charAt(1));
    }

    @Test
    void someData_afterLineIsRead_currentChar_EOLIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        fastForwardSingleCharacterPastFirstLine();
        char c = source.currentChar();
        assertThat(c).isEqualTo(EOL);
    }

    @Test
    void someData_afterAllDataRead_currentChar_EOFIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        fastForwardPastReaderData();
        char c = source.currentChar();
        assertThat(c).isEqualTo(EOF);
    }

/**
                               ┌─┐┌─┐┌─┐┬┌─┌─┐┬ ┬┌─┐┬─┐
                               ├─┘├┤ ├┤ ├┴┐│  ├─┤├─┤├┬┘
                               ┴  └─┘└─┘┴ ┴└─┘┴ ┴┴ ┴┴└─
                   A set of tests verifying peekChar() method results
 */

    @Test
    void nullDataAndNothingReadYet_peekChar_EOFIsReturned() throws Exception {
        doReturn(null).when(reader).readLine();
        initSource();

        char c = source.peekChar();
        assertThat(c).isEqualTo(EOF);
    }

    @Test
    void emptyDataAndNothingReadYet_peekChar_EOLIsReturned() throws Exception {
        doReturn("").when(reader).readLine();
        initSource();

        char c = source.peekChar();
        assertThat(c).isEqualTo(EOL);
    }

    @Test
    void someDataAndNothingReadYet_peekChar_secondCharIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        char c = source.peekChar();
        assertThat(c).isEqualTo(DEFAULT_DATA[0].charAt(1));
    }

    @Test
    void someDataAndFirstCharConsumed_peekChar_thirdCharIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        source.nextChar();
        char c = source.peekChar();
        assertThat(c).isEqualTo(DEFAULT_DATA[0].charAt(2));
    }

    @Test
    void someData_afterLineIsRead_peekChar_EOLIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        fastForwardSingleCharacterPastFirstLine();
        char c = source.peekChar();
        assertThat(c).isEqualTo(EOL);
    }

    @Test
    void someData_afterAllDataRead_peekChar_EOFIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        fastForwardPastReaderData();
        char c = source.peekChar();
        assertThat(c).isEqualTo(EOF);
    }

/**
                                       ┌┐┌┌─┐─┐ ┬┌┬┐┌─┐┬ ┬┌─┐┬─┐
                                       │││├┤ ┌┴┬┘ │ │  ├─┤├─┤├┬┘
                                       ┘└┘└─┘┴ └─ ┴ └─┘┴ ┴┴ ┴┴└─
                          A set of tests verifying nextChar() method results
 */

    @Test
    void someData_afterLineIsReadWithoutNewline_nextChar_EOLIsReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        fastForwardToEndOfFirstLine();
        char c = source.nextChar();
        assertThat(c).isEqualTo(EOL);
    }

    @Test
    void someData_afterDataReadIncludingLastNewline_nextChar_EOFKeepsBeingReturned() throws Exception {
        mockDefaultDataReader();
        initSource();

        fastForward(15);
        char c = source.nextChar();
        assertThat(c).isEqualTo(EOF);
    }

    @Test
    void readerThrows_nextCharThrows() throws Exception {
        initSource();
        doThrow(IOException.class).when(reader).readLine();

        assertThatThrownBy(() -> source.nextChar()).isInstanceOf(IOException.class);
    }

/**
                                      ┌─┐┌─┐┌┬┐┌─┐┬  ┌─┐─┐ ┬
                                      │  │ ││││├─┘│  ├┤ ┌┴┬┘
                                      └─┘└─┘┴ ┴┴  ┴─┘└─┘┴ └─
                    Complex test. Testing curr/peek/nextChar methods at each point
              of journey through the sample text. This is a sum of other tests, highlighted
                              as a whole to better visualize the requirements
 */

    @Test
    void fullReaderFlowTest() throws Exception {
        doReturn("BE", "gin", "\n", null).when(reader).readLine();
        initSource();

        // After first call to either of three methods
        // current = first character of first line returned by reader
        verifyStep('B', 'E', 'E');
        // When curr = last character on the line, next will be returned as EOL
        // (even though BufferedReader strips newlines)
        verifyStep('E', EOL, EOL);
        // Watch out: Source operates on a single line only, peek will return EOL until a call to nextChar
        verifyStep(EOL, EOL, 'g');
        verifyStep('g', 'i', 'i');
        verifyStep('i', 'n', 'n');
        verifyStep('n', EOL, EOL);
        verifyStep(EOL, EOL, EOL);
        verifyStep(EOL, EOL, EOL);
        // Watch out: Above also applies to finding EOF
        verifyStep(EOL, EOL, EOF);
        // After actually getting EOF from nextChar, every call will keep returning EOF until closing
        verifyStep(EOF, EOF, EOF);
        verifyStep(EOF, EOF, EOF);
    }

    private void verifyStep(char expectedCurr, char expectedPeek, char expectedNext) throws Exception {
        assertThat(source.currentChar()).as("current").isEqualTo(expectedCurr);
        assertThat(source.peekChar()).as("peek").isEqualTo(expectedPeek);
        assertThat(source.nextChar()).as("next").isEqualTo(expectedNext);
    }

    private void fastForwardToEndOfFirstLine() throws Exception {
        // Already at first char
        int steps = DEFAULT_DATA[0].length() - 1;
        fastForward(steps);
    }

    private void fastForwardSingleCharacterPastFirstLine() throws Exception {
        // Already at first char
        int steps = DEFAULT_DATA[0].length();
        fastForward(steps);
    }

    private void fastForwardPastReaderData() throws Exception {
        // +2 for two newlines
        int steps = DEFAULT_DATA[0].length() + DEFAULT_DATA[1].length() + 2;
        fastForward(steps);
    }

    private void fastForward(int steps) throws Exception {
        for (int i = 0; i < steps; i++) {
            source.nextChar();
        }
    }

    private void mockDefaultDataReader() throws IOException {
        doReturn(DEFAULT_DATA[0], DEFAULT_DATA[1], DEFAULT_DATA[2]).when(reader).readLine();
    }

    private void initSource() {
        source = new Source(reader);
    }
}