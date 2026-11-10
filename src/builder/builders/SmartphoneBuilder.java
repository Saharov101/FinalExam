package builder.builders;

import java.math.BigDecimal;

/**
 * Builder pattern - Concrete Builder for smartphones.
 */
public class SmartphoneBuilder extends ProductBuilder<SmartphoneBuilder> {

    private static final int STANDARD_CAMERA_MP = 50;
    private static final String PRO_CAMERA_COST = "120.00";
    private static final String DUAL_SIM_COST = "20.00";

    public SmartphoneBuilder camera(int megapixels) {
        BigDecimal cost = megapixels > STANDARD_CAMERA_MP ? new BigDecimal(PRO_CAMERA_COST) : BigDecimal.ZERO;
        return option("Camera", megapixels + " MP", cost);
    }

    public SmartphoneBuilder dualSim() {
        return option("SIM", "Dual SIM", new BigDecimal(DUAL_SIM_COST));
    }

    @Override
    protected String category() {
        return "Smartphone";
    }

    @Override
    protected BigDecimal storageCost(int gigabytes) {
        return stepCost(gigabytes, 128, 128, "60.00");
    }

    @Override
    protected SmartphoneBuilder self() {
        return this;
    }
}
