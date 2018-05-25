package com.github.simonoppowa.bakingapp.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    private String quantity;
    private String measure;
    @SerializedName("ingredient")
    private String ingredientName;


    public Ingredient(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredient) {
        this.ingredientName = ingredient;
    }
}
