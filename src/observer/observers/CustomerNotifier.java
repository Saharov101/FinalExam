package observer.observers;

import builder.product.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Observer pattern - Concrete Observer.
 * Sends a customer alerts about price drops and restocks of products on the wishlist.
 */
public class CustomerNotifier implements InventoryObserver {

    private final String customerEmail;
    private final Set<String> wishlist = new HashSet<>();

    public CustomerNotifier(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void watch(String sku) {
        wishlist.add(sku);
    }

    @Override
    public void onPriceChanged(Product product, BigDecimal oldPrice, BigDecimal newPrice) {
        if (isWatched(product) && newPrice.compareTo(oldPrice) < 0) {
            send("Price drop! %s: $%s -> $%s", product.getName(), oldPrice, newPrice);
        }
    }

    @Override
    public void onStockChanged(Product product, int oldQuantity, int newQuantity) {
        if (isWatched(product) && oldQuantity == 0 && newQuantity > 0) {
            send("%s is back in stock (%d available)", product.getName(), newQuantity);
        }
    }

    private boolean isWatched(Product product) {
        return wishlist.contains(product.getSku());
    }

    private void send(String format, Object... args) {
        System.out.printf("[EMAIL to %s] %s%n", customerEmail, String.format(format, args));
    }
}
