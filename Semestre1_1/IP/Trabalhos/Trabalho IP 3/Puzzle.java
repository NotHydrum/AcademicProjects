import java.util.Arrays;

/**
 * Constructs a Puzzle object.
 * Puzzle objects define a Word Search puzzle, containing the puzzle's board and the words
 * hidden in it.
 * @author Miguel Nunes fc56338
 * @author Bruno Raimundo fc56322
 */
public class Puzzle {

  private char[][] board;
  private String[] hiddenWords;

  /**
   * Verifies that there is at least 1 hidden word, hidden words have at least 1 character,
   * aren't repeated in the array and are hidden in a line, column or diagonal of the board.
   * @param board Board of the puzzle.
   * @param hiddenWords Words hidden in the puzzle.
   * @return {@code true} if data defines a valid Puzzle, {@code false} if otherwise.
   * @requires {@code hiddenWords != null} and board is a matrix.
   */
  public static boolean definesPuzzle(char[][] board, String[] hiddenWords) {
    boolean answer = true;
    if (hiddenWords.length > 0) {
      for (int i = 0; i < hiddenWords.length && answer; i++) {
        if (!(hiddenWords[i].length() > 0) || !isHidden(board, hiddenWords[i])) {
          answer = false;
        }
        for (int f = 0; f < hiddenWords.length && answer; f++) {
          if (hiddenWords[i].equals(hiddenWords[f]) && i != f) {
            answer = false;
          }
        }
      }
    }
    else {
      answer = false;
    }
    return answer;
  }

  private static boolean isHidden(char[][] board, String word) {
  boolean answer = false;
  if (word.length() > 0) {
    StringBuilder sb = new StringBuilder(word);
    sb.reverse();
    String reverseWord = sb.toString();
    //Horizontal
    for (int i = 0; i < board.length && !answer; i++) {
      sb.delete(0, sb.length());
      for (int f = 0; f < board[0].length; f++) {
        sb.append(board[i][f]);
      }
      if ((sb.toString()).contains(word) || (sb.toString()).contains(reverseWord)) {
        answer = true;
      }
    }
    //Vertical
    for (int i = 0; i < board[0].length && !answer; i++) {
      sb.delete(0, sb.length());
      for (int f = 0; f < board.length; f++) {
        sb.append(board[f][i]);
      }
      if ((sb.toString()).contains(word) || (sb.toString()).contains(reverseWord)) {
        answer = true;
      }
    }
    //Diagonal Right
    for (int i = 0; i < 1 + (board.length - 1) + (board[0].length - 1) && !answer; i++) {
      sb.delete(0, sb.length());
      if (i == 0) {
        for (int f = 0; f < board.length && f < board[0].length; f++) {
          sb.append(board[f][f]);
        }
      }
      else if (i > 0 && i < board[0].length) {
        for (int f = 0; f + i - 1 < board.length && f + i < board[0].length; f++) {
          sb.append(board[f + i - 1][f + i]);
        }
      }
      else if (i > board[0].length) {
        for (int f = 0; f + i - board[0].length < board.length && f + i - board[0].length <
             board[0].length; f++) {
          sb.append(board[f + i - board[0].length][f + i - board[0].length - 1]);
        }
      }
      if ((sb.toString()).contains(word) || (sb.toString()).contains(reverseWord)) {
        answer = true;
      }
    }
    //Diagonal Left
    for (int i = 0; i < 1 + (board.length - 1) + (board[0].length - 1) && !answer; i++) {
      sb.delete(0, sb.length());
      for (int f = 0; f < board.length; f++) {
        for (int k = 0; k < board[0].length; k++) {
          if (f + k == i) {
            sb.append(board[f][k]);
          }
        }
      }
      if ((sb.toString()).contains(word) || (sb.toString()).contains(reverseWord)) {
        answer = true;
      }
    }
  }
  return answer;
  }

  /**
   * Constructs a Puzzle Object with the given data.
   * @param board Puzzle's board.
   * @param hiddenWords Words hidden in the board.
   * @requires {@code definesPuzzle(board, hiddenWords)}
   */
  public Puzzle(char[][] board, String[] hiddenWords) {
    this.board = new char[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int f = 0; f < board[0].length; f++) {
        this.board[i][f] = board[i][f];
      }
    }
    this.hiddenWords = new String[hiddenWords.length];
    for (int i = 0; i < hiddenWords.length; i++) {
      this.hiddenWords[i] = hiddenWords[i];
    }
  }

  /**
   * @return An independent copy of the Puzzle's board.
   */
  public char[][] board() {
    char[][] copy = new char[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int f = 0; f < board[0].length; f++) {
        copy[i][f] = board[i][f];
      }
    }
    return copy;
  }

  /**
   * @return The number of rows in the Puzzle's board.
   * @ensures {@code \return > 0}
   */
  public int rows() {
    return board.length;
  }

  /**
   * @return The number of columns in the Puzzle's board.
   * @ensures {@code \return > 0}
   */
  public int columns() {
    return board[0].length;
  }

  /**
   * @return The number of hidden words.
   * @ensures {@code \return > 0}
   */
  public int numberHiddenWords() {
    return hiddenWords.length;
  }

  /**
   * Finds and returns the word hidden in the position of the given Move.
   * @param move Position word is to be found.
   * @return Returns a hidden word if any are found, or {@code null} if none are found.
   * @requires {@code move != null && move.rows() == this.rows() && move.columns() == this.columns()}
   */
  public String getWord(Move move) {
    String answer = null;
    StringBuilder sb = new StringBuilder();
    if (move.direction() == Direction.HORIZONTAL) {
      for (int i = move.startColumn() - 1; i < move.endColumn(); i++) {
        sb.append(board[move.startRow() - 1][i]);
      }
    }
    else if (move.direction() == Direction.VERTICAL) {
      for (int i = move.startRow() - 1; i < move.endRow(); i++) {
        sb.append(board[i][move.startColumn() - 1]);
      }
    }
    else if (move.direction() == Direction.DIAGONAL_RIGHT) {
      for (int i = 0; i < move.endColumn() - move.startColumn() + 1; i++) {
        sb.append(board[move.startRow() - 1 + i][move.startColumn() - 1 + i]);
      }
    }
    else {
      for (int i = 0; i < move.startColumn() - move.endColumn() + 1; i++) {
        sb.append(board[move.startRow() - 1 + i][move.startColumn() - 1 - i]);
      }
    }
    for (String i : hiddenWords) {
      if ((sb.toString()).equals(i) || ((sb.reverse()).toString()).equals(i)) {
        answer = i;
      }
    }
    return answer;
  }

}
