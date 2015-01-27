package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import java.util.Collections;
import java.util.List;

/**
 * Created by mommess on 27/01/15.
 */
public class Apply_cls implements Instr{
    public final Var cls;
    public final Var ret;
    public final List<Operand> args;

    public Apply_cls(Var cls, Var ret, List<Operand> l){
        this.cls = cls;
        this.ret = ret;
        args = Collections.unmodifiableList(l);
    }

    @Override
    public Instr.Type getInstrType() {
        return Type.APPLY_CLS;
    }

    @Override
    public String toString() {
        return "Apply_cls : " + cls + " " + args;
    }
}