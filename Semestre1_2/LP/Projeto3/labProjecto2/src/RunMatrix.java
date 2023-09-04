import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * See Main procedure for an explanation of this class.
 * @author Miguel Nunes fc56338
 */
public class RunMatrix {
	
	/**
	 * Constant that defines the 'Next Line' character for any operating system.
	 */
	private static final String NEXT_LINE = System.getProperty("line.separator");
	
	/**
	 * Main procedure that, using methods from the MatrixOperation class, does the following: </br>
	 * 1 - Reads a matrix from 'inputMatrix1.txt'; </br>
	 * 2 - Verifies if the matrix is an identity matrix; </br>
	 * 3 - Writes the matrix's diagonal matrix; </br>
	 * 4 - Writes the matrix's diagonal array; </br>
	 * 5 - Writes the matrix's transposed matrix. </br>
	 * 6 - Writes the of each line, column and the primary diagonal; </br>
	 * 7 - Multiplies a given line of the matrix by a given integer; </br>
	 * 8 - Determines which line of the matrix has the biggest sum and discovers the necessary integer to multiply by
	 * for the line's maximum number to be the smallest number bigger or equal to 10. if the maximum is 0 the integer
	 * 'discovered' is 1. </br>
	 * 9 - Writes down the matrix obtained from multiplying this line by the discovered integer and then subtracting
	 * this line from all the other lines of the matrix.
	 * @param args No arguments required.
	 */
	public static void main(String[] args) {
		int[][] matrix1 = null;
		try (Scanner myScanner = new Scanner(new File("inputMatrix3.txt"))) {
			matrix1 = new int[myScanner.nextInt()][myScanner.nextInt()];
			for (int i = 0; i < matrix1.length; i++) {
				for (int f = 0; f < matrix1.length; f++) {
					matrix1[i][f] = myScanner.nextInt();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.print("Erro: Ficheiro 'inputMatrix1.txt' não existe ou é inacessível." + NEXT_LINE);
			System.exit(0);
		} catch (NoSuchElementException | IllegalArgumentException e) {
			System.out.print("Erro: Tamanho da matriz inválido." + NEXT_LINE);
			System.exit(0);
		}
		if (!MatrixOperation.isSquare(matrix1)) {
			System.out.print("O ficheiro não contém uma matriz quadrada." + NEXT_LINE);
			System.exit(0);
		}
		System.out.print("Matriz de entrada:" + NEXT_LINE);
		for (int i = 0; i < matrix1.length; i++) {
			for (int f = 0; f < matrix1[i].length; f++) {
				System.out.print(matrix1[i][f]);
				if (f != matrix1.length - 1) {
					System.out.print(" ");
				}
			}
			System.out.print(NEXT_LINE);
		}
		if (MatrixOperation.isIdentity(matrix1)) {
			System.out.print("A matriz é matriz identidade." + NEXT_LINE);
		}
		else {
			System.out.print("A matriz não é matriz identidade." + NEXT_LINE);
		}
		System.out.print("Matriz diagonal:" + NEXT_LINE);
		int[][] diagonal = MatrixOperation.diagonal(matrix1);
		for (int i = 0; i < diagonal.length; i++) {
			for (int f = 0; f < diagonal[i].length; f++) {
				System.out.print(diagonal[i][f]);
				if (f != diagonal.length - 1) {
					System.out.print(" ");
				}
			}
			System.out.print(NEXT_LINE);
		}
		System.out.print("Diagonal principal:" + NEXT_LINE);
		int[] diagonalVector = MatrixOperation.diagonalVector(matrix1);
		for (int i = 0; i < diagonalVector.length; i++) {
			System.out.print(diagonalVector[i] + " ");
		}
		System.out.print(NEXT_LINE);
		System.out.print("Matriz transposta:" + NEXT_LINE);
		int[][] transposed = MatrixOperation.transpose(matrix1);
		for (int i = 0; i < transposed.length; i++) {
			for (int f = 0; f < transposed[i].length; f++) {
				System.out.print(transposed[i][f]);
				if (f != transposed.length - 1) {
					System.out.print(" ");
				}
			}
			System.out.print(NEXT_LINE);
		}
		System.out.print("Soma dos elementos de cada linha: ");
		int[] sums = new int[0];
		for (int i = 0; i < matrix1.length; i++) {
			sums = Arrays.copyOf(sums, sums.length + 1);
			sums[i] = MatrixOperation.sumCalculation(matrix1, i, 1);
			System.out.print(sums[i]);
			if (i != matrix1.length - 1) {
				System.out.print(" ");
			}
			else {
				System.out.print(NEXT_LINE);
			}
		}
		System.out.print("Soma dos elementos de cada coluna: ");
		for (int i = 0; i < matrix1.length; i++) {
			System.out.print(MatrixOperation.sumCalculation(matrix1, i, 2));
			if (i != matrix1.length - 1) {
				System.out.print(" ");
			}
			else {
				System.out.print(NEXT_LINE);
			}
		}
		System.out.print("Soma dos elementos da diagonal principal: " +
						 MatrixOperation.sumCalculation(matrix1, 0, 3) + NEXT_LINE);
		Scanner myScanner = new Scanner(System.in);
		System.out.print("Multiplicação:" + NEXT_LINE + "Índice da linha para ser multiplicada: ");
		boolean valid;
		int index = 0;
		do {
			try {
				index = myScanner.nextInt();
				if (index >= 0 && index < matrix1.length) {
					valid = true;
				}
				else {
					valid = false;
					System.out.print("Input inválido. Escreva um número entre zero e o número de linhas da matriz "
									 + "(" + (matrix1.length - 1) + "): ");
				}
			} catch (InputMismatchException e) {
				valid = false;
				System.out.print("Input inválido. Escreva um número entre zero e o número de linhas da matriz "
								 + "(" + (matrix1.length - 1) + "): ");
			}
		} while (!valid);
		int scalar = 0;
		System.out.print("Número pelo qual a linha será multiplicada: ");
		do {
			try {
				scalar = myScanner.nextInt();
				valid = true;
			} catch (InputMismatchException e) {
				valid = false;
				System.out.print("Input inválido. Escreva um número inteiro: ");
			}
		} while (!valid);
		myScanner.close();
		int[][] multiplied = MatrixOperation.multiplyLine(matrix1, scalar, index);
		System.out.print("Resultado: " + NEXT_LINE);
		for (int i = 0; i < multiplied.length; i++) {
			for (int f = 0; f < multiplied[i].length; f++) {
				System.out.print(multiplied[i][f]);
				if (f != multiplied[i].length - 1) {
					System.out.print(" ");
				}
			}
			System.out.print(NEXT_LINE);
		}
		int iMaxSum = 0;
		for (int i = 1; i < sums.length; i++) {
			if (sums[i] > sums[iMaxSum]) {
				iMaxSum = i;
			}
		}
		System.out.print("Índice da linha cujo sumatório é maior: " + iMaxSum + NEXT_LINE);
		int[] sortedSums = Arrays.copyOf(sums, sums.length - 1);
		Arrays.sort(sortedSums);
		int max = sortedSums[sortedSums.length - 1];
		System.out.print("Maior número desta linha: " + max + NEXT_LINE);
		if (max != 0) {
			scalar = (int)Math.ceil(10.0 / max);
		}
		else {
			scalar = 1;
		}
		System.out.print("Matriz obtida pela multiplicação da linha com maior sumatório (" + iMaxSum + ") " +
						 "por " + scalar + " e subtraindo as restantes linhas por esta última linha:" + NEXT_LINE);
		int[][] matrixFinal = MatrixOperation.multiplyLine(matrix1, scalar, iMaxSum);
		for (int i = 0; i < matrixFinal.length; i++) {
			for (int f = 0; f < matrixFinal.length; f++) {
				if (i != iMaxSum) {
					matrixFinal[i][f] = matrixFinal[i][f] - matrixFinal[iMaxSum][f];
				}
				System.out.print(matrixFinal[i][f]);
				if (f < matrixFinal[i].length - 1) {
					System.out.print(" ");
				}
			}
			System.out.print(NEXT_LINE);
		}
		
	}

}