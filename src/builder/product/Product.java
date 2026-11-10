package builder.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder pattern - Product.
 * A configurable store product. Its configuration is immutable once built;
 * only the catalog price can change (e.g. a sale), which is published by the Inventory.
 */
public class Product {

    private final String sku;
    private final String name;
    private final String category;
    private final Map<String, String> specifications;
    private BigDecimal price;

    public Product(String sku, String name, String category,
                   Map<String, String> specifications, BigDecimal price) {
        this.sku = requireText(sku, "SKU");
        this.name = requireText(name, "Name");
        this.category = requireText(category, "Category");
        this.specifications = Collections.unmodifiableMap(new LinkedHashMap<>(specifications));
        this.price = normalize(price);
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void changePrice(BigDecimal newPrice) {
        this.price = normalize(newPrice);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    private static BigDecimal normalize(BigDecimal price) {
        Objects.requireNonNull(price, "Price must not be null");
        if (price.signum() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return String.format("%s [%s, %s] $%s %s", name, sku, category, price, specifications);
    }
}
