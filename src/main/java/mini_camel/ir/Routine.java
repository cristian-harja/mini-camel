package mini_camel.ir;

/**
 * Created by lina on 1/14/15.
 */
public class Routine extends Instr
{
    private String name;
    private int value;

    public Routine(String s, int v)
    {
        name = s;
        value = v;
    }

    public Routine(String s)
    {
        name = s;
    }

    public Routine(){}

    public String getName()
    {
        return name;
    }

    public int getVal()
    {
        return value;
    }

}
