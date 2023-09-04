import java.util.LinkedList;
import java.util.List;

/**
 * Class whose instances represent a hospital's Emergency Service Unit.
 * @author Miguel Nunes fc56338
 */
public class EmergencyService {
	
	/**
	 * Defines the end-of-line character, independent of operating system.
	 */
	private static final String NEXT_LINE = System.getProperty("line.separator");
	
	/**
	 * E.S.U's doctors.
	 */
	private LinkedList<Doctor> doctors;
	/**
	 * Patients who have checked-in and have yet to be discharged.
	 */
	private LinkedList<Patient> checkedInPatients;
	/**
	 * Identification number of the next patient to check-in.
	 */
	private int nextPatientId;
	
	/**
	 * Constructs an EmergencyService with 'm' number of doctors, with identification numbers from 1 to 'm'.
	 * @param m Number of doctors.
	 * @requires m > 0
	 */
	public EmergencyService(int m) {
		doctors = new LinkedList<>();
		for (int i = 1; i <= m; i++) {
			doctors.add(new Doctor(i));
		}
		checkedInPatients = new LinkedList<>();
		nextPatientId = 1;
	}
	
	/**
	 * Determines whether this Emergency Service Unit has a doctor with the given identification number.
	 * @param doctorId Doctor's identification number.
	 * @return 'true' if a doctor with given ID exists, 'false' otherwise.
	 */
	boolean hasDoctor(int doctorId) {
		return doctorId <= doctors.size();
	}
	
	/**
	 * Registers the entrance of a new patient, with given urgency and consultation time,
	 * to this Emergency Service Unit.
	 * @param urgency Patient's urgency code.
	 * @param consultationTime Time required to treat the Patient.
	 * @requires consultationTime >= 0
	 * @return The patient's identification number.
	 */
	int checkIn(UrgencyStatus urgency, int consultationTime) {
		checkedInPatients.add(new Patient(nextPatientId, urgency, consultationTime));
		nextPatientId++;
		return nextPatientId - 1;
	}
	
	/**
	 * Determines if any patient has already been registered with this identification number.
	 * @param patientId Identification number.
	 * @return 'true' if ID has been registered, 'false' otherwise.
	 */
	boolean hasCheckedIn(int patientId) {
		return patientId < nextPatientId;
	}
	
	/**
	 * Determines the doctor to assign the next patient to according to the following criterion: <br>
	 * - The smallest waiting time; <br>
	 * - Least number of patients; <br>
	 * - Least number of urgent patients; <br>
	 * - Smallest doctor identification number.
	 * @return The chosen doctor's identification number.
	 */
	int chooseDoctor() {
		Doctor best = doctors.get(0);
		for (int i = 1; i < doctors.size(); i++) {
			Doctor next = doctors.get(i);
			if (next.attendanceTime() < best.attendanceTime() ||
					(next.attendanceTime() == best.attendanceTime() &&
					 next.numberPatients(UrgencyStatus.NONURGENT) + next.numberPatients(UrgencyStatus.URGENT) <
					 best.numberPatients(UrgencyStatus.NONURGENT) + best.numberPatients(UrgencyStatus.URGENT)) ||
					(next.attendanceTime() == best.attendanceTime() &&
					 next.numberPatients(UrgencyStatus.NONURGENT) + next.numberPatients(UrgencyStatus.URGENT) ==
					 best.numberPatients(UrgencyStatus.NONURGENT) + best.numberPatients(UrgencyStatus.URGENT) &&
					 next.numberPatients(UrgencyStatus.URGENT) < best.numberPatients(UrgencyStatus.URGENT))) {
				best = next;
			}
		}
		return best.getId();
	}
	
	/**
	 * Assigns the patient with the given Patient identification number to the waiting list of the doctor with 
	 * the given Doctor identification number.
	 * @param doctorId Doctor's identification number.
	 * @param patientId Patient's identification number.
	 * @requires hasDoctor(doctorId) && hasCheckedIn(patientId) <br>
	 *           Patient has not been assigned to any doctor yet.
	 */
	void assignPatient(int doctorId, int patientId) {
		Patient patient = checkedInPatients.get(idSearch(patientId));
		doctors.get(doctorId - 1).addPatient(patientId, patient.urgency(), patient.consultationTime());
	}
	
	/**
	 * @return Number of doctors.
	 */
	int getNumberOfDoctors() {
		return doctors.size();
	}
	
	/**
	 * @return Number of total patients that have yet to be discharged.
	 */
	int totalPatients() {
		return checkedInPatients.size();
	}
	
	/**
	 * Determines what doctors have an attendance time above 'bound'.
	 * @param bound The minimum attendance time, exclusive.
	 * @return A List containing the identification numbers of the doctors with an attendance time above 'bound'.
	 */
	List<Integer> attendanceTimeAbove(int bound) {
		LinkedList<Integer> doctorIds = new LinkedList<>();
		for (int i = 0; i < doctors.size(); i++) {
			if (doctors.get(i).attendanceTime() > bound) {
				doctorIds.add(doctors.get(i).getId()); 
			}
		}
		return doctorIds;
	}
	
	/**
	 * @return The minimum time a new non-urgent patient will have to wait before being attended.
	 */
	int minWaitingTime() {
		int minimum = doctors.get(0).attendanceTime();
		for (int i = 1; i < doctors.size(); i++) {
			if (doctors.get(i).attendanceTime() < minimum) {
				minimum = doctors.get(i).attendanceTime();
			}
		}
		return minimum;
	}
	
	/**
	 * Determines which patient at the front of their doctor's line has the shortest consultation time and
	 * discharges them.
	 * @requires totalPatients() > 0
	 * @return The identification number of the discharged patient.
	 */
	int dischargePatient() {
		Doctor doctor = doctors.get(0);
		int patientIndex = idSearch(doctor.nextToAttend());
		int consultationTime = checkedInPatients.get(patientIndex).consultationTime();
		for (int i = 1; i < doctors.size(); i++) {
			Doctor nextDoctor = doctors.get(i);
			if (!nextDoctor.isFree()) {
				int nextPatientIndex = idSearch(nextDoctor.nextToAttend());
				int nextConsultationTime = checkedInPatients.get(nextPatientIndex).consultationTime();
				if (nextConsultationTime < consultationTime || (nextConsultationTime == consultationTime &&
						nextPatientIndex < patientIndex)) {
					doctor = nextDoctor;
					patientIndex = nextPatientIndex;
					consultationTime = nextConsultationTime;
				}
			}
		}
		checkedInPatients.remove(patientIndex);
		return doctor.dischargePatient();
	}

	/**
	 * Generates a String representation of the Emergency Service Unit containing, for each of the doctors, a list of
	 * their patients by arrival order (See Doctor.java's forAttendance()) or, if their list is empty, "No Patients;".
	 * @return String representation of the Emergency Service Unit.
	 */
	public String toString() {
		StringBuilder myBuilder = new StringBuilder();
		for (int i = 0; i < doctors.size(); i++) {
			Doctor doctor = doctors.get(i);
			myBuilder.append("Doctor " + doctor.getId() + ": ");
			if (doctor.isFree()) {
				myBuilder.append("No Patients;" + NEXT_LINE);
			}
			else {
				myBuilder.append(doctor.forAttendance() + NEXT_LINE);
			}
		}
		return myBuilder.toString();
	}
	
	/**
	 * Searches for the index position of the given identification number in the List of checked-in patients using
	 * searchRecursion(). This method exists solely to make calling searchRecursion() more convenient.
	 * @param patientId Identification number to search for.
	 * @requires totalPatients() > 0
	 * @return Index position of the given identification number.
	 */
	private int idSearch(int patientId) {
		return searchRecursion(patientId, 0, checkedInPatients.size() - 1);
	}
	
	/**
	 * Recursive binary search method used by idSearch to search for the index position of the given identification
	 * number in the List of checked-in patients.
	 * @param patientId Identification number to search for.
	 * @param lowerBound Lower index bound, inclusive.
	 * @param upperBound Upper index bound, inclusive.
	 * @requires lowerBound >= 0 && upperBound < totalPatients() && lowerBound <= upperBound
	 * @return Index position of the given identification number.
	 */
	private int searchRecursion(int patientId, int lowerBound, int upperBound) {
		int wantedPatient;
		int search = (lowerBound + upperBound) / 2;
		if (checkedInPatients.get(search).patientId() == patientId) {
			wantedPatient = search;
		}
		else if (checkedInPatients.get(search).patientId() < patientId) {
			if ((search + upperBound) / 2 != search) {
				wantedPatient = searchRecursion(patientId, search, upperBound);
			}
			else {
				wantedPatient = searchRecursion(patientId, search + 1, upperBound + 1);
			}
		}
		else {
			if ((lowerBound + search) / 2 != search) {
				wantedPatient = searchRecursion(patientId, lowerBound, search);
			}
			else {
				wantedPatient = searchRecursion(patientId, lowerBound - 1, search - 1);
			}
		}
		return wantedPatient;
	}
	
}
