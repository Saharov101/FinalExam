package decorator.decorators;

import decorator.component.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decorator pattern - Concrete Decorator: extended warranty, 8% of the product price per extra year.
 */
public class ExtendedWarrantyDecorator extends CartItemDecorator {

    private static final BigDecimal RATE_PER_YEAR = new BigDecimal("0.08");
    private static final int MAX_YEARS = 3;

    private final int years;

    public ExtendedWarrantyDecorator(CartItem wrapped, int years) {
        super(wrapped);
        if (years < 1 || years > MAX_YEARS) {
            throw new IllegalArgumentException("Warranty must be 1-" + MAX_YEARS + " years: " + years);
        }
        this.years = years;
    }

    @Override
    protected BigDecimal extraCost() {
        BigDecimal productsPrice = getProduct().getPrice().multiply(BigDecimal.valueOf(getQuantity()));
        return productsPrice.multiply(RATE_PER_YEAR)
                .multiply(BigDecimal.valueOf(years))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    protected String extraDescription() {
        return years + "-year extended warranty";
    }
}
