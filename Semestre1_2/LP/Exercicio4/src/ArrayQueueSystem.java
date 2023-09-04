import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements a priority queue with an array of queues, one array for each priority value
 * 
 * @author LabP team
 * @author Miguel Nunes fc56338
 *
 * @param <E> type of the elements in the queue
 */

public class ArrayQueueSystem<E> implements Queue<E>, Iterable<E>{
	
	//array of queues: queue with index 0 has the highest priority
	//the priority decreases as the index increases
	
	Queue<E>[] priorityQueue;
	
	//currentPriorityQueue is the index of the queue that should be updated according to 
	//the priority policy of type E
	//The enqueue operation will enqueue the element of type E into this queue
	int currentPriority;
	
	
	@SuppressWarnings("unchecked")
	ArrayQueueSystem (int numberOfQueues){
		
		priorityQueue = (Queue<E>[]) Array.newInstance(Queue.class, numberOfQueues);
		for (int i=0; i < numberOfQueues; i++) {
			priorityQueue [i] = new ArrayQueue <E> ();
		}
		currentPriority = 0; 
	}

	/**
	 * Sets the index of the queue that will be used in the next enqueue operation
	 * 
	 * @param index of the current priority queue 
	 */
	public void setCurrentPriority (int index) {
		currentPriority = index;
	}

	
	/**
	 * Appends an element at the end of the current priority queue
	 * @requires e!=null
	 * @param f
	 */
	@Override
	public void enqueue(E elem) {
		priorityQueue [currentPriority].enqueue(elem);
		
	}
	
	/**
	 * Removes from the queue the element with the highest priority
	 */
	@Override
	public void dequeue() {
		int i=0;
		while (i< priorityQueue.length && priorityQueue[i].isEmpty())
			i++;
		if (i< priorityQueue.length) {
			priorityQueue[i].dequeue();
		}
	}
	
	/**
	 * 
	 * @requires !isEmpty()
	 * @return the element with the highest priority
	 */
	@Override
	public E front() {
		E res = null;
		int i=0;
		while (i< priorityQueue.length && priorityQueue[i].isEmpty())
			i++;
		if (i< priorityQueue.length)
			res = priorityQueue[i].front();
		return res;
	}

	/**
	 * Verifies if there is no elements in any of the queues 
	 */
	@Override
	public boolean isEmpty() {
		int i=0;
		while (i< priorityQueue.length && priorityQueue[i].isEmpty())
			i++;
		return i == priorityQueue.length;
	}

	/**
	 * Total numbers of elements in the queues 
	 */
	@Override
	public int size() {
		int total = 0;
		int i=0;
		while (i< priorityQueue.length) {
			total += priorityQueue[i].size();
			i++;
		}
		return total;
	}
	
	/**
	 * The textual representation of the ArrayQueueSystem
	 */
	@Override	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<priorityQueue.length; i++) {
			sb.append("Queue "+ i + ": "+ priorityQueue[i].toString()+ System.getProperty("line.separator"));
		}
		return sb.toString();	
	} 
	
	/**
	 * Creates an iterator for the ArrayQueueSystem
	 */
	public Iterator<E> iterator() {
		return new IteratorQueueSystem();
	}
	
	/**
	 * Instances represent iterators for an ArrayQueueSystem.
	 * @author Miguel Nunes fc56338
	 */
	private class IteratorQueueSystem implements Iterator<E> {
		
		/**
		 * Index of the queue currently being iterated.
		 */
		private int priority;
		/**
		 * Queue of the priority currently being iterated.
		 */
		private ArrayQueue<E> thisPriorityQueue;
		/**
		 * Iterator of the queue of the priority currently being iterated.
		 */
		private Iterator<E> thisPriorityIterator;
		
		/**
		 * Constructs an IteratorQueueSystem.
		 */
		IteratorQueueSystem() {
			priority = 0;
			thisPriorityQueue = (ArrayQueue<E>)priorityQueue[priority];
			thisPriorityIterator = thisPriorityQueue.iterator();
		}
		
		/**
		 * Determines if this IteratorQueueSystem has any elements left.
		 * @return 'true' if iterator has elements left, 'false' otherwise.
		 */
		@Override
		public boolean hasNext() {
			return thisPriorityIterator.hasNext() || hasNextInNextPriorities(priority);
		}
		
		/**
		 * Determines if there are any elements in the queues not yet iterated.
		 * @param priority Priority currently being iterated. (this.priority)
		 * @return 'true' if there are elements in the queues not yet iterated, 'false' otherwise.
		 */
		private boolean hasNextInNextPriorities(int priority) {
			boolean hasNext = false;
			if (priority + 1 < priorityQueue.length && priorityQueue[priority + 1].size() > 0) {
				hasNext = true;
			}
			else if (priority + 2 < priorityQueue.length) {
				hasNext = hasNextInNextPriorities(priority + 1);
			}
			return hasNext;
		}
		
		/**
		 * Returns the next element in the iteration.
		 * @return The next element in the iteration.
		 * @throws NoSuchElementException if !hasNext().
		 */
		@Override
		public E next() {
			E next;
			if (thisPriorityIterator.hasNext()) {
				next = thisPriorityIterator.next();
			}
			else if (hasNextInNextPriorities(priority)) {
				do {
					priority++;
					thisPriorityQueue = (ArrayQueue<E>)priorityQueue[priority];
				} while (thisPriorityQueue.isEmpty());
				thisPriorityIterator = thisPriorityQueue.iterator();
				next = thisPriorityIterator.next();
			}
			else {
				throw new NoSuchElementException();
			}
			return next;
		}
		
	}

}
