package factory.product;

import java.math.BigDecimal;

/**
 * Factory Method pattern - Concrete Product.
 */
public class PayPalPayment implements Payment {

    private final String email;

    public PayPalPayment(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid PayPal e-mail: " + email);
        }
        this.email = email;
    }

    @Override
    public String methodName() {
        return "PayPal";
    }

    @Override
    public void pay(BigDecimal amount) {
        System.out.printf("[PAYMENT] Transferred $%s from PayPal account %s%n", amount, email);
    }
}
