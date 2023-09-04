import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class is to test the methods in class Recursive
 * @author LabP team
 *
 */
public class TestsRecursive {

	/**
	 * We use this line separator in order to tests to pass whatever the
	 * end-of-line symbols used by a specific operating system.
	 */
	public static final String EOL = System.getProperty("line.separator");

	@Test
	public void testProduct () {

		long obtained1 = Recursive.product(14);
		long expected1 = 87178291200L;
		assertEquals(expected1, obtained1);
		
		long obtained2 = Recursive.product(18);
		long expected2 = 6402373705728000L;
		assertEquals(expected2, obtained2);				
	}
	@Test 
	public void testNumberDigits() { 
		int obtained1 = Recursive.numberDigits(146);
		int expected1 = 3;
		assertEquals(expected1, obtained1);
		
		int obtained2 = Recursive.numberDigits(14678953);
		int expected2 = 8;
		assertEquals(expected2, obtained2);
	}

	@Test 
	public void testMinimumMaximum() { 
		int[] v1 = {0, -7, 8, 2};
		int[] obtained1 = Recursive.minimumMaximum(v1);
		int[] expected1 = {-7,8};
		assertEquals(expected1[0], obtained1[0]);
		assertEquals(expected1[1], obtained1[1]);
		
		int[] v2 = {6,12,5,9,3};
		int[] obtained2 = Recursive.minimumMaximum(v2);
		int[] expected2 = {3,12};
		assertEquals(expected2[0], obtained2[0]);
		assertEquals(expected2[1], obtained2[1]);

	}

	@Test 
	public void testIsPalindrome() { 
		boolean obtained1 = Recursive.isPalindrome("ana");
		boolean expected1 = true;
		assertEquals(expected1, obtained1);
		
		boolean obtained2 = Recursive.isPalindrome("abaco");
		boolean expected2 = false;
		assertEquals(expected2, obtained2);

	}


	//@Test 
	//public void testHistogram() { 
	//	int[] v = {3,1,4,0,5};
	//	String obtained1 = Recursive.histogram(v);
	//	String expected1 = "|***" + EOL + "|*" + EOL + "|****"+ EOL + "|" + EOL+ "|*****" + EOL;
	//	assertEquals(expected1, obtained1);
		
	//}
	
	@Test
	public void testSequence () {

		long obtained1 = Recursive.sequence(10);
		long expected1 = 9L;
		assertEquals(expected1, obtained1);
		
		long obtained2 = Recursive.sequence(200);
		long expected2 = 7834342423993701066L;
		assertEquals(expected2, obtained2);
				
	}

}
