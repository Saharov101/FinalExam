package factory.product;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Factory Method pattern - Concrete Product.
 */
public class CryptoPayment implements Payment {

    private final String walletAddress;
    private final String coin;
    private final BigDecimal usdPerCoin;

    public CryptoPayment(String walletAddress, String coin, BigDecimal usdPerCoin) {
        if (walletAddress == null || !walletAddress.startsWith("0x")) {
            throw new IllegalArgumentException("Wallet address must start with 0x");
        }
        if (usdPerCoin == null || usdPerCoin.signum() <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
        this.walletAddress = walletAddress;
        this.coin = coin;
        this.usdPerCoin = usdPerCoin;
    }

    @Override
    public String methodName() {
        return "Crypto (" + coin + ")";
    }

    @Override
    public void pay(BigDecimal amount) {
        BigDecimal coins = amount.divide(usdPerCoin, 6, RoundingMode.HALF_UP);
        System.out.printf("[PAYMENT] Sent %s %s ($%s) from wallet %s%n", coins, coin, amount, walletAddress);
    }
}
