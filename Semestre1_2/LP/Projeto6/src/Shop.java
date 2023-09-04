/**
 * Represents a shop for which you can obtain the price of an item, and the cheapest item in the shop.
 *
 * @author LabP team
 *
 */
public interface Shop {

    /**
     * Returns the price of the item on the shop.
     *
     * @param item element which price is to be checked
     * @return the price of the item
     * @requires item != null
     */
    int priceOf(Item item);

    /**
     * Returns the cheapest item being sold on the shop. If there are several items being sold at the cheapest price, no
     * item is the cheapest, so null is returned.
     *
     * @return the cheapest item being sold, or null if there is no single cheapest item
     */
    Item cheapestItem();

    @Override
    String toString();

}