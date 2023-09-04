import java.util.Arrays;
import java.util.Random;

/**
 * Constructs a PuzzleGenerator Object.
 * By default, Puzzle Generators create random Puzzles with 20 x 20 dimensions and 12 random
 * hidden words, taken from a list of car manufacterers.
 * @author Miguel Nunes fc56338
 * @author Bruno Raimundo fc56322
 */
public class PuzzleGenerator {

  private static Random rd = new Random();
  private static StringBuilder sb = new StringBuilder();
  private final static String[] WORDS = {"ALFAROMEO", "ASTONMARTIN", "AUDI","BENTLEY", "BMW",
  "BUGATTI", "BUICK", "CADILLAC", "CHEVROLET", "CHRYSLER", "CITROEN", "DODGE", "FERRARI", "FORD",
  "FIAT", "HONDA", "HUMMER", "HYUNDAI", "INFINITI", "ISUZU", "JAGUAR", "JEEP", "KIA",
  "LAMBORGHINI", "LANDROVER", "LEXUS", "LINCOLN", "LOTUS", "MAZDA", "MASERATI", "MCLAREN",
  "MERCEDES", "MERCURY", "MINI", "MITSUBISHI", "NISSAN", "OLDSMOBILE", "PAGANI", "PEUGEOT",
  "PONTIAC", "PORSCHE", "SKODA", "SMART", "REGAL", "RENAULT", "SAAB", "SATURN", "SUBARU", "SUZUKI",
  "TOYOTA", "TESLA", "VOLKSWAGEN", "VOLVO"};
  private final static Direction[] DIRECTIONS = Direction.values();
  private final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final static char NULL = '\u0000';
  private final static int SIZE = 20;
  private int numberHiddenWords;
  private String[] hiddenWords;
  private char[][] board;

  /**
   * Constructs a PuzzleGenerator Object.
   */
  public PuzzleGenerator() {}

  /**
   * @return A randomly generated Puzzle.
   * @ensures {@code \return != null}
   */
  public Puzzle nextPuzzle() {
    numberHiddenWords = 12;
    hiddenWords = generateHiddenWords(numberHiddenWords);
    board = generateRandomBoard(hiddenWords);
    Puzzle puzzle = new Puzzle(board, hiddenWords);
    return puzzle;
  }

  private static String[] generateHiddenWords(int numberHiddenWords) {
    if (numberHiddenWords > WORDS.length) {
      numberHiddenWords = WORDS.length;
    }
    String[] hiddenWords = new String[numberHiddenWords];
    for (int i = 0; i < numberHiddenWords; i++) {
      boolean repeat;
      do {
        repeat = false;
        hiddenWords[i] = WORDS[rd.nextInt(WORDS.length)];
        for (int f = 0; f < i; f++) {
          if (hiddenWords[i] == hiddenWords[f]) {
            repeat = true;
          }
        }
      } while (repeat);
    }
    return hiddenWords;
  }

  private static char[][] generateRandomBoard(String[] hiddenWords) {
    char[][] board = new char[SIZE][SIZE];
    for (int i = 0; i < SIZE; i++) {
      for (int f = 0; f < SIZE; f++) {
        board[i][f] = NULL;
      }
    }
    for (int i = 0; i < hiddenWords.length; i++) {
      boolean forwards = rd.nextBoolean();
      if (forwards) {
        hideWordInBoard(board, hiddenWords[i]);
      }
      else {
        sb.append(hiddenWords[i]);
        sb.reverse();
        hideWordInBoard(board, sb.toString());
      }
      sb.delete(0, sb.length());
    }
    for (int i = 0; i < SIZE; i++) {
      for (int f = 0; f < SIZE; f++) {
        if (board[i][f] == NULL) {
          board[i][f] = ALPHABET.charAt(rd.nextInt(ALPHABET.length()));
        }
      }
    }
    return board;
  }

  private static char[][] hideWordInBoard(char[][] board, String word) {
    boolean inBoard = false;
    do {
      Boolean possible = true;
      int row = rd.nextInt(SIZE);
      int column = rd.nextInt(SIZE);
      Direction direction = DIRECTIONS[rd.nextInt(DIRECTIONS.length)];
      if (direction == Direction.HORIZONTAL) {
        for (int f = 0; f < word.length(); f++) {
          if (column + f >= SIZE || (board[row][column + f] != NULL &&
              board[row][column + f] != word.charAt(f))) {
            possible = false;
          }
        }
        if (possible) {
          for (int f = 0; f < word.length(); f++) {
            board[row][column + f] = word.charAt(f);
          }
          inBoard = true;
        }
      }
      else if (direction == Direction.VERTICAL) {
        for (int f = 0; f < word.length(); f++) {
          if (row + f >= SIZE || (board[row + f][column] != NULL &&
              board[row + f][column] != word.charAt(f))) {
            possible = false;
          }
        }
        if (possible) {
          for (int f = 0; f < word.length(); f++) {
            board[row + f][column] = word.charAt(f);
          }
          inBoard = true;
        }
      }
      else if (direction == Direction.DIAGONAL_LEFT) {
        for (int f = 0; f < word.length(); f++) {
          if (row + f >= SIZE || column - f < 0 || (board[row + f][column - f] != NULL &&
              board[row + f][column - f] != word.charAt(f))) {
            possible = false;
          }
        }
        if (possible) {
          for (int f = 0; f < word.length(); f++) {
            board[row + f][column - f] = word.charAt(f);
          }
          inBoard = true;
        }
      }
      else {
        for (int f = 0; f < word.length(); f++) {
          if (row + f >= SIZE || column + f >= SIZE || (board[row + f][column + f] != NULL &&
              board[row + f][column + f] != word.charAt(f))) {
            possible = false;
          }
        }
        if (possible) {
          for (int f = 0; f < word.length(); f++) {
            board[row + f][column + f] = word.charAt(f);
          }
          inBoard = true;
        }
      }
    } while (!inBoard);
    return board;
  }

}
