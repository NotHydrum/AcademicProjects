/**
 * Class whose instances represent Item orders.
 * @author Miguel Nunes fc56338
 */
public class ItemOrder {
	
	/**
	 * The Item to order.
	 */
	private Item item;
	/**
	 * The quantity of the Item to order.
	 */
	private int quantity;
	
	/**
	 * Builds a new ItemOrder.
	 * @param item The Item to order.
	 * @param quantity The quantity of the Item to order.
	 * @requires item != null && quantity > 0
	 */
	public ItemOrder(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}
	
	/**
	 * @return The Item to order.
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * @return The quantity of the Item to order.
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * @return Textual representation of this Item order.
	 */
	@Override
	public String toString() {
		return quantity + ":{" + getItem().toString() + "}";
	}
	
}
