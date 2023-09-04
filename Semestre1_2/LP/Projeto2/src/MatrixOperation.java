/**
 * Various methods capable of modifying matrixes as well as methods to determine whether an array is a square and/or
 * identity matrix.
 * @author Miguel Nunes fc56338
 */
public class MatrixOperation {
	
	/**
	 * Determines whether a 2-dimensional array is a square matrix, that is, all columns and rows are the same size.
	 * @param m The array do validate.
	 * @return 'true' if square, 'false' otherwise.
	 */
	static boolean isSquare(int[][] m) {
		boolean answer = true;
		if (m.length > 0) {
			for (int i = 0; i < m.length - 1; i++) {
				if (m[i].length != m.length || (i != 0 && m[i].length != m[i - 1].length)) {
					answer = false;
				}
			}
		}
		else {
			answer = false;
		}
		return answer;
	}
	
	/**
	 * Determines whether a 2-dimensional array is an identity matrix.
	 * @param m The array do validate.
	 * @requires isSquare(m)
	 * @return 'true' if identity, 'false' otherwise.
	 */
	static boolean isIdentity(int[][] m) {
		boolean answer = true;
		for (int i = 0; i < m.length; i++) {
			for (int f = 0; f < m[i].length; f++) {
				if ((i == f && m[i][f] != 1) || (i != f && m[i][f] != 0)) {
					answer = false;
				}
			}
		}
		return answer;
	}
	
	/**
	 * Returns a 'copy' of the matrix where only the values of the primary diagonal are copied and the rest are
	 * set to 0.
	 * @param m Matrix to copy.
	 * @requires isSquare(m)
	 * @return 'Copy' of the matrix where only the values of the primary diagonal are copied.
	 */
	static int[][] diagonal(int[][] m) {
		int[][] answer = new int[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int f = 0; f < m[i].length; f++) {
				if (i == f) {
					answer[i][f] = m[i][f];
				}
				else {
					answer[i][f] = 0;
				}
			}
		}
		return answer;
	}
	
	/**
	 * Returns an array containing the values of array's primary diagonal, from top left to bottom right.
	 * @param m Array whose diagonal to return.
	 * @requires isSquare(m)
	 * @return Array containing the values of array's primary diagonal.
	 */
	static int[] diagonalVector(int[][] m) {
		int[] answer = new int[m.length];
		for (int i = 0; i < m.length; i++) {
			for (int f = 0; f < m[i].length; f++) {
				if (i == f) {
					answer[i] = m[i][f];
				}
			}
		}
		return answer;
	}
	
	/**
	 * Transposes a matrix.
	 * @param m Matrix to transpose.
	 * @requires isSquare(m)
	 * @return 'm's transposed matrix.
	 */
	static int[][] transpose(int[][] m) {
		int[][] answer = new int[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int f = 0; f < m[i].length; f++) {
				answer[f][i] = m[i][f];
			}
		}
		return answer;
	}
	
	/**
	 * Returns the sum of a row, column or diagonal of a matrix depending on the chosen 'type': </br>
	 * type == 1, returns the sum of row 'index'; </br>
	 * type == 2, returns the sum of column 'index'; </br>
	 * type == 3, returns the sum of the primary diagonal (ignores 'index').
	 * @param m Matrix to sum up.
	 * @param index index of the row/column to sum up.
	 * @param type What to sum up.
	 * @requires isSquare(m) && type > 0 && type <= 3 && index >= 0 && index < m.length
	 * @return Returns the sum of a row, column or diagonal.
	 */
	static int sumCalculation(int[][] m, int index, int type) {
		int answer = 0;
		switch (type) {
			case 1:
				for (int i = 0; i < m[index].length; i++) {
					answer += m[index][i];
				}
			break;
			case 2:
				for (int i = 0; i < m[index].length; i++) {
					answer += m[i][index];
				}
			break;
			case 3:
				for (int i = 0; i < m.length; i++) {
					answer += m[i][i];
				}
			break;
		}
		return answer;
	}
	
	/**
	 * Returns a copy of 'm', with the values of line 'index' are multiplied by 'scalar'.
	 * @param m Matrix to multiply
	 * @param scalar Number to multiply by.
	 * @param index Number of the line to multiply.
	 * @required isSquare(m) && index >= 0 && index < m.length
	 * @return Copy of 'm', with line 'index' multiplied by 'scalar'.
	 */
	static int[][] multiplyLine(int[][] m, int scalar, int index) {
		int[][] answer = new int[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int f = 0; f < m[i].length; f++) {
				if (i == index) {
					answer[i][f] = scalar * m[i][f];
				}
				else {
					answer[i][f] = m[i][f];
				}
			}
		}
		return answer;
	}
	
	/**
	 * Returns a copy of the matrix, where all values of all rows, except for row 'index', are subtracted by the value
	 * in the same column of row 'index'.
	 * @param m Matrix to subtract.
	 * @param index Row to subtract by.
	 * @requires isSquare(m) && index >= 0 && index < m.length
	 * @return
	 */
	static int[][] subtractLine(int[][] m, int index) {
		int[][] answer = new int[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int f = 0; f < m[i].length; f++) {
				if (i == index) {
					answer[i][f] = m[i][f];
				}
				else {
					answer[i][f] = m[i][f] - m[index][f];
				}
			}
		}
		return answer;
	}
	
}