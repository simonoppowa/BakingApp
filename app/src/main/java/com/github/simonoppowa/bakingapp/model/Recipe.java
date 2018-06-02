package com.github.simonoppowa.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Recipe implements Parcelable{

    private int id;
    private String name;
    private Ingredient[] ingredients;
    @SerializedName("steps")
    private RecipeStep[] recipeSteps;
    private int servings;
    private String imagePath;

    public Recipe(int id, String name, Ingredient[] ingredients, RecipeStep[] recipeSteps, int servings, String imagePath) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.recipeSteps = recipeSteps;
        this.servings = servings;
        this.imagePath = imagePath;
    }

    //Parcelable constructor
    private Recipe(Parcel input) {
        id = input.readInt();
        name = input.readString();

        Parcelable[] ingredientsParcelableArray = input.readParcelableArray(Ingredient.class.getClassLoader());
        if(ingredientsParcelableArray != null) {
            ingredients = Arrays.copyOf(ingredientsParcelableArray, ingredientsParcelableArray.length, Ingredient[].class);
        }

        Parcelable[] recipeStepsParcelableArray = input.readParcelableArray(RecipeStep.class.getClassLoader());
        if(recipeStepsParcelableArray != null) {
            recipeSteps = Arrays.copyOf(recipeStepsParcelableArray, recipeStepsParcelableArray.length, RecipeStep[].class);
        }

        servings = input.readInt();
        imagePath = input.readString();
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

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Creates a String with all ingredients separated by line breaks
     * @return A String which contains all ingredients
     */
    public String getIngredientNamesListString() {
        StringBuilder ingredientNamesListString = new StringBuilder();

        for (Ingredient ingredient : ingredients) {
            ingredientNamesListString.append("\u25CF ")
                    .append(ingredient.getIngredientName())
                    .append("\n");
        }
        return ingredientNamesListString.toString();
    }

    public RecipeStep[] getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(RecipeStep[] recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeParcelableArray(ingredients, 0);
        parcel.writeParcelableArray(recipeSteps, 0);
        parcel.writeInt(servings);
        parcel.writeString(imagePath);
    }



    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };

}
