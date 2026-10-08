package com.fooddelivery;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Service managing restaurants, menu items, in-memory shopping cart, and orders.
 */
@Service
public class FoodDeliveryService {

    /**
     * Inner class representing an item currently in the user's cart.
     */
    public static class CartItem {
        private FoodItem foodItem;
        private int quantity;

        public CartItem() {
        }

        public CartItem(FoodItem foodItem, int quantity) {
            this.foodItem = foodItem;
            this.quantity = quantity;
        }

        public FoodItem getFoodItem() {
            return foodItem;
        }

        public void setFoodItem(FoodItem foodItem) {
            this.foodItem = foodItem;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getItemTotal() {
            return foodItem != null ? Math.round((foodItem.getPrice() * quantity) * 100.0) / 100.0 : 0.0;
        }

        public String toJson() {
            return String.format(
                "{\"foodItem\":%s,\"quantity\":%d,\"itemTotal\":%.2f}",
                foodItem != null ? foodItem.toJson() : "null", quantity, getItemTotal()
            );
        }
    }

    private final List<Restaurant> restaurants = new ArrayList<>();
    private final List<FoodItem> allFoodItems = new ArrayList<>();
    private final List<Order> orderHistory = new ArrayList<>();
    private final Map<Integer, CartItem> activeCart = new LinkedHashMap<>();
    private final AtomicInteger orderCounter = new AtomicInteger(1000);

    public FoodDeliveryService() {
        initializeSampleData();
    }

    /**
     * Seeds the system with the 5 required sample restaurants and comprehensive menu items.
     */
    public final void initializeSampleData() {
        restaurants.clear();
        allFoodItems.clear();

        // 1. Pizza Palace
        Restaurant pizzaPalace = new Restaurant(1, "Pizza Palace", "Italian & Pizzeria", 4.8, "25-30 mins");
        FoodItem p1 = new FoodItem(101, "Margherita Pizza", "Pizza", 12.99, "Pizza Palace", "Classic stone-baked pizza with San Marzano tomatoes, fresh mozzarella, and aromatic basil.", "🍕");
        FoodItem p2 = new FoodItem(102, "Pepperoni Feast", "Pizza", 15.99, "Pizza Palace", "Loaded double pepperoni, melted mozzarella cheese, and authentic herb marinara.", "🍕");
        FoodItem p3 = new FoodItem(103, "Garlic Herb Breadsticks", "Sides", 6.49, "Pizza Palace", "Warm oven-baked buttery breadsticks served with marinara dipping sauce.", "🥖");
        FoodItem p4 = new FoodItem(104, "Classic Italian Tiramisu", "Desserts", 5.99, "Pizza Palace", "Coffee-dipped ladyfingers layered with rich mascarpone and cocoa powder.", "🍰");
        pizzaPalace.addFoodItem(p1);
        pizzaPalace.addFoodItem(p2);
        pizzaPalace.addFoodItem(p3);
        pizzaPalace.addFoodItem(p4);

        // 2. Burger House
        Restaurant burgerHouse = new Restaurant(2, "Burger House", "American Burgers & Grill", 4.6, "20-25 mins");
        FoodItem b1 = new FoodItem(201, "Classic Cheeseburger", "Burgers", 9.99, "Burger House", "Prime grilled beef patty, cheddar cheese, crisp lettuce, tomato, and house burger sauce.", "🍔");
        FoodItem b2 = new FoodItem(202, "Smoky BBQ Bacon Burger", "Burgers", 13.49, "Burger House", "Crispy applewood smoked bacon, caramelized onions, melted cheddar, and bold BBQ sauce.", "🍔");
        FoodItem b3 = new FoodItem(203, "Crispy Truffle Fries", "Sides", 5.49, "Burger House", "Golden crispy skin-on fries tossed with parmesan cheese and fragrant black truffle oil.", "🍟");
        FoodItem b4 = new FoodItem(204, "Thick Chocolate Milkshake", "Beverages", 4.99, "Burger House", "Ultra-thick hand-spun chocolate shake crowned with whipped cream and syrup.", "🥤");
        burgerHouse.addFoodItem(b1);
        burgerHouse.addFoodItem(b2);
        burgerHouse.addFoodItem(b3);
        burgerHouse.addFoodItem(b4);

        // 3. South Indian Kitchen
        Restaurant southIndianKitchen = new Restaurant(3, "South Indian Kitchen", "Authentic South Indian", 4.9, "15-20 mins");
        FoodItem s1 = new FoodItem(301, "Mysore Masala Dosa", "South Indian", 8.99, "South Indian Kitchen", "Crispy fermented crepe smeared with red chili chutney and stuffed with spiced potato mash.", "🥞");
        FoodItem s2 = new FoodItem(302, "Steamed Ghee Idli Sambar", "South Indian", 6.99, "South Indian Kitchen", "Three pillowy steamed rice cakes served with piping hot vegetable sambar and coconut chutney.", "🥟");
        FoodItem s3 = new FoodItem(303, "Crispy Medu Vada", "South Indian", 5.99, "South Indian Kitchen", "Golden crispy lentil donuts seasoned with peppercorns, curry leaves, and ginger.", "🍩");
        FoodItem s4 = new FoodItem(304, "Traditional Filter Coffee", "Beverages", 3.49, "South Indian Kitchen", "Freshly brewed aromatic chicory coffee poured with frothy whole milk in a brass tumbler.", "☕");
        southIndianKitchen.addFoodItem(s1);
        southIndianKitchen.addFoodItem(s2);
        southIndianKitchen.addFoodItem(s3);
        southIndianKitchen.addFoodItem(s4);

        // 4. Chinese Wok
        Restaurant chineseWok = new Restaurant(4, "Chinese Wok", "Pan-Asian & Chinese", 4.5, "30-35 mins");
        FoodItem c1 = new FoodItem(401, "Veg Hakka Noodles", "Chinese", 10.99, "Chinese Wok", "Flame-tossed noodles with shredded bell peppers, cabbage, spring onion, and savory soy sauce.", "🍜");
        FoodItem c2 = new FoodItem(402, "Veg Manchurian Bowl", "Chinese", 11.49, "Chinese Wok", "Crispy vegetable dumplings simmered in a dark, tangy, ginger-garlic soy gravy.", "🍲");
        FoodItem c3 = new FoodItem(403, "Kung Pao Chicken Bowl", "Chinese", 13.99, "Chinese Wok", "Tender diced chicken wok-seared with roasted peanuts, dried chilies, and scallions.", "🍗");
        FoodItem c4 = new FoodItem(404, "Crispy Golden Spring Rolls", "Chinese", 6.49, "Chinese Wok", "Handmade crispy rolls packed with seasoned garden veggies, served with sweet chili dip.", "🥢");
        chineseWok.addFoodItem(c1);
        chineseWok.addFoodItem(c2);
        chineseWok.addFoodItem(c3);
        chineseWok.addFoodItem(c4);

        // 5. Healthy Bites
        Restaurant healthyBites = new Restaurant(5, "Healthy Bites", "Salads, Bowls & Organic", 4.7, "15-25 mins");
        FoodItem h1 = new FoodItem(501, "Mediterranean Quinoa Bowl", "Healthy", 11.99, "Healthy Bites", "Organic tri-color quinoa with kalamata olives, cucumber, cherry tomatoes, and Greek feta cheese.", "🥗");
        FoodItem h2 = new FoodItem(502, "Avocado Brown Rice Bowl", "Healthy", 12.49, "Healthy Bites", "Fresh sliced Hass avocado, steamed edamame, roasted sweet potatoes, and tahini drizzle.", "🥑");
        FoodItem h3 = new FoodItem(503, "Berry Crunch Greek Yogurt", "Healthy", 6.99, "Healthy Bites", "Creamy Greek yogurt topped with fresh blueberries, strawberries, and toasted honey granola.", "🍓");
        FoodItem h4 = new FoodItem(504, "Green Glow Detox Smoothie", "Beverages", 5.49, "Healthy Bites", "Refreshing blend of baby spinach, green apple, fresh cucumber, ginger, and lemon zest.", "🥬");
        healthyBites.addFoodItem(h1);
        healthyBites.addFoodItem(h2);
        healthyBites.addFoodItem(h3);
        healthyBites.addFoodItem(h4);

        restaurants.add(pizzaPalace);
        restaurants.add(burgerHouse);
        restaurants.add(southIndianKitchen);
        restaurants.add(chineseWok);
        restaurants.add(healthyBites);

        // Index all food items
        for (Restaurant r : restaurants) {
            allFoodItems.addAll(r.getFoodItems());
        }
    }

    // ==========================================
    // RESTAURANT & MENU RETRIEVAL
    // ==========================================

    public List<Restaurant> getAllRestaurants() {
        return Collections.unmodifiableList(restaurants);
    }

    public Restaurant getRestaurantById(int id) {
        return restaurants.stream()
            .filter(r -> r.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public Restaurant getRestaurantByName(String name) {
        return restaurants.stream()
            .filter(r -> r.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public List<FoodItem> getAllFoodItems() {
        return Collections.unmodifiableList(allFoodItems);
    }

    public FoodItem getFoodItemById(int id) {
        return allFoodItems.stream()
            .filter(item -> item.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public List<FoodItem> searchFood(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllFoodItems();
        }
        String lower = query.toLowerCase().trim();
        return allFoodItems.stream()
            .filter(item -> item.getName().toLowerCase().contains(lower)
                || item.getCategory().toLowerCase().contains(lower)
                || item.getDescription().toLowerCase().contains(lower)
                || item.getRestaurantName().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    public List<FoodItem> filterByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All") || category.trim().isEmpty()) {
            return getAllFoodItems();
        }
        return allFoodItems.stream()
            .filter(item -> item.getCategory().equalsIgnoreCase(category.trim()))
            .collect(Collectors.toList());
    }

    public List<FoodItem> getFoodByRestaurant(int restaurantId) {
        Restaurant r = getRestaurantById(restaurantId);
        return r != null ? r.getFoodItems() : Collections.emptyList();
    }

    // ==========================================
    // IN-MEMORY CART MANAGEMENT
    // ==========================================

    public synchronized void addToCart(int foodItemId, int quantity) {
        if (quantity <= 0) return;
        FoodItem item = getFoodItemById(foodItemId);
        if (item == null) return;

        if (activeCart.containsKey(foodItemId)) {
            CartItem cartItem = activeCart.get(foodItemId);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            activeCart.put(foodItemId, new CartItem(item, quantity));
        }
    }

    public synchronized void updateCartQuantity(int foodItemId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(foodItemId);
        } else if (activeCart.containsKey(foodItemId)) {
            activeCart.get(foodItemId).setQuantity(quantity);
        }
    }

    public synchronized void removeFromCart(int foodItemId) {
        activeCart.remove(foodItemId);
    }

    public synchronized void clearCart() {
        activeCart.clear();
    }

    public synchronized List<CartItem> getCartItems() {
        return new ArrayList<>(activeCart.values());
    }

    public synchronized int getCartTotalItemCount() {
        return activeCart.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public synchronized double getCartSubtotal() {
        double subtotal = activeCart.values().stream()
            .mapToDouble(CartItem::getItemTotal)
            .sum();
        return Math.round(subtotal * 100.0) / 100.0;
    }

    public synchronized double getCartDeliveryFee() {
        double subtotal = getCartSubtotal();
        if (subtotal == 0.0 || subtotal >= 30.0) {
            return 0.0;
        }
        return 2.99;
    }

    public synchronized double getCartTax() {
        return Math.round((getCartSubtotal() * 0.08) * 100.0) / 100.0;
    }

    public synchronized double getCartGrandTotal() {
        double total = getCartSubtotal() + getCartDeliveryFee() + getCartTax();
        return Math.round(total * 100.0) / 100.0;
    }

    // ==========================================
    // ORDER CREATION & MANAGEMENT
    // ==========================================

    public synchronized Order placeOrderFromCart(String customerName, String phone, String address, String paymentMethod) {
        if (activeCart.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot place an order.");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery address cannot be empty.");
        }

        String orderId = generateOrderId();
        Order order = new Order(orderId, customerName.trim(), phone != null ? phone.trim() : "", address.trim());
        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            order.setPaymentMethod(paymentMethod.trim());
        }

        for (CartItem ci : activeCart.values()) {
            order.addItem(ci.getFoodItem(), ci.getQuantity());
        }

        orderHistory.add(0, order); // Most recent first
        activeCart.clear();
        return order;
    }

    public synchronized Order createDirectOrder(String customerName, String phone, String address, String paymentMethod, Map<Integer, Integer> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("No items specified for direct order.");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery address cannot be empty.");
        }

        String orderId = generateOrderId();
        Order order = new Order(orderId, customerName.trim(), phone != null ? phone.trim() : "", address.trim());
        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            order.setPaymentMethod(paymentMethod.trim());
        }

        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            FoodItem foodItem = getFoodItemById(entry.getKey());
            if (foodItem != null && entry.getValue() > 0) {
                order.addItem(foodItem, entry.getValue());
            }
        }

        orderHistory.add(0, order);
        return order;
    }

    public synchronized Order getOrderById(String orderId) {
        return orderHistory.stream()
            .filter(o -> o.getOrderId().equalsIgnoreCase(orderId))
            .findFirst()
            .orElse(null);
    }

    public synchronized List<Order> getAllOrders() {
        return Collections.unmodifiableList(orderHistory);
    }

    public String generateOrderId() {
        return "QB-" + orderCounter.incrementAndGet();
    }

    // ==========================================
    // JSON SERIALIZATION HELPERS FOR HTTP API
    // ==========================================

    public String getRestaurantsAsJson() {
        String jsonList = restaurants.stream()
            .map(Restaurant::toJson)
            .collect(Collectors.joining(","));
        return "[" + jsonList + "]";
    }

    public String getFoodsAsJson() {
        String jsonList = allFoodItems.stream()
            .map(FoodItem::toJson)
            .collect(Collectors.joining(","));
        return "[" + jsonList + "]";
    }

    public synchronized String getCartAsJson() {
        String itemsJson = activeCart.values().stream()
            .map(CartItem::toJson)
            .collect(Collectors.joining(","));
        return String.format(
            "{\"itemCount\":%d,\"subtotal\":%.2f,\"deliveryFee\":%.2f,\"tax\":%.2f,\"grandTotal\":%.2f,\"items\":[%s]}",
            getCartTotalItemCount(), getCartSubtotal(), getCartDeliveryFee(), getCartTax(), getCartGrandTotal(), itemsJson
        );
    }

    public synchronized String getOrdersAsJson() {
        String jsonList = orderHistory.stream()
            .map(Order::toJson)
            .collect(Collectors.joining(","));
        return "[" + jsonList + "]";
    }
}
