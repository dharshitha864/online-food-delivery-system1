package com.fooddelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a restaurant with cuisine, rating, delivery time, and menu items.
 */
public class Restaurant {
    private int id;
    private String name;
    private String cuisine;
    private double rating;
    private String deliveryTime;
    private List<FoodItem> foodItems;

    public Restaurant() {
        this.foodItems = new ArrayList<>();
    }

    public Restaurant(int id, String name, String cuisine, double rating, String deliveryTime) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.foodItems = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<FoodItem> getFoodItems() {
        return Collections.unmodifiableList(foodItems);
    }

    public void addFoodItem(FoodItem item) {
        if (item != null && !foodItems.contains(item)) {
            foodItems.add(item);
        }
    }

    public void removeFoodItem(FoodItem item) {
        foodItems.remove(item);
    }

    public FoodItem getFoodItemById(int itemId) {
        for (FoodItem item : foodItems) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    /**
     * Serializes this restaurant to a JSON string.
     */
    public String toJson() {
        String itemsJson = foodItems.stream()
            .map(FoodItem::toJson)
            .collect(Collectors.joining(","));
        return String.format(
            "{\"id\":%d,\"name\":\"%s\",\"cuisine\":\"%s\",\"rating\":%.1f,\"deliveryTime\":\"%s\",\"foodItems\":[%s]}",
            id, escapeJson(name), escapeJson(cuisine), rating, escapeJson(deliveryTime), itemsJson
        );
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Restaurant[#%d %s (%s) - ★%.1f | %s | %d items]", id, name, cuisine, rating, deliveryTime, foodItems.size());
    }
}
