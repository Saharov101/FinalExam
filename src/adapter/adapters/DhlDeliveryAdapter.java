package adapter.adapters;

import adapter.adaptee.DhlExpressApi;
import adapter.target.DeliveryService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Adapter pattern - Object Adapter.
 * Converts parcels to kilograms and euros to dollars so DHL fits the {@link DeliveryService} contract.
 */
public class DhlDeliveryAdapter implements DeliveryService {

    private static final double AVERAGE_PARCEL_KG = 2.0;
    private static final BigDecimal USD_PER_EUR = new BigDecimal("1.08");

    private final DhlExpressApi dhlApi;

    public DhlDeliveryAdapter(DhlExpressApi dhlApi) {
        this.dhlApi = Objects.requireNonNull(dhlApi);
    }

    @Override
    public String carrierName() {
        return "DHL Express";
    }

    @Override
    public BigDecimal calculateCost(String city, int parcels) {
        double rateEur = dhlApi.getRateEur(city, toKilograms(parcels));
        return BigDecimal.valueOf(rateEur).multiply(USD_PER_EUR).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String ship(String city, String address, int parcels) {
        return dhlApi.bookShipment(address + ", " + city, toKilograms(parcels));
    }

    private static double toKilograms(int parcels) {
        return parcels * AVERAGE_PARCEL_KG;
    }
}
