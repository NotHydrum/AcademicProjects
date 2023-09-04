
/**
 * API for mutable queues.
 *
 * @param <E> The type of the elements in the queue.
 */
interface Queue<E> {

	/**
	 * Adds an element to the rear of this queue
	 * 
	 * @param e The object to be added
	 */
	public void enqueue(E e);

	/**
	 * Removes the element at the front of this queue
	 * 
	 * @requires !isEmpty()
	 */
	public void dequeue();

	/**
	 * The element at the front of this queue
	 * 
	 * @return e The element at the front of this queue
	 * @requires !isEmpty()
	 */
	public E front();

	/**
	 * Is this queue empty?
	 * 
	 * @return true if this queue is empty
	 */
	public boolean isEmpty();

	/**
	 * How many elements are there in this queue
	 * 
	 * @return the number of elements in this queue
	 */
	public int size();

	/**
	 * Textual representation of the current state of this queue in the format <a1,
	 * a2, a3, ..., an<
	 * 
	 * @return the Textual representation of this queue
	 */
	public String toString();
}