package projeto.draw;

/**
 * the class that represent function definitions
 * eg: [double:1] $1 $1 *
 * 
 * this is an immutable class
 * 
 * @author jpn3t *
 */
public class Macro {
  
	private String name;        // the function's name 
	private int    arity;       // the function's arity, ie, its number of arguments
	private String description; // the function's expression

	public Macro (String name, int arity, String description) {
		this.name        = name;
		this.arity       = arity;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public int getArity() {
		return arity;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Since the program should not have two functions with the same name,
	 * we consider two functions are equal if they have the same name
	 * If you want to search if there's a function name inside a container
	 * use something like: container.contains(new Macro(name,0,null)) 
	 */
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Macro)
			return this.getName().equals(((Macro)obj).getName());
		else
			return false;
	}
	
	public String toString() {
		return "[" + getName() + ":" + getArity() + "]" + getDescription(); 
	}
}