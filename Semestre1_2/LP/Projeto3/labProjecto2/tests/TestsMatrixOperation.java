import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This class is to test the methods in class MatrixOperation
 * @author LabP team
 *
 */
public class TestsMatrixOperation {

	@Test
	public void testIsSquare1 () {
		
		int [][] m3x3 = {{4, 0, 0}, {0, 1, 0}, {2, -3, 1}};

		boolean expected = true;
		boolean obtained = MatrixOperation.isSquare(m3x3);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testIsSquare2 () {
		
		 int[][] m2x3 = {{1, 2, 3}, {10, 20, 30}};

		boolean expected = false;
		boolean obtained = MatrixOperation.isSquare(m2x3);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testIsSquareRagged () {
		
		int[][] raggedMatrix = {{1, 2, 3}, {10, 20}, {15}};

		boolean expected = false;
		boolean obtained = MatrixOperation.isSquare(raggedMatrix);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testIsIdentity1 () {
		int [][] m3x3 = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
		
		boolean expected = true;
		boolean obtained = MatrixOperation.isIdentity(m3x3);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testIsIdentity2 () {
		//valores diferentes de 0 fora da diagonal
		int [][] m3x3 = {{1, 1, 0}, {0, 1, 1}, {0, 1, 1}};
		
		boolean expected = false;
		boolean obtained = MatrixOperation.isIdentity(m3x3);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testIsIdentity3 () {
		//valores diferentes de 1 na diagonal
		int [][] m3x3 = {{4, 0, 0}, {0, 2, 0}, {0, 0, 1}};
		
		boolean expected = false;
		boolean obtained = MatrixOperation.isIdentity(m3x3);
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testDiagonal () {
		int [][] m3x3 = {{1, 1, 0}, {0, 1, 1}, {0, 1, 1}};
		
		int [][] expected = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
		int [][] obtained = MatrixOperation.diagonal(m3x3);
		
		for (int nr = 0; nr < m3x3.length; nr++){ 
			assertArrayEquals(expected[nr], obtained[nr]);
		}
	}
	
	@Test
	public void testDiagonalVector () {
		int [][] m3x3 = {{1, 1, 0}, {0, 4, 1}, {0, 1, 1}};
		
		int [] expected = {1, 4, 1};
		int [] obtained = MatrixOperation.diagonalVector(m3x3);
		
		assertArrayEquals(expected, obtained);
	}

	@Test
	public void testTranspose () {
		int [][] m3x3 = {{1, 12, 0}, {0, 1, 23}, {0, 32, 1}};
		
		int [][] expected = {{1, 0, 0}, {12, 1, 32}, {0, 23, 1}};
		int [][] obtained = MatrixOperation.transpose(m3x3);
		
		for (int nr = 0; nr < m3x3.length; nr++){ 
			assertArrayEquals(expected[nr], obtained[nr]);
		}
	}
	
	@Test
	public void testSumCalculationType1 () {
		int [][] m3x3 = {{1, 12, 0}, {0, 1, 23}, {0, 32, 1}};
		int type = 1;
		int index = 1;
		
		int expected = 24;
		int obtained = MatrixOperation.sumCalculation(m3x3, index, type);
		
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testSumCalculationType2 () {
		int [][] m3x3 = {{1, 12, 0}, {0, 1, 23}, {0, 32, 1}};
		int type = 2;
		int index = 1;
		
		int expected = 45;
		int obtained = MatrixOperation.sumCalculation(m3x3, index, type);
		
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testSumCalculationType3 () {
		int [][] m3x3 = {{1, 12, 0}, {0, 1, 23}, {0, 32, 1}};
		int type = 3;
		int index = 1;
		
		int expected = 3;
		int obtained = MatrixOperation.sumCalculation(m3x3, index, type);
		
		assertEquals(expected, obtained);
	}
	
	@Test
	public void testMultiplyLine () {
		int [][] m3x3 = {{1, 12, 0}, {0, 1, 23}, {0, 32, 1}};
		int scalar = 2;
		int index = 1;
		
		int [][] expected = {{1, 12, 0}, {0, 2, 46}, {0, 32, 1}};
		int [][] obtained = MatrixOperation.multiplyLine(m3x3, scalar, index);
		
		for (int nr = 0; nr < m3x3.length; nr++){ 
			assertArrayEquals(expected[nr], obtained[nr]);
		}
	}
	
	@Test
	public void testSubtractLine () {
		int [][] m3x3 = {{1, 12, 0}, {0, 1, 23}, {0, 32, 1}};
		int index = 1;
		
		int [][] expected = {{1, 11, -23}, {0, 1, 23}, {0, 31, -22}};
		int [][] obtained = MatrixOperation.subtractLine(m3x3, index);
		
		for (int nr = 0; nr < m3x3.length; nr++){ 
			assertArrayEquals(expected[nr], obtained[nr]);
		}
	}
	
}
