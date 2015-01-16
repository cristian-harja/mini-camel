package mini_camel.ir;

public class BranchEq extends Instr {
    private Label label;

    public BranchEq(Label l) {
        label = l;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public Type getType() {
        return Type.BEQ;
    }

    @Override
    public String toString() {
        return "BEQ " + label;
    }
}
