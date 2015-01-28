package mini_camel.type;

import mini_camel.comp.MyCompiler;
import mini_camel.util.Pair;
import mini_camel.ast.AstExp;
import mini_camel.tests.TestHelper;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.Map;

import static org.junit.Assert.*;

public class CheckerTest extends TestHelper {

    @SuppressWarnings("unused")
    private Checker debugTypes(AstExp pgm) {
        Checker c = new Checker(pgm, MyCompiler.PREDEFS);

        System.out.println("Input:");
        System.out.println("\t" + c.getProgram());
        System.out.println("Equations:");
        for (Pair<Type, Type> eq : c.getEquations()) {
            System.out.println("\t" + eq.left + " = " + eq.right);
        }
        System.out.println("Solution:");
        for (Map.Entry<String, Type> e : c.getSolution().entrySet()) {
            System.out.println("\t" + e.getKey() + " = " + e.getValue());
        }
        System.err.println("Conflicts:");
        for (Pair<Type, Type> e : c.getErrors()) {
            System.out.println("\t" + e.left + " = " + e.right);
        }

        return c;
    }

    private boolean wellTyped(@Nonnull AstExp pgm) {
        return new Checker(pgm, MyCompiler.PREDEFS).wellTyped();
    }

    @Test
    public void testAllSamples() {
        // All the samples must be well-typed
        for (Map.Entry<String, AstExp> sample : allSamples().entrySet()) {
            assertTrue(sample.getKey(), wellTyped(sample.getValue()));
        }
    }

    @Test
    public void test1() {
        // Not well-typed, because the type of `y` can't be determined.
        assertFalse(wellTyped(parse(
                "let rec id x y = x in print_int (id 1 id)"
        )));
    }
}