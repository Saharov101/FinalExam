package strategy.strategies;

import java.math.BigDecimal;

/**
 * Strategy pattern - Strategy interface.
 * A discount algorithm that returns the discount amount for a given subtotal.
 */
public interface DiscountStrategy {

    BigDecimal calculateDiscount(BigDecimal subtotal);

    String description();
}
