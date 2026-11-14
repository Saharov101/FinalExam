# E-Commerce Shopping Cart

Final project for the Design Patterns course. A console e-commerce shopping cart in Java that uses six
design patterns together: products are configured, stocked, added to a cart with extra services,
discounted, shipped and paid for.

## Team and responsibilities

| Student          | Patterns                                     | Packages                 |
|------------------|----------------------------------------------|--------------------------|
| Shalabay Madi    | Builder (creational), Factory Method (creational) | `builder`, `factory`     |
| Mustafin Azamat  | Decorator (structural), Adapter (structural) | `decorator`, `adapter`   |
| Batyushkin Maxim | Observer (behavioral), Strategy (behavioral) | `observer`, `strategy`   |

## Patterns

| Pattern        | Type        | Role in the project                                                            |
|----------------|-------------|--------------------------------------------------------------------------------|
| Builder        | Creational  | Step-by-step configuration of laptops and smartphones (CPU, RAM, storage...)    |
| Factory Method | Creational  | Creates the payment method (credit card, PayPal, crypto) during checkout       |
| Decorator      | Structural  | Adds gift wrap, extended warranty and insurance to cart items at runtime       |
| Adapter        | Structural  | Plugs third-party delivery APIs (DHL, KazPost) into one `DeliveryService` interface |
| Observer       | Behavioral  | Inventory publishes price/stock changes to the cart, customers and warehouse   |
| Strategy       | Behavioral  | Interchangeable discount algorithms (percentage, coupon, tiered)               |

How the patterns work together:

- Products made by the **Builder** are stored in the **Observer** subject (`Inventory`).
- `ShoppingCart` is the **Strategy** context, and it also observes the inventory, so price changes show up in the cart right away.
- Cart items are **Decorator** components that wrap Builder products.
- At checkout, the order is paid through a payment created by a **Factory Method** and shipped through an **Adapter**.
  Reserving stock notifies the observers (for example, a low-stock alert).

## Project structure

```
src/
├── Main.java                      # the only class outside the pattern packages, runs the demo
├── builder/
│   ├── product/    Product
│   ├── builders/   ProductBuilder, LaptopBuilder, SmartphoneBuilder
│   └── director/   ProductDirector
├── factory/
│   ├── product/    Payment, CreditCardPayment, PayPalPayment, CryptoPayment
│   └── creator/    PaymentCreator, CreditCardPaymentCreator, PayPalPaymentCreator, CryptoPaymentCreator
├── decorator/
│   ├── component/  CartItem, BasicCartItem
│   └── decorators/ CartItemDecorator, GiftWrapDecorator, ExtendedWarrantyDecorator, InsuranceDecorator
├── adapter/
│   ├── target/     DeliveryService
│   ├── adaptee/    DhlExpressApi, KazPostApi
│   └── adapters/   DhlDeliveryAdapter, KazPostDeliveryAdapter
├── observer/
│   ├── subject/    InventorySubject, Inventory
│   └── observers/  InventoryObserver, CustomerNotifier, LowStockAlert
└── strategy/
    ├── strategies/ DiscountStrategy, NoDiscount, PercentageDiscount, FixedAmountDiscount, TieredDiscount
    └── context/    ShoppingCart, PriceBreakdown
```

## Run

Requires JDK 17 or newer.

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out Main
```

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out Main
```

You can also open the folder in IntelliJ IDEA, mark `src` as Sources Root and run `Main`.
