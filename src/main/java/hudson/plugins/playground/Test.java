package hudson.plugins.playground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Klasse zum reinen Ausprobieren.
 *
 * @author Christian M&ouml;stl
 */
public final class Test {

    private Test() {

    }

    /**
     * main-Method.
     *
     * @param args
     *            args.
     * @throws RecognitionException
     *             a.
     * @throws TokenStreamException
     *             a.
     * @throws IOException
     *             a.
     */
    public static void main(final String[] args) throws RecognitionException, TokenStreamException, IOException {
        String workspace = System.getProperty("user.dir");
        String filenameBefore = "InsertLine.java";
        String filenameAfter = "InsertLine3.java";

        StringBuilder stringBuilder = new StringBuilder();
        String path = stringBuilder.append(workspace).append("+src+test+resources+hudson+plugins+checkstyle+parser+")
                .toString();

        String pathBefore = stringBuilder.append("before+").append(filenameBefore).toString();
        stringBuilder.setLength(0);
        String pathAfter = stringBuilder.append(path).append("after+").append(filenameAfter).toString();

        String fileSeparator = System.getProperty("file.separator");
        pathBefore = pathBefore.replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));
        pathAfter = pathAfter.replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));

        /*
         * TODO: IDEE: AST ast; switch(category) case "javadoc": ast = new JavadocAST(filename, linenumber); break; case
         * "whitespace": ast = new WhitespaceAST(filename, linenumber); break; ...
         */
        AST ast = new JavadocAST(pathBefore, 7);

        ast.runThroughAST(ast.getAbstractSyntaxTree());
        System.out.println("--+--");
        ast.printList(ast.getElementsInSameLine());
    }

    /**
     * Returns a list of siblings in AST-data-structure.
     *
     * @param ast
     *            The AST.
     * @return A list of siblings in AST-data-structure.
     */
    public static List<DetailAST> getSiblings(final DetailAST ast) {
        List<DetailAST> siblings = new ArrayList<DetailAST>();

        for (DetailAST a = ast; a != null; a = a.getNextSibling()) {
            siblings.add(a);
        }
        for (DetailAST a = ast; a != null; a = a.getPreviousSibling()) {
            siblings.add(a);
        }
        return siblings;
    }

    /**
     * Prints the list.
     *
     * @param list
     *            List which should be printed.
     */
    public void printList(final List<DetailAST> list) {
        if (list != null) {
            for (DetailAST ast : list) {
                System.out.println(ast.getText() + ", " + TokenTypes.getTokenName(ast.getType()));
            }
        }
        else {
            System.out.println("Keine Elemente...");
        }
    }

    /**
     * Tests, if two AST are equal. For example permutations of methods are indifferent and therefore the ast returns
     * true.
     *
     * @param ast1
     *            The first AST.
     * @param ast2
     *            The second AST.
     * @return <code>true</code>, if both ast are equal, otherwise return <code>false</code>.
     */
    public static boolean isEqualAST(final DetailAST ast1, final DetailAST ast2) {
        return ast1.equalsTree(ast2);
    }

    /**
     * Runs through the AST.
     *
     * @param root
     *            Expects the root of the AST which is run through
     */
    public static void runThroughAST(final DetailAST root, final int line) {
        if (root != null) {
            if (root.getLineNo() == line) {
                System.out.println(root.getText());
            }
            if (root.getFirstChild() != null) {
                runThroughAST(root.getFirstChild(), line);
            }
            if (root.getNextSibling() != null) {
                runThroughAST(root.getNextSibling(), line);
            }
        }
    }

    public long calcHashcode(final List<DetailAST> list) {
        if (list == null) {
            return 0;
        }
        else {
            long primenumber = 7;
            long hashCode = 0;

            for (DetailAST element : list) {
                hashCode = (hashCode + element.getType()) * primenumber;
            }

            return hashCode;
        }
    }

}