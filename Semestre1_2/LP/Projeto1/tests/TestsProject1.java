import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * This class is to test the methods in class TUTEDECC
 * @author LabP team
 *
 */
public class TestsProject1 {

	/**
	 * We use this line separator in order to tests to pass whatever the
	 * end-of-line symbols used by a specific operating system.
	 */
	public static final String eol = System.getProperty("line.separator");

	@Test
	public void testCopyPositionMultiple () throws IOException {

		String expected = "2: ghi jk lm" + eol + "4: stu vxz" + eol;

		String text = "abc def" + eol + "ghi jk lm" + eol + 
				"op qr" + eol + "stu vxz" + eol + "aaa bbb" + eol;

		PrintWriter in = new PrintWriter("input.txt");
		in.write(text);
		in.close();

		TUTEDECC.copyPositionMultiple("input.txt", 2, "output.txt");
		String obtainedText = new String(Files.readAllBytes(Paths.get("output.txt")), StandardCharsets.UTF_8);

		assertEquals(expected, obtainedText);
	}


	@Test public void testVowelSubstitution () throws IOException { 
		String expected = "igiri vimis fizir" + eol + "im tisti. i qii ispiris?" + eol;

		String text = "Agora vamos fazer" + eol + "um teste. O que esperas?" + eol;

		PrintWriter in = new PrintWriter("input.txt"); 
		in.write(text); 
		in.close();

		TUTEDECC.vowelSubstitution("input.txt", 'i', "output.txt"); 
		String obtainedText = new
				String(Files.readAllBytes(Paths.get("output.txt")), StandardCharsets.UTF_8);

		assertEquals(expected, obtainedText);
	}


	@Test public void testAverageWordSize () throws IOException { 
		int[] expected = {5, 3};

		String text = "Agora vamos fazer" + eol + "um teste. Ui ui." + eol;

		PrintWriter in = new PrintWriter("input.txt"); 
		in.write(text); 
		in.close();

		int[] obtained = TUTEDECC.averageWordSize("input.txt",2);

		assertArrayEquals(expected, obtained);
	}


	@Test public void testOneWordPerLine () throws IOException { 
		String expected = "1 def" + eol + "2 def" + eol + "3 " + eol +
				"4 qr" + eol + "5 vxz" + eol + "6 bbb" + eol;

		String text = "abc def" + eol + "ghi jk def" + eol + "" + eol + 
				"op qr" + eol + "stu vxz" + eol + "aaa bbb" + eol;

		PrintWriter in = new PrintWriter("input.txt"); 
		in.write(text); 
		in.close();

		TUTEDECC.oneWordPerLine("input.txt", "output.txt"); 
		String obtainedText =
				new String(Files.readAllBytes(Paths.get("output.txt")),
						StandardCharsets.UTF_8);

		assertEquals(expected, obtainedText);
	}


	@Test public void testRotateEveryWord () throws IOException {

		String expected = "oraAg mosva zerfa" + eol + "um stete O que rasespe" + eol;

		String text = "Agora vamos fazer" + eol + "um teste O que esperas" + eol;

		PrintWriter in = new PrintWriter("input.txt"); 
		in.write(text); 
		in.close();

		TUTEDECC.rotateEveryWord("input.txt", 3, "output.txt"); String
		obtainedText = new String(Files.readAllBytes(Paths.get("output.txt")),
				StandardCharsets.UTF_8); 

		assertEquals(expected, obtainedText);
	}

	@Test public void testSwitchNextWordNextLine () throws IOException { 
		String expected = "teste vamos ver" + eol + "um Agora para fazer se" + eol +
				"logo ultimo tens funciona bem" + eol + "Veremos o se metodo razao" + eol;

		String text = "Agora vamos fazer" + eol + "um teste para ver se" + eol +
				"o ultimo metodo funciona bem" + eol + "Veremos logo se tens razao" + eol;

		PrintWriter in = new PrintWriter("input.txt"); 
		in.write(text); 
		in.close();

		TUTEDECC.switchNextWordNextLine("input.txt", "output.txt"); 
		String obtainedText =
				new String(Files.readAllBytes(Paths.get("output.txt")),
						StandardCharsets.UTF_8);

		assertEquals(expected, obtainedText); 
	}


}
