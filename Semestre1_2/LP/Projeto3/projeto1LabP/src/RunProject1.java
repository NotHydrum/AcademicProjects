import java.io.IOException;

/**
 * This class can be used to call the methods in class TUTEDECC.
 * The students may comment the instructions concerning the methods
 * not yet developed.
 * @author Team LabP
 */
public class RunProject1 {

    public static void main (String[] args) throws IOException {

        // Call copyPositionMultiple 
        TUTEDECC.copyPositionMultiple("texto1.txt", 2, "outA.txt");
        TUTEDECC.copyPositionMultiple("texto4.txt", 3, "outB.txt");

        // Call averageWordSize 		
        int[] averages = TUTEDECC.averageWordSize("paraMedias1.txt",10);
        showArray(averages); 
        averages = TUTEDECC.averageWordSize("paraMedias2.txt",6);
        showArray(averages);
        averages = TUTEDECC.averageWordSize("paraMedias3.txt",3);
        showArray(averages);
    	 	
        // Call oneWordPerLine: 		
        TUTEDECC.oneWordPerLine("texto1.txt", "outC.txt");
        TUTEDECC.oneWordPerLine("texto4.txt", "outD.txt");

        // Call vowelSubstitution: 
        TUTEDECC.vowelSubstitution("texto0.txt", 'i', "outE.txt");
        TUTEDECC.vowelSubstitution("texto1.txt", 'u', "outF.txt");

        // Call rotateEveryWord: 		
        TUTEDECC.rotateEveryWord("texto0.txt", 3, "outG.txt");
        TUTEDECC.rotateEveryWord("texto2.txt", 5, "outH.txt");

        // Call switchNextWordNextLine: 
        TUTEDECC.switchNextWordNextLine("texto0.txt", "outI.txt");
        TUTEDECC.switchNextWordNextLine("texto3.txt", "outK.txt");

    }

	/**
	 * Outputs the elements of a given array into the standard output
	 * @param v - the array
	 * @requires v != null
	 */
    private static void showArray(int[] v) {
		for(int val : v) {
			System.out.print(val + " ");
		}
		System.out.println();
	}
}