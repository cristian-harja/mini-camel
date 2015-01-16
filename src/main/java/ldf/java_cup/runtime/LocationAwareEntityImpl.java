package ldf.java_cup.runtime;

/**
 * Base class for {@link ldf.java_cup.runtime.Symbol} and {@link ldf.java_cup.runtime.Comment}, both of which
 * need position information (line number, column number and absolute
 * offset) about themselves.
 *
 * @author Cristian Harja
 */
@SuppressWarnings("unused")
public class LocationAwareEntityImpl
        implements LocationAwareEntity {

    protected int lineL, columnL, offsetL = -1;
    protected int lineR, columnR, offsetR = -1;

    @Override
    public void setLeftPos(LocationAwareEntity l) {
        setLeftPos(l.getLineL(), l.getColumnL(), l.getOffsetL());
    }

    public void setLeftPos(int line, int column, int offset) {
        lineL = line;
        columnL = column;
        offsetL = offset;
    }

    @Override
    public void setRightPos(LocationAwareEntity r) {
        setRightPos(r.getLineR(), r.getColumnR(), r.getOffsetR());
    }

    public void setRightPos(int line, int column, int offset) {
        lineR = line;
        columnR = column;
        offsetR = offset;
    }

    public final int getLineL() {
        return lineL;
    }

    public final int getLineR() {
        return lineR;
    }

    public final int getColumnL() {
        return columnL;
    }

    public final int getColumnR() {
        return columnR;
    }

    public final int getOffsetL() {
        return offsetL;
    }

    public final int getOffsetR() {
        return offsetR;
    }

}
