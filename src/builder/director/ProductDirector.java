package builder.director;

import builder.builders.LaptopBuilder;
import builder.builders.SmartphoneBuilder;
import builder.product.Product;

/**
 * Builder pattern - Director.
 * Knows the construction steps of the store's ready-made configurations.
 */
public class ProductDirector {

    public Product constructGamingLaptop(LaptopBuilder builder) {
        return builder.sku("LAP-GAME-01")
                .name("Gaming Laptop")
                .basePrice("1200.00")
                .processor("Intel Core i9")
                .ram(32)
                .storage(1024)
                .gpu("NVIDIA RTX 4070")
                .color("Black")
                .build();
    }

    public Product constructOfficeLaptop(LaptopBuilder builder) {
        return builder.sku("LAP-OFF-01")
                .name("Office Laptop")
                .basePrice("650.00")
                .processor("Intel Core i5")
                .ram(16)
                .storage(512)
                .color("Silver")
                .build();
    }

    public Product constructFlagshipPhone(SmartphoneBuilder builder) {
        return builder.sku("PHN-PRO-01")
                .name("Flagship Phone")
                .basePrice("900.00")
                .storage(256)
                .camera(200)
                .dualSim()
                .color("Titanium")
                .build();
    }
}
