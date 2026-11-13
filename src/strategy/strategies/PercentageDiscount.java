package strategy.strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Strategy pattern - Concrete Strategy: a fixed percentage off the subtotal (e.g. promo code).
 */
public class PercentageDiscount implements DiscountStrategy {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final int percent;

    public PercentageDiscount(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100: " + percent);
        }
        this.percent = percent;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        return subtotal.multiply(BigDecimal.valueOf(percent)).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    @Override
    public String description() {
        return percent + "% off";
    }
}
