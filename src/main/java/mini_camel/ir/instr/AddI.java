package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class AddI implements Instr {
    @Nonnull
    public final Var var;

    @Nonnull
    public final Operand op1, op2;

    public AddI(Var v, Operand operand1, Operand operand2) {
        this.var = v;
        this.op1 = operand1;
        this.op2 = operand2;
    }

    @Nonnull
    public Type getInstrType() {
        return Type.ADD_I;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "%s := %s + %s",
                var.name, op1.toString(), op2.toString()
        );
    }

}
