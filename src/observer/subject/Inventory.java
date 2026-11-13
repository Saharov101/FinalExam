package observer.subject;

import builder.product.Product;
import observer.observers.InventoryObserver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Observer pattern - Concrete Subject.
 * Owns the product catalog and stock levels, and publishes every price or stock change in real time.
 */
public class Inventory implements InventorySubject {

    private final Map<String, Product> products = new LinkedHashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();
    private final List<InventoryObserver> observers = new CopyOnWriteArrayList<>();

    @Override
    public void attach(InventoryObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(InventoryObserver observer) {
        observers.remove(observer);
    }

    public void addProduct(Product product, int quantity) {
        requireNonNegative(quantity);
        products.put(product.getSku(), product);
        stock.put(product.getSku(), quantity);
    }

    public Product findProduct(String sku) {
        Product product = products.get(sku);
        if (product == null) {
            throw new IllegalArgumentException("Unknown product: " + sku);
        }
        return product;
    }

    public int getStock(String sku) {
        findProduct(sku);
        return stock.get(sku);
    }

    public boolean isAvailable(String sku, int quantity) {
        return getStock(sku) >= quantity;
    }

    public void restock(String sku, int quantity) {
        requirePositive(quantity);
        setStock(sku, getStock(sku) + quantity);
    }

    public void reserve(String sku, int quantity) {
        requirePositive(quantity);
        if (!isAvailable(sku, quantity)) {
            throw new IllegalStateException("Not enough stock for " + sku + ": requested " + quantity
                    + ", available " + getStock(sku));
        }
        setStock(sku, getStock(sku) - quantity);
    }

    public void updatePrice(String sku, BigDecimal newPrice) {
        Product product = findProduct(sku);
        BigDecimal oldPrice = product.getPrice();
        product.changePrice(newPrice);
        observers.forEach(observer -> observer.onPriceChanged(product, oldPrice, product.getPrice()));
    }

    private void setStock(String sku, int newQuantity) {
        Product product = findProduct(sku);
        int oldQuantity = stock.put(sku, newQuantity);
        observers.forEach(observer -> observer.onStockChanged(product, oldQuantity, newQuantity));
    }

    private static void requirePositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + quantity);
        }
    }

    private static void requireNonNegative(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must not be negative: " + quantity);
        }
    }
}
