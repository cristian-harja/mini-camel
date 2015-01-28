package mini_camel.ast;

import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Placeholder for AST nodes that could not be parsed. Created by the error
 * recovery methods in the parser when the source code is syntactically
 * incorrect, but parsing can still continue on the remaining input.
 */
public final class AstErr extends AstExp {

    public void accept(Visitor v) {
        v.visit(this);
    }


    public <T> T accept(Visitor1<T> v) {
        return v.visit(this);
    }


    public <T, U> T accept(Visitor2<T, U> v, @Nullable U a) {
        return v.visit(a, this);
    }

    @Nonnull
    public String toString() {
        return "<error>";
    }
}
