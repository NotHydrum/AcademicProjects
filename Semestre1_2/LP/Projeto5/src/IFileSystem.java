import java.util.List;

/**
 *
 * @author LabPteam
 *
 */
public interface IFileSystem {

    /**
     * Creates a file in a given path
     *
     * @param path the path to the file
     * @return true if the file was successfully created, false otherwise
     */
    boolean createFile(String path);

    /**
     * Finds files with a given name
     *
     * @param filename the name of the file
     * @return list of paths of the found files
     */
    List<String> find(String filename);

    /**
     * Determines if a file exists at a certain path.
     *
     * @param path the path to the file
     * @return true if there is a file at the given path, false otherwise
     */
    boolean existsFile(String path);

    /**
     * Removes a file in a given path
     *
     * @param path the path to the file
     * @return true if the file was successfully removed, false otherwise
     */
    boolean removeFile(String path);

    /**
     * Returns a textual representation of the filesystem.
     */
    @Override
    String toString();

}
