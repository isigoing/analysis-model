package hudson.plugins.ast.factory;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.specific.DefaultAst;
import hudson.plugins.ast.specific.JavadocMethodCheckAst;
import hudson.plugins.ast.specific.StrictDuplicateCodeCheckAst;

/**
 * Factory for abstract syntax trees.
 *
 * @author Christian M&ouml;stl
 */
public final class AstFactory {

    private AstFactory() {
        // prevents initialization
    }

    /**
     * Creates an instance of a specific {@link Ast}.
     *
     * @param filename
     *            the file
     * @param fileAnnotation
     *            the {@link FileAnnotation}
     * @return the specific ast.
     */
    public static Ast getInstance(final String filename, final FileAnnotation fileAnnotation) {
        String type = fileAnnotation.getType();
        Ast ast;
        // Notiz: switch case statement erst ab Java 1.7

        if ("JavadocMethodCheck".equals(type)) {
            ast = new JavadocMethodCheckAst(filename, fileAnnotation);
        }/*
        else if ("com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocPackageCheck".equals(type)) {
            /*
             * Vergleiche nur(!) Klassennamen im Package. (ohne AST lösen...) Wenn Klasse hinzugefügt wird, wird Warning
             * fälschlicherweise als neu deklariert. TODO
             *
        }*/
        else if ("com.puppycrawl.tools.checkstyle.checks.duplicates.StrictDuplicateCodeCheck".equals(type)) {
            ast = new StrictDuplicateCodeCheckAst(filename, fileAnnotation);
        }
        else {
            ast = new DefaultAst(filename, fileAnnotation);
        }

        return ast;
    }
}
