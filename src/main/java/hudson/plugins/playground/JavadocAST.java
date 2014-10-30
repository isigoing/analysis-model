package hudson.plugins.playground;

import hudson.plugins.analysis.util.model.FileAnnotation;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * Abstract Syntax Tree for analyzing Javadoc-warnings.
 *
 * @author Christian M&ouml;stl
 */
public class JavadocAST extends AST {

    /**
     * Creates a new instance of {@link JavadocAST}.
     *
     * @param filename
     *            the file
     * @param fileAnnotation
     *            the FileAnnotation
     */
    public JavadocAST(final String filename, final FileAnnotation fileAnnotation) {
        super(filename, fileAnnotation);
    }

    @Override
    public List<DetailAST> chooseArea() {
        // TODO
        List<DetailAST> elementsInLine = getElementsInSameLine();

        List<DetailAST> chosenElements = new ArrayList<DetailAST>();


        // Package Javadoc?

        // Method Javadoc?
        // Komplette SLIST anschauen...

        // Style Javadoc?

        // Type Javadoc?

        // Variable Javadoc?

        // Write tag?

        return chosenElements;
    }
}