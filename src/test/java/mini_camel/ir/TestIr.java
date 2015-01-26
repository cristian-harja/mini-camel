package mini_camel.ir;

import mini_camel.ast.*;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lina on 1/14/15.
 */
public class TestIr {

    /*@Nonnull
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
        AstExp result = ac.compute(parse(s));
        checkCorrectness(result);
    }*/
/*
    @Test
    public void test1() throws Exception {
        testOnCode("let x = 1 in x");
    }
/*
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
                String id = e.id.id;
                bind(e, id);
                super.visit(e);
                unbind(id);
            }

            @Override
            public void visit(@Nonnull AstLetRec e) {
                String id = e.fd.id.id;
                super.visit(e);
                unbind(id);
            }
        });
    }*/

}
