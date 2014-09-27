package hudson.plugins.checkstyle.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import static org.junit.Assert.*;

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
        FileAnnotation before = readWarning("before/InsertLine.xml");
        FileAnnotation after = readWarning("after/InsertLine.xml");

        verifyWarning(before, "Javadoc", 7, "InsertLine.java");
        verifyWarning(after, "Javadoc", 8, "InsertLine.java");
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
            warnings = parser.parse(NewWarningDetectorTest.class.getResourceAsStream(fileName), "-");
            return Singleton.get(warnings);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Can't read CheckStyle file " + fileName, e);
        }
    }
}
