package strategy.strategies;

import java.math.BigDecimal;

/**
 * Strategy pattern - Concrete Strategy: a coupon worth a fixed amount, valid above a minimum order value.
 */
public class FixedAmountDiscount implements DiscountStrategy {

    private final BigDecimal amount;
    private final BigDecimal minimumSubtotal;

    public FixedAmountDiscount(String amount, String minimumSubtotal) {
        this.amount = new BigDecimal(amount);
        this.minimumSubtotal = new BigDecimal(minimumSubtotal);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        if (subtotal.compareTo(minimumSubtotal) < 0) {
            return BigDecimal.ZERO;
        }
        return amount.min(subtotal);
    }

    @Override
    public String description() {
        return "$" + amount + " coupon (orders over $" + minimumSubtotal + ")";
    }
}
