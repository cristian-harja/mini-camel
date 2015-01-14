package mini_camel.ir;

/**
 * Created by yassine tout seul on 1/12/15.
 */
public class Instr
{
    private int label;
    public Instr()
    {
    }

    void setLabel(int label)
    {
        this.label = label;
    }

    public void printInfo()
    {
        System.out.println("Label : "+label);
    }

    public static class Eq{}
    public static class Leq{}
    public static class Geq{}

    public static class Get{}
    public static class Set{}

}