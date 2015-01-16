package mini_camel.gen;

import java.lang.reflect.Field;

public class ParserHelper {

    private static final String[] symNames;

    static {
        Field[] fields = mini_camel.gen.sym.class.getFields();
        symNames = new String[fields.length];
        for (Field f : fields) {
            try {
                int symValue = f.getInt(null);
                String symName = f.getName();
                symNames[symValue] = symName;

            } catch (IllegalAccessException e) {
                continue; // should never happen
            }
        }
    }

    public static String getSymName(int sym) {
        if (sym < 0 || sym >= symNames.length) {
            return Integer.toString(sym);
        }
        return symNames[sym];
    }

}
