package mini_camel.type;

import java.util.Collections;
import java.util.List;

public final class TTuple extends Type {
    public final List<Type> items;

    public TTuple(List<Type> items) {
        this.items = Collections.unmodifiableList(items);
    }

    @Override
    public String toString() {
        String s = items.toString();
        return "(" + s.substring(1, s.length()-2) + ")";
    }
}