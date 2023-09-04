import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

/**
 * @author Miguel Nunes fc56338
 */
public class Tests {
	
	/**
	 * isJavaClassIdentifier Tests
	 * Characteristics tested:
	 * Starts with an upper case letter: true or false (ucT, ucF);
	 * Contains only letters, numbers, underscores and dollars signs: true or false (coT, coF);
	 * Has one or more characters: true or false (ncT, ncF);
	 */
	
	@Test
	public void javaClassIdentifier_ucTcoTncT() {
		assertTrue(RecognisePatterns.isJavaClassIdentifier("Java_Oracle$"));
	}
	
	@Test
	public void javaClassIdentifier_ucTcoFncT() {
		assertFalse(RecognisePatterns.isJavaClassIdentifier("Java@Oracle"));
	}
	
	@Test
	public void javaClassIdentifier_ucFcoTncT() {
		assertFalse(RecognisePatterns.isJavaClassIdentifier("java_Oracle$"));
		assertFalse(RecognisePatterns.isJavaClassIdentifier("$100"));
	}
	
	@Test
	public void javaClassIdentifier_ncF() {
		assertFalse(RecognisePatterns.isJavaClassIdentifier(""));
	}
	
	/**
	 * matchTimeStampLiteral Tests
	 * Characteristics tested:
	 * Hours >= 0 and <= 24: true or false (htT, htF);
	 * Hours valid (1 or 2 digits): true or false (hvT, hvF);
	 * Minutes >= 0 and <= 59: true or false (mtT, mtF);
	 * Minutes valid (1 or 2 digits): true or false (mvT, mvF);
	 * Seconds >= 0 and <= 59: true or false (stT, stF);
	 * Seconds valid (1 or 2 digits): true or false (svT, svF);
	 * Milliseconds valid (1, 2 or 3 digits): true or false (msvT, msvF);
	 * Delimiters valid (":" between hours, minutes and seconds. "." between seconds and milliseconds:
	 * true or false (dvT, dvF);
	 * White space before and after: true or false (wT, wF);
	 */
	
	@Test
	public void matchTimeStampLiteral_allT() {
		assertTrue(RecognisePatterns.matchTimeStampLiteral(" 5:8:3 "));
		assertTrue(RecognisePatterns.matchTimeStampLiteral("Test 19:01:00 "));
		assertTrue(RecognisePatterns.matchTimeStampLiteral(" 0:5:7.5 Test"));
		assertTrue(RecognisePatterns.matchTimeStampLiteral("Test 04:57:40.09 Test"));
		assertTrue(RecognisePatterns.matchTimeStampLiteral(" 23:33:47.537 "));
	}
	
	@Test
	public void matchTimeStampLiteral_htF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 24:00:00.000 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 37:15:43.248 "));
	}
	
	@Test
	public void matchTimeStampLiteral_hvF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" :15:43.248 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 012:15:43.248 "));
	}
	
	@Test
	public void matchTimeStampLiteral_mtF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:60:00.000 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:73:43.248 "));
	}
	
	@Test
	public void matchTimeStampLiteral_mvF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12::43.248 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:015:43.248 "));
	}
	
	@Test
	public void matchTimeStampLiteral_stF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:15:60.000 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:15:93.248 "));
	}
	
	@Test
	public void matchTimeStampLiteral_svF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:15:.248 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:15:043.248 "));
	}
	
	@Test
	public void matchTimeStampLiteral_msvF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:15:43. "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:15:43.2468 "));
	}
	
	@Test
	public void matchTimeStampLiteral_dvF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12.15:43.248 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12:15.43.248 "));
		assertFalse(RecognisePatterns.matchTimeStampLiteral(" 12.15:43:248 "));
	}
	
	@Test
	public void matchTimeStampLiteral_wF() {
		assertFalse(RecognisePatterns.matchTimeStampLiteral("23:33:47.537test"));
		assertFalse(RecognisePatterns.matchTimeStampLiteral("test23:33:47.537"));
		assertFalse(RecognisePatterns.matchTimeStampLiteral("test23:33:47.537test"));
	}
	
	/**
	 * matchListNotation Tests
	 * Characteristics tested:
	 * All natural numbers: true or false (nT, nF)
	 * Delimiters valid: true or false (dT, dF)
	 */
	
	@Test
	public void matchListNotation_allT() {
		assertTrue(RecognisePatterns.matchListNotation("< 12, 13, 0, 123, 456 >"));
		assertTrue(RecognisePatterns.matchListNotation("< >"));
		assertTrue(RecognisePatterns.matchListNotation("1|<>"));
		assertTrue(RecognisePatterns.matchListNotation("1 |  < 2,3,4>"));
		assertTrue(RecognisePatterns.matchListNotation(" 1|  2| 3 |4  |  <>"));
		assertTrue(RecognisePatterns.matchListNotation("2 < 12, 13, 1, 123, 456 >"));
	}
	
	@Test
	public void matchListNotation_nF() {
		assertFalse(RecognisePatterns.matchListNotation("< -12, 13, 1, 123, 456 >"));
		assertFalse(RecognisePatterns.matchListNotation("1 | < 12, 13, a, 123, 456 >"));
	}
	
	@Test
	public void matchListNotation_dF() {
		assertFalse(RecognisePatterns.matchListNotation(" 12, 13, 1, 123, 456 >"));
		assertFalse(RecognisePatterns.matchListNotation("< 12, 13, 1 123, 456 >"));
		assertFalse(RecognisePatterns.matchListNotation("2 | 3 <"));
		assertFalse(RecognisePatterns.matchListNotation("2 | 3"));
	}
	
	/**
	 * numbersInScientificNotation Tests
	 * Characteristics tested:
	 * Base is a real number: true or false (bT, bF)
	 * 'E' between base and power: true or false (eT, eF)
	 * Power is whole number: true or false (pT, pF)
	 */
	
	@Test
	public void numbersInScientificNotation_allT() {
		ArrayList<Double> expected = new ArrayList<>();
		expected.add(10.08E-3);
		expected.add(+213E4);
		expected.add(-0.78954E+21);
		assertTrue(expected.equals(
				RecognisePatterns.numbersInScientificNotation("10.08E-3 test +213E4 test -0.78954E+21")));
	}
	
	@Test
	public void numbersInScientificNotation_bF() {
		ArrayList<Double> expected = new ArrayList<>();
		assertTrue(expected.equals(
				RecognisePatterns.numbersInScientificNotation("10.E-3 test +aE4")));
	}
	
	@Test
	public void numbersInScientificNotation_eF() {
		ArrayList<Double> expected = new ArrayList<>();
		assertTrue(expected.equals(
				RecognisePatterns.numbersInScientificNotation("10-3 +234e4 -0.78954@+21")));
	}
	
	@Test
	public void numbersInScientificNotation_pF() {
		ArrayList<Double> expected = new ArrayList<>();
		assertTrue(expected.equals(
				RecognisePatterns.numbersInScientificNotation("10.08E-.1 test +213En test -0.78954E")));
	}
	
}
