
/**
 * An implementation of a Stack using a growing array.
 * 
 * @param <E> The type of the elements in this stack.
 */
public class ArrayStack<E> implements Cloneable, Stack<E> {
	
	/**
	 * The array with the elements in the stack.
	 */
	private E[] theStack;
	
	/**
	 * Index of the top element of the stack in the array.
	 * -1 for an empty stack.
	 */
	private int top;
	
	/**
	 * The initial capacity of the array.
	 */
	private static final int DEFAULT_CAPACITY = 8;
	
	/** 
	 * Creates an empty stack.
	 */
	@SuppressWarnings("unchecked")
	public ArrayStack () {
		theStack = (E []) new Object [DEFAULT_CAPACITY];		
		top = -1;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.stack.Stack#push(E)
	 */
	@Override
	public void push (E e) {
		if (size() == theStack.length)
			grow ();
		top++; 
		theStack[top] = e;
	}

	/* (non-Javadoc)
	 * @see datatypes.stack.Stack#peek()
	 */
	@Override
	public E peek () {
		return theStack[top];
	}
	
	/* (non-Javadoc)
	 * @see datatypes.stack.Stack#pop()
	 */
	@Override
	public void pop () {
		theStack[top] = null;
		top--;  
	}

	/* The number of elements in the stack
	 */
	private int size () {
		return top + 1;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.stack.Stack#isEmpty()
	 */
	@Override
	public boolean isEmpty () {
		return size () == 0;
	}
	

	/** 
	 * Increase the capacity of the array holding the Stack.
	 */
	private void grow () {
		@SuppressWarnings("unchecked")
		E [] newStack = (E[]) new Object [theStack.length * 2];
	 	for (int i = 0; i < theStack.length; i++)
	 		newStack[i] = theStack[i];
	 	theStack = newStack;
	}
	
	/**
	 * The textual representation of the Stack,
	 * in the form "a:b:c:[]",
	 * where "c" is the element at the top of the stack.
	 */
	public String toString () {
		StringBuffer buffer = new StringBuffer ();
		for (int i = 0; i <= top; i++)
			buffer.append(theStack [i] + ":");
		buffer.append("[]");
		return buffer.toString();
	}
	
	/**
	 * A faithful copy of this stack.
	 * Clones the backbone (the array) of the
	 * stack, but not its elements.
	 */
	public Object clone() {
		try {
			@SuppressWarnings("unchecked")
			ArrayStack<E>result = (ArrayStack<E>) super.clone();
			result.theStack = theStack.clone();
			return result;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	/**
	 * Is this stack equal to a given object?
	 * 
	 * @param other
	 *            The object.
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		return
			this == other || other instanceof ArrayStack &&
					equalStacks((ArrayStack<E>) other);
	}

	/**
	 * Are two stacks equals?
	 * 
	 * @param other
	 *            The other queue.
	 */
	private boolean equalStacks(ArrayStack<E> other) {
		if (this.size() != other.size())
			return false;
		for (int k = 0; k < size(); k++) {
			if (! equalReferences(this.theStack[k], other.theStack[k]))
				return false;
		}
		return true;
	}
	
	/**
	 * Are two references equal?
	 * Takes into consideration the case when both references are null.
	 * @param one One reference.
	 * @param other The other reference.
	 */
	private static boolean equalReferences(Object one, Object other) {
		return one == null ? other == null : one.equals(other);
	}
}