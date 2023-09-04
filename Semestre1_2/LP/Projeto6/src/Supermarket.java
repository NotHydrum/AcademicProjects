import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Class whose instances represent supermarkets.
 * @author Miguel Nunes fc56338
 */
public class Supermarket implements Shop {
	
	/**
	 * The supermarket's name.
	 */
	private String name;
	/**
	 * The price of Items whose price has not been manually set.
	 */
	private int omissionPrice;
	/**
	 * The manually-set prices.
	 */
	private HashMap<Item, Integer> prices;
	
	/**
	 * Builds a new Supermarket.
	 * @param name The supermarket's name.
	 * @param omissionPrice The price of Items whose price has not been manually set.
	 * @requires name != null
	 */
	public Supermarket(String name, int omissionPrice) {
		this.name = name;
		this.omissionPrice = omissionPrice;
		prices = new HashMap<>();
	}
	
	/**
	 * @return The supermarket's name.
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @return The price of Items whose price has not been manually set.
	 */
	public int omissionPrice() {
		return omissionPrice;
	}
	
	/**
	 * Sets the price of an Item to the given integer.
	 * @param item Item whose price to set.
	 * @param price New price.
	 * @requires item != null && price > 0
	 */
	public void setPriceOf(Item item, int price) {
		prices.put(item, price);
	}
	
	/**
	 * @requires item != null
	 * @return The Item's price in this supermarket.
	 */
	@Override
	public int priceOf(Item item) {
		Integer price = prices.get(item);
		return price != null ? price : omissionPrice;
	}
	
	/**
	 * Determines the cheapest Item in this supermarket.
	 * @return The cheapest Item in this supermarket, or 'null' if there are multiple Items that can be considered the
	 * cheapest.
	 */
	@Override
	public Item cheapestItem() {
		Iterator<Entry<Item, Integer>> priceIterator = prices.entrySet().iterator();
		Entry<Item, Integer> cheapest = null;
		boolean multiple = false;
		if (priceIterator.hasNext()) {
			cheapest = priceIterator.next();
			while (priceIterator.hasNext()) {
				Entry<Item, Integer> next = priceIterator.next();
				if (next.getValue() < cheapest.getValue()) {
					cheapest = next;
					multiple = false;
				}
				else if (next.getValue().equals(cheapest.getValue())) {
					multiple = true;
				}
			}
		}
		return (cheapest != null && cheapest.getValue() < omissionPrice && !multiple) ? cheapest.getKey() : null;
	}
	
	/**
	 * @return Textual representation of this Supermarket.
	 */
	@Override
	public String toString() {
		return name;
	}

}
