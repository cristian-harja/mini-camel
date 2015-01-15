package mini_camel.ir;

/**
 * Created by yassine tout seul on 1/12/15.
 */
public abstract class Instr
{
    private int label;

    public static enum Type {
        ADD_I, // int
        ADD_F, // float
        SUB_I, // int
        SUB_F, // float
        EQ,    //equality
        ASSIGN, //assignment
        MUL_F,  // multiplication
        DIV_F,
        CALL,   // function

    }

    public Instr()
    {
    }

    public abstract Type getType();//Regarder ADD
    public abstract String toString();// Regarder ADD


    void setLabel(int label)
    {
        this.label = label;
    }

    public void printInfo()
    {
        System.out.println("Label : "+label);
    }
}