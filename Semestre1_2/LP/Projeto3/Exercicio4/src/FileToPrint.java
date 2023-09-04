/**
 *This classe implements a file to be printed
 *
 * @author LabP team 2021
 * @author Miguel Nunes fc56338
 *
 */

public class FileToPrint {
	String name; 	//name of the file
	int size;		//size of the file
	String owner;	//owner of the file
	
	/**
	 * Creates a new file
	 * @param name  of the file
	 * @param size  of the file
	 * @param owner  of the file
	 */
	FileToPrint (String name, int size, String owner ){
		this.name = name;
		this.size = size;
		this.owner = owner;
	}	
	
	/**
	 * Gets the name of the file
	 * @return name of the file
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the size of the file
	 * @return size of the file
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the owner of the file
	 * @return owner of the file
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * The textual representation of the file
	 */
	@Override
	public String toString () {
		String st = "["+ name + "," + size + "," + owner + "]";
		return st;
	}

	/**
	 * Determines if this 'FileToPrint' is equal to the given Object. Two 'FileToPrint' Objects are equal if they
	 * have the same name and size. Owner can be different.
	 * @param other The Object with which to compare.
	 * @return 'true' if equal, 'false' otherwise.
	 */
	@Override
	public boolean equals (Object other) {
		boolean equals = false;
		if (other != null && getClass().equals(other.getClass()) && 
				getName().equals(((FileToPrint)other).getName()) && getSize() == ((FileToPrint)other).getSize()) {
			equals = true;
		}
		return equals;
	}

}
