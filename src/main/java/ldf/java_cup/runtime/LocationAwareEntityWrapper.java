package ldf.java_cup.runtime;

/**
 * Provided for convenience, allows a sub-class to expose position
 * information from a {@link ldf.java_cup.runtime.LocationAwareEntity}
 * object to which it has access.
 *
 * @author Cristian Harja
 */
public abstract class LocationAwareEntityWrapper
        implements LocationAwareEntity {

    /**
     * @return the wrapped object whose information is to be exposed
     */
    protected abstract LocationAwareEntity getLocationAwareEntity();

    @Override
    public final void setLeftPos(LocationAwareEntity left) {
        getLocationAwareEntity().setLeftPos(left);
    }

    @Override
    public final void setLeftPos(int line, int column, int offset) {
        getLocationAwareEntity().setLeftPos(line, column, offset);
    }

    @Override
    public final void setRightPos(LocationAwareEntity right) {
        getLocationAwareEntity().setRightPos(right);
    }

    @Override
    public final void setRightPos(int line, int column, int offset) {
        getLocationAwareEntity().setRightPos(line, column, offset);
    }

    @Override
    public final int getLineL() {
        return getLocationAwareEntity().getLineL();
    }

    @Override
    public final int getLineR() {
        return getLocationAwareEntity().getLineR();
    }

    @Override
    public final int getColumnL() {
        return getLocationAwareEntity().getColumnL();
    }

    @Override
    public final int getColumnR() {
        return getLocationAwareEntity().getColumnR();
    }

    @Override
    public int getOffsetL() {
        return getLocationAwareEntity().getOffsetL();
    }

    @Override
    public int getOffsetR() {
        return getLocationAwareEntity().getOffsetR();
    }
}
