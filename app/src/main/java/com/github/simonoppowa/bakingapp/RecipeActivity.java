package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.simonoppowa.bakingapp.model.Recipe;

import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.simonoppowa.bakingapp.MainActivity.*;

public class RecipeActivity extends AppCompatActivity {

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        //get Recipe form Intent
        Intent recipeIntent = getIntent();

        mRecipe = recipeIntent.getParcelableExtra(RECIPE_KEY);

        if(mRecipe == null) {
            throw new NullPointerException("No Recipe was passed to RecipeActivity");
        }

        //set Title
        setTitle(mRecipe.getName());
    }
}
