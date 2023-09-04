/**
 * Class whose instances represent warehouses, groups of articles.
 * @author Miguel Nunes fc56338
 */
public class Warehouse {
	
	/**
	 * Defines the end-of-line character, independent of operating system.
	 */
	private static final String NEXT_LINE = System.getProperty("line.separator");
	
	private String prefix;
	private Article[] articles;
	private int current;
	
	/**
	 * Constructs new a warehouse, with the given parameters.
	 * @param maximum Maximum number of unique articles the warehouse can have in catalog.
	 * @param prefix Common prefix of all of the warehouse's boxes.
	 * @requires maximum > 0 && prefix != null && prefix != ""
	 */
	Warehouse(int maximum, String prefix) {
		articles = new Article[maximum];
		this.prefix = prefix;
		current = 0;
	}
	
	/**
	 * Verifies if an article with a code equal to the one given is in catalog. Article's will still be considered in
	 * catalog even if they do not have any stock available.
	 * @param code The codeword to search for.
	 * @requires code != null;
	 * @return 'true' if in catalog, 'false' otherwise.
	 */
	boolean inCatalog(String code) {
		boolean answer = false;
		int i = 0;
		while (i < current && !answer) {
			if (articles[i].code().equals(code)) {
				answer = true;
			}
			i++;
		}
		return answer;
	}
	
	/**
	 * Constructs a new Article with the given parameters and adds it to the Warehouse.
	 * @param code The article's codeword, articles will be requested in orders using this code.
	 * @param description The article's description, what the article is.
	 * @param volume The article's volume.
	 * @param weight The article's weight.
	 * @param available The amount of this article available in stock.
	 * @requires !inCatalog(code) && code != "" && code != null && description != null && volume > 0 && weight > 0 &&
	 * 			 available >= 0
	 */
	void addArticle(String code, String description, double volume, double weight, int available) {
		articles[current] = new Article(code, description, volume, weight, available);
		current++;
	}
	
	/**
	 * Returns an array containing the volume and weight of the article whose codeword equals 'code'.
	 * @param code The codeword of the article whose weight and volume to return.
	 * @requires inCatalog(code)
	 * @return Index 0 of the array contains the volume, index 1 contains the weight.
	 */
	double[] articleVolumeWeight(String code) {
		double[] answer = {-1, -1};
		int i = 0;
		while (answer[0] == -1) {
			if (articles[i].code().equals(code)) {
				answer[0] = articles[i].volume();
				answer[1] = articles[i].weight();
			}
			i++;
		}
		return answer;
	}
	
	/**
	 * Determines if the order defined by 'description' is valid, and if not returns what the error is.
	 * @param description The String that defines the order. Articles are referred to by their codewords, box covers by
	 * 					  their codewords reversed.
	 * @requires description != null
	 * @return OrderStatus.VALID_ORDER - Order is valid; </br>
	 * 		   OrderStatus.NO_BOX_START - The first article is not a box; </br>
	 * 		   OrderStatus.NO_AVAILABLE_STOCK - Order requires more stock than is available; </br>
	 * 		   OrderStatus.ORDER_VOLUME_EXCEEDS_BOX - The total volume of the articles to put in a box exceeds the
	 * 												  box's maximum volume; </br>
	 * 		   OrderStatus.MISMATCHED_BOX_COVER - A codeword does not represent an article nor the latest box's cover;
	 * 											  </br>
	 * 		   OrderStatus.UNCLOSED_BOX - At least one box is not closed by the end of the order.
	 */
	OrderStatus validOrder(String description) {
		OrderStatus answer = OrderStatus.VALID_ORDER;
		Warehouse warehouse = copy();
		String[] order = description.split(" ");
		if (order.length == 0 || !(warehouse.inCatalog(order[0]) && warehouse.isBox(order[0]))) {
			answer = OrderStatus.NO_BOX_START;
		}
		ArrayStack<String> boxes = new ArrayStack<>();
		ArrayStack<Double> volume = new ArrayStack<>();
		int i = 0;
		while (answer == OrderStatus.VALID_ORDER && i < order.length) {
			if (warehouse.inCatalog(order[i])) {
				Article article = warehouse.articleReference(order[i]);
				if (article.available() > 0 && warehouse.isBox(article.code())) {
					article.withdraw(1);
					if (!volume.isEmpty()) {
						double temp = volume.peek();
						volume.pop();
						volume.push(temp - article.volume());
						if (volume.peek() < 0) {
							answer = OrderStatus.ORDER_VOLUME_EXCEEDS_BOX;
						}
					}
					boxes.push(article.code());
					volume.push(article.volume());
				}
				else if (article.available() > 0 && !warehouse.isBox(article.code())) {
					if (!volume.isEmpty()) {
						article.withdraw(1);
						double temp = volume.peek();
						volume.pop();
						volume.push(temp - article.volume());
						if (volume.peek() < 0) {
							answer = OrderStatus.ORDER_VOLUME_EXCEEDS_BOX;
						}
					}
					else {
						answer = OrderStatus.ORDER_VOLUME_EXCEEDS_BOX;
					}
				}
				else {
					answer = OrderStatus.NO_AVAILABLE_STOCK;
				}
			}
			else if (!boxes.isEmpty() && boxes.peek().equals(new StringBuilder(order[i]).reverse().toString())) {
				boxes.pop();
				volume.pop();
			}
			else {
				answer = OrderStatus.MISMATCHED_BOX_COVER;
			}
			i++;
		}
		if (!boxes.isEmpty() && answer == OrderStatus.VALID_ORDER) {
			answer = OrderStatus.UNCLOSED_BOX;
		}
		return answer;
	}
	
	/**
	 * Returns the article whose codewords equals 'code'.
	 * @param code The codeword to search for.
	 * @requires inCatalog(code)
	 * @return Article whose codewords equals 'code'.
	 */
	private Article articleReference(String code) {
		Article reference = null;
		int i = 0;
		while (i < current && reference == null) {
			if (code.equals(articles[i].code())) {
				reference = articles[i];
			}
			i++;
		}
		return reference;
	}
	
	/**
	 * Determines if an article is a box. An article is a box if its code has the warehouse's defined prefix.
	 * Does not mean the article is in the warehouse's catalog.
	 * @param code Code of the article to determine.
	 * @requires code != null
	 * @return 'true' if box, 'false' otherwise.
	 */
	private boolean isBox(String code) {
		boolean answer;
		if (code.length() >= prefix.length() && prefix.equals(code.substring(0, prefix.length())))	{
			answer = true;
		}
		else {
			answer = false;
		}
		return answer;
	}
	
	/**
	 * Withdraws from stock all the articles whose codes are in 'description', the number of times the code appears.
	 * @param description The order.
	 * @requires validOrder(description)
	 * @return String representing the instructions defined in the order.
	 */
	String putOrder(String description) {
		StringBuilder myBuilder = new StringBuilder("");
		String[] order = description.split(" ");
		ArrayStack<Article> boxes = new ArrayStack<>();
		for (int i = 0; i < order.length; i++) {
			if (inCatalog(order[i])) {
				Article article = articleReference(order[i]);
				article.withdraw(1);
				if (isBox(order[i])) {
					if (boxes.isEmpty()) {
						myBuilder.append("Go fetch " + article.description() + " and put it on the table" + NEXT_LINE);
					}
					else {
						myBuilder.append("Go fetch " + article.description() + " and put it in "
								 + boxes.peek().description() + NEXT_LINE);
					}
					boxes.push(article);
				}
				else {
					myBuilder.append("Go fetch " + article.description() + " and put it in "
									 + boxes.peek().description() + NEXT_LINE);
				}
			}
			else {
				myBuilder.append("Go fetch the cover of " + boxes.peek().description() + " and close it" + NEXT_LINE);
				boxes.pop();
			}
		}
		return myBuilder.toString();
	}
	
	/**
	 * Returns an array containing the volume and weight of the order defined by 'description'.
	 * @param description The order whose weight and volume to return.
	 * @requires validOrder(description) == OrderStatus.VALID_ORDER
	 * @return Index 0 of the array contains the volume, index 1 contains the weight.
	 */
	double[] orderVolumeWeight(String description) {
		double[] answer = {0.0, 0.0};
		String[] order = description.split(" ");
		ArrayStack<Article> boxes = new ArrayStack<>();
		for (int i = 0; i < order.length; i++) {
			if (inCatalog(order[i])) {
				Article article = articleReference(order[i]);
				if (isBox(order[i])) {
					if (boxes.isEmpty()) {
						answer[0] += article.volume();
					}
					boxes.push(article);
				}
				answer[1] += article.weight();
			}
			else {
				boxes.pop();
			}
		}
		return answer;
	}
	
	/**
	 * Makes an exact copy of the warehouse, including copies of the articles inside.
	 * @return Copy of the warehouse.
	 */
	Warehouse copy() {
		Warehouse copy = new Warehouse(articles.length, prefix);
		for (int i = 0; i < current; i++) {
			copy.addArticle(articles[i].code(), articles[i].description(), articles[i].volume(), articles[i].weight(),
						    articles[i].available());
		}
		return copy;
	}
	
}
