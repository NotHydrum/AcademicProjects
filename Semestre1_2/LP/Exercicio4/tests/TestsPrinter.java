import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.BeforeClass;

/**
 * This class is to test the methods in class Printer
 * @author LabP team
 *
 */
public class TestsPrinter {
	public static final String EOL = System.getProperty("line.separator");
	private static Printer pr;
	
	@BeforeClass
	public static void readFilesToPrinter () throws FileNotFoundException{
		Scanner reader = new Scanner(new File("files1"));
		
		int maxCapacity = reader.nextInt();
		pr= new Printer (maxCapacity);
		reader.nextLine();  // this is to consume the last end of line
		while (reader.hasNextLine()) {				
			String[] fileDescription = reader.nextLine().split(" ");
			FileToPrint fp = new FileToPrint (fileDescription[0], 
							Integer.valueOf(fileDescription[1]),fileDescription[2] );
			pr.add(fp);
		}	
	}
	
	@Test
	public void testEqualsFiletoPrint () {
		FileToPrint f1 = new FileToPrint ("testProg", 50,"admin");
		FileToPrint f2 = new FileToPrint ("testProg", 50,"ana");
		FileToPrint f3 = new FileToPrint ("testAlg", 50,"ana");
		
		boolean expected = true;
		boolean obtained = f1.equals(f2);
		assertEquals(expected, obtained);
		
		boolean expected2 = false;
		boolean obtained2 = f1.equals(f3);
		assertEquals(expected2, obtained2);
	}
	
	@Test
	public void testOcurrenciesOfFile1 () {
		FileToPrint f2 = new FileToPrint ("Prog", 50,"ana");
		int expected = 2;
		int obtained = pr.ocurrenciesOfFile(f2);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testOcurrenciesOfFile2 () {
		FileToPrint f4 = new FileToPrint ("Alg", 50,"ana");
		int expected2 = 1;
		int obtained2 = pr.ocurrenciesOfFile(f4);
		assertEquals(expected2, obtained2);
	}
	
	@Test
	public void testPolicy0 () {
		FileToPrint f0 = new FileToPrint ("novo", 25,"admin");
		
		int expected = 0;
		int obtained = pr.priorityPolicy(f0);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testPolicy1 () {
		FileToPrint f1 = new FileToPrint ("novo1", 24,"aht");
		
		int expected = 1;
		int obtained = pr.priorityPolicy(f1);
		assertEquals(expected, obtained);
	}

	@Test
	public void testPolicy2 () {
		FileToPrint f2 = new FileToPrint ("novo2", 125,"otr");
		
		int expected = 2;
		int obtained = pr.priorityPolicy(f2);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testPolicy3 () {
		FileToPrint f3 = new FileToPrint ("novo3", 1100,"bzx");
		
		int expected = 3;
		int obtained = pr.priorityPolicy(f3);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testAddFile0 () {
		FileToPrint f0 = new FileToPrint ("novo", 25,"admin");
		
		String expected = "maxCapacity: 5500"+EOL
				+ "leftCapacity: 2074"+EOL
				+ "Queue 0: <[Prog,50,admin], [list,150,admin], [chapt.txt,1350,admin], [novo,25,admin]<"+EOL
				+ "Queue 1: <[Prog,50,ana], [Alg,50,ana], [Aq,1,mjc]<"+EOL
				+ "Queue 2: <[time,250,afc]<"+EOL
				+ "Queue 3: <[chapt.txt,1500,hrh]<"+EOL;
		if (!pr.add(f0))
			System.out.println("No available space on the printer");
		String obtained = pr.toString();
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testAddFile1 () {
		FileToPrint f1 = new FileToPrint ("novo1", 24,"aht");
		
		String expected2 = "maxCapacity: 5500"+EOL
				+ "leftCapacity: 2050"+EOL
				+ "Queue 0: <[Prog,50,admin], [list,150,admin], [chapt.txt,1350,admin], [novo,25,admin]<"+EOL
				+ "Queue 1: <[Prog,50,ana], [Alg,50,ana], [Aq,1,mjc], [novo1,24,aht]<"+EOL
				+ "Queue 2: <[time,250,afc]<"+EOL
				+ "Queue 3: <[chapt.txt,1500,hrh]<"+EOL;
		if (! pr.add(f1))
			System.out.println("No available space on the printer");
		String obtained2 = pr.toString();
		assertEquals(expected2, obtained2);
	}
	
	@Test
	public void testAddFile2 () {
		FileToPrint f2 = new FileToPrint ("novo2", 125,"otr");
		
		String expected2 = "maxCapacity: 5500"+EOL
				+ "leftCapacity: 1925"+EOL
				+ "Queue 0: <[Prog,50,admin], [list,150,admin], [chapt.txt,1350,admin], [novo,25,admin]<"+EOL
				+ "Queue 1: <[Prog,50,ana], [Alg,50,ana], [Aq,1,mjc], [novo1,24,aht]<"+EOL
				+ "Queue 2: <[time,250,afc], [novo2,125,otr]<"+EOL
				+ "Queue 3: <[chapt.txt,1500,hrh]<"+EOL;
		if (!pr.add(f2))
			System.out.println("No available space on the printer");
		String obtained2 = pr.toString();
		assertEquals(expected2, obtained2);
	}
	
	@Test
	public void testAddFile3 () {
		FileToPrint f3 = new FileToPrint ("novo3", 1100,"bzx");
		
		String expected2 = "maxCapacity: 5500"+EOL
				+ "leftCapacity: 825"+EOL
				+ "Queue 0: <[Prog,50,admin], [list,150,admin], [chapt.txt,1350,admin], [novo,25,admin]<"+EOL
				+ "Queue 1: <[Prog,50,ana], [Alg,50,ana], [Aq,1,mjc], [novo1,24,aht]<"+EOL
				+ "Queue 2: <[time,250,afc], [novo2,125,otr]<"+EOL
				+ "Queue 3: <[chapt.txt,1500,hrh], [novo3,1100,bzx]<"+EOL;
		if (!pr.add(f3))
				System.out.println("No available space on the printer");
		String obtained2 = pr.toString();
		assertEquals(expected2, obtained2);
	}
	
	@Test
	public void testAddFile4 () {
		FileToPrint f4 = new FileToPrint ("novo4", 1100,"bzx");
		
		String expected2 = "maxCapacity: 5500"+EOL
				+ "leftCapacity: 825"+EOL
				+ "Queue 0: <[Prog,50,admin], [list,150,admin], [chapt.txt,1350,admin], [novo,25,admin]<"+EOL
				+ "Queue 1: <[Prog,50,ana], [Alg,50,ana], [Aq,1,mjc], [novo1,24,aht]<"+EOL
				+ "Queue 2: <[time,250,afc], [novo2,125,otr]<"+EOL
				+ "Queue 3: <[chapt.txt,1500,hrh], [novo3,1100,bzx]<"+EOL;
		if (!pr.add(f4))
			System.out.println("No available space on the printer");
		String obtained2 = pr.toString();
		assertEquals(expected2, obtained2);
	}

}
