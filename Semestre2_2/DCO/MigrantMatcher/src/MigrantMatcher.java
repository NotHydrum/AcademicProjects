import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.pidgeonsmssender.sdk.PidgeonSMSSender;

/**
 * @author Miguel Nunes - 56338
 * @author Henrique Catarino - 56278
 */
public class MigrantMatcher {
	
	private static final PidgeonSMSSender SMS_SENDER = new PidgeonSMSSender();
	private static final Random RANDOMIZER = new Random();
	
	private static final String[] REGION_NAMES = {"Açores", "Aveiro", "Beja", "Braga", "Bragança",
			"Castelo Branco", "Coimbra", "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Madeira",
			"Portalegre", "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu"};
	private List<Aid> aids;
	private HashMap<Region, List<Migrant>> notificationRequests; //extensão do caso de uso 2 não diz que os migrantes
	                                                             //têm de ser notificados
	
	private Volunteer currentVolunteer;
	private int currentNumberOccupants;
	private Aid currentAid;
	private int currentConfirmationNumber;
	private Migrant currentMigrant;
	private int currentNumberMembers;
	private List<Aid> currentWantedAids;
	private Region currentRegion;
	
	/**
	 * @return Regions available in MigrantMatcher
	 */
	private static List<Region> getRegions() {
		List<Region> regions = new ArrayList<>();
		for (String name : REGION_NAMES) {
			regions.add(new Region(name));
		}
		return regions;
	}
	
	/**
	 * Builds a new MigrantMatcher.
	 */
	public MigrantMatcher() {
		aids = new ArrayList<>();
		notificationRequests = new HashMap<>();
		for (String region : REGION_NAMES) {
			notificationRequests.put(new Region(region), new ArrayList<>());
		}
		currentVolunteer = null;
		currentNumberOccupants = -1;
		currentAid = null;
		currentConfirmationNumber = -1;
		currentMigrant = null;
		currentNumberMembers = -1;
		currentWantedAids = new ArrayList<>();
	}
	
	/**
	 * Identifies a volunteer willing to offer aid.
	 * @param contact The volunteer's contact.
	 * @ensures Current volunteer exists.
	 */
	public void identifyVolunteer(int contact) {
		currentVolunteer = new Volunteer(contact);
	}
	
	/**
	 * Identifies the number of occupants that may live in the house to be offered
	 * @param occupants The number of occupants that may live in the house.
	 * @return List of regions available.
	 * @ensures Current number of occupants exists.
	 */
	public List<Region> identifyNumberOccupants(int occupants) {
		currentNumberOccupants = occupants;
		return getRegions();
	}
	
	/**
	 * Identifies the region the house to be offered is located in.
	 * @param region The region the house is located in.
	 * @requires Current volunteer exists.
	 *           Current number of occupants exists.
	 * @ensures Current aid exists.
	 */
	public void identifyRegion(Region region) {
		currentAid = new Housing(currentVolunteer, new Date(), null, region, currentNumberOccupants);
	}
	
	/**
	 * Identifies the description of the item to be offered.
	 * @param description The description of the item.
	 * @requires Current volunteer exists.
	 * @ensures Current aid exists.
	 */
	public void identifyItemDescription(String description) {
		currentAid = new Item(currentVolunteer, new Date(), null, description);
	}
	
	/**
	 * Sends the volunteer an SMS containing a code to confirm the aid offer.
	 * @requires Current volunteer exists.
	 *           Current aid exists.
	 */
	public void sendConfirmationSMS() {
		currentConfirmationNumber = RANDOMIZER.nextInt(899999) + 100000;
		SMS_SENDER.send("" + currentVolunteer.getContact(), "" + currentConfirmationNumber);
	}
	
	/**
	 * Confirms the aid offer, if the confirmation code is correct.
	 * @param confirmationNumber Confirmation code sent by sendConfirmationSMS() through SMS.
	 * @requires Current volunteer exists.
	 *           Current aid exists.
	 * @return true if confirmation code is correct;
	 *         false otherwise.
	 */
	public boolean confirmOffer(int confirmationNumber) {
		if (confirmationNumber > 0 && confirmationNumber == currentConfirmationNumber) {
			aids.add(currentAid);
			this.clearCurrents();
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Identifies an individual migrant seeking for aid.
	 * @param name The migrant's name.
	 * @param contact The migrant's contact.
	 * @ensures Current migrant exists.
	 */
	public void identifyIndividualMigrant(String name, int contact) {
		currentMigrant = new MigrantIndividual(name, contact);
	}
	
	/**
	 * Identifies the number of members of a migrant family seeking for aid.
	 * @param number Number of members
	 * @ensures Current number of members exists.
	 */
	public void identifyNumberMembers(int number) {
		currentNumberMembers = number;
	}
	
	/**
	 * Identifies the head of the migrant family seeking for aid.
	 * @param name The name of the family head.
	 * @param contact The contact of the family head.
	 * @requires Current number of members exists.
	 * @ensures Current migrant family exists.
	 */
	public void identifyFamilyHead(String name, int contact) {
		currentMigrant = new MigrantFamily(name, contact, currentNumberMembers);
	}
	
	/**
	 * Identifies another member of the current migrant family.
	 * @param name The name of the family member.
	 * @requires Current migrant family exists.
	 */
	public void identifyOtherMember(String name) {
		((MigrantFamily)currentMigrant).addMember(name);
	}
	
	/**
	 * @return List of regions available.
	 */
	public List<Region> requestRegions() {
		return getRegions();
	}
	
	/**
	 * Identifies the region the migrant/family wishes to move to.
	 * @param region The region to move to.
	 * @param order Order used in the list.
	 * @requires Current migrant or migrant family exists.
	 * @return List of aids available in the region chosen.
	 * @ensures Current region exists.
	 */
	public List<Aid> identifyRegionWanted(Region region, Order order) {
		currentRegion = region;
		List<Aid> regionAids = new ArrayList<>();
		for (Aid aid : aids) {
			if (aid.getAttributedTo() == null &&
					(aid instanceof Item || ((Housing)aid).getRegion().equals(region))) {
				boolean added = false;
				if (order == Order.OLD_TO_NEW) {
					for (int i = 0; i < regionAids.size() && !added; i++) {
						if (aid.getDateAvailable().before(regionAids.get(i).getDateAvailable())) {
							regionAids.add(i, aid);
							added = true;
						}
					}
				}
				else if (order == Order.NEW_TO_OLD) {
					for (int i = 0; i < regionAids.size() && !added; i++) {
						if (aid.getDateAvailable().after(regionAids.get(i).getDateAvailable())) {
							regionAids.add(i, aid);
							added = true;
						}
					}
				}
				else if ((order == Order.HOUSING_FIRST && aid instanceof Housing) ||
						(order == Order.ITEMS_FIRST && aid instanceof Item)) {
					regionAids.add(0, aid);
					added = true;
				}
				if (!added) {
					regionAids.add(aid);
				}
			}
		}
		return regionAids;
	}
	
	/**
	 * Identifies an aid chosen by the migrant/family.
	 * @param aid Aid chosen.
	 * @requires Current migrant or migrant family exists.
	 * @ensures Current aids list exists.
	 */
	public void identifyAidWanted(Aid aid) {
		currentWantedAids.add(aid);
	}
	
	/**
	 * Confirms the aid request and assigns all chosen aids to the migrant/family.
	 * @requires Current migrant or migrant family exists.
	 *           Current aids list exists.
	 */
	public void confirmAidRequest() {
		for (int i = 0; i < currentWantedAids.size(); i++) {
			Aid aid = currentWantedAids.get(i);
			if (aid instanceof Housing) {
				aids.add(aids.indexOf(aid), new Housing(aid.getOfferedBy(), aid.getDateAvailable(),
						currentMigrant, ((Housing)aid).getRegion(), ((Housing)aid).getNumberOccupants()));
			}
			else {
				aids.add(aids.indexOf(aid), new Item(aid.getOfferedBy(), aid.getDateAvailable(),
						currentMigrant, ((Item)aid).getDescription()));
			}
			aids.remove(aid);
			SMS_SENDER.send("" + aid.getOfferedBy().getContact(), "Your aid has been attributed to a migrant. "
					+ "Thank you for your help. :)");
		}
		this.clearCurrents();
	}
	
	/**
	 * Requests notification to the migrant/family head for when an aid offer becomes available
	 * the chosen region.
	 * @requires Current migrant or migrant family exists.
	 *           Current region exists.
	 */
	public void requestNotification() {
		notificationRequests.get(currentRegion).add(currentMigrant);
		this.clearCurrents();
	}
	
	/**
	 * Clears all "currents" (Volunteer, Migrant, etc...)
	 */
	private void clearCurrents() {
		currentVolunteer = null;
		currentNumberOccupants = -1;
		currentAid = null;
		currentConfirmationNumber = -1;
		currentMigrant = null;
		currentNumberMembers = -1;
		currentWantedAids.clear();
	}
	
}













