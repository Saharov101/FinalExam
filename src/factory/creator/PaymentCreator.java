package factory.creator;

import factory.product.Payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Factory Method pattern - Creator.
 * Contains the payment workflow, while subclasses decide which {@link Payment} to create.
 */
public abstract class PaymentCreator {

    /** Factory method. */
    protected abstract Payment createPayment();

    /** Processes the payment and returns the transaction id. */
    public final String processPayment(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        Payment payment = createPayment();
        payment.pay(amount);
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.printf("[PAYMENT] %s payment approved, transaction %s%n", payment.methodName(), transactionId);
        return transactionId;
    }
}
