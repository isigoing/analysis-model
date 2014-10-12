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
public class Test {
    /**
     * main-Method.
     *
     * @param args
     *            args.
     * @throws RecognitionException
     * @throws TokenStreamException
     * @throws IOException
     */
    public static void main(final String[] args) throws RecognitionException, TokenStreamException, IOException {
        String workspace = System.getProperty("user.dir");
        String filenameBefore = "InsertLine.java";
        String filenameAfter = "InsertLine.java";

        StringBuilder stringBuilder = new StringBuilder();
        String path = stringBuilder.append(workspace).append("+src+test+resources+hudson+plugins+checkstyle+parser+")
                .toString();

        String pathBefore = stringBuilder.append("before+").append(filenameBefore).toString();
        stringBuilder.setLength(0);
        String pathAfter = stringBuilder.append(path).append("after+").append(filenameAfter).toString();

        String fileSeparator = System.getProperty("file.separator");
        pathBefore = pathBefore.replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));
        pathAfter = pathAfter.replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));

        System.out.println(pathBefore);
        System.out.println(pathAfter);

        DetailAST ast = TreeWalker.parse(new FileContents(new FileText(new File(pathBefore), "UTF-8")));
        DetailAST ast2 = TreeWalker.parse(new FileContents(new FileText(new File(pathAfter), "UTF-8")));

        List<DetailAST> list = new ArrayList<DetailAST>();

        // for (DetailAST a = ast; a != null; a = a.getNextSibling()) {
        // list.add(a);
        // }
        //
        // readList(list);

        DetailAST asd = ast.getNextSibling();
        System.out.println(asd.getChildCount());

        System.out.println("Gleicher AST: " + isEqualAST(ast, ast2));
    }

    public static boolean isEqualAST(final DetailAST first, final DetailAST second) {
        return first.equalsTree(second);
    }

    private static List<DetailAST> getAffiliatedSiblings(final DetailAST ast) {
        List<DetailAST> list = new ArrayList<DetailAST>();

        for (DetailAST a = ast; a != null; a = a.getNextSibling()) {
            list.add(a);
        }

        return list;
    }

    private static List<DetailAST> getAffiliatedChildren(final DetailAST ast) {
        List<DetailAST> list = new ArrayList<DetailAST>();

        DetailAST firstChild = ast.getFirstChild();

        for (DetailAST a = firstChild; a != null; a = firstChild.getNextSibling()) {
            list.add(a);
        }

        return list;
    }

    private static void readList(final List<DetailAST> list) {
        for (DetailAST ast : list) {
            System.out.println(TokenTypes.getTokenName(ast.getType()));
        }
    }
}