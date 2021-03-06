package mini_camel.tests;

import mini_camel.ast.AstExp;
import mini_camel.comp.MyCompiler;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

public abstract class TestHelper {

    @Nonnull
    public static AstExp parse(@Nonnull String s) {
        return parse(new StringReader(s));
    }

    @Nonnull
    public static AstExp parse(@Nonnull Reader r) {
        try {
            MyCompiler comp = new MyCompiler(r);
            if (!comp.parseCode()) {
                comp.printErrors(System.err);
                throw new RuntimeException("Could not parse sample test");
            }
            return comp.getParseTree();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A list of sample files that have been included as resources in the
     * `mini_camel.tests` package. Names in this list don't include the
     * file extension (.ml).
     */
    private static final String[] samples = {
            "ack", "adder", "cls-bug", "cls-bug2", "cls-rec", "comm",
            "even-odd", "fib", "float", "funcomp", "gcd", "inprod-loop",
            "inprod-rec", "inprod", "join-reg", "join-reg2", "join-stack",
            "join-stack2", "join-stack3", "matmul-flat", "matmul",
            "non-tail-if", "non-tail-if2", "print", "shuffle", "spill",
            "spill2", "spill3", "sum-tail", "sum"
    };

    /**
     * Caches the result of the {@link #allSamples()} method.
     */
    private static Map<String, AstExp> parsedSamples;

    @Nonnull
    public static Map<String, AstExp> allSamples() {
        if (parsedSamples != null) return parsedSamples;

        parsedSamples = new LinkedHashMap<>();
        for (String s : samples) {
            parsedSamples.put(s, parse(new InputStreamReader(
                    TestHelper.class.getResourceAsStream(s + ".ml")
            )));
        }
        parsedSamples = Collections.unmodifiableMap(parsedSamples);

        return parsedSamples;
    }

}
