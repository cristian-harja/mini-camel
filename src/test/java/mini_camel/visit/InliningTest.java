package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.tests.TestHelper;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class InliningTest extends TestHelper {

    private static void testOnCode(String s) throws Exception {
        AstExp result = Inlining.compute(parse(s));
        checkCorrectness(result);
    }

    @Test
    public void test1() throws Exception {
        testOnCode("let x = 1 in x");
    }

    @Test
    public void test2() throws Exception {
        testOnCode("let x = y in z");
    }
    @Test
    public void test3() throws Exception {
        testOnCode("let rec f x = x + 1 in let rec g z a = z + a in f(g 1 2)");
    }

    @Test
    public void test4() throws Exception {
        testOnCode("let x = y in x");
    }


    @Test
    public void test5() throws Exception {
        testOnCode("let rec f x = f x in f");
    }

    @Test
    public void test6() throws Exception {
        testOnCode("let rec fact x = if (x <= 1.0) " +
                        "then 1.0 else (x *. (fact (x -. 1.0))) in fact 7.0"
        );
    }


    @Test
    public void test7() throws Exception {
        testOnCode("let x = x + 4 - z in z = x"
        );
    }

    @Test
    public void test8() throws Exception {
        testOnCode("let rec f x y = x *. y in 5."
        );
    }

    @Test
    public void test9() throws Exception {
        testOnCode("let rec sum x =" +
                        "  if (x <= 0) then 0 else " +
                        "((sum (x - 1)) + x) in sum 10000"
        );
    }

    @Test
    public void test10() throws Exception {
        testOnCode("let rec gcd m n =" +
                        "            if (m = 0) then n else(" +
                        "            if (m <= n) then (gcd m (n - m)) else(" +
                        "    (gcd n (m - n)))) in" +
                        "    gcd 21600 337500"
        );
    }

    @Test
    public void test11() throws Exception {
        testOnCode("let rec fact x = if (x <= 1.0) then 1.0 else (x *. (fact (x -. 1.0))) in fact 7.0"
        );
    }

    @Test
    public void test12() throws Exception {
        testOnCode("let rec f x = x + 1 in f 1");
    }

    @Test
    public void test13() throws Exception {
        testOnCode("let rec f x y z t a = x + y - z + a - t in f 1 2 3 4 5");
    }

    @Test
    public void test14() throws Exception {
        testOnCode("let x = 1 in let rec f y = x in f 1");
    }

    @Test
    public void test15() throws Exception {
        testOnCode("let rec f x = let rec g z = g z in 3 in f 2");
    }

    @Test
    public void test16() throws Exception {
        testOnCode("print_int (789+0)");
    }

    @Test
    public void test17() throws Exception {
        testOnCode("print_int 123");
    }

    private static void checkCorrectness(AstExp exp) {
        exp.accept(new DummyVisitor() {
            private Set<String> bound = new HashSet<>();

            private void bind(AstExp parentExp, String id) {
                if (bound.add(id)) return;
                throw new RuntimeException(id + " already bound in:\n" +
                        parentExp.toString()
                );
            }

            private void unbind(String id) {
                bound.remove(id);
            }

            @Override
            public void visit(@Nonnull AstLet e) {
                String id = e.decl.id;
                bind(e, id);
                super.visit(e);
                unbind(id);
            }

            @Override
            public void visit(@Nonnull AstLetRec e) {
                String id = e.fd.decl.id;
                super.visit(e);
                unbind(id);
            }
        });
    }
}