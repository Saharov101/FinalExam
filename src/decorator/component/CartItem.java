package decorator.component;

import builder.product.Product;

import java.math.BigDecimal;

/**
 * Decorator pattern - Component interface.
 * A line in the shopping cart; add-on services can be stacked on top of it at runtime.
 */
public interface CartItem {

    Product getProduct();

    int getQuantity();

    BigDecimal getPrice();

    String getDescription();
}
