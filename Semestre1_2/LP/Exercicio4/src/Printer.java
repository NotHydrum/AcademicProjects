import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class implements a printer with a specific priority policy
 * 
 * @author LabPteam 2021
 * @author Miguel Nunes fc56338
 *
 */

public class Printer {
	
	private static final int NUMBER_OF_QUEUES = 4; //number of priority queues
	private static final String ADMIN = "admin"; //user with privileges for priority 0
	private static final int LIMPRIOR1 = 64; 	//file size limit for priority 1
	private static final int LIMPRIOR2 = 1024;	//file size limit for priority 2
	
	
	ArrayQueueSystem <FileToPrint> printerQueue; //manages the priority queues
	int maxCapacity;			// printer memory capacity 
	int leftCapacity;			// printer memory not yet used 
	
	/**
	 * Creates a printer with a given capacity and with priorities queues. 
	 * The number of priority queues is NUMBER_OF_QUEUES
	 * 
	 * @param maxCapacity  printer memory capacity
	 */
	Printer (int maxCapacity){
		this.maxCapacity = maxCapacity;
		this.leftCapacity = maxCapacity;
		printerQueue = new ArrayQueueSystem<FileToPrint>(NUMBER_OF_QUEUES);
	}
	
	/**
	 * Gets printer memory not yet used
	 * 
	 * @return printer memory not yet used
	 */
	
	public int leftCapacity () {
		return leftCapacity;
	}
	
	/**
	 * Calculates the index of the priority queue where a file should be inserted
	 * according to the priority policy of the printer.
	 * @param f File to be inserted in the priority queue.
	 * @requires f != null;
	 * @return The index of the queue where the file is to be inserted.
	 */
	public int priorityPolicy (FileToPrint f) {
		int index;
		if (f.getOwner().equals(ADMIN)) {
			index = 0;
		}
		else if (f.getSize() <= LIMPRIOR1) {
			index = 1;
		}
		else if (f.getSize() <= LIMPRIOR2) {
			index = 2;
		}
		else {
			index = 3;
		}
		return index;
	}
	

	/**
	 * Inserts a file in the priority queue
	 * 
	 * @requires f!=null
	 * @param f
	 * @return true if there is available space on the printer
	 */
	public boolean add(FileToPrint f) {
		boolean added = false;
		if (f.getSize() <= leftCapacity()) {
			printerQueue.setCurrentPriority(priorityPolicy(f));
			printerQueue.enqueue(f);
			leftCapacity -= f.getSize();
			added = true;
		}
		return added;
	}

	
	/**
	 * Size of the queue
	 * 
	 * @return number of files in queue
	 */
	public int size() {
		return printerQueue.size();
	}
	
	/**
	 * Counts the number of files in the queue equal to f.
	 * 
	 * @param f File to search.
	 * @return The number of files in the queue equal to f.
	 */
	public int ocurrenciesOfFile (FileToPrint f) {
		Iterator<FileToPrint> iterator = printerQueue.iterator();
		int equals = 0;
		try {
			//this loop is not infinite, it is aborted when iterator.next() throws a NoSuchElementException.
			//this has better performance than while(hasNext) without a try-catch because next() itself use hasNext().
			while (true) {
				if (f.equals(iterator.next())) {
					equals++;
				}
			}
		} catch (NoSuchElementException e) {
			//aborts "infinite" while loop
		}
		return equals;
	}
	
	/**
	 * The textual representation of the printer
	 */
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("maxCapacity: "+ maxCapacity + System.getProperty("line.separator"));
		sb.append("leftCapacity: " + leftCapacity + System.getProperty("line.separator"));
		sb.append(printerQueue.toString());	
		return sb.toString();
	}
	
}

