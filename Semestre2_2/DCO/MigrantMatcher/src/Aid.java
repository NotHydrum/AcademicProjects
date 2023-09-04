import java.util.Date;

/**
 * Represents an aid offered by a volunteer.
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public interface Aid {
	
	/**
	 * @return The volunteer offering the aid.
	 */
	public Volunteer getOfferedBy();
	
	/**
	 * @return The date the aid was first offered.
	 */
	public Date getDateAvailable();
	
	/**
	 * @return The migrant this aid has been attributed to.
	 *         Null if not yet attributed.
	 */
	public Migrant getAttributedTo();
	
}
