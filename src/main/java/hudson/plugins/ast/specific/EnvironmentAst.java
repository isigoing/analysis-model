package hudson.plugins.ast.specific;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * FIXME: Document type EnvironmentAst.
 *
 * @author Christian M&ouml;stl
 */
public class EnvironmentAst extends Ast {

    private final int surrounding;

    /**
     * Creates a new instance of {@link EnvironmentAst}.
     *
     * @param filename
     *            The filename
     * @param fileAnnotation
     *            the fileAnnotation
     * @param surrounding
     *            The surrounded element each above and below.
     */
    public EnvironmentAst(final String filename, final FileAnnotation fileAnnotation, final int surrounding) {
        super(filename, fileAnnotation);
        this.surrounding = surrounding;
    }

    @Override
    public List<DetailAST> chooseArea() {
        List<DetailAST> elementsInLine = getElementsInSameLine();

        //TODO: Problem welches Element ist der Startpunkt (Gesamte "Zeile"?)
        List<DetailAST> chosen = new ArrayList<DetailAST>();
        chosen.addAll(elementsBefore(elementsInLine.get(0), surrounding));
        chosen.addAll(elementsInLine);
        chosen.addAll(elementsAfter(elementsInLine.get(elementsInLine.size() - 1), surrounding));

        return chosen;
    }

    private List<DetailAST> elementsBefore(final DetailAST start, final int surrounding) {

        return null;
    }

    private List<DetailAST> elementsAfter(final DetailAST start, final int surrounding) {

        return null;
    }
}