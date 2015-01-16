package mini_camel.ir;

public class Jump extends Instr {
    private Label label;

    public Jump(Label l) {
        label = l;
    }

    @Override
    public Type getType() {
        return Type.JUMP;
    }

    @Override
    public String toString() {
        return "JMP " + label.getName();
    }

    public Label getLabel() {
        return label;
    }
}
