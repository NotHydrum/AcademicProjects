import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

/**
 *  Class to run the emergency service of an hospital
 * 
 * @author Profs de LabP
 *
 */
public class RunHospital {

    /**
     * Gets, from the input file "patients.txt, the list of patients 
     * arriving to the Hospital, creates the EmergencyService and 
     * processes each one of the events read from the "events.txt" file
     * @param args - Not used
     * @throws IOException
     */
	 public static void main(String[] args) throws IOException {

    	Scanner patientsReader = new Scanner(new File("patients.txt"));
    	Scanner eventsReader = new Scanner(new File("events.txt"));
		PrintWriter out = new PrintWriter("logEmergencyService.txt");
		int numberOfDoctors = 4;
  
        EmergencyService es = new EmergencyService(numberOfDoctors);      
        
        //Read and process the events file
        while (eventsReader.hasNextLine()) {
        	String event = eventsReader.nextLine();
        	
        	switch(event) {
        	case "nextArrival": 
        		processNextArrival(patientsReader, out, es);
        		break;
        	case "nextDeparture":
        		processNextDeparture(out, es);
        		break;
        	case "howManyDoctors":
        		out.println("Emergency Service is working with "  + es.getNumberOfDoctors() + " doctors");
        		break;
        	case "forAttendance":
        		out.println("Patients waiting in the Emergency Service:");
        		out.print(es.toString());
        		break;
        	case "totalPatients":
        		out.println("Emergency Service has a total of "+ es.totalPatients() + " patients");
        		break;
        	case "minWaitingTime":
        		out.println("Emergency Service has a waiting time of "+ es.minWaitingTime() + " minutes");
        		break;
        	default:
        		if (event.contains("attendanceTimeAbove")) {
        			processAttendanceTime(out, es, event);
        		} else {
        			out.println("Unknown event type");
        		}    			
        	}
        }
        
        eventsReader.close();
        out.close();
    }

	 /**
	  * Processes an "attendance time above" event
	  * @param out - Printwriter to write on
	  * @param es - the emergency service
	  * @param event - the event
	  */
	private static void processAttendanceTime(PrintWriter out, 
			EmergencyService es, String event) {
		String m = event.split(" ")[1];
		int minutes = Integer.valueOf(m);
		List<Integer> doctorsList = es.attendanceTimeAbove(minutes);
		out.print("Doctors whose attendance time is longer than " + m + ": ");
		printList(out, doctorsList);
		out.println();
	}

	 /**
	  * Processes a "next departure" event
	  * @param out - Printwriter to write on
	  * @param es - the emergency service
	  */
	private static void processNextDeparture(PrintWriter out, EmergencyService es) {
		
		if (es.totalPatients() == 0) {
			out.println("Emergency Service is empty");
		} else {
			int patientID = es.dischargePatient();
			out.println("Patient " + patientID + " was discharged");
		}
	}

	 /**
	  * Processes a "next arrival" event
	  * @param reader - the scanner to read from
	  * @param out - Printwriter to write on
	  * @param es - the emergency service
	  */
	private static void processNextArrival(Scanner reader, 
			PrintWriter out, EmergencyService es) {
		
		if (! reader.hasNextLine()) {
			out.println("No more patients to admit");
		} else {
			// get the next patient from the patients file
			String line = reader.nextLine();
			String [] patientInfo = line.split(" ");
			UrgencyStatus urgency = UrgencyStatus.valueOf(patientInfo[0]);
			int time = Integer.valueOf(patientInfo[1]);
			int patientID = es.checkIn(urgency, time);
			out.println("Patient " + patientID + " checked in" );
			int doctorID = es.chooseDoctor();
			es.assignPatient(doctorID, patientID);
			out.println("Patient " + patientID + " was assigned to Doctor " + doctorID);
    	}
	}
    
    /**
     * Writes a list of numbers separated by spaces
     * @param toWrite - Printwriter to write on
     * @param listNumbers - list of integers to write
     */
    private static void printList(PrintWriter out, List<Integer> listNumbers) {
    	for (int i: listNumbers) {
    		out.print(i);
    		out.print(" ");
    	}
    }    
}
 