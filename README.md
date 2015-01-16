Mini-Camel
==========

## Objective

This is a group project (part of the MoSIG graduate program) in which we are
required to implement a compiler for the [min-caml][1] language.

## Building and running

In order to build and run this project you need to have installed Java and
Apache Maven on your machine. Make sure the `java` and `mvn` commands are in
your PATH. You also need an Internet connection the first time you build the
project and approximately 50 MB of disk space (for Maven to do its job).

To compile our program, just execute `mvn package`. This will download all
dependencies, generate the parser, run the unit tests and finally, compile
the project. If successful, a `target/mini-camel.jar` file will be created.

If you only want to execute the unit tests, use `mvn test`.

To run our program, execute `java -jar target/mini-camel.jar` (after compiling
the project) followed by any command line options that are specific to our
application. By running the application with no command line arguments, you
can see a brief description of the available commands. For example:
`java -jar target/mini-camel.jar -v` will print the current version number of
our application. 

## Using our program

The application will parse the source code written in the min-caml language
(you can find some samples in `src/test/resources/mini_camel/tests`) and
output assembly code for ARM processors (but not machine code). You also need
to feed this output to an assembler in order to produce an ARM executable.

For example, given a source file `input.ml` (written in min-caml), you should
execute these commands in order to produce an ARM executable file:

- `java -jar target/mini-camel.jar input.ml -o my-assembly.s`
- `arm-none-eabi-as -o my-object.o   my-assembly.s ARM/libmincaml.S`
- `arm-none-eabi-ld -o my-executable my-object.o`
- `qemu-arm ./my-executable` (to run the program in an emulator)

## Documentation

For the most part, we used JavaDoc to document the code of our application.
By running `mvn javadoc:javadoc` (or `mvn site`), an HTML version of our
documentation will be generated into `target/site/apidocs`. Some warnings
might be displayed, but this aspect will be fixed in a future release.

## Navigating the project files

The following files and folders are provided by us:

- `src/main/cup/Parser.cup` - This file describes the grammar of the
        *min-caml* language.
- `src/main/flex/Lexer.flex` - The scanner/tokenizer for our parser.
- `src/main/java/*` - Root folder of the Java source code.
- `src/test/java/*` - Root folder for the Java unit testing code.
- `src/test/resources/mini_camel/tests/*.ml` - Sample source codes written
        in the *min-caml* language. We must be able to compile these files
        by the end of the project.

The following are generated from `Parser.cup` and `Lexer.flex` by running
the appropriate Maven tasks, and become part of the compilation process:

- `target/generated-sources/cup` - Java sources of the generated parser.
- `target/generated-sources/flex` - Java sources of the generated lexer.

## Licensing

All the files and source code included with this project are licensed under
the Public Domain, with no warranty and no copyright restrictions imposed
by us.

You may freely use, modify and/or distribute the code, whether it is for
commercial or non-commercial purposes, with no need for attribution.

[![Public Domain Mark][2]][3]

[1]: https://esumii.github.io/min-caml/paper.pdf
[2]: https://licensebuttons.net/p/mark/1.0/80x15.png
[3]: http://creativecommons.org/publicdomain/mark/1.0/