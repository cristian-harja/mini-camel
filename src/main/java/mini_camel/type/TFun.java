package mini_camel.type;

public final class TFun extends Type {
    public final Type arg;
    public final Type ret;

    public TFun(Type arg, Type ret) {
        this.arg = arg;
        this.ret = ret;
    }

    public TFun() {
        this(Type.gen(), Type.gen());
    }

    @Override
    public String toString() {
        return "(" + arg + " -> " + ret + ")";
    }
}
