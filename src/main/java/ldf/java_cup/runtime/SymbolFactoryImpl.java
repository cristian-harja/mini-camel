package ldf.java_cup.runtime;

/**
 * @author Cristian Harja
 */
public class SymbolFactoryImpl implements SymbolFactory {

    /**
     * This is the only method that directly instantiates {@code Symbol}
     * objects. If you sub-class {@code SymbolFactoryImpl}, you should
     * override this if you wish to create instances of a subclass of
     * {@code Symbol} (of your own).
     */
    protected Symbol newSymbol(
            String symName,
            int symCode,
            int parse_state
    ) {
        return new Symbol(symName, symCode, parse_state);
    }

    /**
     * Used by CUP to create the start symbol. In contrast with every
     * other kind of symbol, the internal {@code parse_state} needs to
     * be set explicitly in order for the parser to work.
     */
    @Override
    public Symbol startSymbol(
            String symName, int symCode,
            int parse_state
    ) {
        return newSymbol(symName, symCode, parse_state);
    }

    @Override
    public Symbol newSymbol(String name, int id) {
        return newSymbol(name, id, 0);
    }

    @Override
    public Symbol newSymbol(String name, int id, Object value) {
        Symbol s = newSymbol(name, id);
        s.value = value;
        return s;
    }

    @Override
    public Symbol newSymbol(
            String name, int id,
            Symbol left, Symbol right
    ) {
        Symbol s = newSymbol(name, id);
        s.setLeftRightSymbols(left, right);
        return s;
    }

    @Override
    public Symbol newEmptySymbol(
            String name, int id,
            Symbol previousSymbol
    ) {
        Symbol s = newSymbol(name, id);
        int line = previousSymbol.lineR;
        int column = previousSymbol.columnR;
        int offset = previousSymbol.offsetR;
        s.setLeftPos(line, column, offset);
        s.setRightPos(line, column, offset);
        return s;
    }

    @Override
    public Symbol newEmptySymbol(
            String name, int id,
            Symbol previousSymbol,
            Object value
    ) {
        Symbol s = newEmptySymbol(name, id, previousSymbol);
        s.value = value;
        return s;
    }

    @Override
    public Symbol newSymbol(
            String name, int id,
            Symbol left, Symbol right,
            Object value
    ) {
        Symbol s = newSymbol(name, id, left, right);
        s.value = value;
        return s;
    }


}
