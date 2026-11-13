package observer.observers;

import builder.product.Product;

import java.math.BigDecimal;

/**
 * Observer pattern - Observer interface.
 * Default no-op methods let each observer react only to the events it cares about.
 */
public interface InventoryObserver {

    default void onPriceChanged(Product product, BigDecimal oldPrice, BigDecimal newPrice) {
    }

    default void onStockChanged(Product product, int oldQuantity, int newQuantity) {
    }
}
