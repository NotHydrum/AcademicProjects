/**
 * Class whose instances represent emergency service patients.
 * @author Miguel Nunes fc56338
 */
public class Patient {
	
	/**
	 * Patient's identification number.
	 */
	private int patientId;
	/**
	 * Patient's urgency code.
	 */
	private UrgencyStatus urgency;
	/**
	 * Time required to treat the Patient.
	 */
	private int consultationTime;
	
	/**
	 * Constructs a new Patient, with the given parameters.
	 * @param patientId The Patient's identification number.
	 * @param urgency The Patient's urgency code.
	 * @param consultationTime Time required to treat the Patient.
	 */
	public Patient(int patientId, UrgencyStatus urgency, int consultationTime) {
		this.patientId = patientId;
		this.urgency = urgency;
		this.consultationTime = consultationTime;
	}
	
	/**
	 * @return The Patient's identification number.
	 */
	int patientId() {
		return patientId;
	}
	
	/**
	 * @return The patient's urgency code.
	 */
	UrgencyStatus urgency() {
		return urgency;
	}
	
	/**
	 * @return The time required to treat the Patient.
	 */
	int consultationTime() {
		return consultationTime;
	}
	
	/**
	 * @return A String representation of the Patient.
	 */
	public String toString() {
		return "Patient " + patientId + ": " + urgency + " " + consultationTime + "; ";
	}
	
}
