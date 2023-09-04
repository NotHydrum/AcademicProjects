/**
 * Class whose instances represent articles.
 * @author Miguel Nunes fc56338
 */
public class Article {
	
	private String code;
	private String description;
	private double volume;
	private double weight;
	private int available;
	
	/**
	 * Constructs a new Article with the given parameters.
	 * @param code The article's codeword, articles will be requested in orders using this code.
	 * @param description The article's description, what the article is.
	 * @param volume The article's volume.
	 * @param weight The article's weight.
	 * @param available The amount of this article available in stock.
	 * @requires code != "" && code != null && description != null && volume > 0 && weight > 0 && available >= 0
	 */
	Article(String code, String description, double volume, double weight, int available) {
		this.code = code;
		this.description = description;
		this.volume = volume;
		this.weight = weight;
		this.available = available;
	}
	
	/**
	 * @return The article's codeword.
	 */
	String code() {
		return code;
	}
	
	/**
	 * @return The article's description.
	 */
	String description() {
		return description;
	}
	
	/**
	 * @return The article's volume.
	 */
	double volume() {
		return volume;
	}
	
	/**
	 * @return The article's weight.
	 */
	double weight() {
		return weight;
	}
	
	/**
	 * @return The amount of this article available in stock.
	 */
	int available() {
		return available;
	}
	
	/**
	 * Withdraws a number of this article from its stock.
	 * @param number Number to withdraw.
	 * @requires number <= available
	 */
	void withdraw(int number) {
		available -= number;
	}
	
	/**
	 * Creates an exact copy of this article.
	 * @return Article's copy.
	 */
	Article copy() {
		return new Article(code, description, volume, weight, available);
	}
	
}
