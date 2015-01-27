package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import java.util.Collections;
import java.util.List;

/**
 * Created by mommess on 27/01/15.
 */
public class Make_cls implements Instr{
    public final Var v;
    public final Label fun;
    public final List<Operand> free_args;

    public Make_cls(Var v, Label l, List<Operand> list){
        this.v = v;
        fun = l;
        free_args = Collections.unmodifiableList(list);
    }

    @Override
    public Instr.Type getInstrType() {
        return Instr.Type.MAKE_CLS;
    }

    @Override
    public String toString() {
        return "Make_cls : " + fun + " " + free_args;
    }
}
