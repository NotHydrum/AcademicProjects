import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A queue implemented with an array.
 * 
 * @param <E> The type of the elements in the queue.
 * 
 */
public class ArrayQueue<E> implements Queue<E>, Cloneable, Iterable<E> {

	/**
	 * The length of initial array.
	 */
	private static final int DEFAULT_CAPACITY = 4;

	/**
	 * The elements on the queue.
	 */
	private E[] q;

	/**
	 * The index of the front of the queue, that is, of the first element to exit
	 * the queue.
	 */
	private int head;

	/**
	 * The index of the tail of the queue, that is, where the next element will be
	 * stored.
	 */
	private int tail;

	/**
	 * The number of elements in the queue.
	 */
	private int size;

	/**
	 * Construct an empty queue.
	 */
	@SuppressWarnings("unchecked")
	public ArrayQueue() {
		q = (E[]) new Object[DEFAULT_CAPACITY];
		head = 0;
		tail = 0;
		size = 0;
	}

	@Override
	public void enqueue(E item) {
		if (size == q.length)
			reallocate_();
		q[tail] = item;
		tail = inc(tail);
		size++;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * The number of elements in the queue.
	 */
	public int size() {
		return size;
	}

	@Override
	public E front() {
		return q[head];
	}

	@Override
	public void dequeue() {
		q[head] = null;
		head = inc(head);
		size--;
	}

	/**
	 * Arithmetic modulo the length of the array holding the elements in the queue.
	 */
	private int inc(int i) {
		return (i + 1) % q.length;
	}

	/**
	 * Move the elements in the array to a larger array.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void reallocate() {
		E[] newQ = (E[]) new Object[q.length * 2];
		int j = head;
		for (int i = 0; i < size; i++) {
			newQ[i] = q[j];
			j = inc(j);
		}
		head = 0;
		tail = size;
		q = newQ;
	}

	/**
	 * Move the elements in the array to a larger array. with two calls to
	 * System.arraycopy.
	 */
	@SuppressWarnings("unchecked")
	private void reallocate_() {
		E[] newQ = (E[]) new Object[q.length * 2];
		System.arraycopy(q, head, newQ, 0, size - head);
		// copy size-head elements from position head of array q
		// to positions 0 to 0+size-head of array newQ
		System.arraycopy(q, 0, newQ, size - head, head);
		head = 0;
		tail = size;
		q = newQ;
	}

	/**
	 * The textual representation of this queue, in format <a, b, c<
	 */
	public String toString() {
		StringBuffer result = new StringBuffer("<");
		boolean first = true;
		for (E e : this) {
			if (first) {
				result.append(e);
				first = false;
			} else
				result.append(", " + e);
				
		}
		result.append("<");
		return result.toString();
	}

	/**
	 * A faithful copy of this queue. Clones the backbone (the array) of the queue,
	 * but not its elements.
	 */

	public ArrayQueue<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			ArrayQueue<E> result = (ArrayQueue<E>) super.clone();
			result.q = q.clone();
			return result;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	/**
	 * Is this queue equal to a given object?
	 * 
	 * @param other The object.
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		return this == other || other instanceof ArrayQueue && equalQueues((ArrayQueue<E>) other);
	}

	/**
	 * Are two queues equals?
	 * 
	 * @param other The other queue.
	 */
	private boolean equalQueues(ArrayQueue<E> other) {
		if (this.size != other.size)
			return false;
		int i = this.head, j = other.head;
		for (int k = 0; k < size; k++) {
			if (!equalReferences(this.q[i], other.q[j]))
				return false;
			i = (i + 1) % this.q.length;
			j = (j + 1) % other.q.length;
		}
		return true;
	}

	/**
	 * Are two references equal? Takes into consideration the case when both
	 * references are null.
	 * 
	 * @param one   One reference.
	 * @param other The other reference.
	 */
	private static boolean equalReferences(Object one, Object other) {
		return one == null ? other == null : one.equals(other);
	}

	/**
	 * A variant of equalQueues independent of the representation (uses public
	 * methods only).
	 * 
	 * @param other The other queue.
	 */
	@SuppressWarnings("unused")
	private boolean equalQueues_(ArrayQueue<E> other) {
		ArrayQueue<E> first = (ArrayQueue<E>) this.clone(), second = (ArrayQueue<E>) other.clone();
		while (!first.isEmpty() && !second.isEmpty()) {
			if (!equalReferences(first.front(), second.front()))
				return false;
			first.dequeue();
			second.dequeue();
		}
		return first.isEmpty() && second.isEmpty();
	}

	public Iterator<E> iterator() {
		return new Iter();
	}

	/**
	 * Inner class to implement the Iterator<E> interface.
	 */

	private class Iter implements Iterator<E> {
		// Data Fields

		// Index of next element
		private int index;
		// Count of elements accessed so far */
		private int count = 0;

		// Constructor
		/**
		 * Initializes the Iter object to reference the first queue element.
		 */
		public Iter() {
			index = head;
		}

		/**
		 * Returns true if there are more elements in the queue to iterate.
		 * 
		 * @return true if there are more elements in the queue to iterate.
		 */
		@Override
		public boolean hasNext() {
			return count < size;
		}

		/**
		 * Returns the next element in the iteration of the queue.
		 * 
		 * @return The next element in the iteration
		 * @throws NoSuchElementException - if the iteration has no more elements
		 */
		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			E returnValue = q[index];
			index = inc(index);
			count++;
			return returnValue;
		}

		@Override
		public void remove() {
		}

	}
}