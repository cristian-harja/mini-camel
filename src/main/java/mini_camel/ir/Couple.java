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

    public Couple(List<Instr> l, Var t) {
        code = l;
        var = t;
    }

    public void addInstr(Instr i) {
        code.add(i);
    }

    public void setVar(Var v) {
        var = v;
    }

    public List<Instr> getInstr() {
        return code;
    }

    public Var getVar() {
        return var;
    }

    public void addListInstr(List<Instr> l) {
        for (Instr i : code)
            l.add(i);
    }

    public String toString() {
        return "Mon contenu est: " + code.toString() + " Var is " + var;
    }

}
