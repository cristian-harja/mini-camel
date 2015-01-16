package mini_camel.ir;

public class Label extends Instr {

    private static int x = 0;
    private String name;

    public Label(String name) {
        this.name = name;
    }

    public static Label gen() {
        return new Label("l"+x++);
    }

    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return Type.LABEL;
    }

    @Override
    public String toString() {
        return name + ":";
    }
}
