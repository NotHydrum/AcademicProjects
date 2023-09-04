/**
 * Various methods using recursion.
 * @author Miguel Nunes fc56338
 */
public class Recursive {
	
	/**
	 * Represents the 'Next Line' character, independent of operating system.
	 */
	private static final String NEXT_LINE = System.getProperty("line.separator");
	
	/**
	 * Calculates the product of all integers between 1 and the limit ('n').
	 * @param n The product's limit.
	 * @requires n > 0
	 * @returns The product of all integers between 1 and 'n'.
	 */
	public static long product(int n) {
		long product;
		if (n != 1) {
			product = n * product(n - 1);
		}
		else {
			product = n;
		}
		return product;
	}
	
	/**
	 * Calculates the number of digits of 'n'.
	 * @param n Number whose digits to calculate.
	 * @return Number of digits of 'n'.
	 */
	public static int numberDigits(int n) {
		int digits;
		if (n > 9) {
			digits = 1 + numberDigits(n / 10);
		}
		else {
			digits = 1;
		}
		return digits;
	}
	
	/**
	 * Determines the minimum and the maximum values of an array.
	 * @param v Array whose limits will be determined.
	 * @requires v.length > 0.
	 * @return An array containing the 'v's minimum in index 0 and maximum in index 1.
	 */
	public static int[] minimumMaximum(int[] v) {
		int[] minimumMaximum = {minimum(v, 0), maximum(v, 0)};
		return minimumMaximum;
	}
	
	/**
	 * Sub-method of 'minimumMaximum'. Determines the minimum value of an array from 'index' to the array's last value.
	 * @param v Array whose minimum will be determined.
	 * @param index Search will be limited between this number and the end of the array.
	 * @requires v.length > 0
	 * @returns The minimum value of the array.
	 */
	private static int minimum(int[] v, int index) {
		int minimum;
		if (index != v.length - 1) {
			minimum = Math.min(v[index], minimum(v, index + 1));
		}
		else {
			minimum = v[index];
		}
		return minimum;
	}
	
	/**
	 * Sub-method of 'minimumMaximum'. Determines the maximum value of an array from 'index' to the array's last value.
	 * @param v Array whose maximum will be determined.
	 * @param index Search will be limited between this number and the end of the array.
	 * @requires v.length > 0
	 * @returns The maximum value of the array.
	 */
	private static int maximum(int[] v, int index) {
		int maximum;
		if (index != v.length - 1) {
			maximum = Math.max(v[index], maximum(v, index + 1));
		}
		else {
			maximum = v[index];
		}
		return maximum;
	}
	
	/**
	 * Determines if 's' is a palindrome. A string is a palindrome if it is the same forwards and backwards.
	 * @param s String to determine.
	 * @requires s != null && s != ""
	 * @return 'true' if palindrome, 'false' otherwise.
	 */
	public static boolean isPalindrome(String s) {
		s = s.toLowerCase();
		boolean palindrome;
		if (s.length() == 1) {
			palindrome = true;
		}
		else if (s.length() == 2) {
			if (s.charAt(0) == s.charAt(1)) {
				palindrome = true;
			}
			else {
				palindrome = false;
			}
		}
		else if (s.charAt(0) == s.charAt(s.length() - 1)) {
			palindrome = isPalindrome(s.substring(1, s.length() - 1));
		}
		else {
			palindrome = false;
		}
		return palindrome;
	}
	
	/**
	 * Creates a histogram that consists of the 'header' followed by an empty line followed by 'c.length' lines,
	 * all of these lines consist of that line's category 'c' followed by however many spaces are required to have
	 * 'ident' characters within that line, followed by a separator '|' and 'v' asterisks.
	 * @param header The histogram's title.
	 * @param c	The line's category.
	 * @param ident Space between the start of each line and the separator.
	 * @param v The number of asterisks to add to each line.
	 * @requires c.length > 0 && v.length > 0 && c.length == v.length
	 * @return String containing the histogram.
	 */
	public static String histogram(String header, String[] c, int ident, int[] v) {
		StringBuilder myBuilder = new StringBuilder(header + NEXT_LINE + NEXT_LINE);
		histogramRecursion(myBuilder, c, ident, v, 0);
		return myBuilder.toString();
	}
	
	/**
	 * Sub-method of 'histogram' which creates the histogram's lines and appends them to a given Stringbuilder.
	 * @param myBuilder Stringbuilder the lines will be appended to.
	 * @param c The line's category.
	 * @param ident Space between the start of each line and the separator.
	 * @param v The number of asterisks to add to each line.
	 * @param index c.length > 0 && v.length > 0 && c.length == v.length
	 * @requires c.length > 0 && v.length > 0 && c.length == v.length
	 */
	private static void histogramRecursion(StringBuilder myBuilder, String[] c, int ident, int[] v, int index) {
		myBuilder.append(c[index]);
		charRecursion(myBuilder, ' ', ident - c[index].length());
		myBuilder.append('|');
		if (v[index] > 0) {
			charRecursion(myBuilder, '*', v[index]);
		}
		myBuilder.append(NEXT_LINE);
		if (index < c.length - 1) {
			histogramRecursion(myBuilder, c, ident, v, index + 1);
		}
	}
	
	/**
	 * Appends to 'myBuilder' 'amount' chars 'c'.
	 * @param myBuilder Stringbuilder to append to.
	 * @param c Char to append.
	 * @param amount Number of 'c' to append.
	 * @requires c != null && amount >= 1
	 */
	private static void charRecursion(StringBuilder myBuilder, char c, int amount) {
		myBuilder.append(c);
		if (amount > 1) {
			charRecursion(myBuilder, c, amount - 1);
		}
	}
	
	/**
	 * Calculates the n'th number of a sequence where the first 3 numbers equal 1 and the rest equal
	 * sequence(n - 2) + sequence(n - 3).
	 * @param n Position to calculate.
	 * @requires n > 0
	 * @return Value of the given position.
	 */
	public static long sequence (int n) {
		long value;
		if (n < 4) {
			value = 1;
		}
		else {
			Long[] sequence = new Long[n];
			//using Long instead of long so values can be 'null'
			sequence[0] = (long)1;
			sequence[1] = (long)1;
			sequence[2] = (long)1;
			value = sequenceRecursion(n - 1, sequence);
		}
		return value;
	}
	
	/**
	 * Sub-method of 'sequence'.
	 * @param n Position to calculate.
	 * @param sequence Positions already calculated.
	 * @requires n > 0
	 * @return Value of the given position.
	 */
	private static long sequenceRecursion(int n, Long[] sequence) {
		if (sequence[n] == null) {
			sequence[n] = sequenceRecursion(n - 2, sequence) + sequenceRecursion(n - 3, sequence);
		}
		return sequence[n];
	}
	
}
