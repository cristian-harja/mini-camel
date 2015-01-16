package mini_camel.ir;

public class BranchLe extends Instr {
    private Label label;

    public BranchLe(Label l) {
        label = l;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public Type getType() {
        return Type.BLE;
    }

    @Override
    public String toString() {
        return "BLE " + label.getName();
    }
}
