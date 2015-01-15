package mini_camel.ir;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lina on 1/13/15.
 */
public class Couple {

    private List<Instr> code;
    private Var var;

    public Couple () {
        code = new ArrayList<>();
    }

    public Couple (List<Instr> l, Var t) {
        code = l;
        var = t;
    }

    public void setInstr(Instr i)
    {
        code.add(i);
    }

    public void setVar(Var v)
    {
        var = v;
    }
    List<Instr> getInstr()
    {
        return code;
    }
    Var getVar()
    {
        return  var;
    }

    void printCouple() {
        System.out.println("Mon contenu est: "+ code.toString()+"\n" +"Var is "+var);
    }
}
