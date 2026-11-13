package strategy.context;

import java.math.BigDecimal;

/**
 * Result produced by the Strategy context: every component of the final price.
 */
public record PriceBreakdown(BigDecimal subtotal,
                             BigDecimal discount,
                             BigDecimal tax,
                             BigDecimal total,
                             String discountDescription) {
}
