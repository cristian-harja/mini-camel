package mini_camel;

public final class Id {
    public final String id;

    public Id(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    static int x = -1;

    static Id gen() {
        x++;
        return new Id("?v" + x);
    }
}
