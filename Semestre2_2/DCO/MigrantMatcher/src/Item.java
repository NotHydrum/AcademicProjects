import java.util.Date;

/**
 * Represents an item offered by a volunteer.
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public class Item implements Aid {
	
	/**
	 * The volunteer offering the aid.
	 */
	private Volunteer offeredBy;
	/**
	 * The date the aid was first offered.
	 */
	private Date dateAvailable;
	/**
	 * The migrant this aid has been attributed to.
	 * Null if not yet attributed.
	 */
	private Migrant attributedTo;
	/**
	 * The description of the item.
	 */
	private String description;
	
	/**
	 * Builds a new Item
	 * @param offeredBy The volunteer offering the aid.
	 * @param date The date the aid was first offered.
	 * @param attributedTo The migrant this aid has been attributed to.
	 *                     Null if not yet attributed.
	 * @param description The description of the item.
	 */
	public Item(Volunteer offeredBy, Date date, Migrant attributedTo, String description) {
		this.offeredBy = offeredBy;
		this.dateAvailable = date;
		this.attributedTo = attributedTo;
		this.description = description;
	}
	
	/**
	 * @return The volunteer offering the aid.
	 */
	public Volunteer getOfferedBy() {
		return offeredBy;
	}
	
	/**
	 * @return The date the aid was first offered.
	 */
	public Date getDateAvailable() {
		return dateAvailable;
	}
	
	/**
	 * @return The migrant this aid has been attributed to.
	 *         Null if not yet attributed.
	 */
	public Migrant getAttributedTo() {
		return attributedTo;
	}
	
	/**
	 * @return The description of the item.
	 */
	public String getDescription() {
		return description;
	}
	
}
