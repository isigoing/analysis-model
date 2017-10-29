package edu.hm.hafner.analysis.parser;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertions.IssueSoftAssertions;
import static edu.hm.hafner.analysis.assertions.IssuesAssert.*;

/**
 * Tests the class {@link GoLintParser}.
 */
public class GoLintParserTest extends ParserTester {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a file with multiple golint warnings
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new GoLintParser().parse(openFile());

        assertThat(warnings).hasSize(7);

        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(64)
                    .hasLineEnd(64)
                    .hasMessage("exported var ErrCloseSent should have comment or be unexported")
                    .hasFileName("conn.go");
        });


        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(104)
                    .hasLineEnd(104)
                    .hasMessage("should replace pos += 1 with pos++")
                    .hasFileName("conn.go");
        });


        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(305)
                    .hasLineEnd(305)
                    .hasMessage("should replace c.writeSeq += 1 with c.writeSeq++")
                    .hasFileName("conn.go");
        });


        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(warnings.get(3))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(360)
                    .hasLineEnd(360)
                    .hasMessage("should replace c.writeSeq += 1 with c.writeSeq++")
                    .hasFileName("conn.go");
        });


        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(warnings.get(4))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(669)
                    .hasLineEnd(669)
                    .hasMessage("should replace c.readSeq += 1 with c.readSeq++")
                    .hasFileName("conn.go");
        });


        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(warnings.get(5))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(706)
                    .hasLineEnd(706)
                    .hasMessage("should replace r.c.readSeq += 1 with r.c.readSeq++")
                    .hasFileName("conn.go");
        });


        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(warnings.get(6))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(18)
                    .hasLineEnd(18)
                    .hasMessage("should omit type net.Error from declaration of var timeoutErrImplementsNetError; it will be inferred from the right-hand side")
                    .hasFileName("conn_test.go");
        });


    }


    @Override
    protected String getWarningsFile() {
        return "golint.txt";
    }
}