package hudson.plugins.ast.specific;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;

import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * FIXME: Document type StrictDuplicateCodeCheckAst.
 *
 * @author Christian M&ouml;stl
 */
public class StrictDuplicateCodeCheckAst extends Ast {

    /**
     * Creates a new instance of {@link StrictDuplicateCodeCheckAst}.
     * @param filename
     * @param fileAnnotation
     */
    public StrictDuplicateCodeCheckAst(final String filename, final FileAnnotation fileAnnotation) {
        super(filename, fileAnnotation);
    }

    @Override
    public List<DetailAST> chooseArea() {
        // FIXME Auto-generated method stub
        return null;
    }

}

