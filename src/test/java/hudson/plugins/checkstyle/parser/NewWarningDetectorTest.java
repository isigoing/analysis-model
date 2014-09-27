package hudson.plugins.checkstyle.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import hudson.plugins.analysis.util.ContextHashCode;
import hudson.plugins.analysis.util.Singleton;
import hudson.plugins.analysis.util.model.FileAnnotation;

/**
 * Test cases for the new warnings detector.
 *
 * @author Ullrich Hafner
 */
public class NewWarningDetectorTest {
    /**
     * Verifies that the insertion of a new line will not mark a warning as new.
     */
    @Test
    public void testInsertLine() {
        FileAnnotation beforeWarning = readWarning("before/InsertLine.xml");
        FileAnnotation afterWarning = readWarning("after/InsertLine.xml");

        verifyWarning(beforeWarning, "Javadoc", 7, "InsertLine.java");
        verifyWarning(afterWarning, "Javadoc", 8, "InsertLine.java");

        int beforeCode = createHashCode("before/InsertLine.java");
        int afterCode = createHashCode("after/InsertLine.java");

        assertEquals("Hash codes do not match: ", beforeCode, afterCode);
    }

    private int createHashCode(final String fileName) {
        try {
            ContextHashCode hashCode = new ContextHashCode();
            return hashCode.create(read(fileName), "UTF-8", 7);
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
}
