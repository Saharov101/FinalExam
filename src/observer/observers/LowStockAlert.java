package observer.observers;

import builder.product.Product;

/**
 * Observer pattern - Concrete Observer.
 * Warns the warehouse manager when a product is running out.
 */
public class LowStockAlert implements InventoryObserver {

    private final int threshold;

    public LowStockAlert(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void onStockChanged(Product product, int oldQuantity, int newQuantity) {
        boolean decreased = newQuantity < oldQuantity;
        if (!decreased || newQuantity > threshold) {
            return;
        }
        if (newQuantity == 0) {
            System.out.printf("[WAREHOUSE] OUT OF STOCK: %s - reorder immediately!%n", product.getName());
        } else {
            System.out.printf("[WAREHOUSE] Low stock: %s - only %d left%n", product.getName(), newQuantity);
        }
    }
}
