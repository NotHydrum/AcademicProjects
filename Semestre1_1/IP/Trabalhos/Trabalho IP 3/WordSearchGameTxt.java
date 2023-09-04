import java.util.Scanner;

/**
 * Lets the user play a round of Word Search.
 * The class requires a valid Puzzle, the user can specify the location of a valid text file (see
 * PuzzleReader Docs) or can not specify and the class will create and use a PuzzleGenerator to
 * randomly generate Puzzles. Every round will use a different random Puzzle.
 */
public class WordSearchGameTxt {

  private static Scanner sc;
  private static PuzzleGenerator gen = new PuzzleGenerator();
  private static PuzzleReader reader;
  private static final int TIME = 120;

  /**
   * Main Procedure.
   */
  public static void main(String[] args) {
    Puzzle puzzle;
    if (args.length == 0) {
      puzzle = gen.nextPuzzle();
    }
    else {
      reader = new PuzzleReader(args[0]);
      if (reader.isPuzzleAvailable()) {
        if (Puzzle.definesPuzzle(reader.getPuzzle(), reader.getHiddenWords())) {
          puzzle = new Puzzle(reader.getPuzzle(), reader.getHiddenWords());
        }
        else {
          System.out.println("Given file does not contain a valid puzzle.");
          System.exit(0);
          puzzle = gen.nextPuzzle(); //This line is here because otherwise the compiler would throw
                                     //an error due to puzzle not being initialized.
        }
      }
      else {
        System.out.println(reader.getErrorMsgs());
        System.exit(0);
        puzzle = gen.nextPuzzle(); //This line is here because otherwise the compiler would throw
                                   //an error due to puzzle not being initialized.
      }
    }
    boolean playAgain = false;
    do {
      sc = new Scanner(System.in);
      WordSearch game = new WordSearch(puzzle, TIME);
      do {
        System.out.print(game.toString());
        Move move = readMove(game.puzzle());
        if (game.play(move)) {
          System.out.println("You found a hidden word: " + game.puzzle().getWord(move));
        }
        else if (!game.isFinished()) {
          System.out.println("You did not find a hidden word.");
        }
      } while (!game.isFinished());
      if (game.timeLeft() < game.duration()) {
        System.out.println("\n" + "Time has ran out.");
        System.out.println(game.toString());
      }
      else {
        System.out.println("\n" + game.toString());
        System.out.println("Good work, you found all hidden words.");
      }
      boolean loop;
      do {
        System.out.print("Do you want to play again? (True/False) ");
        if (sc.hasNextBoolean()) {
          playAgain = sc.nextBoolean();
          loop = false;
        }
        else {
          System.out.println("Invalid answer." + "\n");
          loop = true;
          sc = new Scanner(System.in);
        }
      } while (loop);
      if (playAgain && args.length == 0) {
        puzzle = gen.nextPuzzle();
      }
    } while (playAgain);
    System.out.println("Thanks for playing. Hope you enjoyed. :)");
  }

  private static Move readMove(Puzzle puzzle) {
    int[] moveInts = {-1, -1, -1, -1};
    String moveString;
    boolean isValid;
    do {
      isValid = true;
      System.out.print("Give your move: ");
      moveString = sc.nextLine();
      moveString.replace(' ', '\n');
      Scanner sc2 = new Scanner(moveString);
      for (int i = 0; i < 4; i++) {
        if (sc2.hasNextInt()) {
          moveInts[i] = sc2.nextInt();
        }
      }
      if (!Move.definesMove(moveInts[0], moveInts[1], moveInts[2], moveInts[3], puzzle.rows(),
          puzzle.columns()) || sc2.hasNext()) {
        System.out.println("Your move is invalid.");
        System.out.println();
        isValid = false;
      }
    } while (!isValid);
    Move move = new Move(moveInts[0], moveInts[1], moveInts[2], moveInts[3], puzzle.rows(), puzzle.columns());
    return move;
  }

}
