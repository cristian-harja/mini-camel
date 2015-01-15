package mini_camel.ir;

/**
 * Created by lina on 1/12/15.
 */
public abstract class Op
{
    public enum Type {
        VAR,
        CONST
    }
    public void printInfo(){}

    public abstract Type getType();
}
