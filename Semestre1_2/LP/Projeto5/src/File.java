/**
 * Class whose instances represent files of a file system.
 * @author Miguel Nunes fc56338
 */
public class File implements SystemNode {
	
	/**
	 * The file's name.
	 */
	private String name;
	
	/**
	 * Builds a new file.
	 * @param name The file's name.
	 * @requires name != null
	 */
	public File(String name) {
		this.name = name;
	}

	/**
	 * @return The file's name.
	 */
	@Override
	public String name() {
		return name;
	}

}
