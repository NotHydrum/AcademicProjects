import static org.junit.Assert.*;
import java.util.List;
import java.util.Scanner;

import org.junit.BeforeClass;

public class TestProject4 {
	/**
	 * We use this line separator in order to tests to pass whatever the
	 * end-of-line symbols used by a specific operating system.
	 */
	public static final String EOL = System.getProperty("line.separator");

	private static String patients;
	
	@BeforeClass
	public static void definePatients() {
		patients = "NONURGENT 10" + EOL + 
				"NONURGENT 20" + EOL +
				"URGENT 20" + EOL +
				"NONURGENT 50" + EOL +
				"URGENT 40" + EOL + 
				"URGENT 10" + EOL;
				
	}
	
	
	@org.junit.Test
	public void testDoctorAdd() {
		Doctor myDoctor = new Doctor(20);
		Scanner in = new Scanner(patients);
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			String [] patientInfo = in.nextLine().split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			myDoctor.addPatient(i+1,urgency, time );
			s.append("Patient " +  (i+1) + ": " + urgency + " " + time + "; ");
		}
		in.close();
		int expectedUrgentPatients = 3;
		int expectedNonUrgentPatients = 3;
		
		assertEquals(expectedUrgentPatients, myDoctor.numberPatients(UrgencyStatus.URGENT));
		assertEquals(expectedNonUrgentPatients, myDoctor.numberPatients(UrgencyStatus.NONURGENT));
		assertEquals(s.toString(), myDoctor.forAttendance());
		
		
	}
			
	
	@org.junit.Test
	public void testDoctorAttendanceTime() {
		Doctor myDoctor = new Doctor(20);
		Scanner in = new Scanner(patients);
		for (int i = 0; i < 3; i++) {
			String [] patientInfo = in.nextLine().split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			myDoctor.addPatient(i+1,urgency, time );
		}
		in.close();
		int t = myDoctor.attendanceTime();
		assertEquals(50,t);
	}

	@org.junit.Test
	public void testDoctorDischarge() {
		Doctor myDoctor = new Doctor(20);
		Scanner in = new Scanner(patients);
		for (int i = 0; i < 6; i++) {
			String [] patientInfo = in.nextLine().split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			myDoctor.addPatient(i+1,urgency, time );
		}
		in.close();

		myDoctor.dischargePatient();
		myDoctor.dischargePatient();
		int patientID = myDoctor.nextToAttend();		
		assertEquals(6, patientID );
	}

	@org.junit.Test
	public void testServiceChooseDoctor() {
		EmergencyService myService = new EmergencyService(2);
		Scanner in = new Scanner(patients);
		for (int i = 0; i < 6; i++) {
			String [] patientInfo = in.nextLine().split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			int patientID = myService.checkIn(urgency, time);
			int doctorID = myService.chooseDoctor();
			myService.assignPatient(doctorID, patientID);
			assertEquals(i%2 + 1, doctorID);
		}		
		in.close();
		assertEquals(6, myService.totalPatients());	
	}
	
	@org.junit.Test
	public void testServiceWaitingTime() {
		EmergencyService myService = new EmergencyService(2);
		Scanner in = new Scanner(patients);
		for (int i = 0; i < 6; i++) {
			String [] patientInfo = in.nextLine().split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			int patientID = myService.checkIn(urgency, time);
			int doctorID = myService.chooseDoctor();
			myService.assignPatient(doctorID, patientID);
		}
		in.close();
		
		assertEquals(70, myService.minWaitingTime());
	}
	
	@org.junit.Test
	public void testServiceDischarge() {
		EmergencyService myService = new EmergencyService(2);
		Scanner in = new Scanner(patients);
		for (int i = 0; i < 6; i++) {
			String [] patientInfo = in.nextLine().split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			int patientID = myService.checkIn(urgency, time);
			int doctorID = myService.chooseDoctor();
			myService.assignPatient(doctorID, patientID);
		}
		in.close();
		
		int patientID = myService.dischargePatient();
		assertEquals(6, patientID);
		patientID = myService.dischargePatient();
		assertEquals(2, patientID);
		
		assertEquals(4, myService.totalPatients());
	}
		

	@org.junit.Test
	public void testAttendanceTimeAbove() {
		EmergencyService myService = new EmergencyService(2);
		Scanner in = new Scanner(patients);
		for (int i = 0; i < 4; i++) {
			String [] patientInfo = in.nextLine().split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			int patientID = myService.checkIn(urgency, time);
			int doctorID = myService.chooseDoctor();
			myService.assignPatient(doctorID, patientID);
		}
		in.close();
			
		List<Integer> doctorsList = myService.attendanceTimeAbove(30);
		assertEquals(2, (int) doctorsList.get(0));
		assertEquals(1, doctorsList.size());
		
		doctorsList = myService.attendanceTimeAbove(70);
		assertEquals(0, doctorsList.size());
	
	}


}
