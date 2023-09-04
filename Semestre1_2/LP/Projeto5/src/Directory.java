import java.util.ArrayList;
import java.util.List;

/**
 * Class whose instances represent directories of a file system.
 * @author Miguel Nunes fc56338
 */
public class Directory implements SystemNode {
	
	/**
	 * The directory's name.
	 */
	private String name;
	/**
	 * The directory's files and sub-directories.
	 */
	private ArrayList<SystemNode> children;
	
	/**
	 * Builds a new directory.
	 * @param name The directory's name.
	 * @requires name.
	 */
	public Directory(String name) {
		this.name = name;
		children = new ArrayList<>();
	}
	
	/**
	 * @return The directory's name.
	 */
	@Override
	public String name() {
		return name;
	}
	
	/**
	 * Returns a List containing this directory's files and sub-directories.
	 * @return List containing this directory's files and sub-directories.
	 */
	public List<SystemNode> children() {
		List<SystemNode> childrenCopy = new ArrayList<>();
		for (SystemNode i : children) {
			childrenCopy.add(i);
		}
		return childrenCopy;
	}
	
	/**
	 * Adds a file/directory to this directory. <br>
	 * Adding a directory to itself or one of its sub-directories may cause issues, including infinite loops.
	 * @param node File/directory to add to this directory.
	 * @requires node != null && findNode(node.name()) == null
	 */
	public void addNode(SystemNode node) {
		children.add(node);
	}
	
	/**
	 * Removes a file/directory from this directory. <br>
	 * If the directory does not contain the file/directory to remove, it is left unchanged.
	 * @param node File/directory to add to this directory.
	 * @requires node != null
	 */
	public void removeNode(SystemNode node) {
		children.remove(node);
	}
	
	/**
	 * Returns whether or not this directory is empty.
	 * @return 'true' if empty, 'false' otherwise.
	 */
	public boolean isEmpty() {
		return children.isEmpty();
	}

	/**
	 * Attempts to find a file/directory whose name() matches with the given name. Only the first level of content
	 * will be searched through, any files/directories inside this one's directories will be ignored.
	 * @param name The name to search for.
	 * @requires name != null
	 * @return The Node whose name() matches the wanted name or 'null' if none are found.
	 */
	public SystemNode findNode(String name) {
		SystemNode node = null;
		for (SystemNode i : children) {
			if (i.name().equals(name)) {
				node = i;
			}
		}
		return node;
	}
	
}
