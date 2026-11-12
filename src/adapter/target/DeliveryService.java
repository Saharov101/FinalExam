package adapter.target;

import java.math.BigDecimal;

/**
 * Adapter pattern - Target interface.
 * The only delivery contract the shop knows about; every carrier is plugged in through an adapter.
 */
public interface DeliveryService {

    String carrierName();

    /** Delivery cost in USD. */
    BigDecimal calculateCost(String city, int parcels);

    /** Books the delivery and returns a tracking number. */
    String ship(String city, String address, int parcels);
}
