package mini_camel.comp;

import ldf.java_cup.runtime.Symbol;
import ldf.java_cup.runtime.TokenFactoryImpl;
import mini_camel.ast.AstNode;

public class MyTokenFactory extends TokenFactoryImpl {

    private Symbol addAstBackReference(Symbol sym) {
        Object value = sym.value;
        if (value instanceof AstNode) {
            AstNode astNode = ((AstNode) value);
            if (astNode.getSymbol() == null) {
                astNode.setSymbol(sym);
            }
        }
        return sym;
    }

    @Override
    public Symbol newSymbol(
            String name, int id,
            Symbol left, Symbol right,
            Object value
    ) {
        return addAstBackReference(super.newSymbol(
                name, id, left, right, value
        ));
    }

    @Override
    public Symbol newSymbol(String name, int id, Object value) {
        return addAstBackReference(super.newSymbol(
                name, id, value
        ));
    }

    @Override
    public Symbol newEmptySymbol(
            String name, int id,
            Symbol previousSymbol, Object value
    ) {
        return addAstBackReference(super.newEmptySymbol(
                name, id, previousSymbol, value
        ));
    }

}
