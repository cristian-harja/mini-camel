package mini_camel.ast;

public abstract class AstExp {
    public abstract void accept(Visitor v);
    public abstract <T, U> T accept(Visitor2<T, U> v, U arg);
}
