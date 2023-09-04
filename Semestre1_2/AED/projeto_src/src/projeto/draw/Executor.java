package projeto.draw;

// Dicas:

// (1) Double.parseDouble(string) converte uma string num double

// (2) É possível usar a classe Scanner para retirar tokens de uma string
//
//   Scanner sc = new Scanner(string).useDelimiter("\\s* \\s*");
//   while (sc.hasNext()) {
//     String token = sc.next();
//     ... // process token
//   }
//   sc.close();

// (3) Estudem os métodos split() e trim() da API da classe String

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import projeto.strategies.*;

/**
 * Grupo numero:  
 * 
 * @author (nome e num de aluno)
 * @author
 * @author  
 */
public class Executor {

	private BufferedReader in; 	  // input and ouput streams
	private BufferedWriter out;
	
	// Print values using this format 
	//  use eg:  df.format(value).replace(",", "."))
	private static DecimalFormat df = new DecimalFormat("#.###");
	
	public Executor(BufferedReader in, BufferedWriter out) {
		this.in  = in;
		this.out = out;
	}

	/**
	 * Process a line. It can be:                          <br>
	 *  * a comment: starts with '#'                       <br>
	 *  * a function definition: "[name:arity] expression" <br>
	 *  * an expression (produces an effect on the stack)
	 * @param line A text line with code
	 * @requires A syntatical valid line of code
	 * @ensures A function definition adds to the function data structure
	 * @ensures An expression is evaluated and its effects modify the stack
	 */
	public void processLine(String line) {
		//TODO
	}
}