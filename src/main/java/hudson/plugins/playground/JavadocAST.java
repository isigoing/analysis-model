package hudson.plugins.playground;

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
     * @param lineOfWarning
     *            the linenumber
     */
    public JavadocAST(final String filename, final int lineOfWarning) {
        super(filename, lineOfWarning);
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