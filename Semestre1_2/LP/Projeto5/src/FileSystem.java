import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Class whose instances represent a file system.
 * @author Miguel Nunes fc56338
 */
public class FileSystem implements IFileSystem {
	
	/**
	 * Defines the end-of-line character, independent of operating system.
	 */
	private static final String NEXT_LINE = System.getProperty("line.separator");
	
	/**
	 * The file system's main directory.
	 */
	private Directory root;
	
	/**
	 * Builds a new file system.
	 */
	public FileSystem() {
		root = new Directory("");
	}
	
	/**
	 * Creates a file in the given path.
	 * @param path Where to create the file.
	 * @returns 'true' if a file is successfully created, 'false' otherwise.
	 */
	@Override
	public boolean createFile(String path) {
		ArrayList<String> pathList = pathToList(path);
		return pathList != null && createFileRecursive(root, pathList);
	}
	
	/**
	 * Auxiliary method used by createFile() to follow the given path and create the file.
	 * @param current Directory method is currently in.
	 * @param path List version of the path given in createFile().
	 * @requires current != null && path.size() > 0
	 * @return 'true' if a file is successfully created, 'false' otherwise.
	 */
	private boolean createFileRecursive(Directory current, ArrayList<String> path) {
		boolean created = false;
		SystemNode node = current.findNode(path.get(0));
		if (path.size() > 1) {
			if (node == null) {
				node = new Directory(path.get(0));
				current.addNode(node);
				path.remove(0);
				created = createFileRecursive((Directory)node, path);
				if (((Directory)node).isEmpty()) {
					current.removeNode(node);
				}
			}
			else if (node instanceof Directory) {
				path.remove(0);
				created = createFileRecursive((Directory)node, path);
			}
		}
		else if (node == null) {
			current.addNode(new File(path.get(0)));
			created = true;
		}
		return created;
	}
	
	/**
	 * Finds all files in the system whose name() is equal to 'fileName'.
	 * @param fileName Name to search for.
	 * @requires fileName != null
	 * @return A list containing the paths to all files in the system whose name() is equal to 'name'.
	 */
	@Override
	public List<String> find(String fileName) {
		ArrayList<String> paths = new ArrayList<>();
		findFilesRecursive(root, fileName, paths, new StringBuilder());
		return paths;
	}
	
	/**
	 * Auxiliary method used by find() to find all files in the system whose name() is equal to 'fileName'.
	 * Method will add these paths to the given 'paths' list.
	 * @param current Directory method is currently in.
	 * @param fileName Name to search for.
	 * @param paths List where the found paths will be added.
	 * @param currentPath StringBuilder used by the method to keep track of the path to 'current'.
	 * @requires current != null && fileName != null && paths != null && currentPath != null <br>
	 *           currentPath.isEmpty() at first call.
	 */
	private void findFilesRecursive(Directory current, String fileName, ArrayList<String> paths,
			StringBuilder currentPath) {
		currentPath.append(current.name() + "/");
		SystemNode wanted = current.findNode(fileName);
		if (wanted instanceof File) {
			paths.add(currentPath.toString() + fileName);
		}
		for (SystemNode i : current.children()) {
			if (i instanceof Directory) {
				findFilesRecursive((Directory)i, fileName, paths, currentPath);
			}
		}
		currentPath.delete(currentPath.length() - current.name().length() - 1, currentPath.length());
	}
	
	/**
	 * Determines if a path to a file is valid and exists.
	 * @param path Path to the file.
	 * @return 'true' if path exists, 'false' otherwise.
	 */
	@Override
	public boolean existsFile(String path) {
		ArrayList<String> pathList = pathToList(path);
		return pathList != null && existsFileRecursive(root, pathList);
	}
	
	/**
	 * Auxiliary method used by existsFile() to determine if a path to a file is valid and exists.
	 * @param current Directory method is currently in.
	 * @param path List version of the path given in existsFile().
	 * @requires current != null && path.size() > 0
	 * @return 'true' if path exists, 'false' otherwise.
	 */
	private boolean existsFileRecursive(Directory current, ArrayList<String> path) {
		boolean exists = false;
		SystemNode node = current.findNode(path.get(0));
		if (node != null) {
			if (path.size() > 1 && node instanceof Directory) {
				path.remove(0);
				exists = existsFileRecursive((Directory)node, path);
			}
			else if (path.size() == 1 && node instanceof File) {
				exists = true;
			}
		}
		return exists;
	}
	
	/**
	 * Removes a file from the given path. If by removing a file a directory is left empty it will be removed as well.
	 * If the path is invalid and/or the file does not exist the file system will be left unchanged.
	 * @param path Path to the file.
	 * @returns 'true' if successfully removed, 'false' otherwise.
	 */
	@Override
	public boolean removeFile(String path) {
		ArrayList<String> pathList = pathToList(path);
		return pathList != null && removeFileRecursive(root, pathList);
	}
	
	/**
	 * Auxiliary method used by removeFile() to remove a file from the given path.
	 * @param current Directory method is currently in.
	 * @param path List version of the path given in removeFile().
	 * @requires current != null && path.size() > 0
	 * @return 'true' if successfully removed, 'false' otherwise.
	 */
	private boolean removeFileRecursive(Directory current, ArrayList<String> path) {
		boolean removed = false;
		SystemNode node = current.findNode(path.get(0));
		if (path.size() > 1 && node instanceof Directory) {
			path.remove(0);
			removed = removeFileRecursive((Directory)node, path);
			if (((Directory)node).isEmpty()) {
				current.removeNode(node);
			}
		}
		else if (path.size() == 1 && node instanceof File) {
			current.removeNode(node);
			removed = true;
		}
		return removed;
	}
	
	/**
	 * Generates a textual representation of the file system.
	 * @return A textual representation of the file system.
	 */
	@Override
	public String toString() {
		StringBuilder myBuilder = new StringBuilder("/" + NEXT_LINE);
		if (!root.children().isEmpty()) {
			toStringRecursive(root, myBuilder, new Stack<>());
		}
		return myBuilder.toString();
	}
	
	/**
	 * Auxiliary method used by toString() to generate a textual representation of the file system.
	 * @param current Directory method is currently in.
	 * @param myBuilder StringBuilder used to generate the textual representation.
	 * @param indentation Stack used to keep track of how indentation should be generated.
	 * @requires current != null && myBuilder != null && indentation != null <br>
	 *           currentPath.isEmpty() && indentation.isEmpty() at first call.
	 */
	private void toStringRecursive(Directory current, StringBuilder myBuilder, Stack<Boolean> indentation) {
		ArrayList<SystemNode> children = (ArrayList<SystemNode>)current.children();
		SystemNode last = children.get(children.size() - 1);
		children.remove(children.size() - 1);
		StringBuilder indentationString = new StringBuilder();
		for (boolean i : indentation) {
			if (i) {
				indentationString.append("     ");
			}
			else {
				indentationString.append("│    ");
			}
		}
		for (SystemNode i : children) {
			myBuilder.append(indentationString + "├── " + i.name());
			if (i instanceof Directory) {
				myBuilder.append("/" + NEXT_LINE);
				indentation.push(false);
				toStringRecursive((Directory)i, myBuilder, indentation);
				indentation.pop();
			}
			else {
				myBuilder.append(NEXT_LINE);
			}
		}
		myBuilder.append(indentationString + "└── " + last.name());
		if (last instanceof Directory) {
			myBuilder.append("/" + NEXT_LINE);
			indentation.push(true);
			toStringRecursive((Directory)last, myBuilder, indentation);
			indentation.pop();
		}
		else {
			myBuilder.append(NEXT_LINE);
		}
	}
	
	/**
	 * Turns a valid path String into a List.
	 * @param path String to convert to List.
	 * @return A List containing the path if it path is valid, a 'null' List otherwise.
	 */
	private static ArrayList<String> pathToList(String path) {
		//removes the first "/", splits to array and turns array to list
		return path != null && path.length() > 0 && path.charAt(0) == '/' ?
				new ArrayList<>(Arrays.asList(path.substring(1, path.length()).split("/"))) : null;
	}

}
