package ldf.java_cup.runtime;

/**
 * Defines the Symbol class, which is used to represent all terminals
 * and nonterminals while parsing.  The lexer should pass CUP Symbols
 * and CUP returns a Symbol.
 *
 * @author Frank Flannery
 * @author Cristian Harja
 */

@SuppressWarnings("unused")
public class Symbol extends LocationAwareEntityImpl {

    /**
     * Name of this symbol (token name or non-terminal name).
     * For debugging purposes.
     */
    protected String symName;

    /**
     * Identification code for this symbol (assigned by CUP).
     */
    protected int symCode;

    /**
     * Semantic value of this symbol.
     */
    public Object  value;

    /**
     * <p>If the {@code Symbol} object is a token, this field may point
     * to a {@link ldf.java_cup.runtime.Comment} object.
     * </p>
     * <p>If used, this field should be populated by the lexer (see {@link
     * TokenFactory#newComment TokenFactory.newComment}) and points
     * to the comment (if any) that's located right before this token.
     * </p>
     * <p>If there is no such comment, the field should be {@code null}.
     * If there are multiple ones though, this field should point to the
     * <em>last one</em> only. The comment object would then point to the
     * one before it (linked list).
     * </p>
     */
    protected Comment prevComment;

    /**
     * The parse state to be recorded on the parse stack with this symbol.
     * This field is for the convenience of the parser and shouldn't be
     * modified except by the parser.
     */
    protected int parse_state;

    /**
     * This allows us to catch some errors caused by scanners recycling
     * symbols.  For the use of the parser only. [CSA, 23-Jul-1999]
     */
    boolean used_by_parser;

    public Symbol() {
    }

    public Symbol(String symbolName, int symbolCode) {
        symName = symbolName;
        symCode = symbolCode;
    }

    public Symbol(String symbolName, int symbolCode, int parseState) {
        this(symbolName, symbolCode);
        parse_state = parseState;
    }

    /**
     * TODO: explain this well
     */
    public void setLeftRightSymbols(Symbol l, Symbol r) {
        setLeftPos(l.lineL, l.columnL, l.offsetL);
        setRightPos(r.lineR, r.columnR, r.offsetR);
        prevComment = l.prevComment;
    }

    public String getSymbolName() {
        return symName;
    }

    public int getSymbolCode() {
        return symCode;
    }

    /**
     * Sets the {@code prevComment} parameter as the {@code Comment}
     * before this symbol. In turn, that will point to the one before
     * and so on (linked list).
     */
    public void setPrevComment(Comment prevComment) {
        this.prevComment = prevComment;
    }

    public boolean hasComments() {
        return prevComment != null;
    }

    /**
     * An enumeration (in reversed order) of the comments located before
     * this symbol.
     */
    public CommentEnumeration getCommentsReverse() {
        return new CommentEnumeration(prevComment);
    }

    @Override
    public String toString() {
        return symName + " (" + lineL + ":" + columnL + ")";
    }

}






