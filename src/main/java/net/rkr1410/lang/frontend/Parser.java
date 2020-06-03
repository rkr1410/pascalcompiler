package net.rkr1410.lang.frontend;

/**
 * <h1>Parser</h1>
 *
 * <p>A language-independent framework class.
 * To be implemented by language-specific subclasses.</p>
 */
public abstract class Parser {

    // Generated symbol table
    protected static SymTab symTab;

//    static {
//        symTab = null;
//    }

    // Scanner used by this parser
    protected Scanner scanner;
    // Intermediate code generated by this parser
    protected ICode iCode;

    /**
     * Constructor
     * @param scanner scanner to use with this parser
     */
    protected Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Parse source to generate intermediate code and a symbol table.
     * For implementation by language-specific parsers.
     *
     * @throws Exception when exception during parsing occurs
     */
    //TODO Exception? Maybe narrow the type
    public abstract void parse() throws Exception;

    /**
     * Gets number of errors which occurred during parsing.
     * For implementation by language-specific parsers.
     *
     * @return error count
     */
    public abstract int etErrorCount();

    /**
     * Forwards the call to scanner's <code>currentToken()</code>
     *
     * @return the current token
     */
    public Token currentToken() {
        return scanner.currentToken();
    }

    /**
     * Forwards the call to scanner's <code>nextToken()</code>
     *
     * @return the next token
     * @throws Exception if an error occurred
     */
    //TODO Exception? Maybe narrow the type
    public Token nextToken() throws Exception {
        return scanner.nextToken();
    }
}
