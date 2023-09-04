/**
 * Class whose instances represent a rock piece.
 * @author Miguel Nunes fc56338
 */
public class Piece {
	
	private final int width;
	private final int heigth;
	
	/**
	 * Constructs a new rock Piece with the give parameters.
	 * @param width The rock piece's width.
	 * @param heigth The rock piece's height
	 * @requires width > 0 && height > 0
	 */
	Piece(int width, int heigth) {
		this.width = width;
		this.heigth = heigth;
	}
	
	/**
	 * @return The piece's height.
	 */
	int heigth() {
		return heigth;
	}

	/**
	 * @return The piece's width.
	 */
	int width() {
		return width;
	}

	/**
	 * @return The piece's area.
	 */
	int area() {
		return heigth * width;
	}
	
	/**
	 * @return An exact copy of this Piece.
	 */
	Piece copy() {
		return new Piece(width, heigth);
	}
	
}
