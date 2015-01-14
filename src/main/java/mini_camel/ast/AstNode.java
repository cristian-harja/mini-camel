package mini_camel.ast;

import ldf.java_cup.runtime.Symbol;

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
    }}
