/*
 * Submission Tests - JUnit test case class for testing Query class
 *
 * Created - Paul J. Wagner, 18-Oct-2017
 */
package sqlfe.junit;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlfe.general.QuestionAnswer;
import sqlfe.general.Submission;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SubmissionTests extends AbstractTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @Before
    public void setup() {
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testReadSubmission() {
        try {
            // Setup
            String assignmentName = "testAssignment";
            PrintWriter commWriter = new PrintWriter("./testEvaluations/commFileReadSubmissionTests.out", "UTF-8");
            PrintWriter parseWriter = new PrintWriter("./testEvaluations/parseFileReadSubmissionTests.out", "UTF-8");

            // output general comment file information
            commWriter.println("Assignment  : " + assignmentName);
            commWriter.println("");

            // output general parse file information
            parseWriter.println("Assignment  : " + assignmentName);
            parseWriter.println("");

            // Run Tests
            Submission s = new Submission();
            String folderPath = "./files-sample-MySQL/";
            String fileName = "lt_s66.sql";
            s.readSubmission(folderPath + fileName, commWriter, parseWriter);
            assertEquals(folderPath + fileName, s.getSubmissionFileName());

            ArrayList<QuestionAnswer> answers = s.getAnswers();
            assertNotNull(answers);
            assertEquals(7, answers.size());

            QuestionAnswer a = answers.get(0);
            assertEquals("1", a.getQNumStr());
            String qString = "SELECT CustID, FName, LName, AccClosedDate\n" +
                    "FROM Customer C\n" +
                    "JOIN Account A ON (C.CustID = A.Customer)\n" +
                    "WHERE A.AccOpenLocation = 'Central' AND A.AccClosedDate >= '2017-03-01'";
            assertEquals(qString, a.getActualQuery().toString());

            //Test cross join
            a = answers.get(5);
            qString = "SELECT CustID, FName, LName\n" +
                    "FROM Customer\n" +
                    "WHERE CustID NOT IN(\n" +
                    "    SELECT CustID\n" +
                    "    FROM Customer C2\n" +
                    "    JOIN Account A2 ON (C2.CustID = A2.Customer)\n" +
                    "    WHERE AccStatus = 'Active')";
            assertEquals(qString, a.getActualQuery().toString());

            //Test nonexistant file
            commWriter = new PrintWriter("./testEvaluations/commFileReadSubmissionTestsFail.out", "UTF-8");
            parseWriter = new PrintWriter("./testEvaluations/parseFileReadSubmissionTestsFail.out", "UTF-8");
            s.readSubmission("garbage", commWriter, parseWriter);
            assertEquals("Cannot find file garbage\n", errContent.toString());

            //Test parse exception
            // TODO: Add example with parse exception

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @After
    public void teardown () {
        System.setErr(originalErr);
    }

}	// end - test case class QueryTests
