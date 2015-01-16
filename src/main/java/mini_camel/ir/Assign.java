package mini_camel.ir;

public class Assign extends Instr {
    Var leq;
    Op op;

    public Assign(Var v, Op o) {
        leq = v;
        op = o;
    }

    @Override
    public Type getType() {
        return Type.ASSIGN;
    }

    public Var getVar() {
        return leq;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public String toString() {
        return leq + " := " + op;
    }


}
