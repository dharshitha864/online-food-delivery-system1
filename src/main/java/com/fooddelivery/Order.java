package com.fooddelivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a customer food order with item details, pricing, and delivery info.
 */
public class Order {

    /**
     * Represents a single item entry within an order.
     */
    public static class OrderItem {
        private FoodItem foodItem;
        private int quantity;
        private double unitPrice;

        public OrderItem() {
        }

        public OrderItem(FoodItem foodItem, int quantity) {
            this.foodItem = foodItem;
            this.quantity = quantity;
            this.unitPrice = foodItem != null ? foodItem.getPrice() : 0.0;
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

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getItemTotal() {
            return Math.round((unitPrice * quantity) * 100.0) / 100.0;
        }

        public void setItemTotal(double itemTotal) {
            // Ignored, computed from unitPrice * quantity
        }

        public String toJson() {
            return String.format(
                "{\"foodItem\":%s,\"quantity\":%d,\"unitPrice\":%.2f,\"itemTotal\":%.2f}",
                foodItem != null ? foodItem.toJson() : "null", quantity, unitPrice, getItemTotal()
            );
        }

        @Override
        public String toString() {
            return String.format("%s x %d ($%.2f)", foodItem != null ? foodItem.getName() : "Item", quantity, getItemTotal());
        }
    }

    private String orderId;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String paymentMethod;
    private String orderStatus;
    private String orderTime;
    private int estimatedDeliveryMinutes;
    private double deliveryFee;
    private double tax;
    private double totalAmount;
    private List<OrderItem> items;

    public Order() {
        this.items = new ArrayList<>();
        this.orderStatus = "CONFIRMED";
        this.deliveryFee = 2.99;
        this.paymentMethod = "Cash on Delivery";
        this.orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.estimatedDeliveryMinutes = 30;
    }

    public Order(String orderId, String customerName, String customerPhone, String deliveryAddress) {
        this();
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getEstimatedDeliveryMinutes() {
        return estimatedDeliveryMinutes;
    }

    public void setEstimatedDeliveryMinutes(int estimatedDeliveryMinutes) {
        this.estimatedDeliveryMinutes = estimatedDeliveryMinutes;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
        recalculateTotals();
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<OrderItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        recalculateTotals();
    }

    public void addItem(FoodItem item, int quantity) {
        if (item == null || quantity <= 0) return;
        for (OrderItem oi : items) {
            if (oi.getFoodItem().getId() == item.getId()) {
                oi.setQuantity(oi.getQuantity() + quantity);
                recalculateTotals();
                return;
            }
        }
        items.add(new OrderItem(item, quantity));
        recalculateTotals();
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (OrderItem oi : items) {
            subtotal += oi.getItemTotal();
        }
        return Math.round(subtotal * 100.0) / 100.0;
    }

    public void recalculateTotals() {
        double sub = getSubtotal();
        if (sub >= 30.0 || items.isEmpty()) {
            this.deliveryFee = 0.0; // Free delivery for orders over $30
        } else {
            this.deliveryFee = 2.99;
        }
        this.tax = Math.round((sub * 0.08) * 100.0) / 100.0; // 8% tax
        this.totalAmount = Math.round((sub + deliveryFee + tax) * 100.0) / 100.0;
    }

    public String toJson() {
        String itemsJson = items.stream()
            .map(OrderItem::toJson)
            .collect(Collectors.joining(","));
        return String.format(
            "{\"orderId\":\"%s\",\"customerName\":\"%s\",\"customerPhone\":\"%s\",\"deliveryAddress\":\"%s\",\"paymentMethod\":\"%s\",\"orderStatus\":\"%s\",\"orderTime\":\"%s\",\"estimatedDeliveryMinutes\":%d,\"subtotal\":%.2f,\"deliveryFee\":%.2f,\"tax\":%.2f,\"totalAmount\":%.2f,\"items\":[%s]}",
            escapeJson(orderId), escapeJson(customerName), escapeJson(customerPhone), escapeJson(deliveryAddress),
            escapeJson(paymentMethod), escapeJson(orderStatus), escapeJson(orderTime),
            estimatedDeliveryMinutes, getSubtotal(), deliveryFee, tax, totalAmount, itemsJson
        );
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", "");
    }

    @Override
    public String toString() {
        return String.format("Order[%s by %s | %d items | Total: $%.2f | Status: %s]",
            orderId, customerName, items.size(), totalAmount, orderStatus);
    }
}
