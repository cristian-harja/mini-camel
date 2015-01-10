package mini_camel.transform;

import mini_camel.Lexer;
import mini_camel.Parser;
import mini_camel.ast.*;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class AlphaConvTest {

    @Nonnull
    private static AstExp parse(String s) throws Exception {
        Parser p = new Parser(new Lexer(new StringReader(s)));
        AstExp result = (AstExp) p.parse().value;
        if (result == null) {
            throw new Exception("Parse error");
        }
        return result;
    }

    private static void testOnCode(String s) throws Exception {
        AlphaConv ac = new AlphaConv();
        AstExp result = ac.applyTransform(parse(s));
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
        testOnCode("let x = 1 in (let x = 1 in (x + x))");
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
            public void visit(AstLet e) {
                String id = e.id.id;
                bind(e, id);
                super.visit(e);
                unbind(id);
            }

            @Override
            public void visit(AstLetRec e) {
                String id = e.fd.id.id;
                super.visit(e);
                unbind(id);
            }
        });
    }
}