package strategy.context;

import builder.product.Product;
import decorator.component.CartItem;
import observer.observers.InventoryObserver;
import strategy.strategies.DiscountStrategy;
import strategy.strategies.NoDiscount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Strategy pattern - Context.
 * Delegates the discount calculation to an interchangeable {@link DiscountStrategy}.
 * It is also an Observer of the inventory, so the customer sees catalog price changes in real time.
 */
public class ShoppingCart implements InventoryObserver {

    private final List<CartItem> items = new ArrayList<>();
    private final BigDecimal taxRate;
    private DiscountStrategy discountStrategy = new NoDiscount();

    public ShoppingCart(String taxRate) {
        this.taxRate = new BigDecimal(taxRate);
        if (this.taxRate.signum() < 0) {
            throw new IllegalArgumentException("Tax rate must not be negative");
        }
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = Objects.requireNonNull(discountStrategy, "Use NoDiscount instead of null");
    }

    public void addItem(CartItem item) {
        items.add(Objects.requireNonNull(item));
        System.out.printf("[CART] Added: %s ($%s)%n", item.getDescription(), item.getPrice());
    }

    public void removeItem(String sku) {
        boolean removed = items.removeIf(item -> item.getProduct().getSku().equals(sku));
        if (removed) {
            System.out.printf("[CART] Removed all items with SKU %s%n", sku);
        }
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /** Total quantity per SKU, used to check and reserve stock. */
    public Map<String, Integer> quantitiesBySku() {
        return items.stream().collect(Collectors.groupingBy(
                item -> item.getProduct().getSku(), Collectors.summingInt(CartItem::getQuantity)));
    }

    public int totalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public PriceBreakdown calculateTotal() {
        BigDecimal subtotal = items.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discount = discountStrategy.calculateDiscount(subtotal).min(subtotal)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = taxable.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        return new PriceBreakdown(subtotal, discount, tax, taxable.add(tax), discountStrategy.description());
    }

    @Override
    public void onPriceChanged(Product product, BigDecimal oldPrice, BigDecimal newPrice) {
        if (contains(product)) {
            System.out.printf("[CART] Price of %s in your cart changed: $%s -> $%s. New total: $%s%n",
                    product.getName(), oldPrice, newPrice, calculateTotal().total());
        }
    }

    private boolean contains(Product product) {
        return items.stream().anyMatch(item -> item.getProduct().getSku().equals(product.getSku()));
    }
}
