package factory.product;

import java.math.BigDecimal;

/**
 * Factory Method pattern - Product interface.
 * Common contract for every payment method supported by the shop.
 */
public interface Payment {

    String methodName();

    void pay(BigDecimal amount);
}
