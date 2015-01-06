Mini-Camel
==========

## Objective

This is a group project (part of the MoSIG graduate program) in which we are
required to implement a compiler for the [min-caml][1] language.

## Building and running

In order to build and run this project you need to have installed Java and
Apache Maven on your machine. Further instructions will be added to this
file as the project is being developed.

## Navigating the project files

The following files and folders are provided by us:

- `src/main/cup/Parser.cup` - This file describes the grammar of the
        *min-caml* language.
- `src/main/flex/Lexer.flex` - The scanner/tokenizer for our parser.
- `src/main/java/*` - Root folder of the Java source code.
- `src/main/resources/` - This folder contains any files that might be
        needed at runtime by our Java application.
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
