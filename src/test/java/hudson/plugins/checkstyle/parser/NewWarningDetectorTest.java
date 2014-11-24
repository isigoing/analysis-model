package hudson.plugins.checkstyle.parser;

import static org.junit.Assert.*;
import hudson.plugins.analysis.util.ContextHashCode;
import hudson.plugins.analysis.util.Singleton;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.ast.factory.Ast;
import hudson.plugins.ast.factory.AstFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.regex.Matcher;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

/**
 * Test cases for the new warnings detector.
 *
 * @author Ullrich Hafner
 */
public class NewWarningDetectorTest {
    /**
     * Verifies that the insertion of a new line above the warning does produce a different hashCode.
     */
    @Test
    public void testInsertLineAboveWarning() {
        FileAnnotation beforeWarning = readWarning("before/InsertLine.xml");
        FileAnnotation afterWarning = readWarning("after/InsertLine.xml");

        verifyWarning(beforeWarning, "Javadoc", 7, "InsertLine.java");
        verifyWarning(afterWarning, "Javadoc", 8, "InsertLine.java");

        int beforeCode = createHashCode("before/InsertLine.java", 7);
        int afterCode = createHashCode("after/InsertLine.java", 8);

        assertNotEquals("Hash codes do not match: ", beforeCode, afterCode);
    }

    /**
     * Verifies that the insertion of a new line before the package declaration does not change the hash code.
     */
    @Test
    public void testInsertLineBeforePackage() {
        FileAnnotation beforeWarning = readWarning("before/InsertLine.xml");
        FileAnnotation afterWarning = readWarning("after/InsertLine2.xml");

        verifyWarning(beforeWarning, "Javadoc", 7, "InsertLine.java");
        verifyWarning(afterWarning, "Javadoc", 8, "InsertLine.java");

        int beforeCode = createHashCode("before/InsertLine.java", 7);
        int afterCode = createHashCode("after/InsertLine2.java", 8);

        assertEquals("Hash codes do not match: ", beforeCode, afterCode);
    }

    /**
     * Verifies that the insertion of a new line above the warning doesn't change the AST.
     */
    @Test
    public void testInsertLineAboveWarningWithAST() {
        try {
            assertTrue("AST do not match",
                    getASTFromFile("InsertLine.java", true).equalsTree(getASTFromFile("InsertLine.java", false)));
        }
        catch (RecognitionException exception) {
            exception.printStackTrace();
        }
        catch (TokenStreamException exception) {
            exception.printStackTrace();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Verifies that the insertion of a new line before the package declaration does not change the AST.
     */
    @Test
    public void testInsertLineBeforePackageWithAST() {
        try {
            assertTrue("AST do not match",
                    getASTFromFile("InsertLine.java", true).equalsTree(getASTFromFile("InsertLine2.java", false)));
        }
        catch (RecognitionException exception) {
            exception.printStackTrace();
        }
        catch (TokenStreamException exception) {
            exception.printStackTrace();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Verifies that two different Java-Source-Files has different AST.
     */
    @Test
    public void testDifferentSourceFilesShowsDifferentAST() {
        try {
            assertFalse("AST do match",
                    getASTFromFile("InsertLine.java", true).equalsTree(getASTFromFile("InsertLine3.java", false)));
        }
        catch (RecognitionException exception) {
            exception.printStackTrace();
        }
        catch (TokenStreamException exception) {
            exception.printStackTrace();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private DetailAST getASTFromFile(final String filename, final boolean before) throws RecognitionException,
            TokenStreamException, IOException {
        String workspace = System.getProperty("user.dir");

        @SuppressWarnings("PMD.InsufficientStringBufferDeclaration")
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(workspace).append("+src+test+resources+hudson+plugins+checkstyle+parser+");

        if (before) {
            stringBuilder.append("before+");
        }
        else {
            stringBuilder.append("after+");
        }
        stringBuilder.append(filename);

        String fileSeparator = System.getProperty("file.separator");
        String path = stringBuilder.toString().replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));

        return TreeWalker.parse(new FileContents(new FileText(new File(path), "UTF-8")));
    }

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    private int createHashCode(final String fileName, final int line) {
        try {
            ContextHashCode hashCode = new ContextHashCode();
            return hashCode.create(read(fileName), "UTF-8", line);
        }
        catch (IOException e) {
            throw new RuntimeException("Can't read Java file " + fileName, e);
        }
    }

    private void verifyWarning(final FileAnnotation before, final String category, final int line, final String fileName) {
        assertEquals("Wrong category", category, before.getCategory());
        assertEquals("Wrong line", line, before.getPrimaryLineNumber());
        assertEquals("Wrong line", fileName, FilenameUtils.getName(before.getFileName()));
    }

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    private FileAnnotation readWarning(final String fileName) {
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

    private InputStream read(final String fileName) {
        return NewWarningDetectorTest.class.getResourceAsStream(fileName);
    }

    /**
     * Verifies that the ast calculates the same hashcode as given for a missing Method-Javadoc.
     */
    @Test
    public void testJavadocMethodCheck() {
        String hashBefore = calcHashcode("InsertLine.java", "InsertLine.xml", true);
        String hashAfter = calcHashcode("InsertLine.java", "InsertLine.xml", false);

        compareHashcode(hashBefore, hashAfter);
    }

    private void compareHashcode(final String hashBefore, final String hashAfter) {
        assertNotNull("Hash code isn't not null", hashBefore);
        assertEquals("Hash codes don't match: ", hashBefore, hashAfter);
    }

    private String calcHashcode(final String javaFile, final String xml, final boolean before) {
        Ast ast;
        if (before) {
            ast = getAst(javaFile, "before/" + xml, true);
        }
        else {
            ast = getAst(javaFile, "after/" + xml, false);
        }

        return ast.calcSha(ast.chooseArea());
    }

    private Ast getAst(final String filename, final String xmlFile, final boolean before) {
        String workspace = System.getProperty("user.dir");
        FileAnnotation fileAnnotation = readWarning(xmlFile);

        @SuppressWarnings("PMD.InsufficientStringBufferDeclaration")
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(workspace).append("+src+test+resources+hudson+plugins+checkstyle+parser+");

        if (before) {
            stringBuilder.append("before+");
        }
        else {
            stringBuilder.append("after+");
        }
        stringBuilder.append(filename);

        String fileSeparator = System.getProperty("file.separator");
        String path = stringBuilder.toString().replaceAll("\\+", Matcher.quoteReplacement(fileSeparator));

        return AstFactory.getInstance(path, fileAnnotation);
    }
}