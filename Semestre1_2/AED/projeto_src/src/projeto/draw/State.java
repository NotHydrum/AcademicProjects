package projeto.draw;

import java.util.*;

public class State {

	private Stack<Double> theStack; // the stack itself
	private double x, y;            // the current point at drawing window
	private boolean fill;           // will the shapes be filled?

	/**
	 * The stack constructor
	 * @ensures isEmpty()
	 */
	public State() {
		theStack = new Stack<Double>();
		x=y=0;
		fill=false;
	}

	/**
	 * Check if the stack is empty
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return theStack.isEmpty();
	}

	/**
	 * Adds a new value to the top of the stack
	 * @param value The new top
	 * @ensures !isEmpty()
	 * @ensures peek() == value
	 */
	public void push(Double value) {
		theStack.push(value);
	}

	/**
	 * Removes the top of the stack
	 * @return The value removed (ie, the old stack's top)
	 * @requires !isEmpty()
	 */
	public Double pop() {
		return theStack.pop();
	}

	/**
	 * Returns the top of the stack
	 * @return the top of the stack
	 * @requires !isEmpty()
	 */
	public Double peek() {
		return theStack.peek();
	}

	/**
	 * @return the size of the stack
	 */
	public int size() {
		return theStack.size();
	}

	/**
	 * Clears the content of the stack
	 * @ensures isEmpty()
	 */
	public void reset() {
		theStack.clear();
	}

	/////////////// getters and setters ///////////////
	
	public double getX() {
	  return x;
	}
  
	public double getY() {
	  return y;
	}
	
	public void setX(double x) {
	  this.x=x;
	}
  
	public void setY(double y) {
	  this.y=y;
	}
	
  public void setXY(double x, double y) {
    this.x=x; 
    this.y=y;
  }
    
  public boolean isFill() {
    return fill;
  }
  
  public void setFill(boolean fill) {
    this.fill=fill;
  }
    
  ///////////////////////////////////////////////////
  
	/**
	 * @return a textual representation of the stack's state
	 */
	public String toString() {
		return theStack.toString() + "\n" + "x=" + x + ", y=" + y;
	}
}