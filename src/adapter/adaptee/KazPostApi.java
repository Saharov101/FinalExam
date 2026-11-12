package adapter.adaptee;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Adapter pattern - Adaptee.
 * Simulated local postal service API: prices in tenge, numeric parcel ids, city and street passed separately.
 */
public class KazPostApi {

    private static final long LOCAL_TARIFF_KZT = 2_000;
    private static final long INTERCITY_TARIFF_KZT = 3_500;
    private static final String HOME_CITY = "Astana";

    public long tariffInTenge(String city, int parcelCount) {
        long tariff = HOME_CITY.equalsIgnoreCase(city) ? LOCAL_TARIFF_KZT : INTERCITY_TARIFF_KZT;
        return tariff * parcelCount;
    }

    public long registerParcel(String city, String street, int parcelCount) {
        long parcelId = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        System.out.printf("[KAZPOST API] Parcel #%d registered: %s, %s (%d pcs)%n", parcelId, city, street, parcelCount);
        return parcelId;
    }
}
