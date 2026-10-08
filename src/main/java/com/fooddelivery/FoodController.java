package com.fooddelivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Spring REST Controller exposing endpoints for QuickBite Food Delivery.
 * Manages restaurants, food catalog, active shopping cart, customer orders, and serves the home page.
 */
@RestController
@CrossOrigin(origins = "*")
public class FoodController {

    private final FoodDeliveryService deliveryService;

    @Autowired
    public FoodController(FoodDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * GET /
     * Serves the home page (index.html) at the root URL: http://localhost:8081/
     */
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public Resource getIndexPage() {
        return new ClassPathResource("static/index.html");
    }

    /**
     * GET /api/restaurants
     * Returns the full list of available restaurants.
     */
    @GetMapping("/api/restaurants")
    public List<Restaurant> getRestaurants() {
        return deliveryService.getAllRestaurants();
    }

    /**
     * GET /api/restaurants/{id}
     * Returns the restaurant with the matching ID, or 404 if not found.
     */
    @GetMapping("/api/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable int id) {
        Restaurant restaurant = deliveryService.getRestaurantById(id);
        if (restaurant != null) {
            return ResponseEntity.ok(restaurant);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/foods
     * Returns food items, optionally filtered by search query and category.
     */
    @GetMapping("/api/foods")
    public List<FoodItem> getFoods(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category) {
        
        String term = (search != null && !search.trim().isEmpty()) ? search : query;
        if (term != null && !term.trim().isEmpty()) {
            return deliveryService.searchFood(term);
        }
        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All")) {
            return deliveryService.filterByCategory(category);
        }
        return deliveryService.getAllFoodItems();
    }

    /**
     * GET /api/foods/{id}
     * Returns a specific food item by its ID.
     */
    @GetMapping("/api/foods/{id}")
    public ResponseEntity<FoodItem> getFoodById(@PathVariable int id) {
        FoodItem item = deliveryService.getFoodItemById(id);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/orders
     * Returns the complete order history.
     */
    @GetMapping("/api/orders")
    public List<Order> getAllOrders() {
        return deliveryService.getAllOrders();
    }

    /**
     * GET /api/orders/{id}
     * Returns details for a specific order.
     */
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Order order = deliveryService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST /api/orders
     * Places a new order from provided customer details and ordered items list.
     */
    @PostMapping("/api/orders")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            String customerName = (String) orderRequest.get("customerName");
            String customerPhone = (String) orderRequest.get("customerPhone");
            String deliveryAddress = (String) orderRequest.get("deliveryAddress");
            String paymentMethod = (String) orderRequest.getOrDefault("paymentMethod", "Cash on Delivery");

            if (customerName == null || customerName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Customer name is required."));
            }
            if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Delivery address is required."));
            }

            Map<Integer, Integer> itemsMap = new LinkedHashMap<>();
            Object itemsObj = orderRequest.get("items");

            if (itemsObj instanceof List<?>) {
                List<?> list = (List<?>) itemsObj;
                for (Object elem : list) {
                    if (elem instanceof Map<?, ?>) {
                        Map<?, ?> map = (Map<?, ?>) elem;
                        int id = 0;
                        int qty = 1;

                        if (map.containsKey("id")) {
                            id = ((Number) map.get("id")).intValue();
                        } else if (map.containsKey("foodItemId")) {
                            id = ((Number) map.get("foodItemId")).intValue();
                        } else if (map.containsKey("foodItem") && map.get("foodItem") instanceof Map<?, ?>) {
                            Map<?, ?> fi = (Map<?, ?>) map.get("foodItem");
                            if (fi.containsKey("id")) {
                                id = ((Number) fi.get("id")).intValue();
                            }
                        }

                        if (map.containsKey("quantity")) {
                            qty = ((Number) map.get("quantity")).intValue();
                        }

                        if (id > 0 && qty > 0) {
                            itemsMap.put(id, itemsMap.getOrDefault(id, 0) + qty);
                        }
                    }
                }
            }

            if (itemsMap.isEmpty()) {
                // If cart was used on server, place order from cart
                if (!deliveryService.getCartItems().isEmpty()) {
                    Order order = deliveryService.placeOrderFromCart(customerName, customerPhone, deliveryAddress, paymentMethod);
                    return ResponseEntity.status(HttpStatus.CREATED).body(order);
                }
                return ResponseEntity.badRequest().body(Map.of("error", "No valid food items provided for order."));
            }

            Order order = deliveryService.createDirectOrder(customerName, customerPhone, deliveryAddress, paymentMethod, itemsMap);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to place order: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/orders/{id}/status
     * Updates the status of an existing order (e.g. PREPARING, OUT_FOR_DELIVERY, DELIVERED).
     */
    @PutMapping("/api/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        Order order = deliveryService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        String status = body.get("status");
        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Status field is required."));
        }
        order.setOrderStatus(status.trim().toUpperCase());
        return ResponseEntity.ok(order);
    }

    /**
     * GET /api/cart
     * Returns the active shopping cart summary.
     */
    @GetMapping("/api/cart")
    public Map<String, Object> getCart() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", deliveryService.getCartItems());
        response.put("itemCount", deliveryService.getCartTotalItemCount());
        response.put("subtotal", deliveryService.getCartSubtotal());
        response.put("deliveryFee", deliveryService.getCartDeliveryFee());
        response.put("tax", deliveryService.getCartTax());
        response.put("grandTotal", deliveryService.getCartGrandTotal());
        return response;
    }

    /**
     * POST /api/cart/add
     * Adds an item to the shopping cart.
     */
    @PostMapping("/api/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Integer> payload) {
        Integer foodItemId = payload.get("foodItemId");
        Integer quantity = payload.getOrDefault("quantity", 1);
        if (foodItemId == null || quantity <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid foodItemId or quantity"));
        }
        deliveryService.addToCart(foodItemId, quantity);
        return ResponseEntity.ok(getCart());
    }

    /**
     * DELETE /api/cart/clear
     * Clears all items from the shopping cart.
     */
    @DeleteMapping("/api/cart/clear")
    public ResponseEntity<?> clearCart() {
        deliveryService.clearCart();
        return ResponseEntity.ok(Map.of("message", "Cart cleared successfully"));
    }

    /**
     * GET /api/health
     * Returns system health status and high-level platform statistics.
     */
    @GetMapping("/api/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("application", "QuickBite Online Food Delivery System");
        health.put("framework", "Spring Boot");
        health.put("javaVersion", System.getProperty("java.version"));
        health.put("restaurantsCount", deliveryService.getAllRestaurants().size());
        health.put("foodsCount", deliveryService.getAllFoodItems().size());
        health.put("ordersCount", deliveryService.getAllOrders().size());
        return health;
    }
}
