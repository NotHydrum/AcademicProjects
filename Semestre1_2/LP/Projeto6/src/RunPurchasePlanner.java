import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RunPurchasePlanner {
    public static void main(String[] args) throws FileNotFoundException {
	PurchasePlanner planner = new PurchasePlanner();

	int numItems = 10;
	Random rand = new Random(42);

	for (int i = 0; i < numItems; i++) {
	    planner.addToOrder(Item.getRandomItem(rand), 1 + rand.nextInt(10));
	}

	Item banana = new Item("banana", false);
	planner.addToOrder(banana, 200);

	System.out.println(planner);

	Supermarket cheapMarket = new Supermarket("Aldee", 1);
	Supermarket midMarket = new Supermarket("Pingoo", 2);
	Supermarket expensiveMarket = new Supermarket("Super", 20);

	// markets is a List of Shop, so that we can use it in the PurchasePlanner methods
	List<Shop> markets = new ArrayList<>();
	markets.add(cheapMarket);
	markets.add(midMarket);
	markets.add(expensiveMarket);

	midMarket.setPriceOf(banana, 1);
	expensiveMarket.setPriceOf(banana, 1);

	for (Shop market : markets) {
	    System.out.println("Order costs " + planner.priceInMarket(market) + " at " + market);
	}

	System.out.println("Cheapest: " + planner.cheapestMarket(markets));
	System.out.println("Generally cheap item: " + planner.mostlyCheaper(markets));

    }
}
