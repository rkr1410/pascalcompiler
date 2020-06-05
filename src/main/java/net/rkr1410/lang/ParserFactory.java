package net.rkr1410.lang;

import net.rkr1410.lang.frontend.Parser;
import net.rkr1410.lang.frontend.Scanner;
import net.rkr1410.lang.frontend.Source;
import net.rkr1410.pascal.frontend.PascalErrorHandler;
import net.rkr1410.pascal.frontend.STDPParser;
import net.rkr1410.pascal.frontend.scanner.STDPScanner;

/**
 * <h1>ParserFactory</h1>
 *
 * <p>Factory class to create parsers for different languages.
 * Herein lies the association of parsers and scanners
 */
public class ParserFactory {
    public static final String LANG_PASCAL = "PASCAL";
    public static final String TYPE_TOP_DOWN = "TOP-DOWN";


    public static Parser createParser(String language, String parserType, Source source) {
        String normalizedLanguage = validateLanguage(language);
        String normalizedParserType = validateParserType(parserType);
        if (LANG_PASCAL.equals(normalizedLanguage)
                && TYPE_TOP_DOWN.equals(normalizedParserType)) {
            Scanner scanner = new STDPScanner(source);
            STDPParser parser = new STDPParser(scanner);
            PascalErrorHandler errorHandler = new PascalErrorHandler();
            parser.setErrorHandler(errorHandler);
            return parser;
        }
        return null;
    }

    private static String validateLanguage(String language) {
        if (LANG_PASCAL.equalsIgnoreCase(language)) {
            return LANG_PASCAL;
        }
        throw new IllegalArgumentException("Unknown language " + language);
    }

    private static String validateParserType(String parserType) {
        if (TYPE_TOP_DOWN.equalsIgnoreCase(parserType)) {
            return TYPE_TOP_DOWN;
        }
        throw new IllegalArgumentException("Unknown parser type " + parserType);
    }
}
