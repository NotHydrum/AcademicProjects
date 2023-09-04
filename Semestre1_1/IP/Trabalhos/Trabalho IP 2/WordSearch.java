import java.util.Scanner;

/**
 * This class, {@code WordSearch}, with the help of the PuzzleReader class and a Puzzle text file (see PuzzleReader
 * Docs), lets the user play a game of word search.
 * @author Miguel Nunes fc56338
 * @author Bruno Raimundo fc56322
 */
public class WordSearch {

  /**
   * Main Procedure
   */
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    PuzzleReader puzzle = new PuzzleReader(args[0]);
    if (puzzle.isPuzzleAvailable()) {
      char[][] board = puzzle.getPuzzle();
      for (int i = 0; i < board.length; i++) {
        for (int f = 0; f < board[0].length; f++) {
          board[i][f] = Character.toUpperCase(board[i][f]);
        }
      }
      String[] hiddenWords = puzzle.getHiddenWords();
      for (String i : hiddenWords) {
        i = i.toUpperCase();
      }
      if (isValidGame(board, hiddenWords)) {
        printPuzzle(board, hiddenWords);
        int wordsHidden = hiddenWords.length;
        String wordsFound = "";
        do {
          int[] move = readMove(sc, board.length, board[0].length);
          if (findWord(board, move, hiddenWords) != null) {
            if (!wordsFound.contains(findWord(board, move, hiddenWords))) {
              wordsFound += ((findWord(board, move, hiddenWords)) + " ");
              System.out.println("Words found: " + wordsFound);
              wordsHidden -= 1;
              System.out.println("Hidden words: " + wordsHidden);
              System.out.println();
            }
            else {
              System.out.println("You have already found this hidden word.");
              System.out.println();
            }
          }
          else {
            System.out.println("Your move does not contain a hidden word.");
            System.out.println();
          }
        } while (wordsHidden > 0);
        System.out.println("Good work. All hidden words were found.");
      }
      else {
        System.out.println("[x] Invalid puzzle. Not all given words are hidden in the puzzle.");
      }
    }
    else {
      System.out.println(puzzle.getErrorMsgs());
    }
  }

  /**
   * Prints the puzzle and the number of hidden words.
   * @param board Puzzle to be printed.
   * @param hiddenWords Words hidden in the puzzle.
   * @requires {@code hiddenWords != null} and {@code board} is a valid matrix.
   */
  public static void printPuzzle(char[][] board, String[] hiddenWords) {
    int spacesLeftNumbers = 0;
    int boardVerticalLength = board.length;
    do {
      spacesLeftNumbers += 1;
      boardVerticalLength /= 10;
    } while (boardVerticalLength > 0);
    int spacesTopNumbers = 0;
    int boardHorizontalLength = board[0].length;
    do {
      spacesTopNumbers += 1;
      boardHorizontalLength /= 10;
    } while (boardHorizontalLength > 0);
    int spacesBetweenChars = spacesTopNumbers;
    for (int i = 0; i <= spacesLeftNumbers; i++) {
      System.out.print(" ");
    }
    for (int i = 1; i <= board[0].length; i++) {
      System.out.print(i);
      if (Math.log10(i) % 1 == 0 && i != 1) {
        spacesTopNumbers -= 1;
      }
      for (int f = 0; f < spacesTopNumbers; f++) {
        System.out.print(" ");
      }
    }
    System.out.println();
    for (int i = 0; i < board.length; i++) {
      System.out.print(i + 1);
      if (Math.log10(i + 1) % 1 == 0 && i != 0) {
        spacesLeftNumbers -= 1;
      }
      for (int f = 0; f < spacesLeftNumbers; f++) {
        System.out.print(" ");
      }
      for (int f = 0; f < board[0].length; f++) {
        String spaces = "";
        for (int k = 0; k < spacesBetweenChars; k++) {
          spaces += " ";
        }
        System.out.print(board[i][f] + spaces);
      }
      System.out.println();
    }
    System.out.println();
    System.out.println("Hidden words: " + hiddenWords.length);
    System.out.println();
  }

  /**
   * Determines if the puzzle is valid, that is, all given words are hidden in the puzzle.
   * @param board Puzzle.
   * @param hiddenWords Words to search for in the puzzle.
   * @return {@code true} if the puzzle is valid, {@code false} if otherwise.
   * @requires {@code hiddenWords != null} and {@code board} is a valid matrix.
   * @ensures {@code \return == true || \return == false}
   */
  public static boolean isValidGame(char[][] board, String[] hiddenWords) {
    boolean answer = false;
    if (hiddenWords.length > 0) {
      answer = true;
      for (String i : hiddenWords) {
        if (!isHidden(board, i)) {
          answer = false;
        }
      }
    }
    return answer;
  }

  /**
   * Determines if {@code word} is hidden in the puzzle, verticaly or horizontally,
   * forwards or backwards.
   * @param board Puzzle.
   * @param word Word to search for in the puzzle.
   * @return {@code true} if {@code word} is hidden in the puzzle, {@code false} if otherwise.
   * @requires {@code word != null} and {@code board} is a valid matrix.
   * @ensures {@code \return == true || \return == false}
   */
  public static boolean isHidden(char[][] board, String word) {
    boolean answer = false;
    if (word.length() > 0) {
      StringBuilder sb = new StringBuilder();
      sb.append(word);
      sb.reverse();
      String reverseWord = sb.toString();
      for (int i = 0; i < board.length; i++) {
        sb.delete(0, sb.length());
        for (int f = 0; f < board[0].length; f++) {
          sb.append(board[i][f]);
        }
        String lineRow = sb.toString();
        if (lineRow.contains(word) || lineRow.contains(reverseWord)) {
          answer = true;
        }
      }
      for (int i = 0; i < board[0].length; i++) {
        sb.delete(0, sb.length());
        for (int f = 0; f < board.length; f++) {
          sb.append(board[f][i]);
        }
        String lineRow = sb.toString();
        if (lineRow.contains(word) || lineRow.contains(reverseWord)) {
          answer = true;
        }
      }
    }
    return answer;
  }

  /**
   * Reads the player's next move through the given scanner and determines if said move is valid.
   * If the move isn't valid, prints an error message and attempts to read a new move.
   * @param sc Scanner the function will use to read the player's move.
   * @param rows Number of rows in the puzzle.
   * @param columns Number of columns in the puzzle.
   * @return An array that contains the player's move.
   * @requires {@code sc != null && rows > 0 && columns > 0}
   * @ensures {@code isValidMove(\return, rows, colums)}
   */
  public static int[] readMove(Scanner sc, int rows, int columns) {
    int[] move = {-1, -1, -1, -1};
    String moveString;
    boolean isValid;
    do {
      System.out.print("Give your move: ");
      moveString = sc.nextLine();
      moveString.replace(' ', '\n');
      Scanner sc2 = new Scanner(moveString);
      for (int i = 0; i < 4; i++) {
        if (sc2.hasNextInt()) {
          move[i] = sc2.nextInt();
        }
      }
      if (!isValidMove(move, rows, columns) || sc2.hasNext()) {
        System.out.println("Your move is invalid.");
        System.out.println();
        isValid = false;
      }
      else {
        isValid = true;
        sc2.close();
      }
    } while (!isValid);
    return move;
  }

  /**
   * Determines if the given move is valid. To be valid the move needs to represent a vertical or
   * horizontal line of contiguos positions, from left to right or top to bottom, within the rows
   * and lines of the puzzle.
   * @param move Move to validate.
   * @param rows Number of rows in the puzzle.
   * @param columns Number of columns in the puzzle.
   * @return {@code true} if move is valid, {@code false} if otherwise.
   * @requires {@code move != null && rows > 0 && columns > 0}
   * @ensures {@code \return == true || \return == false}
   */
  public static boolean isValidMove(int[] move, int rows, int columns) {
    boolean answer;
    if (move.length == 4) {
      if (move[0] >= 0 && move[1] >= 0 && move[2] >= 0 && move[3] >= 0 &&
          move[1] == move [3] && move[2] <= rows && move[3] <= columns &&
          move[0] <= move[2]) {
        answer = true;
      }
      else if (move[0] >= 0 && move[1] >= 0 && move[2] >= 0 && move[3] >= 0 &&
               move[0] == move[2] && move[2] <= rows && move[3] <= columns &&
               move[1] <= move[3]) {
        answer = true;
      }
      else {
        answer = false;
      }
    }
    else {
      answer = false;
    }
    return answer;
  }

  /**
   * Determines if a hidden word was found in the position of the given {@code move}.
   * @param board Puzzle.
   * @param move Position to search in.
   * @param hiddenWords Words to search for in the given position.
   * @return Returns a hidden word if it is found in {@code move},
   *         or {@code null} if none are found.
   * @requires {@code isValidGame(board, hiddenWords) && isValidMove(move, board.length,
               board[0].length)} and {@code board} is a valid matrix.
   */
  public static String findWord(char[][] board, int[] move, String[] hiddenWords) {
    StringBuilder sb = new StringBuilder();
    String returnThis = null;
    if (move[0] == move[2]) {
      for (int i = move[1] - 1; i < move[3]; i++) {
        sb.append(board[move[0] - 1][i]);
      }
    }
    else if (move[1] == move[3]) {
      for (int i = move[0] - 1; i < move[2]; i++) {
        sb.append(board[i][move[1] - 1]);
      }
    }
    String moveWord = sb.toString();
    String reverseMoveWord = (sb.reverse()).toString();
    for (String i : hiddenWords) {
      if (moveWord.equals(i) || reverseMoveWord.equals(i)) {
        returnThis = i;
      }
    }
    return returnThis;
  }

}
