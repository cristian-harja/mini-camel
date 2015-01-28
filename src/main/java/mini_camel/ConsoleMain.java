package mini_camel;

import mini_camel.comp.MyCompiler;

import java.io.*;

public class ConsoleMain {

    private static final int EXIT_ERROR = 1;
    private static final int EXIT_SUCCESS = 0;

    private int optionsSelected;
    private String outputFileName;
    private String inputFileName;
    private boolean displayHelp;
    private boolean displayVersion;
    private boolean onlyParse;
    private boolean onlyTypeCheck;
    private boolean printAST;
    private boolean printIR;
    private boolean printASM;

    private Reader in;
    private PrintStream out;
    private MyCompiler comp;


    static public void main(String argv[]) {
        ConsoleMain main = new ConsoleMain();
        if (!main.parseCommandLine(argv)) {
            System.exit(EXIT_ERROR);
            return;
        }
        if (main.execute()) {
            System.exit(EXIT_SUCCESS);
        } else {
            System.exit(EXIT_ERROR);
        }
    }

    private boolean parseCommandLine(String argv[]) {
        int i = 0, n = argv.length;

        if (n == 0) {
            printHelp();
            return false;
        }

        while (i < n) {
            String arg = argv[i++];
            if (arg.equals("-h")) {
                optionsSelected++;
                displayHelp = true;
                continue;
            }
            if (arg.equals("-v")) {
                optionsSelected++;
                displayVersion = true;
                continue;
            }
            if (arg.equals("-p")) {
                optionsSelected++;
                onlyParse = true;
                continue;
            }
            if (arg.equals("-t")) {
                optionsSelected++;
                onlyTypeCheck = true;
                continue;
            }
            if (arg.equals("-A")) {
                optionsSelected++;
                printAST = true;
                continue;
            }
            if (arg.equals("-I")) {
                optionsSelected++;
                printIR = true;
                continue;
            }
            if (arg.equals("-o")) {
                if (i == n) {
                    serr("Missing filename after `-o`");
                    return false;
                }
                if (outputFileName != null) {
                    serr("Duplicate `-o` argument");
                    return false;
                }

                outputFileName = argv[i++];
                continue;
            }
            if (inputFileName == null) {
                inputFileName = arg;
                continue;
            }
            serr("Unexpected argument " + i + ": \n" + arg);
            return false;
        }

        if (optionsSelected > 1) {
            serr("Too many actions (you must pick one).");
            return false;
        }

        if (optionsSelected == 0) {
            printASM = true;
        }

        if (inputFileName == null) {
            in = new BufferedReader(new InputStreamReader(System.in));
        } else {
            try {
                in = new FileReader(inputFileName);
            } catch (FileNotFoundException e) {
                serr("File inaccessible: " + e.getMessage());
                return false;
            }
        }

        if (outputFileName == null) {
            out = System.out;
        } else {
            try {
                out = new PrintStream(new FileOutputStream(outputFileName));
            } catch (FileNotFoundException e) {
                serr("File inaccessible: " + e.getMessage());
                return false;
            }
        }

        return true;
    }

    private boolean execute() {
        if (displayHelp) {
            printHelp();
            return true;
        }
        if (displayVersion) {
            printVersionInfo();
            return true;
        }
        comp = new MyCompiler(in);
        boolean result = executeCompiler();
        comp.printErrors(System.err);
        return result;
    }

    private boolean executeCompiler() {
        // Parse the code
        boolean parseOk = comp.parseCode();
        if (!parseOk && !printAST) return false;
        if (onlyParse) return true;

        // Print the AST (if requested)
        if (printAST) {
            comp.outputAST(out);
            out.println();
            return parseOk;
        }

        // Type check
        if (!comp.freeCheck()) return false;
        if (!comp.typeCheck()) return false;
        if (onlyTypeCheck) return true;

        // Compile
        if (!comp.preProcessCode()) return false;
        /*//
        // there's a bug in closure conversion, which crashes the compiler
        if (!comp.performKNormalization()) return false;
        if (!comp.codeGeneration_new()) return false;
        /*/
        if (!comp.codeGeneration_old()) return false;
        //*/

        // Output
        if (printIR) {
            comp.outputIR(out);
            return true;
        } else {
            comp.outputAssembly(out);
            return true;
        }
    }

    private static void printVersionInfo() {
        Package p = ConsoleMain.class.getPackage();
        String v1 = p.getSpecificationVersion();
        String v2 = p.getImplementationVersion();
        if (v1 != null) {
            sout(v1);
            return;
        }
        if (v2 != null) {
            sout(v2);
            return;
        }
        sout("unknown version");
    }

    private static void serr(String s) {
        System.err.println(s);
    }

    private static void sout(String s) {
        System.out.println(s);
    }

    private static void printHelp() {
        sout("Usage: java -jar mini-camel.jar <action> [-o <output>] [<input>]");
        sout("Actions:");
        sout("\t-h\tDisplays this message");
        sout("\t-v\tDisplays the version number");
        sout("\t-p\tOnly parse the input (no further processing)");
        sout("\t-t\tOnly perform type checking (after parsing)");
        sout("\t-A\tPrint Abstract Syntax Tree (AST)");
        sout("\t-I\tPrint compiled code in Intermediate Representation (IR)");
        sout("Other options:");
        sout("\t-o <output>");
        sout("\t  \tOptional. Specifies an output file; default is stdout.");
        sout("\t<input>");
        sout("\t  \tOptional. Specifies the input file; default is stdin.");
    }
}

