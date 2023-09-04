import java.util.Arrays;

/**
 * This class tests the methods of Move,Puzzle and WordSearch,
 * on some simple situations.
 *
 * @author Graca Gaspar
 * @date December 2020
 */

public class WordSearchTest{

	private static final char[][] BOARD =  {
			{'R', 'O', 'M', 'A', 'E'},
			{'G', 'A', 'I', 'J', 'K'},
			{'M', 'T', 'M', 'P', 'Q'},
			{'S', 'A', 'U', 'A', 'X'},
			{'B', 'P', 'D', 'E', 'F'},
			{'H', 'I', 'J', 'K', 'L'},
			{'N', 'O', 'P', 'Q', 'R'},
			{'T', 'U', 'V', 'X', 'Y'},
			{'C', 'D', 'E', 'F', 'G'},
			{'I', 'J', 'K', 'L', 'M'}
	};

	private static final int ROWS = BOARD.length;
	private static final int COLUMNS = BOARD[0].length;

	private static final  int[] VALID_POSITIONS = new int[]{1, 1, 4, 4};
	private static final  int[] INVALID_POSITIONS = new int[]{10, 2, 5, 2};

	private static final  String[] VALID_HIDDEN = new String [] {"ROMA", "PATA", "RAMA"};
	private static final  String[] INVALID_HIDDEN = new String[]{"CBA", "XX"}; // be brave and add also "" :-)
	private static final  int DURATION_IN_SECONDS = 50;


	/**
	 * Executes tests on classes Move, Puzzle and WordSearch
	 * @param args
	 */
	public static void main (String[] args){

		System.out.println(">>>>>> Starting tests <<<<<<");
		System.out.println();
		testMove();
		testPuzzle();
		testWordSearch();

		System.out.println(">>>>>> Tests finished <<<<<< \n");
		System.out.println("Do not forget: these are just a couple of simple tests.");
		System.out.println("Test your code with additional tests!!!");
	}

	/**
	 * Tests functions and methods of class Move:
	 * - static boolean  definesMove(int row1, int col1,
	 *                     int row2, int col2, int rows, int columns)
	 * - int startRow()/startColumn()
	 * - int endRow()/endColumn()
	 * - Direction direction()
	 * - int rows()/columns()
	 */
	private static void testMove(){

		// initialize the valid Move
		Move validMove = new Move(VALID_POSITIONS[0], VALID_POSITIONS[1], VALID_POSITIONS[2], VALID_POSITIONS[3],
				ROWS, COLUMNS);

		System.out.println();
		System.out.println(">>> Testing class Move:<<<");
		System.out.println();
		boolean obtained = Move.definesMove(VALID_POSITIONS[0], VALID_POSITIONS[1],
				VALID_POSITIONS[2], VALID_POSITIONS[3], ROWS, COLUMNS);
		printTest("definesMove", "valid", "move", obtained);
		obtained = Move.definesMove(INVALID_POSITIONS[0], INVALID_POSITIONS[1],
				INVALID_POSITIONS[2], INVALID_POSITIONS[3], ROWS, COLUMNS);
		printTest("definesMove", "invalid", "move", obtained);

		int position = validMove.startRow();
		printIntTest("startRow", position, VALID_POSITIONS[0]);
		position = validMove.startColumn();
		printIntTest("startColumn", position, VALID_POSITIONS[1]);
		position = validMove.endRow();
		printIntTest("endRow", position, VALID_POSITIONS[2]);
		position = validMove.endColumn();
		printIntTest("endColumn", position, VALID_POSITIONS[3]);
		int bound = validMove.rows();
		printIntTest("rows", bound, ROWS);
		bound = validMove.columns();
		printIntTest("columns", bound, COLUMNS);

		Direction dir = validMove.direction();
		Direction expDir = Direction.DIAGONAL_RIGHT;
		printDirTest("direction", dir, expDir);

	}

	/**
	 * Tests the methods of class Puzzle:
	 * - static boolean isHidden(char[][] board, String word)
	 * - static boolean definesPuzzle(char[][] board, String[] hiddenWords)
	 * - int rows()/columns()
	 * - int numberHiddenWords()
	 * - char[][] board()
	 * - String getWord(Move move)
	 */
	public static void testPuzzle(){
		Move [] m = new Move[3];
		// initializes the moves that find hiddenWords
		m[0] = new Move(1,1,1,1+VALID_HIDDEN[0].length() - 1, ROWS, COLUMNS);
		m[1] = new Move(2, 2, 2 + VALID_HIDDEN[1].length() - 1, 2, ROWS, COLUMNS);
		m[2] = new Move(1,1,1+VALID_HIDDEN[2].length()-1,1+VALID_HIDDEN[2].length() - 1, ROWS, COLUMNS);

		// create the valid puzzle
		Puzzle validPuzzle = new Puzzle(BOARD, VALID_HIDDEN);

		System.out.println("\n >>> Testing class Puzzle: <<<");
		System.out.println("\n**** BOARD ****");
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++)
				System.out.print(BOARD[i][j] + "  ");
			System.out.println();
		}

		System.out.println("\n **** Invalid HiddenWords ****");
		System.out.println(Arrays.toString(INVALID_HIDDEN));

		boolean obtained = Puzzle.definesPuzzle(BOARD, INVALID_HIDDEN);
		printTest("definesPuzzle", "invalid", "puzzle", obtained);

		System.out.println("\n**** Valid HiddenWords ****");
		System.out.println(Arrays.toString(VALID_HIDDEN));


		obtained = Puzzle.definesPuzzle(BOARD, VALID_HIDDEN);
		printTest("definesPuzzle", "valid", "puzzle", obtained);

		for (int i = 0; i < VALID_HIDDEN.length; i++){
			String w = validPuzzle.getWord(m[i]);
			printStrTest("getWord with move "+ m[i].toString(), w, VALID_HIDDEN[i]);
		}

		Move moveThatDoesnotFindHiddenWord = new Move(1,1,1,2,ROWS,COLUMNS);
		String w = validPuzzle.getWord(moveThatDoesnotFindHiddenWord);
		printStrTest("getWord with move " + moveThatDoesnotFindHiddenWord.toString(), w , null);

		char[][]boardCopy = validPuzzle.board();
		printTest("board", "well copied", " board", BOARD != boardCopy );
		boolean same = true;
		for (int i = 0; i < ROWS && same ; i++) {
			for (int j = 0; j < COLUMNS && same; j++) {
				if (BOARD[i][j] != boardCopy[i][j]){
					printTest("board", "well copied", " board", false  );
					same = false;
				}
			}
		}
	}

	/**
	 * Tests the methods of class WordSearch:
	 * - Puzzle puzzle()
	 * - int duration()
	 * - int howManyFoundWords()
	 * - public int score()
	 * - String[] foundWords()
	 * - boolean isFinished()
	 * - boolean play(Move move)
	 * - String toString()
	 */
	public static void testWordSearch(){
		Move [] m = new Move[5];

		//  the moves to be executed
		m[0] = new Move(1, 1, 1, 4, ROWS, COLUMNS);
		// a move already played
		m[1] = new Move(1, 1, 1, 4, ROWS, COLUMNS);
		m[2] = new Move(2, 2, 5, 2, ROWS, COLUMNS);
		// a move that does not find a hidden word
		m[3] = new Move(4, 1, 4, 4, ROWS, COLUMNS);
		m[4] = new Move(1, 1, 4, 4, ROWS, COLUMNS);

		// the valid puzzle to use in the game
		Puzzle validPuzzle = new Puzzle(BOARD, VALID_HIDDEN);

		//initialize the WordSearch game
		WordSearch game = new WordSearch(validPuzzle, DURATION_IN_SECONDS);
		System.out.println();
		System.out.println(">>> Testing class WordSearch: <<<");
		System.out.println();
		System.out.println("The starting state:");
		System.out.println(game.toString() + "\n");

		int gameDuration = game.duration();
		printIntTest("duration", gameDuration, DURATION_IN_SECONDS);

		double meanTime = DURATION_IN_SECONDS / validPuzzle.numberHiddenWords();
		System.out.println("\n> Word Points in this game: "+ game.puzzle().rows() * game.puzzle().columns() / 10);
		System.out.println("> Expected time for finding a word: "+ (int)meanTime + "seconds");

		int nMoves = 0;
		int words;

		do {
			System.out.println("\n> Executing move: " + m[nMoves]);
			System.out.println("\n> Executing move: " +
					m[nMoves].startRow() + ", " +  m[nMoves].startColumn() + ", " +
					m[nMoves].endRow() + ", "  +  m[nMoves].endColumn());
			boolean wordIsFound = game.play(m[nMoves]);

			System.out.println("> move found word?: " + wordIsFound);
			System.out.println("> move finds word: " + game.puzzle().getWord(m[nMoves]));
			System.out.println("> found words: ");
			words = game.howManyFoundWords();
			for (int k = 0; k < words; k++)
				System.out.print(game.foundWords()[k] + "  " );
			System.out.println();
			printIntTest("howManyFoundWords", words, nMoves / 2  + 1);
			System.out.println("> current score: " + game.score());
			System.out.println("  (be aware this value can vary from run to run, since it depends on elapsed time)");

			nMoves++;

			if (nMoves ==  m.length - 1) {
				System.out.println("\n> Making a big pause before the last move....");
				try {
					Thread.sleep((long) (meanTime*1000 + 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} while (nMoves < m.length && !game.isFinished());

		printTest("isFinished", "finished", "game",  game.isFinished() );
		System.out.println();

		System.out.println("The ending state:");
		System.out.println(game.toString() + "\n");
	}

	public static void printTest(String methodName, String msg,
			String className, boolean obtained){
		System.out.println(">>Testing " + methodName + " for " + msg + " " + className);
		boolean expected =  (msg.equals("valid") || msg.equals("well copied") || msg.equals("finished") );
		if (obtained == expected )
			System.out.println("  Ok");
		else System.out.println("  ERROR: obtained " +  obtained + " but should be " +  expected);

	}

	public static void printIntTest(String methodName, int obtained, int expected){
		System.out.println(">>Testing " + methodName + ": ");
		if (obtained == expected)
			System.out.println("  Ok "+ obtained);
		else
			System.out.println("  ERROR: obtained " + obtained + "but should be " + expected);
	}

	public static void printStrTest(String methodName, String obtained, String expected){
		System.out.println(">>Testing " + methodName + ": ");
		if (obtained == null && expected == null || obtained.equals(expected) )
			System.out.println("  Ok " + obtained);
		else
			System.out.println("  ERROR: obtained " + obtained + "but should be " + expected);
	}

	public static void printDirTest(String methodName, Direction obtained, Direction expected){
		System.out.println(">>Testing " + methodName + ": ");
		if (obtained.equals(expected))
			System.out.println("  Ok  " + obtained.name());
		else
			System.out.println("  ERROR: obtained " + obtained.name() + "but should be " +  expected.name());
	}


}
