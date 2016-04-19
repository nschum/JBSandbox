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
        String path = args[0];

        try {
            List<ASTError> errors = run(createReader(path));
            printErrors(errors, path);
        } catch (IOException e) {
            System.err.println("Could not read: " + e.getMessage());
        } catch (IllegalTokenException e) {
            System.err.println("Illegal token: " + humanReadableLocation(path, e.getLocation()));
        } catch (UnexpectedTokenException e) {
            System.err.println("Unexpected token: " + humanReadableLocation(path, e.getLocation()));
        } catch (MissingTokenException e) {
            System.err.println(e.getMessage() + humanReadableLocation(path, e.getLocation()));
        } catch (InterpreterRuntimeException e) {
            System.err.println(e.getMessage() + ": " + humanReadableLocation(path, e.getLocation()));
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

    private static void printErrors(List<ASTError> errors, String path) {
        for (ASTError error : errors) {
            System.err.println("Parse error: " + humanReadableLocation(path, error.getLocation()));
        }
    }

    private static String humanReadableLocation(SourceLocation location) {
        return (location.getLine() + 1) + ":" + (location.getColumn() + 1);
    }

    private static String humanReadableLocation(String path, SourceLocation location) {
        return new File(path).getName() + ":" + humanReadableLocation(location);
    }

    private static String humanReadableLocation(String path, SourceRange location) {
        return new File(path).getName() + ":"
                + humanReadableLocation(location.getStart()) + "-"
                + humanReadableLocation(location.getEnd());
    }

    private static List<ASTError> run(Reader input)
            throws IllegalTokenException, UnexpectedTokenException, MissingTokenException, IOException,
            InterpreterRuntimeException {

        JBGrammar grammar = new JBGrammar();
        Parser parser = new Parser(grammar);

        List<ScannerToken> tokens = new JBScanner().scan(input);
        ParserTree parserTree = parser.parse(tokens);
        ASTBuilder astBuilder = new ASTBuilder(grammar);
        Program syntaxTree = astBuilder.createSyntaxTree(parserTree);

        if (astBuilder.getErrors().isEmpty()) {
            new Interpreter(new BufferedWriter(new OutputStreamWriter(System.out))).execute(syntaxTree);
        }

        return astBuilder.getErrors();
    }
}
