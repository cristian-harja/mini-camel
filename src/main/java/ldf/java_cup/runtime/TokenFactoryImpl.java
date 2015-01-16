package ldf.java_cup.runtime;

/**
 * @author Cristian Harja
 */
public class TokenFactoryImpl extends SymbolFactoryImpl
        implements TokenFactory {

    protected Comment lastComment;
    protected Symbol lastSymbol;
    protected boolean lastRightPosWasSet;

    /**
     * {@inheritDoc}
     */
    @Override
    public Symbol newToken(
            String symName, int symCode,
            int lineL, int columnL, int offsetL
    ) {
        markRightPos(lineL, columnL, offsetL);

        Symbol s = newSymbol(symName, symCode);
        s.setLeftPos(lineL, columnL, offsetL);
        s.prevComment = lastComment;

        lastSymbol = s;
        lastComment = null;
        lastRightPosWasSet = false;

        return s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Symbol newToken(
            String symName, int symCode,
            int lineL, int columnL, int offsetL,
            int lineR, int columnR, int offsetR
    ) {
        newToken(symName, symCode, lineL, columnL, offsetL);
        markRightPos(lineR, columnR, offsetR);
        return lastSymbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Symbol newEOF(
            String symName, int symCode,
            int lineL, int columnL,
            int offsetL
    ) {
        markRightPos(lineL, columnL, offsetL);

        Symbol t = newToken(
                symName, symCode,
                lineL, columnL, offsetL
        );

        t.setRightPos(lineL, columnL, offsetL);
        lastRightPosWasSet = true;

        lastSymbol = null;

        return t;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment newComment(
            int lineL, int columnL, int offsetL
    ) {
        markRightPos(lineL, columnL, offsetL);

        Comment c = new Comment(lastComment);
        c.setLeftPos(lineL, columnL, offsetL);
        lastComment = c;

        return c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signalWhitespace(
            int lineL, int columnL, int offsetL
    ) {
        markRightPos(lineL, columnL, offsetL);
    }

    /**
     * Marks the end position of whatever was the previously returned
     * object. Of course, it won't set it any more than once for an
     * object.
     */
    protected void markRightPos(
            int line, int column, int offset
    ) {
        if (lastRightPosWasSet) return;
        LocationAwareEntityImpl last = (lastComment != null)
                ? lastComment
                : lastSymbol;

        if (last != null) {
            last.setRightPos(line, column, offset);
        }
        lastRightPosWasSet = true;
    }

}
