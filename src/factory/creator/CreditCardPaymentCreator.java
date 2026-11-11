package factory.creator;

import factory.product.CreditCardPayment;
import factory.product.Payment;

/**
 * Factory Method pattern - Concrete Creator.
 */
public class CreditCardPaymentCreator extends PaymentCreator {

    private final String cardNumber;
    private final String cardHolder;

    public CreditCardPaymentCreator(String cardNumber, String cardHolder) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    protected Payment createPayment() {
        return new CreditCardPayment(cardNumber, cardHolder);
    }
}
