package mini_camel.ir;

import mini_camel.ast.AstExp;
import mini_camel.tests.TestHelper;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeGeneratorTest extends TestHelper {

    @Test
    void test1() {
        AstExp result = (AstExp) parse("let x = 1 in x = 1");



    }

}