import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.BeforeClass;

public class TestsWarehouse {
	/**
	 * We use this line separator in order to tests to pass whatever the
	 * end-of-line symbols used by a specific operating system.
	 */
	public static final String EOL = System.getProperty("line.separator");

	private static Warehouse myWare;
	
	@BeforeClass
	public static void createWarehouse() throws FileNotFoundException {
		Scanner reader = new Scanner(new File("articlesIn.txt"));
		
		// The first line of the file has an integer that defines the number of
		// article descriptions that follow, one per line
		int nLines = reader.nextInt();
		reader.nextLine();  // this is to consume the last end of line
		
		/*
		 * Creates a warehouse with maximum nLines articles and where
		 * all boxes codenames have "bx" prefix
		 */
		myWare = new Warehouse(nLines, "bx");
		
		for(int i = 0 ; i < nLines ; i++) {
			String[] parts = reader.nextLine().split(";");
			myWare.addArticle(parts[0], parts[1], Double.valueOf(parts[2]), 
						Double.valueOf(parts[3]), Integer.valueOf(parts[4]));
		}
		
	}
	
	@org.junit.Test
	public void testValidDescription() {

		String description = "bx1 Fr2 bx3 Tri 3xb anim1 1xb";
		
		OrderStatus status = myWare.validOrder(description);

		assertEquals(OrderStatus.VALID_ORDER, status);
	}
	
	@org.junit.Test
	public void testNoBoxStart() {

		String description = "Fr2 bx3 Tri 3xb anim1 2xb";
		
		OrderStatus status = myWare.validOrder(description);

		assertEquals(OrderStatus.NO_BOX_START, status);
	}
	
	@org.junit.Test
	public void testUnclosedBox() {

		String description = "bx2 Fr2 bx3 Tri 3xb anim1";
		
		OrderStatus status = myWare.validOrder(description);

		assertEquals(OrderStatus.UNCLOSED_BOX, status);
	}
	
	@org.junit.Test
	public void testVolumeExceeds() {

		String description = "bx2 Clo bx3 Clo 3xb Geom3 2xb";
		
		OrderStatus status = myWare.validOrder(description);

		assertEquals(OrderStatus.ORDER_VOLUME_EXCEEDS_BOX, status);
	}
	
	@org.junit.Test
	public void testMismatchCover() {

		String description = "bx2 Clo bx3 Clo 2xb Tri 3xb";
		
		OrderStatus status = myWare.validOrder(description);

		assertEquals(OrderStatus.MISMATCHED_BOX_COVER, status);
	}
	
	@org.junit.Test
	public void testNoAvailableStock() {

		String description = "bx2 Tri bx3 Clo 3xb Tri 2xb";
		
		OrderStatus status = myWare.validOrder(description);

		assertEquals(OrderStatus.NO_AVAILABLE_STOCK, status);
	}
	
	@org.junit.Test
	public void testPutOrderInstructions() throws FileNotFoundException {

		String description = "bx2 Fr2 bx3 Clo 3xb Fr2 2xb";

		String expected = 
				"Go fetch Black Box and put it on the table" + EOL
				+ "Go fetch Apple and put it in Black Box" + EOL
				+ "Go fetch Pink Box and put it in Black Box" + EOL
				+ "Go fetch Cloud and put it in Pink Box" + EOL
				+ "Go fetch the cover of Pink Box and close it" + EOL
				+ "Go fetch Apple and put it in Black Box" + EOL
				+ "Go fetch the cover of Black Box and close it" + EOL;
		
		String instructions = myWare.putOrder(description);
		
		assertEquals(expected, instructions);
	}
	
	@org.junit.Test
	public void testPutOrderStockReduction() throws FileNotFoundException {

		String description = "bx2 anim1 bx3 Clo 3xb anim1 2xb";

		myWare.putOrder(description);

		OrderStatus status = myWare.validOrder(description);

		assertEquals(OrderStatus.NO_AVAILABLE_STOCK, status);
	}
	
	@org.junit.Test
	public void testArticleVolumeWeight() {

		double[] obtained = myWare.articleVolumeWeight("Clo");
		double[] expected = {10.0 , 0.5};
		assertArrayEquals(expected, obtained);
	}
	
	@org.junit.Test
	public void testOrderVolumeWeight() {

		double[] obtained = myWare.orderVolumeWeight("bx2 Tri bx3 Clo 3xb anim1 2xb");
		double[] expected = {35.0 , 3.94};

		assertArrayEquals(expected, obtained);
	}
	
}