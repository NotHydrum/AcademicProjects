import java.util.Objects;

/**
 * Represents a region.
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public class Region {
	
	/**
	 * The region's name.
	 */
	private String name;
	
	/**
	 * Builds a new Region.
	 * @param name The region's name.
	 */
	public Region(String name) {
		this.name = name;
	}
	
	/**
	 * @return The region's name.
	 */
	public String getName() {
		return name;
	}
	
	public boolean equals(Object other) {
		return other instanceof Region && name.equals(((Region)other).name);
	}
	
	public int hashCode() {
		return Objects.hash(name);
	}
	
}
