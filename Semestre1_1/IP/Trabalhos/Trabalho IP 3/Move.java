/**
 * Constructs a Move object.
 * Move objects define a group of contiguous positions in a 2-dimensional board.
 * Rows and Columns start at the number 1.
 * @author Miguel Nunes fc56338
 * @author Bruno Raimundo fc56322
 */
public class Move {

  private int startRow;
  private int endRow;
  private int startColumn;
  private int endColumn;
  private int rows;
  private int columns;
  private Direction moveDirection;

  /**
   * Verifies that row1, col1, row2, col2 are lines and columns of a board with rows x columns
   * dimensions and define contiguous position in a line, column or diagonal.
   * To be valid the starting position needs to be the left-most position (Horizontal) or the
   * top-most position (Vertical and Diagonals).
   * @param row1 Line of the starting position.
   * @param col1 Column of the starting position.
   * @param row2 Line of the ending position.
   * @param col2 Column of the ending position.
   * @param rows Number of lines in the board.
   * @param columns Number of columns in the board.
   * @return {@code true} if given positions define a valid move in the given board dimensions,
   * {@code false} if otherwise.
   */
  public static boolean definesMove(int row1, int col1, int row2, int col2, int rows, int columns) {
    boolean answer = false;
    if (row1 <= rows && row2 <= rows && col1 <= columns && col2 <= columns) {
      //Horizontal
      if (row1 == row2 && col1 <= col2) {
        answer = true;
      }
      //Vertical
      else if (row1 < row2 && col1 == col2) {
        answer = true;
      }
      //Diagonal Right || Diagonal Left
      else if ((row2 - row1 == col2 - col1 || row2 - row1 == -(col2 - col1)) && row1 < row2) {
        answer = true;
      }
    }
    return answer;
  }

  /**
   * Constructs a Move Object with the given data.
   * @param row1 Line of the starting position.
   * @param col1 Column of the starting position.
   * @param row2 Line of the ending position.
   * @param col2 Column of the ending position.
   * @param rows Number of lines in the board.
   * @param columns Number of columns in the board.
   * @requires {@code definesMove(row1, col1, row2, col2, rows, columns)}
   */
  public Move(int row1, int col1, int row2, int col2, int rows, int columns) {
    startRow = row1;
    endRow = row2;
    startColumn = col1;
    endColumn = col2;
    this.rows = rows;
    this.columns = columns;
    if (startRow == endRow) {
      moveDirection = Direction.HORIZONTAL;
    }
    else if (startColumn == endColumn) {
      moveDirection = Direction.VERTICAL;
    }
    else if (endRow - startRow == -(endColumn - startColumn)) {
      moveDirection = Direction.DIAGONAL_LEFT;
    }
    else if (endRow - startRow == endColumn - startColumn) {
      moveDirection = Direction.DIAGONAL_RIGHT;
    }
  }

  /**
   * @return The move's starting line.
   * @ensures {@code \return > 0}
   */
  public int startRow() {
    return startRow;
  }

  /**
   * @return The move's starting column.
   * @ensures {@code \return > 0}
   */
  public int startColumn() {
    return startColumn;
  }

  /**
   * @return The move's ending line.
   * @ensures {@code \return > 0}
   */
  public int endRow() {
    return endRow;
  }

  /**
   * @return The move's ending column.
   * @ensures {@code \return > 0}
   */
  public int endColumn() {
    return endColumn;
  }

  /**
   * @return The move's direction.
   */
  public Direction direction() {
    return moveDirection;
  }

  /**
   * @return The board's rows.
   * @ensures {@code \return > 0}
   */
  public int rows() {
    return rows;
  }

  /**
   * @return The board's columns.
   * @ensures {@code \return > 0}
   */
  public int columns() {
    return columns;
  }

}
