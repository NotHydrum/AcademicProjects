import java.util.Arrays;

/**
 * Constructs a WordSearch object.
 * WordSearch objects represent a round of a Word Search game.
 * @author Miguel Nunes fc56338
 * @author Bruno Raimundo fc56322
 */
public class WordSearch {

  private Puzzle puzzle;
  private String[] wordsFound;
  private int score;
  private long startTime;
  private int maximumDuration;
  private boolean isFinished;

  /**
   * Constructs a WordSearch Object with the given data.
   * @param puzzle Game's puzzle.
   * @param durationInSeconds Duration of the round.
   * @requires {@code puzzle != null && durationInSeconds > 0 &&
   * durationInSeconds / puzzle.numberHiddenWords() > 5}
   */
  public WordSearch(Puzzle puzzle, int durationInSeconds) {
    this.puzzle = puzzle;
    wordsFound = new String[0];
    score = 0;
    isFinished = false;
    startTime = System.currentTimeMillis();
    maximumDuration = durationInSeconds;
  }

  /**
   * @return The game's Puzzle.
   */
  public Puzzle puzzle() {
    return puzzle;
  }

  /**
   * @return The round's duration.
   * @ensures {@code \return > 0}
   */
  public int duration() {
    return maximumDuration;
  }

  /**
   * @return The number of words found.
   * @ensures {@code \return >= 0}
   */
  public int howManyFoundWords() {
    return wordsFound.length;
  }

  /**
   * @return An array containing the words found.
   */
  public String[] foundWords() {
    String[] copy = new String[wordsFound.length];
    for (int i = 0; i < wordsFound.length; i++) {
      copy[i] = wordsFound[i];
    }
    return copy;
  }

  /**
   * @return The current score.
   * @ensure {@code \return > 0}
   */
  public int score() {
    return score;
  }

  /**
   * @return Wether or not the round has ended.
   */
  public boolean isFinished() {
    return isFinished;
  }

  /**
   * @return The time left before the round ends.
   */
  public int timeLeft() {
    int timeLeft = (int)(maximumDuration - currentDuration());
    return timeLeft;
  }

  /**
   * If round time hasn't passed the maximum duration, registers the play and indicates if a word
   * was found in the given positions, otherwise terminates the round.
   * The round is also terminated if all words have been found.
   * @param move Move being played.
   * @return {@code true} if a hidden word was found, {@code false} if otherwise or round time was up.
   * @requires {@code move != null && move.rows() == puzzle.rows() && move.columns == puzzle.columns()
   * && !isFinished()}
   */
  public boolean play(Move move) {
    boolean answer;
    if (currentDuration() < maximumDuration) {
      if (puzzle.getWord(move) != null && !((Arrays.toString(wordsFound)).contains(puzzle.getWord(move)))) {
        wordsFound = Arrays.copyOf(wordsFound, wordsFound.length + 1);
        wordsFound[wordsFound.length - 1] = puzzle.getWord(move);
        if (currentDuration() >= meanTime()) {
          score += wordPoints();
        }
        else {
          score += (1 + meanTime() - (int)currentDuration()) * wordPoints();
        }
        answer = true;
        if (wordsFound.length >= puzzle.numberHiddenWords()) {
          isFinished = true;
        }
      }
      else {
        answer = false;
      }
    }
    else {
      isFinished = true;
      answer = false;
    }
    return answer;
  }

  private long currentDuration() {
    return (System.currentTimeMillis() - startTime) / 1000;
  }

  private int meanTime() {
    return maximumDuration / puzzle.numberHiddenWords() * (1 + wordsFound.length);
  }

  private int wordPoints() {
    return puzzle.rows() * puzzle.columns() / 10;
  }

  /**
   * @return String version of the game state.
   * @ensures {@code \return != null}
   */
  public String toString() {
    String answer = "";
    answer += boardString();
    answer += "Words Found: ";
    for (String i : wordsFound) {
      answer += i + " ";
    }
    answer += "\n" + "Number of Words Left: " + (puzzle.numberHiddenWords() - wordsFound.length)
            + "\n" + "Score: " + score;
    if (!isFinished) {
      answer += "\n" + "Time left: " + timeLeft() + "\n";
    }
    else {
      answer += "\n" + "Game finished." + "\n";
    }
    return answer;
  }

  private String boardString() {
    String answer = "\n";
    char[][] board = puzzle.board();
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
      answer += " ";
    }
    for (int i = 1; i <= board[0].length; i++) {
      answer += i;
      if (Math.log10(i) % 1 == 0 && i != 1) {
        spacesTopNumbers -= 1;
      }
      for (int f = 0; f < spacesTopNumbers; f++) {
        answer += " ";
      }
    }
    answer += "\n";
    for (int i = 0; i < board.length; i++) {
      answer += (i + 1);
      if (Math.log10(i + 1) % 1 == 0 && i != 0) {
        spacesLeftNumbers -= 1;
      }
      for (int f = 0; f < spacesLeftNumbers; f++) {
        answer += " ";
      }
      for (int f = 0; f < board[0].length; f++) {
        String spaces = "";
        for (int k = 0; k < spacesBetweenChars; k++) {
          spaces += " ";
        }
        answer += board[i][f] + spaces;
      }
      answer += "\n";
    }
    answer += "\n";
    return answer;
  }

}
