package mini_camel.ir;

/**
 * Created by lina on 1/12/15.
 */
public class Var extends Op
{
    public String varName;

    public Var (String varName){this.varName = varName;}

    public void printInfo()
    {
        System.out.println("Variable value : "+varName);
    }

    @Override
    public Type getType() {
        return Type.VAR;
    }

    @Override
    public String toString() {
        return varName;
    }
}
