package mini_camel.ast;

import ldf.java_cup.runtime.Symbol;

import javax.annotation.Nonnull;

public abstract class AstNode {
    private Symbol symbol;

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        if (this.symbol != null) {
            throw new IllegalStateException();
        }
        this.symbol = symbol;
    }

    /**
     * <p>For debugging purposes; returns a string representation of the AST
     * node (by reproducing the source code that let to this syntax tree).
     * </p>
     * <p>The graphical debugger that we are using displays the result of
     * {@code .toString} next to each variable, so this makes it easier for
     * us to understand the code represented by each AST object.
     * </p>
     */
    @Nonnull
    public abstract String toString();

}
