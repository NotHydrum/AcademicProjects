import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class is to be used by students to test their solution to the
 * Project 3 of LabP
 * 
 * @author LabP team
 *
 */
public class RunOrders {

	/**
	 * This method reads a file containing information on several articles,
	 * creates a warehouse and add the articles to it.
	 * Then, it reads a file containing 6 order descriptions and asks the
	 * warehouse whether they are valid descriptions. For those that are valid,
	 * it asks the warehouse to put the order.
	 * 
	 * @param args Not used
	 * @throws FileNotFoundException if some of the files does not exist
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		/**
		 * The orders file has 6 order descriptions:
		 * - One that is correct;
		 * - One that, after the previous one being served, is incorrect because
		 *   one of the products is out of stock;
		 * - One with a box cover not matching the current box;
		 * - One that does not start with a box;
		 * - One where the volume of the articles exceed the volume of the box;
		 * - One where the first box is never closed.
		 */

		Scanner reader = new Scanner(new File("articlesIn.txt"));
		
		// The first line of the file has an integer that defines the number of
		// article descriptions that follow, one per line
		int nLines = reader.nextInt();
		reader.nextLine();  // this is to consume the last end of line
		
		/*
		 * Creates a warehouse with maximum nLines articles and where
		 * all boxes codenames have "bx" prefix
		 */
		Warehouse myWare = new Warehouse(nLines, "bx");
		
		String[] codeNames = new String[nLines];
		
		System.out.println("From the articles input file:");
		// For each article, reads its information, prints it and adds an 
		// article to the warehouse with that information
		int nArticles = 0;
		for(int i = 0 ; i < nLines ; i++) {
			String[] parts = reader.nextLine().split(";");
			print(parts);
			if(!myWare.inCatalog(parts[0])) {
				myWare.addArticle(parts[0], parts[1], Double.valueOf(parts[2]), 
						Double.valueOf(parts[3]), Integer.valueOf(parts[4]));
				codeNames[nArticles] = parts[0];
				nArticles++;				
			}
		}
		
		System.out.println("===========================================================");
		System.out.println();
		System.out.println("Asking the warehouse for information about articles:");
		
		for(int i = 0 ; i < nArticles ; i++) {
			double[] measures = myWare.articleVolumeWeight(codeNames[i]);
			System.out.println("Article " + codeNames[i] + ":   Volume: " + measures[0] +
					   "    Weight: " + measures[1]);			
		}

		System.out.println("===========================================================");
		System.out.println();
		
		reader = new Scanner(new File("orderIn.txt"));
		
		// For each order description in the file
		while(reader.hasNextLine()) {
			String orderDescription = reader.nextLine();
			
			OrderStatus status = myWare.validOrder(orderDescription);
			System.out.println("The description '" + orderDescription + "' gives the following:");

			switch (status) {
			case VALID_ORDER: {
			String instructions = myWare.putOrder(orderDescription);
			System.out.println(instructions);
			System.out.println();
			double[] measures = myWare.orderVolumeWeight(orderDescription);
			System.out.println("Order volume: " + measures[0] + "  and weight: " + measures[1]);
		    }
			break;

		    case NO_BOX_START:
			System.out.println("The order does not begin with a box");
			break;

		    case ORDER_VOLUME_EXCEEDS_BOX:
			System.out.println("The contents volume exceeds the box volume");
			break;

		    case MISMATCHED_BOX_COVER:
			System.out.println("The cover does not match the box");
			break;

		    case NO_AVAILABLE_STOCK:
			System.out.println("Article not in stock");
			break;

		    case UNCLOSED_BOX:
			System.out.println("A box is not closed");
			break;

		    }
			
			System.out.println("===========================================================");
			System.out.println();
		}
	}

	/**
	 * Prints the elements of an array of Strings in the standard output
	 * @param parts The elements to print
	 * @requires parts != null
	 */
	private static void print(String[] parts) {
		for(String s : parts) {
			System.out.print(s + "  ");
		}
		System.out.println();
	}

}
