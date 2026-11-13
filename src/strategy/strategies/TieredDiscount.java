package strategy.strategies;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Strategy pattern - Concrete Strategy: the bigger the order, the bigger the percentage.
 * Reuses {@link PercentageDiscount} for the calculation itself (DRY).
 */
public class TieredDiscount implements DiscountStrategy {

    private final NavigableMap<BigDecimal, Integer> percentByThreshold = new TreeMap<>();

    public TieredDiscount() {
        percentByThreshold.put(new BigDecimal("500"), 5);
        percentByThreshold.put(new BigDecimal("1000"), 10);
        percentByThreshold.put(new BigDecimal("2000"), 15);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        Map.Entry<BigDecimal, Integer> tier = percentByThreshold.floorEntry(subtotal);
        if (tier == null) {
            return BigDecimal.ZERO;
        }
        return new PercentageDiscount(tier.getValue()).calculateDiscount(subtotal);
    }

    @Override
    public String description() {
        return "Tiered discount " + percentByThreshold;
    }
}
