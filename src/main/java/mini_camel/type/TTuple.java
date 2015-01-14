package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * Represents the type of a tuple. The i<sup>th</sup> expression in a tuple
 * of this type has the type denoted by the i<sup>th</sup> element of the
 * {@link #items} field in this class.
 */
@Immutable
public final class TTuple extends Type {
    public final List<Type> items;

    public TTuple(@Nonnull List<Type> items) {
        this.items = Collections.unmodifiableList(items);
    }

    @Override
    public String toString() {
        String s = items.toString();
        return "(" + s.substring(1, s.length()-2) + ")";
    }
}