package decorator.decorators;

import decorator.component.CartItem;

import java.math.BigDecimal;

/**
 * Decorator pattern - Concrete Decorator: gift wrapping, charged per unit.
 */
public class GiftWrapDecorator extends CartItemDecorator {

    private static final BigDecimal PRICE_PER_UNIT = new BigDecimal("5.00");

    public GiftWrapDecorator(CartItem wrapped) {
        super(wrapped);
    }

    @Override
    protected BigDecimal extraCost() {
        return PRICE_PER_UNIT.multiply(BigDecimal.valueOf(getQuantity()));
    }

    @Override
    protected String extraDescription() {
        return "Gift wrap";
    }
}
