package factory.creator;

import factory.product.PayPalPayment;
import factory.product.Payment;

/**
 * Factory Method pattern - Concrete Creator.
 */
public class PayPalPaymentCreator extends PaymentCreator {

    private final String email;

    public PayPalPaymentCreator(String email) {
        this.email = email;
    }

    @Override
    protected Payment createPayment() {
        return new PayPalPayment(email);
    }
}
