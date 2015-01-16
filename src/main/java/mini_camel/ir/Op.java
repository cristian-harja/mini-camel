package mini_camel.ir;

public abstract class Op
{
    public enum Type {
        VAR,
        CONST
    }

    public abstract Type getType();
}
