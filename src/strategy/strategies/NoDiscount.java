package strategy.strategies;

import java.math.BigDecimal;

/**
 * Strategy pattern - Concrete Strategy (default, "null object").
 */
public class NoDiscount implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        return BigDecimal.ZERO;
    }

    @Override
    public String description() {
        return "No discount";
    }
}
