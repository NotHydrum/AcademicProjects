import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import junit.framework.ComparisonFailure;

/**
 *
 * @author LabPTeam
 *
 */
// @FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMarsSoftware {

    /**
     * Compare two integers and throw useful junit output
     *
     * @param a first integer
     * @param b second integer
     * @throws ComparisonFailure when the integers differ
     */
    private static void testInts(int a, int b) throws ComparisonFailure {
	if (a != b) {
	    String msg = "Incorrect original height: ";
	    throw new ComparisonFailure(msg, Integer.toString(a), Integer.toString(b));
	}
    }

    @Test
    public void aTestComputeHeightInput() throws FileNotFoundException {
	int height = RockAnalysis.computeOriginalHeight("input.rock");
	testInts(6, height);
    }

    @Test
    public void bTestComputeSmallHeightInput() throws FileNotFoundException {
	int height = RockAnalysis.computeOriginalHeight("small.rock");
	testInts(587, height);
    }

    @Test(timeout = 7000)
    public void cMultipleTestComputeHeight() throws FileNotFoundException {
	String filename = "generatedFile.rock";
	for (int i = 1; i <= 100; i++) {
	    RockTestGenerator.generateToFile(i * i, i * i, i * i, filename);
	    int height = RockAnalysis.computeOriginalHeight(filename);
	    testInts(i * i, height);
	}
	File f = new File(filename);
	if (f.isFile()) {
	    f.delete();
	}
    }

    @Test(timeout = 60000)
    public void dTestComputeBigHeightInput() throws FileNotFoundException {
	int height = RockAnalysis.computeOriginalHeight("big.rock");
	testInts(5487, height);
    }
}
