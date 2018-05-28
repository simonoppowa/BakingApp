package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.simonoppowa.bakingapp.model.RecipeStep;

import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.simonoppowa.bakingapp.RecipeActivity.RECIPE_STEP_KEY;

public class RecipeStepActivity extends AppCompatActivity {

    private RecipeStep mRecipeStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        //getting RecipeStep from Intent
        Intent recipeStepIntent = getIntent();

        mRecipeStep = recipeStepIntent.getParcelableExtra(RECIPE_STEP_KEY);

        if (mRecipeStep == null) {
            throw new NullPointerException("No RecipeStep was passed to RecipeStepActivity");
        }

        //setting title
        String title = (mRecipeStep.getId() + 1) + ". " + mRecipeStep.getShortDescription();
        setTitle(title);
    }
}
