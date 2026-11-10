package builder.builders;

import java.math.BigDecimal;

/**
 * Builder pattern - Concrete Builder for laptops.
 */
public class LaptopBuilder extends ProductBuilder<LaptopBuilder> {

    private static final String GPU_COST = "350.00";

    public LaptopBuilder processor(String model) {
        return option("CPU", model, BigDecimal.ZERO);
    }

    public LaptopBuilder ram(int gigabytes) {
        return option("RAM", gigabytes + " GB", stepCost(gigabytes, 8, 8, "40.00"));
    }

    public LaptopBuilder gpu(String model) {
        return option("GPU", model, new BigDecimal(GPU_COST));
    }

    @Override
    protected String category() {
        return "Laptop";
    }

    @Override
    protected BigDecimal storageCost(int gigabytes) {
        return stepCost(gigabytes, 256, 256, "50.00");
    }

    @Override
    protected LaptopBuilder self() {
        return this;
    }
}
