package decorator.component;

import builder.product.Product;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Decorator pattern - Concrete Component.
 * The price is read from the product on every call, so catalog price changes are reflected immediately.
 */
public class BasicCartItem implements CartItem {

    private final Product product;
    private final int quantity;

    public BasicCartItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + quantity);
        }
        this.product = Objects.requireNonNull(product, "Product must not be null");
        this.quantity = quantity;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public BigDecimal getPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String getDescription() {
        return quantity + " x " + product.getName();
    }
}
