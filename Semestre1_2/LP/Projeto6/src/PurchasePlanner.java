import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Class whose instances represent purchase planners.
 * @author Miguel Nunes fc56338
 */
public class PurchasePlanner {
	
	/**
	 * Defines the end-of-line character, independent of operating system.
	 */
	private static final String NEXT_LINE = System.getProperty("line.separator");
	
	/**
	 * The order.
	 */
	private HashMap<Item, Integer> order;
	
	/**
	 * Builds a new PurchasePlanner.
	 */
	public PurchasePlanner() {
		order = new HashMap<>();
	}
	
	/**
	 * Calls addToOrder(itemOrder.getItem(), itemOrder.getQuantity()) for every ItemOrder contained in 'order'.
	 * @param order List of ItemOrders to add to the order.
	 * @requires order != null
	 */
	public void addToOrder(List<ItemOrder> order) {
		for (ItemOrder i : order) {
			addToOrder(i.getItem(), i.getQuantity());
		}
	}
	
	/**
	 * Calls addToOrder(itemOrder.getItem(), itemOrder.getQuantity()).
	 * @param itemOrder ItemOrder to add to the order.
	 * @requires itemOrder != null
	 */
	public void addToOrder(ItemOrder itemOrder) {
		addToOrder(itemOrder.getItem(), itemOrder.getQuantity());
	}
	
	/**
	 * Adds to the order the item and the respective quantity to order. If the order already contains the item the
	 * given quantity will be added to preexisting quantity.
	 * @param item The Item to add to the order.
	 * @param quantity The quantity of the Item to add to the order.
	 * @requires item != null && quantity > 0
	 */
	public void addToOrder(Item item, int quantity) {
		if (order.containsKey(item)) {
			order.put(item, order.get(item) + quantity);
		}
		else {
			order.put(item, quantity);
		}
	}
	
	/**
	 * Calls removeFromOrder(itemOrder.getItem(), itemOrder.getQuantity()) for every ItemOrder contained in 'order'.
	 * @param order List of ItemOrders to remove from the order.
	 * @requires order != null
	 */
	public void removeFromOrder(List<ItemOrder> order) {
		for (ItemOrder i : order) {
			removeFromOrder(i.getItem(), i.getQuantity());
		}
	}
	
	/**
	 * Calls removeFromOrder(itemOrder.getItem(), itemOrder.getQuantity()).
	 * @param itemOrder ItemOrder to remove from the order.
	 * @requires itemOrder != null
	 */
	public void removeFromOrder(ItemOrder itemOrder) {
		removeFromOrder(itemOrder.getItem(), itemOrder.getQuantity());
	}
	
	/**
	 * Removes from the order 'quantity' of the given Item.
	 * @param item The Item to remove a quantity of from the order.
	 * @param quantity The quantity of the Item to remove from the order.
	 * @requires item != null && quantity > 0
	 */
	public void removeFromOrder(Item item, int quantity) {
		if (order.containsKey(item)) {
			if (order.get(item) > quantity) {
				order.put(item, order.get(item) - quantity);
			}
			else {
				order.remove(item);
			}
		}
	}
	
	/**
	 * Calculates the price of the order in the given shop.
	 * @param market Shop to calculate order from.
	 * @requires market != null
	 * @return Price of the order in the given shop.
	 */
	public int priceInMarket(Shop market) {
		int price = 0;
		for (Entry<Item, Integer> i : order.entrySet()) {
			price += i.getValue() * market.priceOf(i.getKey());
		}
		return price;
	}
	
	/**
	 * Determines the cheapest shop from the given list to buy this order at.
	 * @param markets List of shops to determine the cheapest from.
	 * @requires markets != null && markets.size() > 1
	 * @return The cheapest shop to buy this order at.
	 */
	public Shop cheapestMarket(List<Shop> markets) {
		Iterator<Shop> marketIterator = markets.iterator();
		Shop cheapest = marketIterator.next();
		int price = priceInMarket(cheapest);
		while (marketIterator.hasNext()) {
			Shop nextShop = marketIterator.next();
			int nextPrice = priceInMarket(nextShop);
			if (nextPrice < price) {
				cheapest = nextShop;
				price = nextPrice;
			}
		}
		return cheapest;
	}
	
	/**
	 * Determines if any of the Items part of order is the cheapest in the majority of the shops of 'markets'. 
	 * @param markets List of shops to search through.
	 * @requires markets != null
	 * @return The Item part of the order that is the cheapest in the majority of the shops, or 'null' if none are.
	 */
	public Item mostlyCheaper(List<Shop> markets) {
		HashMap<Item, Integer> cheapestItems = new HashMap<>();
		for (Shop i : markets) {
			Item cheapest = i.cheapestItem();
			if (order.containsKey(cheapest) && cheapestItems.containsKey(cheapest)) {
				cheapestItems.put(cheapest, cheapestItems.get(cheapest) + 1);
			}
			else if (order.containsKey(cheapest)) {
				cheapestItems.put(cheapest, 1);
			}
		}
		Item major = null;
		Iterator<Entry<Item, Integer>> itemIterator = cheapestItems.entrySet().iterator();
		while (itemIterator.hasNext() && major == null) {
			Entry<Item, Integer> next = itemIterator.next();
			if (next.getValue() > markets.size() / 2) {
				major = next.getKey();
			}
		}
		return major;
	}
	
	/**
	 * @return Textual representation of this PurchasePlanner.
	 */
	@Override
	public String toString() {
		StringBuilder myBuilder = new StringBuilder();
		Iterator<Entry<Item, Integer>> orderIterator = order.entrySet().iterator();
		Entry<Item, Integer> next;
		for (int i = 0; i < order.size() - 1; i++) {
			next = orderIterator.next();
			myBuilder.append(next.getValue() + " of {" + next.getKey().toString() + "};" + NEXT_LINE);
		}
		if (order.size() > 0) {
			next = orderIterator.next();
			myBuilder.append(next.getValue() + " of {" + next.getKey().toString() + "}.");
		}
		return myBuilder.toString();
	}
	
}
