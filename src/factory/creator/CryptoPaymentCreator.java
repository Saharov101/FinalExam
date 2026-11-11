package factory.creator;

import factory.product.CryptoPayment;
import factory.product.Payment;

import java.math.BigDecimal;

/**
 * Factory Method pattern - Concrete Creator.
 */
public class CryptoPaymentCreator extends PaymentCreator {

    private final String walletAddress;
    private final String coin;
    private final BigDecimal usdPerCoin;

    public CryptoPaymentCreator(String walletAddress, String coin, BigDecimal usdPerCoin) {
        this.walletAddress = walletAddress;
        this.coin = coin;
        this.usdPerCoin = usdPerCoin;
    }

    @Override
    protected Payment createPayment() {
        return new CryptoPayment(walletAddress, coin, usdPerCoin);
    }
}
