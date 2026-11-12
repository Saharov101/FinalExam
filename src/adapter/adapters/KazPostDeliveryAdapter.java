package adapter.adapters;

import adapter.adaptee.KazPostApi;
import adapter.target.DeliveryService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Adapter pattern - Object Adapter.
 * Converts tenge to dollars and numeric parcel ids to tracking strings.
 */
public class KazPostDeliveryAdapter implements DeliveryService {

    private static final BigDecimal KZT_PER_USD = new BigDecimal("500");

    private final KazPostApi kazPostApi;

    public KazPostDeliveryAdapter(KazPostApi kazPostApi) {
        this.kazPostApi = Objects.requireNonNull(kazPostApi);
    }

    @Override
    public String carrierName() {
        return "KazPost";
    }

    @Override
    public BigDecimal calculateCost(String city, int parcels) {
        long tenge = kazPostApi.tariffInTenge(city, parcels);
        return BigDecimal.valueOf(tenge).divide(KZT_PER_USD, 2, RoundingMode.HALF_UP);
    }

    @Override
    public String ship(String city, String address, int parcels) {
        return "KZ" + kazPostApi.registerParcel(city, address, parcels);
    }
}
