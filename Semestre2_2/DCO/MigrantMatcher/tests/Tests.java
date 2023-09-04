import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

class Tests {
	
	@Test
	void testRegionsExist() {
		MigrantMatcher matcher = new MigrantMatcher();
		matcher.identifyVolunteer(123456789);
		List<Region> regionsV = matcher.identifyNumberOccupants(7);
		matcher.identifyIndividualMigrant("Asdrubal", 123456789);
		List<Region> regionsM = matcher.requestRegions();
		assertTrue(!regionsV.isEmpty() && !regionsM.isEmpty() && regionsV.size() == regionsM.size());
		for (Region region : regionsV) {
			boolean found = false;
			for (Region region2 : regionsM) {
				if (region.equals(region2)) {
					found = true;
				}
			}
			assertTrue(found);
		}
		
	}
	
	@Test
	void testConfirmationSMS() {
		//SMSs são "enviados" para o System.out, estes comandos redireçionam o System.out para um objeto que pode ser 
		//lido
		ByteArrayOutputStream out = new ByteArrayOutputStream();    
		System.setOut(new PrintStream(out));
		
		int contact = 123456789;
		MigrantMatcher matcher = new MigrantMatcher();
		matcher.identifyVolunteer(contact);
		List<Region> regions = matcher.identifyNumberOccupants(7);
		matcher.identifyRegion(regions.get(regions.size() - 1));
		matcher.sendConfirmationSMS();
		String[] sms = out.toString().split(" ");
		assertTrue(sms.length == 3 &&
				((sms[0] + " " + sms[1]).equals("{SMSSender} " + contact + ":") || //caso de Pidgeon ser usado
				(sms[0] + " " + sms[1]).equals("{TelegramSMSSender} " + contact + ":"))); //caso de Telegram ser usado
	}
	
	@Test
	void testCodeUnique() {
		//SMSs são "enviados" para o System.out, estes comandos redireçionam o System.out para um objeto que pode ser 
		//lido
		ByteArrayOutputStream out = new ByteArrayOutputStream();    
		System.setOut(new PrintStream(out));
		
		int contact = 123456789;
		MigrantMatcher matcher = new MigrantMatcher();
		matcher.identifyVolunteer(contact);
		List<Region> regions = matcher.identifyNumberOccupants(7);
		matcher.identifyRegion(regions.get(regions.size() - 1));
		matcher.sendConfirmationSMS();
		String[] sms1 = out.toString().split(" ");
		out.reset();
		matcher.sendConfirmationSMS();
		String[] sms2 = out.toString().split(" ");
		assertNotEquals(sms1[2], sms2[2]);
	}
	
	@Test
	void testStartAidsEmpty() {
		MigrantMatcher matcher = new MigrantMatcher();
		matcher.identifyIndividualMigrant("Asdrubal", 123456789);
		List<Region> regions = matcher.requestRegions();
		boolean empty = true;
		for (Region region : regions) {
			empty = matcher.identifyRegionWanted(region, Order.NONE).isEmpty();
		}
		assertTrue(empty);
	}
	
	@Test
	void testAidsList() throws InterruptedException {
		//SMSs são "enviados" para o System.out, estes comandos redireçionam o System.out para um objeto que pode ser 
		//lido
		ByteArrayOutputStream out = new ByteArrayOutputStream();    
		System.setOut(new PrintStream(out));
				
		MigrantMatcher matcher = new MigrantMatcher();
		matcher.identifyVolunteer(123456789);
		List<Region> regions = matcher.identifyNumberOccupants(7);
		matcher.identifyRegion(regions.get(0));
		matcher.sendConfirmationSMS();
		String code = out.toString().split(" ")[2];
		matcher.confirmOffer(Integer.parseInt(code.substring(0, code.length() - 1)));
		out.reset();
		
		Thread.sleep(10); //Isto é para as ajudas não serem criadas no mesmo milisegundo
		
		matcher.identifyVolunteer(123456789);
		matcher.identifyItemDescription("Blanket");
		matcher.sendConfirmationSMS();
		code = out.toString().split(" ")[2];
		matcher.confirmOffer(Integer.parseInt(code.substring(0, code.length() - 1)));
		out.reset();
		
		matcher.identifyIndividualMigrant("Asdrubal", 987654321);
		List<Aid> aids = matcher.identifyRegionWanted(regions.get(0), Order.OLD_TO_NEW);
		assertTrue(aids.size() == 2 && aids.get(0).getDateAvailable().before(aids.get(1).getDateAvailable()));
		
		aids = matcher.identifyRegionWanted(regions.get(0), Order.NEW_TO_OLD);
		assertTrue(aids.size() == 2 && aids.get(0).getDateAvailable().after(aids.get(1).getDateAvailable()));
		
		aids = matcher.identifyRegionWanted(regions.get(1), Order.NONE);
		assertTrue(aids.size() == 1 && aids.get(0) instanceof Item);

		aids = matcher.identifyRegionWanted(regions.get(0), Order.HOUSING_FIRST);
		assertTrue(aids.size() == 2 && aids.get(0) instanceof Housing);

		aids = matcher.identifyRegionWanted(regions.get(0), Order.ITEMS_FIRST);
		assertTrue(aids.size() == 2 && aids.get(0) instanceof Item);
		
		matcher.identifyAidWanted(aids.get(0));
		matcher.confirmAidRequest();
		
		aids = matcher.identifyRegionWanted(regions.get(0), Order.NONE);
		assertTrue(aids.size() == 1);
	}
	
}













