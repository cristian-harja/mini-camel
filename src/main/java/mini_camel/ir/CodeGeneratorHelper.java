package mini_camel.ir;

import mini_camel.ir.instr.Instr;
import mini_camel.ir.instr.Label;
import mini_camel.ir.op.Var;
import mini_camel.util.Pair;
import mini_camel.util.Visitor2;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CodeGeneratorHelper implements Visitor2<
        CodeGeneratorHelper.Couple, CodeGeneratorHelper.Branches> {

    public static final class Couple {

        private List<Instr> code;
        private Var var;

        public Couple(List<Instr> l, @Nullable Var t) {
            code = l;
            var = t;
        }

        public String toString() {
            return String.format("Couple(%s, %s)", var.name, code.toString());
        }

        public List<Instr> getInstr() {
            return code;
        }

        public Var getVar() {
            return var;
        }
    }

    public static final class Branches extends Pair<Label, Label> {

        public Branches(Label ifTrue, Label ifFalse) {
            super(ifTrue, ifFalse);
        }

        public Label ifTrue() {
            return left;
        }

        public Label ifFalse() {
            return right;
        }
    }


}
