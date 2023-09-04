/**
 * Represents a volunteer.
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public class Volunteer {
	
	/**
	 * The volunteer's contact.
	 */
	private int contact;
	
	/**
	 * Builds a new Volunteer.
	 * @param contact The volunteer's contact.
	 */
	public Volunteer(int contact) {
		this.contact = contact;
	}
	
	/**
	 * @return The volunteer's contact.
	 */
	public int getContact() {
		return contact;
	}
	
}
