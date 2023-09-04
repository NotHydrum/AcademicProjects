import java.util.LinkedList;

/**
 * Class whose instances represent emergency service doctors.
 * @author Miguel Nunes fc56338
 */
public class Doctor {
	
	/**
	 * Doctor's identification number.
	 */
	private int doctorId;
	/**
	 * Doctor's urgent patients.
	 */
	private LinkedList<Patient> urgentLine;
	/**
	 * Doctor's non-urgent patients.
	 */
	private LinkedList<Patient> regularLine;
	
	/**
	 * Constructs a new Doctor, with the given parameters.
	 * @param doctorId The doctor's identification number.
	 */
	public Doctor(int doctorId) {
		this.doctorId = doctorId;
		urgentLine = new LinkedList<>();
		regularLine = new LinkedList<>();
	}
	
	/**
	 * @return The doctor's identification number.
	 */
	int getId() {
		return doctorId;
	}
	
	/**
	 * @return The time required for the doctor to treat all their patients.
	 */
	int attendanceTime() {
		int attendanceTime = 0;
		for (int i = 0; i < urgentLine.size(); i++) {
			attendanceTime += urgentLine.get(i).consultationTime();
		}
		for (int i = 0; i < regularLine.size(); i++) {
			attendanceTime += regularLine.get(i).consultationTime();
		}
		return attendanceTime;
	}
	
	/**
	 * @return 'true' if the doctor has no patients, 'false' otherwise.
	 */
	boolean isFree() {
		return urgentLine.isEmpty() && regularLine.isEmpty();
	}
	
	/**
	 * Adds a patient with the given parameters to this doctor's waiting line.
	 * @param patientId The patient's identification number.
	 * @param urgency The patient's urgency code.
	 * @param consultationTime Time required to treat the patient.
	 */
	void addPatient(int patientId, UrgencyStatus urgency, int consultationTime) {
		if (urgency == UrgencyStatus.URGENT) {
			urgentLine.add(new Patient(patientId, urgency, consultationTime));
		}
		else {
			regularLine.add(new Patient(patientId, urgency, consultationTime));
		}
	}
	
	/**
	 * @return The identification number of the patient this doctor will attend next.
	 * @requires !isFree()
	 */
	int nextToAttend() {
		int patientId;
		if (!urgentLine.isEmpty()) {
			patientId = urgentLine.peek().patientId();
		}
		else {
			patientId = regularLine.peek().patientId();
		}
		return patientId;
	}
	
	/**
	 * Treats and discharges the doctor's next patient, prioritizing urgency first and arrival order second.
	 * @return The discharged patient's identification number.
	 */
	int dischargePatient() {
		int patientId;
		if (!urgentLine.isEmpty()) {
			patientId = urgentLine.poll().patientId();
		}
		else {
			patientId = regularLine.poll().patientId();
		}
		return patientId;
	}
	
	/**
	 * @param urgency The urgency code to count.
	 * @return The number of patients, with the given urgency, this doctor has in their wait line.
	 */
	int numberPatients(UrgencyStatus urgency) {
		int numberPatients;
		if (urgency == UrgencyStatus.URGENT) {
			numberPatients = urgentLine.size();
		}
		else {
			numberPatients = regularLine.size();
		}
		return numberPatients;
	}
	
	/**
	 * Generates a String representation of the doctor's wait line, ordered by arrival order, using Patient.java's
	 * toString() method.
	 * @return String representation of the doctor's wait line.
	 */
	String forAttendance() {
		StringBuilder myBuilder = new StringBuilder();
		int urgentsAdded = 0;
		int regularsAdded = 0;
		for (int i = 0; i < urgentLine.size() + regularLine.size(); i++) {
			if (regularLine.isEmpty() || regularsAdded >= regularLine.size() || (urgentsAdded < urgentLine.size() &&
					urgentLine.get(urgentsAdded).patientId() < regularLine.get(regularsAdded).patientId())) {
				myBuilder.append(urgentLine.get(urgentsAdded).toString());
				urgentsAdded++;
			}
			else {
				myBuilder.append(regularLine.get(regularsAdded).toString());
				regularsAdded++;
			}
		}
		return myBuilder.toString();
	}
	
}
