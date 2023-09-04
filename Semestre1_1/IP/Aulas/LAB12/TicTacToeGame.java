/**
 * The class {@code TicTacToeGame} defines a type whose objects represent
 * the state of a game of tictactoe with players 1 and 2.
 * The the first player is 1 and he plays with Piece.X.
 *
 * @author IP1920
 */
public class TicTacToeGame{

  private Board game;

  /**
   * Creates a tictactoe game in the initial state.
   */
  public TicTacToeGame() {
    game = new Board(3);
  }

  /**
   * Checks if the game is over, i.e., if one row, column or diagonal is filled
   * with the same piece or if the board is full
   *
   * @return      if the game is over
   */
  public boolean isOver(){
    boolean answer = false;
    for (int i = 0; i < game.dim(); i++) {
      
    }
    return answer;
  }

  /**
   * The next player to play
   *
   * @return      The next player to play
   * @requires    {@code !isOver()}
   * @ensures     {@code 1 ≤ \result ≤ 2}
   */
  public int getPlayer(){
      //TODO
      return 1;
  }

  /**
   * Checks if the given integers define a valid move
   * @param row   the row number
   * @param col   the column number
   * @return      if the given integers define a valid move
   */
  public boolean isValid(int row, int col){
      //TODO
      return false;
  }

  /**
   * Gets a copy of the board in the current state
   *
   * @return an independent copy of the board
   * @ensures {@code \result != null}
   */
  //public Board getBoard(){
      //TODO
  //}

  /**
   * Plays the symbol associated to getPlayer() in the square indentified
   * by the row and col
   *
   * @param row   the row number
   * @param col   the column number
   * @requires    {@code isValid(row,col) && !isOver()}
   */
  //public void play(int row, int col){
      //TODO
  //}

  /**
   * Gets a textual representation of the state of the game
   *
   * @return  A textual representation of the state of the game
   */
  public String toString(){
      //TODO
      return " ";
  }
}
