
/**
* Interface for a mutable stack: a collection of objects that are inserted 
* and removed according to the last-in first-out principle.
*/
public interface Stack<E> {

	/**
	 * Insert an element at the top of the Stack.
	 * @param e The object to insert.
	 */
	public void push(E e);

	/**
	 * @returns The element at the top of the Stack
	 * @requires !isEmpty();
	 */
	public E peek();

	/**
	 * Remove from the Stack the element at the top.
	 * @requires !isEmpty();
	 */
	public void pop();

	/**
	 * Is this Stack empty?
	 */
	public boolean isEmpty();

}