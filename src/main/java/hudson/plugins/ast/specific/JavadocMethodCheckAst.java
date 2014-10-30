package hudson.plugins.ast.specific;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * Abstract Syntax Tree for analyzing JavadocMethodCheck-warnings.
 *
 * @author Christian M&ouml;stl
 */
public class JavadocMethodCheckAst extends Ast {

    /**
     * Creates a new instance of {@link JavadocMethodCheckAst}.
     *
     * @param filename
     *            the file
     * @param fileAnnotation
     *            the fileAnnotation
     */
    public JavadocMethodCheckAst(final String filename, final FileAnnotation fileAnnotation) {
        super(filename, fileAnnotation);
    }

    @Override
    public List<DetailAST> chooseArea() {
        // TODO
        List<DetailAST> elementsInLine = getElementsInSameLine();

        List<DetailAST> chosenElements = new ArrayList<DetailAST>();

        return chosenElements;
    }
}