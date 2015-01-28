package mini_camel.util;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Pair<K, V> {
    public final K left;
    public final V right;

    public Pair(K left, V right) {
        this.left = left;
        this.right = right;
    }
}
