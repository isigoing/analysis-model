package hudson.plugins.ast.specific;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;

import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Creates the abstract syntax tree (AST).
 *
 * @author Christian M&ouml;stl
 */
public class NameMethodAst extends Ast {

    /**
     * Creates a new instance of {@link NameMethodAst}.
     *
     * @param filename
     *            The filename
     * @param fileAnnotation
     *            the fileAnnotation
     */
    public NameMethodAst(final String filename, final FileAnnotation fileAnnotation) {
        super(filename, fileAnnotation);
    }

    @Override
    public List<DetailAST> chooseArea() {
        Ast ast = new MethodAst(getFilename(), getFileAnnotation());

        List<DetailAST> chosenArea = ast.chooseArea();

        DetailAST first = chosenArea.get(0).getFirstChild();
        calcMethodName(first, 0);

        ast.setName(ident.getText());

        return chosenArea;
    }

    private DetailAST ident;

    private void calcMethodName(final DetailAST method, int counter) {
        if (method != null) {
            if (method.getType() == TokenTypes.IDENT && counter == 0) {
                ident = method;
                counter++;
            }
            if (method.getFirstChild() != null) {
                calcMethodName(method.getFirstChild(), counter);
            }
            if (method.getNextSibling() != null) {
                calcMethodName(method.getNextSibling(), counter);
            }
        }
    }
}