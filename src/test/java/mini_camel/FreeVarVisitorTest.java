package mini_camel;

import mini_camel.ast.AstExp;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class FreeVarVisitorTest {

    @Nonnull
    private AstExp parse(String s) throws Exception {
        Parser p = new Parser(new Lexer(new StringReader(s)));
        AstExp result = (AstExp) p.parse().value;
        if (result == null) {
            throw new Exception("Parse error");
        }
        return result;
    }

    private void assertFreeVars(String sourceCode, String ... vars) throws Exception {
        AstExp root = parse(sourceCode);
        FreeVarVisitor visitor = new FreeVarVisitor();
        root.accept(visitor);
        Set<String> expectedFreeSet = new LinkedHashSet<>();
        Collections.addAll(expectedFreeSet, vars);
        assertEquals(expectedFreeSet, visitor.free);
    }

    @Test
    public void test1() throws Exception {
        assertFreeVars("a+b+c",
                "a",
                "b",
                "c"
        );
    }

    @Test
    public void test2() throws Exception {
        assertFreeVars("let x = a in let x = b in c",
                "a",
                "b",
                "c"
        );
    }

    @Test
    public void test3() throws Exception {
        assertFreeVars("let x = a in ((let x = b in c) + x)",
                "a",
                "b",
                "c"
        );
    }
}