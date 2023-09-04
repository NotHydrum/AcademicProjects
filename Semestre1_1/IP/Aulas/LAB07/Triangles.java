import java.util.Scanner;
/**
 * The {@code Triangles} class defines a set of procedures that
 * print several types of triangles.
 *
 * The program can be tested using a positive integer
 * given as an argument when running the class.
 *
 *
 * Compile:     javac Triangles.java
 * Run:         java Triangles 6
 *
 * @author antonialopes IP1819
 * @author filipecasal IP1920
 */
public class Triangles {

	public static void main(String[] args) {
		int inputValue;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.print("Insira a altura do triangulo: ");
			inputValue = sc.nextInt();
			sc.nextLine();
			if (inputValue < 0) {
				System.out.println("A altura do triangulo deve de ser um numero inteiro positivo.");
			}
		} while (inputValue <= 0);
		System.out.print("Insira o simbolo a ser utilizado na representação do triangulo: ");
		String inputText = sc.nextLine();
		StringBuilder sb = new StringBuilder(inputText);
		char inputSymbol = inputText.charAt(0);
		printIsoscelesTriangle(inputValue, inputSymbol);
	}

	/**
	 * Print an isosceles triangle with a given height and symbol
	 *
	 * @param height height of the triangle to be printed
	 * @param symbol symbol to be used when printing
	 * @requires {@code height > 0}
	 */
	public static void printIsoscelesTriangle(int height, char symbol) {
		for (int l = 1; l <= height; l++) {
			StringBuilder spaceSequence = new StringBuilder(generateSequence(height - l, ' '));
			System.out.print(spaceSequence);
			StringBuilder symbolSequence = new StringBuilder(generateSequence(2 * l - 1, symbol));
			System.out.print(symbolSequence);
			System.out.print("\n");
		}
	}

	/**
	 * Generates a line with {@code n} copies of {@code symbol}
	 *
	 * @param n      size of the sequence
	 * @param symbol character to be printed
	 * @requires {@code n > 0}
	 */
	public static StringBuilder generateSequence(int n, char symbol) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= n; i++) {
			sb.append(symbol);
		}
		return sb;
	}

}
