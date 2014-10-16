package hudson.plugins.playground;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Klasse zum reinen Ausprobieren.
 *
 * @author <a href="mailto:moestl@hm.edu">Christian M&ouml;stl</a>
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

        DetailAST ast = TreeWalker.parse(new FileContents(new FileText(new File(pathBefore), "UTF-8")));
        DetailAST ast2 = TreeWalker.parse(new FileContents(new FileText(new File(pathAfter), "UTF-8")));

        System.out.println(ast.getNextSibling().getText());
        System.out.println(ast.getFirstChild().getText());

        System.out.println("Gleicher AST: " + isEqualAST(ast, ast2));
        System.out.println("Anzahl Methoden:" + getMethods(ast2).size());

        DetailAST a = getClassDef(ast);
        System.out.println("Zeile: " + a.getLineNo());
        System.out.println("---");
        runThroughAST(ast);
    }

    /**
     * Returns the DetailAST from {@link com.puppycrawl.tools.checkstyle.api.TokenTypes#CLASS_DEF TokenTypes.CLASS_DEF}.
     *
     * @param root
     *            The root of the ast.
     * @return DetailAST from CLASS_DEF.
     */
    private static DetailAST getClassDef(final DetailAST root) {
        for (DetailAST a = root; a != null; a = a.getNextSibling()) {
            if (a.getType() == TokenTypes.CLASS_DEF) {
                return a;
            }
        }
        return null;
    }

    /**
     * Returns the DetailAST from {@link com.puppycrawl.tools.checkstyle.api.TokenTypes#OBJBLOCK TokenTypes.OBJBLOCK}.
     * TODO: OBJBLOCK könnte nicht nur CLASS_DEF als Parent haben...
     *
     * @param root
     *            The root of the ast.
     * @return DetailAST from OBJBLOCK.
     */
    private static DetailAST getOBJBLOCK(final DetailAST root) {
        DetailAST classDef = getClassDef(root).getFirstChild();
        if (classDef == null) {
            return null;
        }
        else {
            for (DetailAST a = classDef; a != null; a = a.getNextSibling()) {
                if (a.getType() == TokenTypes.OBJBLOCK) {
                    return a;
                }
            }
            return null;
        }
    }

    /**
     * Returns a list of methods in AST-data-structure.
     *
     * @param root
     *            The root of the ast.
     * @return The list of methods in AST-data-structure.
     */
    public static List<DetailAST> getMethods(final DetailAST root) {
        List<DetailAST> methods = new ArrayList<DetailAST>();
        DetailAST objBlock = getOBJBLOCK(root);

        if (objBlock == null) {
            return null;
        }
        else {
            DetailAST firstElement = objBlock.getFirstChild();
            // Bemerkung: objBlock muss immer mind. 2 Kinder haben (Die geschweiften Klammern).
            for (DetailAST ast = firstElement; ast != null; ast = ast.getNextSibling()) {
                if (ast.getType() == TokenTypes.METHOD_DEF) {
                    methods.add(ast);
                }
            }
        }

        return methods;
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
    public static void printList(final List<DetailAST> list) {
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
     * Returns true, if the ast contains the other ast.
     *
     * @param bigAST
     *            The ast in that is searched.
     * @param part
     *            the ast which is searched in the bigAST.
     * @return <code>true</code>, if the ast contains the other ast, otherwise <code>false</code>
     */
    public static boolean findAST(final DetailAST bigAST, final DetailAST part) {
        // TODO
        return true;
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
    public static void runThroughAST(final DetailAST root) {
        if (root != null) {
            System.out.println(root.getText());
            if (root.getFirstChild() != null) {
                runThroughAST(root.getFirstChild());
            }
            if (root.getNextSibling() != null) {
                runThroughAST(root.getNextSibling());
            }
        }
    }
}