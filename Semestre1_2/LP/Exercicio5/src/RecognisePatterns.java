import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to recognize some patterns of regular expressions and to use them to analyze the
 * contents of file "in.txt"
 * 
 * @author Miguel Nunes fc56338
 *
 */
public class RecognisePatterns {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner (new File("in.txt"));
		PrintWriter out = new PrintWriter("out.txt");

		if (in.hasNextLine()) {
			boolean isIdentifier = isJavaClassIdentifier(in.nextLine());
			out.println("Is the whole line 1 a java class identifier? " + isIdentifier );
		}
		int lineNo = 2;
		while (in.hasNextLine()) {
			String linha = in.nextLine();
			if (matchTimeStampLiteral(linha))
				out.println("Line " + lineNo + " contains at least one time stamp literal");
			if (matchListNotation(linha))
				out.println("Line " + lineNo + " contains at least one list");
			List<Double> doublesList = numbersInScientificNotation(linha);
			if (!doublesList.isEmpty()) {
				out.print("Line " + lineNo + " contains at least one number in scientific notation. ");
				out.print("List of recognised numbers: ");
				out.println(doublesList.toString());
			}
			
			lineNo++;
		}
		
		in.close();
		out.close();
	}
	
	/**
	 * Determines if a String is a valid name for a Java Class.
	 * @param str String to validate.
	 * @requires str != null
	 * @return 'true' if valid, 'false' otherwise.
	 */
	public static boolean isJavaClassIdentifier(String str) {
		return Pattern.compile("[A-Z][a-zA-Z_0-9\\$]*").matcher(str).matches();
	}
	
	/**
	 * Tries to find a valid time stamp within the given String.
	 * @param str String to find a time stamp in.
	 * @requires str != null
	 * @return 'true' if found, 'false' otherwise.
	 */
	public static boolean matchTimeStampLiteral(String str) {
		return Pattern.compile("\s(([01]?[0-9])|(2[0-3])):[0-5]?[0-9]:[0-5]?[0-9](.[0-9]{1,3})?\s")
				.matcher(str).find();
	}
	
	/**
	 * Tries to find a valid list stamp within the given String.
	 * @param str String to find a list in.
	 * @requires str != null
	 * @return 'true' if found, 'false' otherwise.
	 */
	public static boolean matchListNotation(String str) {
		return Pattern.compile("((([0-9]+ *[|] *)*)|( *))< *([0-9]+( *, *[0-9]+)*)? *>")
				.matcher(str).find();
	}
	
	/**
	 * Finds all numbers in scietific notation within the given String.
	 * @param str String to find numbers in.
	 * @requires str != null
	 * @return A List containing the numbers found.
	 */
	public static List<Double> numbersInScientificNotation(String str) {
		Matcher matcher = Pattern.compile("([+-])?[0-9]+(.[0-9]+)?E[+-]?[0-9]+").matcher(str);
		ArrayList<Double> numbers = new ArrayList<>();
		while (matcher.find()) {
			numbers.add(Double.valueOf(matcher.group()));
		}
		return numbers;
	}
	
}
