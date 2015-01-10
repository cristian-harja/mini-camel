package mini_camel.type;

public final class TArray extends Type {
    public final Type elementType;

    public TArray(Type t) {
        elementType = t;
    }

    @Override
    public String toString() {
        return "[" + elementType + "]";
    }
}