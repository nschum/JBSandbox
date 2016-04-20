package de.nschum.jbsandbox;

import de.nschum.jbsandbox.source.SourceFile;
import de.nschum.jbsandbox.ui.EditorWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Optional;

public class JBSandbox {

    public static void main(String[] args) {

        System.setProperty("apple.laf.useScreenMenuBar", "true");

        EditorWindow.showNewEditorWindow(args.length > 0 ? Optional.of(new File(args[0])) : Optional.empty());
    }

    private static SourceFile createFile(String path) throws FileNotFoundException {
        if (path.equals("-")) {
            return new SourceFile("-", new InputStreamReader(System.in));
        } else {
            return new SourceFile(new File(path).getName(), new FileReader(path));
        }
    }
}
