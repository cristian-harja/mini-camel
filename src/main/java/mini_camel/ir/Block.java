package mini_camel.ir;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lina on 1/12/15.
 */
public class Block
{
    Block lBranch; //Left
    Block rBranch; //Right
    List<Instr> sequence;
    private int label = 0;

    public Block()
    {
        lBranch = null;
        rBranch = null;
        sequence  = new ArrayList<Instr>();
    }

    public void setL(Block b)
    {
        lBranch = b;
    }

    public void setR(Block b)
    {
        rBranch = b;
    }

    public void addInstr(Instr i)
    {
        sequence.add(i);
        label ++;
        i.setLabel(label);
    }

    public void printInfo()
    {
        for (Instr i : sequence) {
            i.printInfo();
            System.out.println("------------------------------");
        }
    }

}
