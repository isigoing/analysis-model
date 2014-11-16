package hudson.plugins.ast.specific;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;

import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * FIXME: Document type MethodOrClassAst.
 *
 * @author Christian M&ouml;stl
 */
public class MethodOrClassAst extends Ast {

    /**
     * Creates a new instance of {@link MethodOrClassAst}.
     *
     * @param filename
     *            The filename
     * @param fileAnnotation
     *            the fileAnnotation
     */
    public MethodOrClassAst(final String filename, final FileAnnotation fileAnnotation) {
        super(filename, fileAnnotation);
    }

    @Override
    public List<DetailAST> chooseArea() {

        DetailAST element = getElementsInSameLine().get(0);

        if (isLevelOfMethod(element)) {
            return new MethodAst(getFilename(), getFileAnnotation()).chooseArea();
        }
        else {
            return new ClassAst(getFilename(), getFileAnnotation()).chooseArea();
        }
    }

    private boolean isLevelOfMethod(final DetailAST element) {
        if (element != null) {
            if (element.getType() == TokenTypes.METHOD_DEF || element.getType() == TokenTypes.CTOR_DEF) {
                return true;
            }
            else if (element.getType() == TokenTypes.CLASS_DEF || element.getType() == TokenTypes.INTERFACE_DEF) {
                return false;
            }
            else {
                isLevelOfMethod(element.getParent());
            }
        }
        return false;
    }
}