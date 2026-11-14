# E-Commerce Shopping Cart — Final Project Report

**Course:** Software Design Patterns
**Team:** Shalabay Madi, Mustafin Azamat, Batyushkin Maxim
**Language:** Java 17+
**Repository:** _<insert GitHub link>_

---

## 1. Introduction

Online shops have to handle configurable products, stock that changes in real time, optional services,
different promotions, several payment providers and external delivery companies. If all of this is
written in one place, the code becomes tightly coupled and hard to change.

The goal of this project is a console e-commerce shopping cart in which every one of these concerns is
handled by a suitable design pattern. Together the patterns form one working purchase flow:
*configure product → put it in stock → add to cart with services → apply discount → choose delivery → pay*.

The project uses six patterns, two of each GoF category:

| Pattern        | Category    | Author            |
|----------------|-------------|-------------------|
| Builder        | Creational  | Shalabay Madi     |
| Factory Method | Creational  | Shalabay Madi     |
| Decorator      | Structural  | Mustafin Azamat   |
| Adapter        | Structural  | Mustafin Azamat   |
| Observer       | Behavioral  | Batyushkin Maxim  |
| Strategy       | Behavioral  | Batyushkin Maxim  |

## 2. Body

### 2.1 Architecture and project structure

Each pattern has its own package. Inside each package, subpackages match the roles in the pattern
(for example `adapter/target`, `adapter/adaptee`, `adapter/adapters`). `Main` is the only class
outside the pattern packages, and it runs the full scenario.

```
src/
├── Main.java
├── builder/   product/ builders/ director/
├── factory/   product/ creator/
├── decorator/ component/ decorators/
├── adapter/   target/ adaptee/ adapters/
├── observer/  subject/ observers/
└── strategy/  strategies/ context/
```

_Insert the rendered class diagram here (`docs/uml/class-diagram.puml`)._

### 2.2 Builder (Shalabay Madi)

**Problem:** a laptop or smartphone has many optional parameters (CPU, RAM, storage, GPU, camera, colour).
A constructor with ten arguments is unreadable and error-prone.

**Solution:** `ProductBuilder<B>` is an abstract builder with common steps (`sku`, `name`, `color`,
`storage`) and the price calculation. `LaptopBuilder` and `SmartphoneBuilder` add product-specific steps.
The generic self-type `B` keeps method chaining fluent. `ProductDirector` holds the ready-made
configurations ("Gaming Laptop", "Flagship Phone").

```java
Product customPhone = new SmartphoneBuilder()
        .sku("PHN-CUSTOM-01").name("Custom Phone").basePrice("400.00")
        .storage(512).camera(48).color("Blue")
        .build();
```

### 2.3 Factory Method (Shalabay Madi)

**Problem:** checkout must work with any payment method, and adding a new one should not change checkout code.

**Solution:** the abstract creator `PaymentCreator` contains the payment workflow (`processPayment`:
validation → payment → transaction id). The factory method `createPayment()` is implemented by
`CreditCardPaymentCreator`, `PayPalPaymentCreator` and `CryptoPaymentCreator`.

```java
public final String processPayment(BigDecimal amount) {
    Payment payment = createPayment();   // factory method
    payment.pay(amount);
    ...
}
```

### 2.4 Decorator (Mustafin Azamat)

**Problem:** a customer can add any combination of services (gift wrap, extended warranty, insurance)
to a cart item. A separate subclass for every combination would lead to a class explosion.

**Solution:** `CartItem` is the component interface and `BasicCartItem` is the concrete component.
`CartItemDecorator` delegates calls to the wrapped item and adds `extraCost()`. Decorators can be
stacked in any order at runtime:

```java
cart.addItem(new InsuranceDecorator(new GiftWrapDecorator(new BasicCartItem(flagshipPhone, 2))));
```

### 2.5 Adapter (Mustafin Azamat)

**Problem:** delivery companies have incompatible APIs. DHL uses euros, kilograms and one address
line. KazPost uses tenge, numeric parcel ids and separate city and street fields.

**Solution:** the shop depends only on the `DeliveryService` target interface. `DhlDeliveryAdapter`
and `KazPostDeliveryAdapter` are object adapters: they wrap `DhlExpressApi` and `KazPostApi` and
convert units, currencies and tracking-number formats.

### 2.6 Observer (Batyushkin Maxim)

**Problem:** when a price or stock level changes, several parts of the system must react
(the cart, the customer's wishlist, the warehouse) without the inventory knowing about them.

**Solution:** `Inventory` (concrete subject) notifies every attached `InventoryObserver`. The observer
interface has default methods, so each observer implements only the events it needs (ISP):

- `ShoppingCart` recalculates and shows the new total when a product in it gets a new price;
- `CustomerNotifier` e-mails about price drops and restocks of wishlist items;
- `LowStockAlert` warns the warehouse when stock drops below a threshold.

### 2.7 Strategy (Batyushkin Maxim)

**Problem:** the shop runs different promotions that change often.

**Solution:** `DiscountStrategy` has the implementations `NoDiscount` (null object), `PercentageDiscount`,
`FixedAmountDiscount` (coupon with a minimum order value) and `TieredDiscount` (which reuses
`PercentageDiscount`). `ShoppingCart` is the context: `setDiscountStrategy()` switches the algorithm at runtime.

### 2.8 Interaction between patterns

_Insert the rendered sequence diagram here (`docs/uml/checkout-sequence.puml`)._

1. Builder products are registered in the Observer subject `Inventory`.
2. Decorator `CartItem`s wrap Builder products and are stored in the Strategy context `ShoppingCart`.
3. `ShoppingCart` is also an Observer, so an `Inventory.updatePrice(...)` call immediately updates the cart total.
4. At checkout, the cost goes through Strategy, shipping is quoted through an Adapter, payment is made
   through a Factory Method, and reserving stock triggers Observer notifications (low-stock alert).

### 2.9 SOLID, KISS, DRY

- **SRP:** each class has one job (payment, delivery, discount, notification...).
- **OCP:** a new discount, decorator, carrier or payment method is a new class, and existing code does not change.
- **LSP:** any `DiscountStrategy`, `CartItem`, `DeliveryService` or `Payment` can replace another.
- **ISP:** `InventoryObserver` has default methods, so observers do not implement events they ignore.
- **DIP:** `ShoppingCart` and checkout depend on abstractions (`DiscountStrategy`, `CartItem`,
  `DeliveryService`, `PaymentCreator`), not on concrete classes.
- **DRY:** common builder steps are in `ProductBuilder`, common delegation is in `CartItemDecorator`,
  and `TieredDiscount` reuses `PercentageDiscount`.
- **KISS:** no frameworks; the console application can be run with one command.
- Money is stored as `BigDecimal`, and input is validated in constructors.

### 2.10 Screenshots

_Insert screenshots of `java -cp out Main` output for each section (1–9)._

## 3. Conclusion

All six patterns are fully implemented and work together in one purchase flow. Each pattern
solves a real problem of the domain: Builder for complex product configurations, Factory Method for
payment methods, Decorator for optional services, Adapter for third-party carriers, Observer for
real-time updates and Strategy for promotions. As a result, the system is easy to extend: most new
features only need new classes.

## 4. Further work

- Graphical or web interface (JavaFX / Spring Boot REST API).
- Database storage for the catalog and orders.
- Real payment and delivery integrations through the existing adapters and factories.
- Combining discounts with a Composite strategy.
- Unit tests (JUnit 5) for every pattern.
- Facade `CheckoutService` to move the checkout process out of `Main`.
