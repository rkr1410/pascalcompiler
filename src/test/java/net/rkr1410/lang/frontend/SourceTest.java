package net.rkr1410.lang.frontend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class SourceTest {

    /*
    positions:    0 1 2 3  4 5  6
    default data: a b c \n w \n \000
     */
    private static final String[] DEFAULT_DATA = new String[]{"abc", "w", null};

    @Mock BufferedReader reader;
    Source source;

    @Test
    void readerCannotBeNull() {
        assertThatThrownBy(() -> new Source(null)).isInstanceOf(NullPointerException.class);
    }

/*
                               ┌─┐┬ ┬┬─┐┬─┐┌─┐┌┐┌┌┬┐┌─┐┬ ┬┌─┐┬─┐
                               │  │ │├┬┘├┬┘├┤ │││ │ │  ├─┤├─┤├┬┘
                               └─┘└─┘┴└─┴└─└─┘┘└┘ ┴ └─┘┴ ┴┴ ┴┴└─
                      A set of tests verifying currentChar() method results
 */

    @Test
    void nullDataAndNothingReadYet_currentChar_EOFIsReturned() throws Exception {
        initSource(null);

        char c = source.currentChar();
        assertThat(c).isEqualTo((char) 0);
    }

    @Test
    void emptyDataAndNothingReadYet_currentChar_EOLIsReturned() throws Exception {
        initSource("");

        char c = source.currentChar();
        assertThat(c).isEqualTo('\n');
    }

    @Test
    void someDataAndNothingReadYet_currentChar_firstCharIsReturned() throws Exception {
        initSource();

        char c = source.currentChar();
        assertThat(c).isEqualTo('a');
    }

    @Test
    void someDataAndSingleCharRead_currentChar_secondCharIsReturned() throws Exception {
        initSource();

        source.currentChar();
        source.nextChar();
        char c = source.currentChar();
        assertThat(c).isEqualTo('b');
    }

    @Test
    void someData_afterLineIsRead_currentChar_EOLIsReturned() throws Exception {
        initSource();

        source.currentChar();
        fastForward(3);
        char c = source.currentChar();
        assertThat(c).isEqualTo('\n');
    }

    @Test
    void someData_afterAllDataRead_currentChar_EOFIsReturned() throws Exception {
        initSource();

        source.currentChar();
        fastForward(6);
        char c = source.currentChar();
        assertThat(c).isEqualTo((char) 0);
    }

    @Test
    void readerThrows_currentCharThrows() throws IOException {
        initSource(false, null);
        doThrow(IOException.class).when(reader).readLine();

        assertThatThrownBy(() -> source.currentChar()).isInstanceOf(IOException.class);
    }

/*
                               ┌┐┌┌─┐─┐ ┬┌┬┐┌─┐┬ ┬┌─┐┬─┐
                               │││├┤ ┌┴┬┘ │ │  ├─┤├─┤├┬┘
                               ┘└┘└─┘┴ └─ ┴ └─┘┴ ┴┴ ┴┴└─
                      A set of tests verifying nextChar() method results
 */

    @Test
    void nothingReadYet_nextChar_throws() throws Exception {
        initSource(false, null);

        assertThatThrownBy(() -> source.nextChar()).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("currentChar");
    }

    @Test
    void someData_afterLineIsReadWithoutNewline_nextChar_EOLIsReturned() throws Exception {
        initSource();

        source.currentChar();
        fastForward(2);
        char c = source.nextChar();
        assertThat(c).isEqualTo('\n');
    }

    @Test
    void someData_afterDataReadIncludingLastNewline_nextChar_EOFKeepsBeingReturned() throws Exception {
        initSource();

        source.currentChar();
        fastForward(15);
        char c = source.nextChar();
        assertThat(c).isEqualTo((char) 0);
    }

    @Test
    void readerThrows_nextCharThrows() throws Exception {
        initSource();
        source.currentChar();
        fastForward(3);
        doThrow(IOException.class).when(reader).readLine();

        assertThatThrownBy(() -> source.nextChar()).isInstanceOf(IOException.class);
    }


    private void fastForward(int steps) throws Exception {
        for (int i = 0; i < steps; i++) {
            source.nextChar();
        }
    }

    private void initSource() {
        initSource(true, null);
    }

    private void initSource(String data) {
        initSource(false, data);
    }

    private void initSource(boolean initDefaultData, String data) {
        try {
            if (initDefaultData) {
                doReturn(DEFAULT_DATA[0], DEFAULT_DATA[1], DEFAULT_DATA[2]).when(reader).readLine();
            } else if (data != null) {
                doReturn(data).when(reader).readLine();
            }
            source = new Source(reader);
        } catch (IOException e) {
            fail("Error creating Source");
            e.printStackTrace();
        }
    }


}