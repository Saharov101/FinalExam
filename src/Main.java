import adapter.adaptee.DhlExpressApi;
import adapter.adaptee.KazPostApi;
import adapter.adapters.DhlDeliveryAdapter;
import adapter.adapters.KazPostDeliveryAdapter;
import adapter.target.DeliveryService;
import builder.builders.LaptopBuilder;
import builder.builders.SmartphoneBuilder;
import builder.director.ProductDirector;
import builder.product.Product;
import decorator.component.BasicCartItem;
import decorator.decorators.ExtendedWarrantyDecorator;
import decorator.decorators.GiftWrapDecorator;
import decorator.decorators.InsuranceDecorator;
import factory.creator.CreditCardPaymentCreator;
import factory.creator.CryptoPaymentCreator;
import factory.creator.PayPalPaymentCreator;
import factory.creator.PaymentCreator;
import observer.observers.CustomerNotifier;
import observer.observers.LowStockAlert;
import observer.subject.Inventory;
import strategy.context.PriceBreakdown;
import strategy.context.ShoppingCart;
import strategy.strategies.DiscountStrategy;
import strategy.strategies.FixedAmountDiscount;
import strategy.strategies.NoDiscount;
import strategy.strategies.PercentageDiscount;
import strategy.strategies.TieredDiscount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * E-Commerce Shopping Cart - demonstrates all six patterns working together:
 * Builder, Factory Method (creational), Decorator, Adapter (structural), Observer, Strategy (behavioral).
 */
public class Main {

    private static final String VAT_RATE = "0.12";
    private static final String CITY = "Astana";
    private static final String ADDRESS = "Mangilik El 55/11";

    public static void main(String[] args) {
        section("1. BUILDER - configuring products");
        ProductDirector director = new ProductDirector();
        Product gamingLaptop = director.constructGamingLaptop(new LaptopBuilder());
        Product officeLaptop = director.constructOfficeLaptop(new LaptopBuilder());
        Product flagshipPhone = director.constructFlagshipPhone(new SmartphoneBuilder());
        Product customPhone = new SmartphoneBuilder()
                .sku("PHN-CUSTOM-01")
                .name("Custom Phone")
                .basePrice("400.00")
                .storage(512)
                .camera(48)
                .color("Blue")
                .build();
        List.of(gamingLaptop, officeLaptop, flagshipPhone, customPhone).forEach(System.out::println);

        section("2. OBSERVER - inventory with subscribers");
        Inventory inventory = new Inventory();
        inventory.addProduct(gamingLaptop, 5);
        inventory.addProduct(officeLaptop, 10);
        inventory.addProduct(flagshipPhone, 4);
        inventory.addProduct(customPhone, 0);

        ShoppingCart cart = new ShoppingCart(VAT_RATE);
        CustomerNotifier customer = new CustomerNotifier("madi@example.com");
        customer.watch(customPhone.getSku());
        customer.watch(officeLaptop.getSku());
        inventory.attach(new LowStockAlert(2));
        inventory.attach(customer);
        inventory.attach(cart);
        System.out.println("Subscribed: LowStockAlert, CustomerNotifier, ShoppingCart");

        section("3. DECORATOR - adding services to cart items");
        cart.addItem(new ExtendedWarrantyDecorator(new BasicCartItem(gamingLaptop, 1), 2));
        cart.addItem(new InsuranceDecorator(new GiftWrapDecorator(new BasicCartItem(flagshipPhone, 2))));
        cart.addItem(new BasicCartItem(officeLaptop, 1));

        section("4. STRATEGY - comparing discount algorithms");
        List<DiscountStrategy> strategies = List.of(
                new NoDiscount(),
                new PercentageDiscount(10),
                new FixedAmountDiscount("150.00", "1000.00"),
                new TieredDiscount());
        for (DiscountStrategy strategy : strategies) {
            cart.setDiscountStrategy(strategy);
            printBreakdown(cart.calculateTotal());
        }
        cart.setDiscountStrategy(new TieredDiscount());

        section("5. OBSERVER - real-time price and stock changes");
        inventory.updatePrice(officeLaptop.getSku(), new BigDecimal("599.00"));
        inventory.restock(customPhone.getSku(), 7);

        section("6. ADAPTER - delivery quotes from different carriers");
        List<DeliveryService> carriers = List.of(
                new DhlDeliveryAdapter(new DhlExpressApi()),
                new KazPostDeliveryAdapter(new KazPostApi()));
        for (DeliveryService carrier : carriers) {
            System.out.printf("%-12s -> $%s%n", carrier.carrierName(), carrier.calculateCost(CITY, cart.totalQuantity()));
        }

        section("7. FACTORY METHOD - checkout with credit card + DHL");
        checkout(cart, inventory, carriers.get(0),
                new CreditCardPaymentCreator("4400430212345678", "MADI SHALABAY"));

        section("8. Second order: PayPal + KazPost, then crypto");
        ShoppingCart secondCart = new ShoppingCart(VAT_RATE);
        secondCart.setDiscountStrategy(new PercentageDiscount(5));
        secondCart.addItem(new GiftWrapDecorator(new BasicCartItem(customPhone, 2)));
        checkout(secondCart, inventory, carriers.get(1), new PayPalPaymentCreator("azamat@example.com"));

        ShoppingCart thirdCart = new ShoppingCart(VAT_RATE);
        thirdCart.addItem(new BasicCartItem(flagshipPhone, 2));
        checkout(thirdCart, inventory, carriers.get(1),
                new CryptoPaymentCreator("0xA1b2C3d4E5f6", "ETH", new BigDecimal("3200.00")));

        section("9. Validation - ordering more than in stock");
        ShoppingCart greedyCart = new ShoppingCart(VAT_RATE);
        greedyCart.addItem(new BasicCartItem(gamingLaptop, 100));
        checkout(greedyCart, inventory, carriers.get(0), new PayPalPaymentCreator("maxim@example.com"));
    }

    private static void checkout(ShoppingCart cart, Inventory inventory,
                                 DeliveryService delivery, PaymentCreator paymentCreator) {
        if (cart.isEmpty()) {
            System.out.println("[CHECKOUT] Cart is empty");
            return;
        }
        Map<String, Integer> quantities = cart.quantitiesBySku();
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            if (!inventory.isAvailable(entry.getKey(), entry.getValue())) {
                System.out.printf("[CHECKOUT] Rejected: only %d of %s available%n",
                        inventory.getStock(entry.getKey()), entry.getKey());
                return;
            }
        }

        PriceBreakdown breakdown = cart.calculateTotal();
        BigDecimal shipping = delivery.calculateCost(CITY, cart.totalQuantity());
        BigDecimal grandTotal = breakdown.total().add(shipping);
        printBreakdown(breakdown);
        System.out.printf("  Shipping (%s): $%s%n  GRAND TOTAL: $%s%n", delivery.carrierName(), shipping, grandTotal);

        String transactionId = paymentCreator.processPayment(grandTotal);
        quantities.forEach(inventory::reserve);
        String tracking = delivery.ship(CITY, ADDRESS, cart.totalQuantity());
        cart.clear();
        System.out.printf("[CHECKOUT] Order completed. Transaction: %s, tracking: %s%n", transactionId, tracking);
    }

    private static void printBreakdown(PriceBreakdown breakdown) {
        System.out.printf("  [%s] subtotal $%s, discount -$%s, VAT $%s, total $%s%n",
                breakdown.discountDescription(), breakdown.subtotal(), breakdown.discount(),
                breakdown.tax(), breakdown.total());
    }

    private static void section(String title) {
        System.out.printf("%n========== %s ==========%n", title);
    }
}
