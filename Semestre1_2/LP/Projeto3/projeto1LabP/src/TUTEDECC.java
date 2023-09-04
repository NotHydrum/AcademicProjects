import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Various methods, all capable of modifying text with the objective of obscuring it.
 * @author Miguel Nunes fc56338
 */
public class TUTEDECC {
	
	/**
	 * Possible word separators (used in String split).
	 * Value: {@value}
	 */
	private static final String SEPARATORS = "\\s*[ ,.]\\s*";
	
	/**
	 * Copies to 'fileOut' the lines of the text in 'fileIn' that are in a position multiple of 'n'.
	 * @param fileIn Pathname of the input file.
	 * @param n Number whose multiples will be copied.
	 * @param fileOut Pathname of the output file.
	 * @throws FileNotFoundException If 'fileIn' does not exist or is inaccessible. If 'fileOut' is inaccessible.
	 */
	static void copyPositionMultiple(String fileIn, int n, String fileOut) throws FileNotFoundException {
		if (n != 0) {
			try (Scanner myScanner = new Scanner(new File(fileIn));
				 FileWriter myWriter = new FileWriter(new File(fileOut))) {
				int line = 1;
				while (myScanner.hasNextLine()) {
					if (line % n == 0) {
						myWriter.append(line + ": " + myScanner.nextLine() + "\n");
					}
					else {
						myScanner.nextLine();
					}
					line++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Copies to 'fileOut' the text in 'fileIn', substituting all vowels with a given character.
	 * @param fileIn Pathname of the input file.
	 * @param c Character vowels will be substituted for.
	 * @param fileOut Pathname of the output file.
	 * @requires No accentuated vowels.
	 * @throws FileNotFoundException If 'fileIn' does not exist or is inaccessible. If 'fileOut' is inaccessible.
	 */
	static void vowelSubstitution(String fileIn, char c, String fileOut) throws FileNotFoundException {
		try (Scanner myScanner = new Scanner(new File(fileIn));
			 FileWriter myWriter = new FileWriter(new File(fileOut))) {
			String[] line = new String[0];
			while (myScanner.hasNextLine()) {
				line = Arrays.copyOf(line, line.length + 1);
				line[line.length - 1] = myScanner.nextLine();
			}
			for (String i : line) {
				i = i.replace('a', c);
				i = i.replace('A', c);
				i = i.replace('e', c);
				i = i.replace('E', c);
				i = i.replace('i', c);
				i = i.replace('I', c);
				i = i.replace('o', c);
				i = i.replace('O', c);
				i = i.replace('u', c);
				i = i.replace('U', c);
				myWriter.append(i + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the text in 'fileIn' and returns an array containing the average word size of the first 'n' lines in the
	 * text.
	 * @param fileIn Pathname of the input file.
	 * @param n Number of lines to read (size of the array).
	 * @return An array containing the average word size of the first 'n' lines in the text.
	 * @requires Text must have at least 'n' lines.
	 * @throws FileNotFoundException If 'fileIn' does not exist or is inaccessible.
	 */
	static int[] averageWordSize(String fileIn, int n) throws FileNotFoundException {
		int[] average = new int[n];
		try (Scanner myScanner = new Scanner(new File(fileIn))) {
			int line = 0;
			while (myScanner.hasNextLine() && line < n) {
				String[] splitLine = myScanner.nextLine().trim().split(SEPARATORS);
				int total = 0;
				for (String i : splitLine) {
					total += i.length();
				}
				average[line] = (int)((double)total / splitLine.length + 0.5);
				line++;
			}
		}
		return average;
	}
	
	/**
	 * For every line in 'fileIn', writes in 'fileOut' a line consisting of the line number and the line's last word.
	 * @param fileIn Pathname of the input file.
	 * @param fileOut Pathname of the output file.
	 * @requires Word separators can only be spaces, dots, or commas.
	 * @throws FileNotFoundException If 'fileIn' does not exist or is inaccessible. If 'fileOut' is inaccessible.
	 */
	static void oneWordPerLine(String fileIn, String fileOut) throws FileNotFoundException {
		try (Scanner myScanner = new Scanner(new File(fileIn));
			 FileWriter myWriter = new FileWriter(new File(fileOut))) {
			int line = 1;
			while (myScanner.hasNextLine()) {
				String[] splitLine = myScanner.nextLine().split(SEPARATORS);
				myWriter.append(line + " " + splitLine[splitLine.length - 1] + "\n");
				line++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Copies to 'fileOut' the text in 'fileIn' after modifying the text with the following parameters:
	 * The last 'n' letters of a word will be move to the start of the word. Words with length equal or lesser than 'n'
	 * will remain unchanged.
	 * @param fileIn Pathname of the input file.
	 * @param n Number of characters to move.
	 * @param fileOut Pathname of the output file.
	 * @requires Text can only have letters and/or spaces
	 * @throws FileNotFoundException If 'fileIn' does not exist or is inaccessible. If 'fileOut' is inaccessible.
	 */
	static void rotateEveryWord(String fileIn, int n, String fileOut) throws FileNotFoundException {
		try (Scanner myScanner = new Scanner(new File(fileIn));
			 FileWriter myWriter = new FileWriter(new File(fileOut))) {
			while (myScanner.hasNextLine()) {
				String[] splitLine = myScanner.nextLine().trim().split(SEPARATORS);
				for (int i = 0; i < splitLine.length; i++) {
					if (splitLine[i].length() > n) {
						for (int f = splitLine[i].length() - n; f < splitLine[i].length(); f++) {
							myWriter.append(splitLine[i].charAt(f));
						}
						for (int f = 0; f < splitLine[i].length() - n; f++) {
							myWriter.append(splitLine[i].charAt(f));
						}
					}
					else {
						myWriter.append(splitLine[i]);
					}
					if (i == splitLine.length - 1) {
						myWriter.append("\n");
					}
					else {
						myWriter.append(" ");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Copies to 'fileOut' the text in 'fileIn' after modifying the text with the following parameters:
	 * For each pair of lines changes the word in position "k" of the 1st line with the word in position "k+1" of the
	 * 2nd line, if k is an odd number.
	 * @param fileIn Pathname of the input file.
	 * @param fileOut Pathname of the output file.
	 * @throws FileNotFoundException If 'fileIn' does not exist or is inaccessible. If 'fileOut' is inaccessible.
	 */
	static void switchNextWordNextLine(String fileIn, String fileOut) throws FileNotFoundException {
		try (Scanner myScanner = new Scanner(new File(fileIn));
			 FileWriter myWriter = new FileWriter(new File(fileOut))) {
			String[][] originalText = new String[0][0];
			while (myScanner.hasNextLine()) {
				String line = myScanner.nextLine().trim();
				originalText = Arrays.copyOf(originalText, originalText.length + 1);
				originalText[originalText.length - 1] = line.split(" ");
			}
			String[][] modifiedText = new String[originalText.length][0];
			for (int i = 0; i < originalText.length; i++) {
				modifiedText[i] = Arrays.copyOf(modifiedText[i], originalText[i].length);
			}
			for (int i = 0; i < modifiedText.length; i++) {
				for (int f = 0; f < modifiedText[i].length; f++) {
					if ((f + 1) % 2 != 0 && (i + 1) % 2 != 0 && modifiedText.length > i + 1
						&& modifiedText[i + 1].length > f + 1) {
						modifiedText[i][f] = originalText[i + 1][f + 1];
						modifiedText[i + 1][f + 1] = originalText[i][f];
					}
					else if (modifiedText[i][f] == null) {
						modifiedText[i][f] = originalText[i][f];
					}
				}
			}
			for (int i = 0; i < modifiedText.length; i++) {
				for (int f = 0; f < modifiedText[i].length; f++) {
					myWriter.append(modifiedText[i][f]);
					if (f < modifiedText[i].length - 1) {
						myWriter.append(" ");
					}
				}
				myWriter.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
