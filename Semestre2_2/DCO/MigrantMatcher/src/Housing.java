import java.util.Date;

/**
 * Represent a house offered by a volunteer.
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public class Housing implements Aid {
	
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
	 * The region this house is located in.
	 */
	private Region region;
	/**
	 * The number of occupants that may live in the house.
	 */
	private int numberOccupants;
	
	/**
	 * Builds a new Housing
	 * @param offeredBy The volunteer offering the aid.
	 * @param date The date the aid was first offered.
	 * @param attributedTo The migrant this aid has been attributed to.
	 *                     Null if not yet attributed.
	 * @param region The region this house is located in.
	 * @param numberOccupants The number of occupants that may live in the house.
	 */
	public Housing(Volunteer offeredBy, Date date, Migrant attributedTo, Region region,
			int numberOccupants) {
		this.offeredBy = offeredBy;
		this.dateAvailable = date;
		this.attributedTo = attributedTo;
		this.region = region;
		this.numberOccupants = numberOccupants;
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
	 * @return The region this house is located in.
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @return The number of occupants that may live in the house.
	 */
	public int getNumberOccupants() {
		return numberOccupants;
	}
	
}
