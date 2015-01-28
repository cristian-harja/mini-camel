package mini_camel.knorm;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Map;

@ParametersAreNonnullByDefault
public final class Program {
    @Nonnull
    public final Map<String, KFunDef> topLevel;

    @Nonnull
    public final KNode mainBody;

    public Program(Map<String, KFunDef> topLevel, KNode mainBody) {
        this.topLevel = Collections.unmodifiableMap(topLevel);
        this.mainBody = mainBody;
    }
}
