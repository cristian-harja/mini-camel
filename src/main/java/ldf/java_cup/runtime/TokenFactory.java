package ldf.java_cup.runtime;

/**
 * <p>This is a specialization of the SymbolFactory interface, aimed
 * towards being used in a lexer. <strong>This class is NOT stateless
 * </strong> (as one might expect from a factory class) in that the
 * sequence of calls to certain methods may influence the returned
 * objects.
 * </p>
 * <p>The main concern for introducing this interface was that a lexer,
 * even though it knew the index of both the beginning and the end of a
 * token, it would often only know the <em>line and column</em> of its
 * <em>beginning</em>.
 * </p>
 * <p>For this reason, implementations of this interface are expected to
 * remember the last returned object, until a new token (or whitespace)
 * is found (the lexer now being able to provide the line and column of
 * the end of the previous object).
 * </p>
 * <p>The {@code newToken}, {@code newEOF} and {@code newComment} methods
 * create different types of objects, while {@code signalWhitespace}
 * can mark the end position of a token or comment.
 * </p>
 *
 * @author Cristian Harja
 */
public interface TokenFactory extends SymbolFactory{

    /**
     * Creates a {@code Symbol} object whose left position is known.
     * Uses it to mark the right position of the previous token if it
     * isn't already set.
     */
    Symbol newToken(
            String symName, int symCode,
            int lineL, int columnL, int offsetL
    );

    Symbol newToken(
            String symName, int symCode,
            int lineL, int columnL, int offsetL,
            int lineR, int columnR, int offsetR
    );

    /**
     * Just like {@link #newToken(String, int, int, int, int) newToken},
     * only it might get special treatment from the class implementing
     * this interface.
     */
    Symbol newEOF(
            String symName, int symCode,
            int lineL, int columnL, int offsetL
    );

    /**
     * To be called when a comment is found by the lexer. The comment
     * can be stored (but is not required to) then attached to the next
     * token (via the {@link ldf.java_cup.runtime.Symbol#setPrevComment} method).
     */
    Comment newComment(
            int lineL, int columnL, int offsetL
    );

    /**
     * To be called when whitespace has been encountered. It uses the
     * position to mark the end of the previous token (if not already
     * set).
     */
    void signalWhitespace(
            int lineL, int columnL, int offsetL
    );

}
