import java.util.Objects;
import java.util.Random;

/**
 * Class whose instances represent shop items.
 * @author Miguel Nunes fc56338
 */
public class Item {
	
	/**
	 * The item's name.
	 */
	private String description;
	/**
	 * Whether or not the item requires refrigeration.
	 */
	private boolean isRefrigerated;
	
	/**
	 * Builds a randomly generated Item.
	 * @param random The Random instance used to create the Item.
	 * @requires rand != null
	 * @return Randomly generated Item.
	 */
	public static Item getRandomItem(Random random) {
		//description are hexadecimals ranging from "1" to "FFFFFF"
		return new Item(Integer.toHexString(random.nextInt(16777215) + 1).toUpperCase(), random.nextBoolean());
	}
	
	/**
	 * Builds a new Item.
	 * @param description The item's name.
	 * @param isRefrigerated Whether or not the item requires refrigeration.
	 * @requires description != null
	 */
	public Item(String description, boolean isRefrigerated) {
		this.description = description;
		this.isRefrigerated = isRefrigerated;
	}
	
	/**
	 * @return The item's name.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return Whether or not the item requires refrigeration.
	 */
	public boolean isRefrigerated() {
		return isRefrigerated;
	}
	
	/**
	 * Determines if this Item and the given Object are equal.
	 * @return 'true' if equals, 'false' otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		return other instanceof Item && description.equals(((Item)other).description)
				&& isRefrigerated == ((Item)other).isRefrigerated;
	}
	
	/**
	 * @return Textual representation of this Item.
	 */
	@Override
	public String toString() {
		return "\"" + description + "\"" + ";" + (isRefrigerated ? "Refrigerated" : "Non-Refrigerated");
	}
	
	/**
	 * @return Hash code value of this Item.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(description, isRefrigerated);
	}
	
}
