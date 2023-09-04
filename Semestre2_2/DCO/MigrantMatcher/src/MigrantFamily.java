import java.util.ArrayList;
import java.util.List;

/**
 * Represents a migrant family.
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public class MigrantFamily implements Migrant {
	
	/**
	 * The name of the family head.
	 */
	private String familyHeadName;
	/**
	 * The contact of the family head.
	 */
	private int familyHeadContact;
	/**
	 * The number of members of the family.
	 */
	private int numberMembers;
	/**
	 * The names of all members besides the family head.
	 */
	private List<String> otherMemberNames;
	
	/**
	 * Builds a new MigrantFamily.
	 * @param familyHeadName The name of the family head.
	 * @param familyHeadContact The contact of the family head.
	 * @param numberMembers The names of all members besides the family head.
	 */
	public MigrantFamily(String familyHeadName, int familyHeadContact, int numberMembers) {
		this.familyHeadName = familyHeadName;
		this.familyHeadContact = familyHeadContact;
		this.numberMembers = numberMembers;
		otherMemberNames = new ArrayList<>();
	}
	
	/**
	 * Adds a member's name to the list of members.
	 * @param name Member's name
	 */
	public void addMember(String name) {
		otherMemberNames.add(name);
	}
	
	/**
	 * @return The name of the family head.
	 */
	public String getFamilyHeadName() {
		return familyHeadName;
	}
	
	/**
	 * @return The contact of the family head.
	 */
	public int getContact() {
		return familyHeadContact;
	}
	
	/**
	 * @return The number of members of the family.
	 */
	public int getNumberMembers() {
		return numberMembers;
	}
	
	/**
	 * @return The names of all members besides the family head.
	 */
	public List<String> getOtherMemberNames() {
		List<String> copy = new ArrayList<>();
		for (String name : otherMemberNames) {
			copy.add(name);
		}
		return copy;
	}
	
}
