package mini_camel.comp;

import ldf.java_cup.runtime.LocationAwareEntity;

import javax.annotation.Nonnull;

public class ErrMsg implements Comparable<ErrMsg> {

    public Type type;
    public LocationAwareEntity loc;
    public String message;

    public Exception ex;

    public enum Type {
        INFO,
        WARN,
        ERROR
    }

    @Override
    public int compareTo(@Nonnull ErrMsg that) {
        int off1, off2;
        if (this.loc == null || that.loc == null) return 0;
        off1 = this.loc.getOffsetL();
        off2 = that.loc.getOffsetL();
        if (off1 != off2) return off1 - off2;
        off1 = this.loc.getOffsetR();
        off2 = that.loc.getOffsetR();
        return off1 - off2;
    }


}
