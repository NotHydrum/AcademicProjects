import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class TestPurchasePlanner {

    @Test
    void testItem() {
	Item coldStrawberry = new Item("strawberry", true);
	Item regularStrawberry = new Item("strawberry", false);
	Item blueberry = new Item("blueberry", false);

	assertNotEquals(coldStrawberry, regularStrawberry);
	assertNotEquals(blueberry, coldStrawberry);

	assertEquals(coldStrawberry.getDescription(), "strawberry");
	assertEquals(coldStrawberry.isRefrigerated(), true);
    }

    @Test
    void testItemOrder() {
	Item coldStrawberry = new Item("strawberry", true);
	int qtty = 20;

	ItemOrder order = new ItemOrder(coldStrawberry, qtty);

	assertEquals(order.getItem(), coldStrawberry);
	assertEquals(order.getQuantity(), qtty);
    }

    @Test
    void testSupermarket() {
	int defaultPrice = 3;
	Supermarket market = new Supermarket("Aldee", defaultPrice);
	Item coldStrawberry = new Item("strawberry", true);

	// all items are the default price, so there is no cheapest item
	assertEquals(market.cheapestItem(), null);
	assertEquals(market.priceOf(coldStrawberry), defaultPrice);

	int newPrice = 1;
	market.setPriceOf(coldStrawberry, newPrice);
	assertEquals(market.priceOf(coldStrawberry), newPrice);

	assertEquals(market.priceOf(coldStrawberry), newPrice);

	// now we have a cheapest item
	assertEquals(market.cheapestItem(), coldStrawberry);
    }

    @Test
    void testPurchasePlanner() {
	Supermarket market1 = new Supermarket("Aldee", 1);
	Supermarket market2 = new Supermarket("LOLDL", 2);

	Item coldStrawberry = new Item("strawberry", true);
	int qtty = 20;

	ItemOrder order = new ItemOrder(coldStrawberry, qtty);

	PurchasePlanner plan = new PurchasePlanner();
	plan.addToOrder(order);

	assertEquals(plan.priceInMarket(market1), qtty);
	assertEquals(plan.priceInMarket(market2), 2 * qtty);

	plan.removeFromOrder(coldStrawberry, 1);
	assertEquals(plan.priceInMarket(market1), qtty - 1);

	plan.removeFromOrder(order);
	assertEquals(plan.priceInMarket(market1), 0);

    }

}
