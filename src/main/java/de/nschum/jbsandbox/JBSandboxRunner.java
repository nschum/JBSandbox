package de.nschum.jbsandbox;

import de.nschum.jbsandbox.ast.ASTBuilder;
import de.nschum.jbsandbox.ast.ASTError;
import de.nschum.jbsandbox.ast.Program;
import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.interpreter.Interpreter;
import de.nschum.jbsandbox.interpreter.InterpreterRuntimeException;
import de.nschum.jbsandbox.parser.MissingTokenException;
import de.nschum.jbsandbox.parser.Parser;
import de.nschum.jbsandbox.parser.ParserTree;
import de.nschum.jbsandbox.parser.UnexpectedTokenException;
import de.nschum.jbsandbox.scanner.IllegalTokenException;
import de.nschum.jbsandbox.scanner.JBScanner;
import de.nschum.jbsandbox.scanner.ScannerToken;
import de.nschum.jbsandbox.source.SourceFile;

import java.io.*;
import java.util.List;

/**
 * Command line interface to interpreter (and indirectly parser)
 */
public class JBSandboxRunner {

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }

        SourceFile file;
        try {
            file = createFile(args[0]);
        } catch (FileNotFoundException e) {
            System.err.println("Could not read: " + e.getMessage());
            System.exit(1);
            return;
        }

        ErrorPrinter errorPrinter = new ErrorPrinter();
        try {
            List<ASTError> errors = run(file);
            errors.forEach(e -> errorPrinter.print(e.getMessage(), file, e.getLocation()));
        } catch (IOException e) {
            System.err.println("Could not read: " + e.getMessage());
        } catch (IllegalTokenException e) {
            errorPrinter.print("Illegal token", file, e.getLocation());
        } catch (UnexpectedTokenException e) {
            errorPrinter.print("Unexpected token", file, e.getLocation());
        } catch (MissingTokenException e) {
            errorPrinter.print(e.getMessage(), file, e.getLocation());
        } catch (InterpreterRuntimeException e) {
            errorPrinter.print(e.getMessage(), file, e.getLocation());
        }
    }

    private static SourceFile createFile(String path) throws FileNotFoundException {
        if (path.equals("-")) {
            return new SourceFile("-", new InputStreamReader(System.in));
        } else {
            return new SourceFile(new File(path).getName(), new FileReader(path));
        }
    }

    private static Reader createReader(String path) throws FileNotFoundException {
        if (path.equals("-")) {
            return new InputStreamReader(System.in);
        } else {
            return new FileReader(path);
        }
    }

    private static void printUsage() {
        System.err.println("usage:");
        System.err.println("java -cp JBSandbox.jar " + JBSandboxRunner.class.getName() + " [FILE]");
        System.err.println();
        System.err.println("  FILE:\tThe file to parse or \"-\" for reading a program from stdin");
    }

    private static List<ASTError> run(SourceFile file)
            throws IllegalTokenException, UnexpectedTokenException, MissingTokenException, IOException,
            InterpreterRuntimeException {

        JBGrammar grammar = new JBGrammar();
        Parser parser = new Parser(grammar);

        List<ScannerToken> tokens = new JBScanner().scan(file);
        ParserTree parserTree = parser.parse(tokens);
        ASTBuilder astBuilder = new ASTBuilder(grammar);
        Program syntaxTree = astBuilder.createSyntaxTree(parserTree);

        if (astBuilder.getErrors().isEmpty()) {
            new Interpreter(new BufferedWriter(new OutputStreamWriter(System.out))).execute(syntaxTree);
        }

        return astBuilder.getErrors();
    }
}
