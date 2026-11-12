package decorator.decorators;

import builder.product.Product;
import decorator.component.CartItem;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Decorator pattern - base Decorator.
 * Delegates everything to the wrapped item; subclasses only describe their own extra cost (DRY).
 */
public abstract class CartItemDecorator implements CartItem {

    private final CartItem wrapped;

    protected CartItemDecorator(CartItem wrapped) {
        this.wrapped = Objects.requireNonNull(wrapped, "Wrapped item must not be null");
    }

    @Override
    public Product getProduct() {
        return wrapped.getProduct();
    }

    @Override
    public int getQuantity() {
        return wrapped.getQuantity();
    }

    @Override
    public BigDecimal getPrice() {
        return wrapped.getPrice().add(extraCost());
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + " + extraDescription();
    }

    /** Price of the wrapped item without this decorator. */
    protected BigDecimal wrappedPrice() {
        return wrapped.getPrice();
    }

    protected abstract BigDecimal extraCost();

    protected abstract String extraDescription();
}
