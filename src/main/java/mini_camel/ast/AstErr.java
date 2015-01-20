package mini_camel.ast;

import mini_camel.ir.instr.Couple;

import javax.annotation.Nonnull;

/**
 * Placeholder for AST nodes that could not be parsed. Created by the error
 * recovery methods in the parser when the source code is syntactically
 * incorrect, but parsing can still continue on the remaining input.
 */
public final class AstErr extends AstExp {
    @Override
    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    @Override
    public Couple accept(@Nonnull Visitor3 v) {
        return v.visit(this);
    }

    @Override
    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U arg) {
        return v.visit(arg, this);
    }

    @Override
    public String toString() {
        return "<error>";
    }
}
