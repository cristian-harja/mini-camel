package mini_camel.ir;

public class Const extends Op
{
    String constType;
    int valueI;
    float valueF;

    public Const (int value)
    {
        this.constType = "int"; valueI = value;
    }

    public Const (float value)
    {
        this.constType = "float"; valueF = value;
    }

    public Const (double value)
    {
        this.constType = "float"; valueF = (float)value;
    }

    public String getConstType(){return constType;}

    public int getValueI(){return valueI;}

    public float getValueF(){return valueF;}

    @Override
    public Type getType() {
        return Type.CONST;
    }

    @Override
    public String toString() {
        return Integer.toString(valueI);
    }


}
