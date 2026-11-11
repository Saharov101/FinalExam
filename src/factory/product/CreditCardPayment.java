package factory.product;

import java.math.BigDecimal;

/**
 * Factory Method pattern - Concrete Product.
 */
public class CreditCardPayment implements Payment {

    private static final String CARD_NUMBER_FORMAT = "\\d{16}";

    private final String cardNumber;
    private final String cardHolder;

    public CreditCardPayment(String cardNumber, String cardHolder) {
        if (cardNumber == null || !cardNumber.matches(CARD_NUMBER_FORMAT)) {
            throw new IllegalArgumentException("Card number must contain exactly 16 digits");
        }
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public String methodName() {
        return "Credit Card";
    }

    @Override
    public void pay(BigDecimal amount) {
        String maskedNumber = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        System.out.printf("[PAYMENT] Charged $%s to card %s (%s)%n", amount, maskedNumber, cardHolder);
    }
}
