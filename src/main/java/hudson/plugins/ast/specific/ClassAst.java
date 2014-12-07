package hudson.plugins.ast.specific;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Creates the abstract syntax tree for the complete class except inner types.
 *
 * @author Christian M&ouml;stl
 */
//TODO: Betreffende Klasse berücksichtigen und nicht die oberste aller Klassen!
public class ClassAst extends Ast {

    /**
     * Creates a new instance of {@link ClassAst}.
     *
     * @param filename
     *            The filename
     * @param fileAnnotation
     *            the fileAnnotation
     */
    public ClassAst(final String filename, final FileAnnotation fileAnnotation) {
        super(filename, fileAnnotation);
    }

    @Override
    public List<DetailAST> chooseArea() {
        DetailAST objBlock = getObjBlock(getAbstractSyntaxTree());

        getSpecialSiblings(objBlock.getFirstChild());
        List<DetailAST> chosenArea = new ArrayList<DetailAST>();

        chosenArea.addAll(elementsToObjBlock(getAbstractSyntaxTree()));

        chosenArea.add(objBlock);
        for (int i = 0; i < siblings.size(); i++) {
            clear();
            chosenArea.add(siblings.get(i));
            chosenArea.addAll(calcAllChildren(siblings.get(i).getFirstChild()));
        }

        return chosenArea;
    }

    private final List<DetailAST> siblings = new ArrayList<DetailAST>();

    private void getSpecialSiblings(final DetailAST element) {
        if (element != null) {
            if (element.getType() != TokenTypes.CLASS_DEF && element.getType() != TokenTypes.INTERFACE_DEF) {
                siblings.add(element);
            }
            if (element.getNextSibling() != null) {
                getSpecialSiblings(element.getNextSibling());
            }
        }
    }

    private List<DetailAST> elementsToObjBlock(final DetailAST root) {
        List<DetailAST> elementsToObjBlock = new ArrayList<DetailAST>();

        elementsToObjBlock(elementsToObjBlock, root);

        return elementsToObjBlock;
    }

    private void elementsToObjBlock(final List<DetailAST> list, final DetailAST root) {
        if (root.getType() != TokenTypes.OBJBLOCK) {
            list.add(root);
            if (root.getFirstChild() != null) {
                elementsToObjBlock(list, root.getFirstChild());
            }
            if (root.getNextSibling() != null) {
                elementsToObjBlock(list, root.getNextSibling());
            }
        }
    }
}