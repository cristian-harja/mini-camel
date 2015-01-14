package mini_camel.comp;

import mini_camel.ast.AstExp;
import mini_camel.ast.Id;
import mini_camel.tests.TestHelper;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class FreeVarVisitorTest extends TestHelper {

    private void assertFreeVars(String sourceCode, String ... vars) throws Exception {
        AstExp root = parse(sourceCode);
        FreeVarVisitor visitor = new FreeVarVisitor();
        Set<String> expectedFreeSet = new LinkedHashSet<>();
        Set<String> returnedFreeSet = new LinkedHashSet<>();

        Collections.addAll(expectedFreeSet, vars);

        root.accept(visitor);
        for (Id var : visitor.getFreeVariables()) {
            returnedFreeSet.add(var.id);
        }

        assertEquals(expectedFreeSet, returnedFreeSet);
    }

    @Test
    public void testAllSamples() throws Exception {
        FreeVarVisitor visitor = new FreeVarVisitor(
                "print_newline", "print_int", "abs_float", "sqrt", "sin",
                "cos", "float_of_int", "int_of_float", "truncate"
        );
        for (Map.Entry<String, AstExp> e: allSamples().entrySet()) {
            e.getValue().accept(visitor);
            assertEquals(e.getKey(), // name of the sample
                    Collections.<Id>emptySet(), // expecting no free vars
                    visitor.getFreeVariables() // actual result
            );
        }
    }

    @Test
    public void test1() throws Exception {
        assertFreeVars("a+b+c",
                "a", "b", "c"
        );
    }

    @Test
    public void test2() throws Exception {
        assertFreeVars("let x = a in let x = b in c",
                "a", "b", "c"
        );
    }

    @Test
    public void test3() throws Exception {
        assertFreeVars("let x = a in ((let x = b in c) + x)",
                "a", "b", "c"
        );
    }

    @Test
    public void test4() throws Exception {
        assertFreeVars(
                "let rec ack x y =\n" +
                "  if x <= 0 then y + 1 else\n" +
                "  if y <= 0 then ack (x - 1) 1 else\n" +
                "  ack (x - 1) (ack x (y - 1)) in ack 3 10"
        );
    }

    @Test
    public void test5() throws Exception {
        assertFreeVars(
                "let rec make_adder x =\n" +
                "  let rec adder y = x + y in\n" +
                "  adder in\n" +
                "make_adder 3"
        );
    }

    @Test
    public void test6() throws Exception {
        assertFreeVars(
                "let rec f x = x + 123 in\n" +
                "let rec g y = f in\n" +
                "g 456"
        );
    }

    @Test
    public void test7() throws Exception {
        assertFreeVars("let x = (* bla *) 1 in 2"
        );
    }

    @Test
    public void test8() throws Exception {
        assertFreeVars("if a then not (a >= b) else not d",
                "a", "b", "d"
        );
    }


    @Test
    public void test9() throws Exception {
        assertFreeVars(" if a+b then c else c-a",
                "a", "b", "c"
        );
    }

}