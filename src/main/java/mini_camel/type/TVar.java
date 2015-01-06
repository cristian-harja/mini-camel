package mini_camel.type;

public final class TVar extends Type {
    public final String v;
    public TVar(String v) {
        this.v = v;
    }

    public String toString() {
        return v;
    }
}
