/**
 * Represents an individual migrant.
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public class MigrantIndividual implements Migrant {
	
	/**
	 * The migrant's name.
	 */
	private String name;
	/**
	 * The migrant's contact.
	 */
	private int contact;
	
	/**
	 * Builds a new MigrantIndividual.
	 * @param name The migrant's name.
	 * @param contact The migrant's contact.
	 */
	public MigrantIndividual(String name, int contact) {
		this.name = name;
		this.contact = contact;
	}
	
	/**
	 * @return The migrant's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The migrant's contact.
	 */
	public int getContact() {
		return contact;
	}
	
}
