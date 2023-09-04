/**
 * The class {@code Board} defines a type whose objects represent
 * the state of a square board with a given dimension. The cells
 * of the board can be empty or filled with a piece.
 *
 * @author  IP1920
 */
public class Board {

  private final int dimension;
  private Piece[][] positions;

  /**
  * TODO Adicionar documentacao
  */
  public Board(int dimension) {
    this.dimension = dimension;
    positions = new Piece[dimension][dimension];
    for (int i = 0; i < dimension; i++) {
      for (int f = 0; f < dimension; f++) {
        positions[i][f] = null;
      }
    }
  }

  /**
  * TODO Adicionar documentacao
  */
  public void fill(int row, int col, Piece piece) {
    positions[row - 1][col - 1] = piece;
  }

  /**
   * TODO Adicionar documentacao
   */
  public Piece getPiece(int row, int col) {
    return positions[row - 1][col - 1];
  }

  /**
   * The dimension of the board
   *
   * @return The dimension of the board
   * @ensures {@code \result > 0 }
   */
  public int dim() {
    return dimension;
  }

  /**
  * Checks if the cell is filled
  *
  * @return whether the cell is filled
  * @requires {@code 1<= row,col <= dim()}
  */
  public boolean isFilled(int row, int col){
    return !(positions[row - 1][col - 1] == null);
  }

  /**
  * Checks if the board is full, i.e., every
  * cell is filled with a piece
  *
  * @return whether the board is full
  */
  public boolean isFull() {
    boolean answer = true;
    for (int i = 0; i < dimension; i++) {
      for (int f = 0; f < dimension; f++) {
        if (positions[i][f] == null) {
          answer = false;
        }
      }
    }
    return answer;
  }

  /**
  * Copies the board
  *
  * @return an independent copy of the board
  */
  public Board copy() {
    Board copy = new Board(dimension);
    for (int i = 0; i < dimension; i++) {
      for (int f = 0; f < dimension; f++) {
        copy.positions[i][f] = this.positions[i][f];
      }
    }
    return copy;
  }

  /**
  * TODO Adicionar documentacao
  */
  public String toString() {
    String answer = "";
    for (int i = 0; i < dimension; i++) {
      answer += toStringRow(positions[i]);
    }
    return answer;
  }

  /**
  * Returns a textual representation of an array of pieces
  * Null elements are represented with white spaces
  *
  * @param pieces 	the array with the pieces
  * @return 			a textual representation of the pieces in the array
  */
  private static String toStringRow(Piece[] pieces) {
	  StringBuilder sb = new StringBuilder("| ");
	  for (int i = 0; i < pieces.length; i++)
		   sb.append(pieces[i]==null? " ": pieces[i]);
	  sb.append(" | \n");
	  return sb.toString();
  }
}
