package hudson.plugins.playground;

import hudson.plugins.analysis.util.Singleton;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;
import hudson.plugins.ast.factory.AstFactory;
import hudson.plugins.checkstyle.parser.CheckStyleParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
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
        String filenameAfter = "InsertLine2.java";

        StringBuilder stringBuilder = new StringBuilder();
        String path = stringBuilder.append(workspace).append("+src+test+resources+hudson+plugins+checkstyle+parser+")
                .toString();

        String pathBefore = stringBuilder.append("before+").append(filenameBefore).toString();
        stringBuilder.setLength(0);
        String pathAfter = stringBuilder.append(path).append("after+").append(filenameAfter).toString();

        String fileSeparator = System.getProperty("file.separator");
        pathBefore = pathBefore.replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));
        pathAfter = pathAfter.replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));

        FileAnnotation beforeWarning = readWarning("before/InsertLine.xml");
        Ast ast = AstFactory.getInstance(pathBefore, beforeWarning);

        ast.runThroughAST(ast.getAbstractSyntaxTree());
        System.out.println("--+--");
        ast.printList(ast.getElementsInSameLine());

        String s = ast.calcSha1(ast.getElementsInSameLine());
        System.out.println("----");
        System.out.println("Hash = " + s);

//        Ast ast2 = new JavadocMethodCheckAst(pathAfter, 8);
//        System.out.println("Hash2 = " + ast2.calcSha1(ast2.getElementsInSameLine()));
    }

    private static FileAnnotation readWarning(final String fileName) {
        try {
            CheckStyleParser parser = new CheckStyleParser();
            Collection<FileAnnotation> warnings = null;
            warnings = parser.parse(read(fileName), "-");
            return Singleton.get(warnings);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Can't read CheckStyle file " + fileName, e);
        }
    }

    private static InputStream read(final String fileName) {
        return Test.class.getResourceAsStream(fileName);
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