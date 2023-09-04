import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class meant to generate .rock files.
 * @author Miguel Nunes fc56338
 */
public class RockTestGenerator {
	
	/**
	 * Defines the end-of-line character, independent of operating system.
	 */
	private static final String NEXT_LINE = System.getProperty("line.separator");
	
	/**
	 * The randomizer used by generate().
	 * Not local because it guarantees only one is ever created as creating multiple would potentially make the
	 * randomization worse.
	 */
	private static final Random myRandom = new Random();

    /**
     * Generates a rock analysis test and writes it to a file.
     * @param numberOfPieces Number of pieces the original rock will be split into.
     * @param width Width of the original rock.
     * @param height Height of the original rock.
     * @param fileOut Path to the .rock file the test will be written in.
     * @requires width > 0 && height > 0 && numberOfPieces > 0 && numberOfPieces <= width * height
     * @throws FileNotFoundException If 'fileOut' is inaccessible by Java
     */
    public static void generateToFile(int numberOfPieces, int width, int height, String fileOut)
    throws FileNotFoundException {
    	String rockFile = generate(numberOfPieces, width, height);
    	PrintWriter writer = new PrintWriter(fileOut);
    	writer.println(rockFile);
    	writer.close();
    }
    
    /**
     * Generates a rock analysis test and returns it as a String. </br>
     * With the help of Piece.java, this method starts by creating a single Piece with dimension of the original rock.
     * All Pieces are stored in an array and the indexes of the pieces that can be broken further are stored in a List.
     * Until the number of pieces is correct the method will randomly pick one index from the list and break the Piece
     * with that index in two. The way pieces are broken is random, first picking the direction (either vertical or
     * horizontal) and then deciding the size of each. If a new piece has an area bigger than 1 its index is added to
     * the list, if a piece whose index is on the list ever gets broken into an area equal to 1 its index will be
     * removed from the list. Once the number of pieces is correct the method uses a StringBuilder to generate the
     * String and return it.
     * @param numberPieces Number of pieces to split the original rock into.
     * @param width Original rock's width.
     * @param heigth Original rock's height.
     * @requires width > 0 && height > 0 && numberOfPieces > 0 && numberOfPieces <= width * height
     * @return String for a valid .rock file.
     */
    static String generate(int numberPieces, int width, int heigth) {
    	Piece[] pieces = new Piece[numberPieces];
    	pieces[0] = new Piece(width, heigth);
    	List<Integer> dividiblePieces = new ArrayList<>();
    	dividiblePieces.add(0);
    	for (int i = 1; i < numberPieces; i++) {
    		int dividedPiece = dividiblePieces.get(myRandom.nextInt(dividiblePieces.size()));
    		Piece copy = pieces[dividedPiece].copy();
    		int type;
    		if (copy.width() > 1 && copy.heigth() == 1) {
    			type = 0;
    		}
    		else if (copy.width() == 1 && copy.heigth() > 1) {
    			type = 1;
    		}
    		else {
    			type = myRandom.nextInt(2);
    		}
    		if (type == 0) { //horizontal division
    			pieces[dividedPiece] = new Piece(myRandom.nextInt(copy.width() - 1) + 1, copy.heigth());
    			pieces[i] = new Piece(copy.width() - pieces[dividedPiece].width(), copy.heigth());
    		}
    		else { //vertical division
    			pieces[dividedPiece] = new Piece(copy.width(), myRandom.nextInt(copy.heigth() - 1) + 1);
    			pieces[i] = new Piece(copy.width(), copy.heigth() - pieces[dividedPiece].heigth());
    		}
    		if (pieces[dividedPiece].area() == 1) {
    			dividiblePieces.remove(dividiblePieces.indexOf(dividedPiece));
    		}
    		if (pieces[i].area() != 1) {
    			dividiblePieces.add(i);
    		}
    	}
    	StringBuilder myBuilder = new StringBuilder();
    	myBuilder.append(width + NEXT_LINE + numberPieces + NEXT_LINE);
    	for (int i = 0; i < numberPieces; i++) {
    		myBuilder.append(pieces[i].width() + " " + pieces[i].heigth() + NEXT_LINE);
    	}
    	return myBuilder.toString();
    }

}
