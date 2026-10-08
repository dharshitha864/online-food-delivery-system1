package com.fooddelivery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Complete test suite for QuickBite Online Food Delivery System.
 * Tests Spring Boot context loading, REST endpoints via MockMvc, and core domain business logic.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodDeliveryService service;

    @BeforeEach
    public void setUp() {
        service.initializeSampleData();
        service.clearCart();
    }

    // ==========================================
    // 1. Spring Context & Health Endpoint Test
    // ==========================================
    @Test
    @DisplayName("Test Spring Boot context loads and health endpoint returns UP")
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("QuickBite Online Food Delivery System"));
    }

    // ==========================================
    // 1b. Root URL Serves index.html Test
    // ==========================================
    @Test
    @DisplayName("Test root URL (/) serves index.html with QuickBite branding")
    public void testIndexPageReturnsHtml() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("QuickBite")));
    }

    // ==========================================
    // 2. REST API: Get Restaurants Test
    // ==========================================
    @Test
    @DisplayName("Test GET /api/restaurants returns list of restaurants")
    public void testGetRestaurantsApi() throws Exception {
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Pizza Palace"));
    }

    // ==========================================
    // 3. REST API: Get Food Items & Search Test
    // ==========================================
    @Test
    @DisplayName("Test GET /api/foods with search and category filtering")
    public void testGetFoodsApi() throws Exception {
        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/foods").param("search", "Pizza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());

        mockMvc.perform(get("/api/foods").param("category", "Burgers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Burgers"));
    }

    // ==========================================
    // 4. REST API: Place Order via POST /api/orders
    // ==========================================
    @Test
    @DisplayName("Test POST /api/orders creates order successfully")
    public void testPlaceOrderApi() throws Exception {
        String orderJson = """
            {
                "customerName": "Jane Doe",
                "customerPhone": "555-4321",
                "deliveryAddress": "456 Elm Street",
                "paymentMethod": "Cash on Delivery",
                "items": [
                    { "id": 101, "quantity": 2 },
                    { "id": 201, "quantity": 1 }
                ]
            }
            """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.customerName").value("Jane Doe"))
                .andExpect(jsonPath("$.orderStatus").value("CONFIRMED"));
    }

    // ==========================================
    // 5. Food Item Creation & Validation Test
    // ==========================================
    @Test
    @DisplayName("Test food item creation with all attributes")
    public void testFoodItemCreation() {
        FoodItem item = new FoodItem(999, "Truffle Pasta", "Italian", 18.50, "Pasta House", "Creamy homemade fettuccine with fresh truffles", "🍝");
        
        assertNotNull(item, "FoodItem instance should not be null");
        assertEquals(999, item.getId());
        assertEquals("Truffle Pasta", item.getName());
        assertEquals("Italian", item.getCategory());
        assertEquals(18.50, item.getPrice(), 0.001);
        assertEquals("Pasta House", item.getRestaurantName());
        assertEquals("Creamy homemade fettuccine with fresh truffles", item.getDescription());
        assertEquals("🍝", item.getEmoji());
        assertTrue(item.toJson().contains("Truffle Pasta"), "toJson output should contain item name");
    }

    // ==========================================
    // 6. Restaurant Creation & Menu Association Test
    // ==========================================
    @Test
    @DisplayName("Test restaurant creation and menu item association")
    public void testRestaurantCreation() {
        Restaurant restaurant = new Restaurant(10, "Spice Symphony", "Indian Curry", 4.9, "20-30 mins");
        
        assertNotNull(restaurant);
        assertEquals(10, restaurant.getId());
        assertEquals("Spice Symphony", restaurant.getName());
        assertEquals("Indian Curry", restaurant.getCuisine());
        assertEquals(4.9, restaurant.getRating(), 0.01);
        assertEquals("20-30 mins", restaurant.getDeliveryTime());
        assertTrue(restaurant.getFoodItems().isEmpty(), "Initial food item list should be empty");

        FoodItem curry = new FoodItem(1001, "Paneer Butter Masala", "Curry", 12.99, "Spice Symphony", "Rich tomato cashew gravy with cottage cheese");
        restaurant.addFoodItem(curry);

        assertEquals(1, restaurant.getFoodItems().size(), "Menu should contain 1 item after adding");
        assertEquals(curry, restaurant.getFoodItemById(1001));
    }

    // ==========================================
    // 7. Adding Items to Cart Test
    // ==========================================
    @Test
    @DisplayName("Test adding food items to active cart and updating quantities")
    public void testAddingItemsToCart() {
        // Add Margherita Pizza (id: 101)
        service.addToCart(101, 2);
        
        assertEquals(1, service.getCartItems().size(), "Cart should contain 1 distinct item type");
        assertEquals(2, service.getCartTotalItemCount(), "Total item count in cart should be 2");

        // Add more of the same item
        service.addToCart(101, 1);
        assertEquals(3, service.getCartTotalItemCount(), "Total item count in cart should be 3");

        // Add a different item (Classic Cheeseburger id: 201)
        service.addToCart(201, 2);
        assertEquals(2, service.getCartItems().size(), "Cart should contain 2 distinct item types");
        assertEquals(5, service.getCartTotalItemCount(), "Total item count in cart should be 5");
    }

    // ==========================================
    // 8. Cart Total Calculation Test
    // ==========================================
    @Test
    @DisplayName("Test cart subtotal, delivery fee, tax, and grand total calculations")
    public void testCartTotalCalculations() {
        // Margherita Pizza ($12.99) x 2 = $25.98
        service.addToCart(101, 2);
        
        double expectedSubtotal = 12.99 * 2;
        assertEquals(expectedSubtotal, service.getCartSubtotal(), 0.01);

        // Since subtotal < $30, standard delivery fee of $2.99 applies
        assertEquals(2.99, service.getCartDeliveryFee(), 0.01);

        // Expected tax is 8% of subtotal
        double expectedTax = Math.round((expectedSubtotal * 0.08) * 100.0) / 100.0;
        assertEquals(expectedTax, service.getCartTax(), 0.01);

        // Expected grand total = subtotal + fee + tax
        double expectedGrandTotal = Math.round((expectedSubtotal + 2.99 + expectedTax) * 100.0) / 100.0;
        assertEquals(expectedGrandTotal, service.getCartGrandTotal(), 0.01);

        // Add item to push subtotal over $30 for free delivery:
        service.addToCart(101, 1);
        assertEquals(0.0, service.getCartDeliveryFee(), 0.01, "Orders $30+ should qualify for free delivery");
    }

    // ==========================================
    // 9. Order Creation & Confirmation Flow Test
    // ==========================================
    @Test
    @DisplayName("Test order placement flow, order ID generation and confirmation details")
    public void testOrderCreation() {
        service.addToCart(101, 1); // $12.99
        service.addToCart(201, 2); // $9.99 * 2 = $19.98

        Order order = service.placeOrderFromCart("Alex Mercer", "555-0199", "742 Evergreen Terrace", "Card");

        assertNotNull(order, "Order should be created");
        assertNotNull(order.getOrderId(), "Order should have an order ID");
        assertTrue(order.getOrderId().startsWith("QB-"), "Order ID should start with prefix 'QB-'");
        assertEquals("Alex Mercer", order.getCustomerName());
        assertEquals("555-0199", order.getCustomerPhone());
        assertEquals("742 Evergreen Terrace", order.getDeliveryAddress());
        assertEquals("Card", order.getPaymentMethod());
        assertEquals("CONFIRMED", order.getOrderStatus());
        assertEquals(2, order.getItems().size());
        assertTrue(order.getTotalAmount() > 0, "Total amount should be greater than 0");

        // Cart should now be empty after order placement
        assertTrue(service.getCartItems().isEmpty(), "Active cart should be empty after checkout");

        // Order history should contain the new order
        Order retrievedOrder = service.getOrderById(order.getOrderId());
        assertNotNull(retrievedOrder, "Order should be retrievable from order history");
        assertEquals(order.getOrderId(), retrievedOrder.getOrderId());
    }

    // ==========================================
    // 10. Direct Order Creation Test
    // ==========================================
    @Test
    @DisplayName("Test direct order creation with items map")
    public void testDirectOrderCreation() {
        Map<Integer, Integer> items = new HashMap<>();
        items.put(301, 2); // Mysore Masala Dosa
        items.put(304, 2); // Filter coffee

        Order directOrder = service.createDirectOrder("Priya Sharma", "555-0342", "221B Baker Street", "UPI", items);

        assertNotNull(directOrder);
        assertTrue(directOrder.getOrderId().startsWith("QB-"));
        assertEquals("Priya Sharma", directOrder.getCustomerName());
        assertEquals(2, directOrder.getItems().size());
        assertEquals("UPI", directOrder.getPaymentMethod());
    }

    // ==========================================
    // 11. Cart Modification & Clear Test
    // ==========================================
    @Test
    @DisplayName("Test updating quantity, removing items, and clearing cart")
    public void testCartModifications() {
        service.addToCart(101, 3);
        service.addToCart(201, 1);

        // Update quantity
        service.updateCartQuantity(101, 1);
        assertEquals(2, service.getCartTotalItemCount(), "Total item count should be 2 after reducing item 101 to 1");

        // Remove item 201
        service.removeFromCart(201);
        assertEquals(1, service.getCartItems().size(), "Cart should only contain 1 item type");
        assertEquals(1, service.getCartTotalItemCount());

        // Clear cart
        service.clearCart();
        assertTrue(service.getCartItems().isEmpty(), "Cart should be empty after clearCart()");
        assertEquals(0.0, service.getCartSubtotal(), 0.001);
    }

    // ==========================================
    // 12. Sample Restaurants Verification Test
    // ==========================================
    @Test
    @DisplayName("Verify the 5 required sample restaurants are initialized")
    public void testSampleRestaurantsInitialization() {
        List<Restaurant> restaurants = service.getAllRestaurants();
        assertEquals(5, restaurants.size(), "Should have exactly 5 pre-configured restaurants");

        assertNotNull(service.getRestaurantByName("Pizza Palace"));
        assertNotNull(service.getRestaurantByName("Burger House"));
        assertNotNull(service.getRestaurantByName("South Indian Kitchen"));
        assertNotNull(service.getRestaurantByName("Chinese Wok"));
        assertNotNull(service.getRestaurantByName("Healthy Bites"));

        for (Restaurant r : restaurants) {
            assertFalse(r.getFoodItems().isEmpty(), r.getName() + " should have menu items");
            assertTrue(r.getRating() >= 4.0, r.getName() + " rating should be >= 4.0");
            assertNotNull(r.getDeliveryTime(), r.getName() + " should have delivery time");
        }
    }

    // ==========================================
    // 13. Food Search and Filter Test
    // ==========================================
    @Test
    @DisplayName("Test food search by name/category and filtering by category")
    public void testFoodSearchAndFilter() {
        List<FoodItem> searchBurger = service.searchFood("burger");
        assertFalse(searchBurger.isEmpty(), "Searching 'burger' should return matches");
        assertTrue(searchBurger.stream().anyMatch(i -> i.getName().toLowerCase().contains("burger")));

        List<FoodItem> searchPizza = service.searchFood("pizza");
        assertFalse(searchPizza.isEmpty(), "Searching 'pizza' should return matches");

        List<FoodItem> chineseCategory = service.filterByCategory("Chinese");
        assertFalse(chineseCategory.isEmpty(), "Filtering by 'Chinese' category should return matches");
        for (FoodItem item : chineseCategory) {
            assertEquals("Chinese", item.getCategory());
        }

        List<FoodItem> all = service.filterByCategory("All");
        assertEquals(service.getAllFoodItems().size(), all.size());
    }

    // ==========================================
    // 14. Order Edge Cases Test
    // ==========================================
    @Test
    @DisplayName("Test order validation when cart or fields are empty")
    public void testOrderValidationExceptions() {
        // Empty cart throws IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            service.placeOrderFromCart("John", "555-1234", "123 Elm St", "Cash");
        });

        // Blank customer name throws IllegalArgumentException
        service.addToCart(101, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            service.placeOrderFromCart("", "555-1234", "123 Elm St", "Cash");
        });

        // Blank address throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            service.placeOrderFromCart("John", "555-1234", "   ", "Cash");
        });
    }
}
