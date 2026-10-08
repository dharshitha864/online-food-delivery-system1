package com.fooddelivery;

import java.util.Objects;

/**
 * Represents a food item in the menu.
 */
public class FoodItem {
    private int id;
    private String name;
    private String category;
    private double price;
    private String restaurantName;
    private String description;
    private String emoji;

    public FoodItem() {
    }

    public FoodItem(int id, String name, String category, double price, String restaurantName, String description) {
        this(id, name, category, price, restaurantName, description, "🍽️");
    }

    public FoodItem(int id, String name, String category, double price, String restaurantName, String description, String emoji) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.restaurantName = restaurantName;
        this.description = description;
        this.emoji = emoji != null ? emoji : "🍽️";
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    /**
     * Serializes this food item to a JSON object string.
     */
    public String toJson() {
        return String.format(
            "{\"id\":%d,\"name\":\"%s\",\"category\":\"%s\",\"price\":%.2f,\"restaurantName\":\"%s\",\"description\":\"%s\",\"emoji\":\"%s\"}",
            id, escapeJson(name), escapeJson(category), price, escapeJson(restaurantName), escapeJson(description), escapeJson(emoji)
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
        FoodItem foodItem = (FoodItem) o;
        return id == foodItem.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("FoodItem[#%d %s (%s) - $%.2f | %s]", id, name, category, price, restaurantName);
    }
}
