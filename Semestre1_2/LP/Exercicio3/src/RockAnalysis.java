import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class meant to analyze rock pieces, using .rock files. </br>
 * .rock files must follow the following format: </br> </br>
 * ORIGINAL_WIDTH                                </br>
 * NUMBER_PIECES                                 </br>
 * Width1 Height1                                </br>
 * ...                                           </br>
 * ...                                           </br>
 * WidthNUMBER_PIECES HeightNUMBER_PIECES        </br> </br>
 * All widths and heights must be larger or equal to 1.
 * @author Miguel Nunes fc56338
 */
public class RockAnalysis {
	
	/**
	 * Determines a rock's original height from a .rock file. </br>
	 * We know the original width of the rock and the dimensions of all the pieces.
	 * Using the individual piece's dimensions we can calculate their area (Area = Height * Width).
	 * Adding up all the individual areas we get the original area which we can divide by the original width to get
	 * the original height (Area = Height * Width <=> Height = Area / Width)
	 * @param filename The path to the .rock file whose original height will be determined.
	 * @requires 'filename' must be a valid .rock file. (See class doc)
	 * @return The rock's original height.
	 * @throws FileNotFoundException If 'filename' does not exist or is inaccessible by Java.
	 */
	static int computeOriginalHeight(String filename) throws FileNotFoundException {
		Scanner myScanner = new Scanner(new File(filename));
		int originalWidth = myScanner.nextInt();
		int numberPieces = myScanner.nextInt();
		int area = 0;
		for (int i = 0; i < numberPieces; i++) {
			area += myScanner.nextInt() * myScanner.nextInt();
		}
		myScanner.close();
		return area / originalWidth;
	}
		
}
