package builder.builders;

import builder.product.Product;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder pattern - abstract Builder.
 * Holds the configuration steps shared by every product type (DRY) and assembles the final {@link Product}.
 * The self-type parameter {@code B} keeps method chaining fluent in subclasses.
 */
public abstract class ProductBuilder<B extends ProductBuilder<B>> {

    private String sku;
    private String name;
    private BigDecimal basePrice = BigDecimal.ZERO;
    private final Map<String, String> specifications = new LinkedHashMap<>();
    private final Map<String, BigDecimal> optionCosts = new LinkedHashMap<>();

    public B sku(String sku) {
        this.sku = sku;
        return self();
    }

    public B name(String name) {
        this.name = name;
        return self();
    }

    public B basePrice(String basePrice) {
        this.basePrice = new BigDecimal(basePrice);
        return self();
    }

    public B color(String color) {
        return option("Color", color, BigDecimal.ZERO);
    }

    public B storage(int gigabytes) {
        return option("Storage", gigabytes + " GB", storageCost(gigabytes));
    }

    public Product build() {
        BigDecimal optionsTotal = optionCosts.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Product(sku, name, category(), specifications, basePrice.add(optionsTotal));
    }

    /** Registers (or replaces) a configuration option together with its extra cost. */
    protected final B option(String key, String value, BigDecimal extraCost) {
        specifications.put(key, value);
        optionCosts.put(key, extraCost);
        return self();
    }

    /** Charges {@code pricePerStep} for every full {@code step} above the {@code included} amount. */
    protected static BigDecimal stepCost(int value, int included, int step, String pricePerStep) {
        if (value <= 0) {
            throw new IllegalArgumentException("Option value must be positive: " + value);
        }
        int extraSteps = Math.max(0, (value - included) / step);
        return new BigDecimal(pricePerStep).multiply(BigDecimal.valueOf(extraSteps));
    }

    protected abstract String category();

    protected abstract BigDecimal storageCost(int gigabytes);

    protected abstract B self();
}
