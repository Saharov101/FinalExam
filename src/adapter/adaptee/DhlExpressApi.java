package adapter.adaptee;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Adapter pattern - Adaptee.
 * Simulated third-party SDK: works with euros, kilograms and a single free-form address line.
 */
public class DhlExpressApi {

    private static final double BASE_RATE_EUR = 15.0;
    private static final double RATE_PER_KG_EUR = 2.5;

    public double getRateEur(String destination, double weightKg) {
        return BASE_RATE_EUR + RATE_PER_KG_EUR * weightKg;
    }

    public String bookShipment(String fullAddress, double weightKg) {
        String awb = "DHL-" + ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        System.out.printf("[DHL API] Shipment %s booked to \"%s\" (%.1f kg)%n", awb, fullAddress, weightKg);
        return awb;
    }
}
