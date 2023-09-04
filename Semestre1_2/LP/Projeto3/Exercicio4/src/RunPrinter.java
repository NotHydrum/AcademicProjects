import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class is to be used by students to test their solution of  
 * Exercise 4 of LabP 2021
 * 
 * @author LabP team
 *
 */

public class RunPrinter {
	
	/**
	 *  @param args Not used
	 *  @throws FileNotFoundException if the file does not exist
	 */
	
	public static void main (String[] args) throws FileNotFoundException { 
		
		//Reads a file containing the description of the files to print
		//and creates a printer 
		Printer pr = readFilesToPrinter ("files1");
		
		System.out.print("Printer"+ System.getProperty("line.separator") + pr.toString());
		System.out.println( "Size " + pr.size());
		FileToPrint f1 = new FileToPrint ("Prog", 50,"admin");
		System.out.println("Number of files equal to "+ f1.toString() + ": " + pr.ocurrenciesOfFile(f1));
		
	}
	
	/**
	 * Reads a file containing in the first line the capacity of the printer
	 * and then the descriptions of a file per line;
	 * creates a printer and adds the files to the printer
	 * according to its priority policy 
	 *
	 * @param fileIn
	 * @throws FileNotFoundException
	 */
	public static Printer readFilesToPrinter (String fileIn) throws FileNotFoundException{
		Scanner reader = new Scanner(new File(fileIn));
		Printer pr = null;
		
		int maxCapacity = reader.nextInt();
		pr= new Printer (maxCapacity);
		reader.nextLine();  // this is to consume the end of line
		while (reader.hasNextLine()) {
			String[] fileDescription = reader.nextLine().split(" ");
			FileToPrint fp = new FileToPrint (fileDescription[0], 
							Integer.valueOf(fileDescription[1]),fileDescription[2] );
			if (!pr.add(fp))
				System.out.println("No available space on the printer");
		}
		reader.close();
		return pr;		
	}


}
