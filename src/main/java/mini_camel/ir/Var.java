package mini_camel.ir;

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

    public String getName(){
        return varName;
    }

    @Override
    public String toString() {
        return varName;
    }
}