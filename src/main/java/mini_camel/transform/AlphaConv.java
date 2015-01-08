package mini_camel.transform;

import mini_camel.Id;
import mini_camel.ast.AstExp;
import mini_camel.ast.AstLet;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlphaConv extends AstTransformHelper<AlphaConv.Ctx> {

    int lastId = 0;

    static class Ctx {
        Map<String, String> reMapping = new LinkedHashMap<>();
    }

    @Override
    public AstExp visit(Ctx ctx, AstLet e) {
        Ctx newCtx = new Ctx();
        newCtx.reMapping.putAll(ctx.reMapping);

        String newId = e.id.id + "_" + (++lastId);
        newCtx.reMapping.put(e.id.id, newId);

        AstExp new_e1 = e.e1.accept(this, ctx);
        AstExp new_e2 = e.e2.accept(this, newCtx);

        return new AstLet(new Id(newId), e.t, new_e1, new_e2);
    }
}
