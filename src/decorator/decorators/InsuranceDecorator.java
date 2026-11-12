package decorator.decorators;

import decorator.component.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decorator pattern - Concrete Decorator: damage/theft insurance, 3% of everything wrapped so far.
 * Because it is based on the wrapped price, the decorator order matters (insurance covers previous add-ons too).
 */
public class InsuranceDecorator extends CartItemDecorator {

    private static final BigDecimal RATE = new BigDecimal("0.03");

    public InsuranceDecorator(CartItem wrapped) {
        super(wrapped);
    }

    @Override
    protected BigDecimal extraCost() {
        return wrappedPrice().multiply(RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    protected String extraDescription() {
        return "Insurance";
    }
}
