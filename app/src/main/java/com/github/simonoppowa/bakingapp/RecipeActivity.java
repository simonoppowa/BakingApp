package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.simonoppowa.bakingapp.fragments.RecipeInfoFragment;
import com.github.simonoppowa.bakingapp.model.Recipe;

import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe";
    public static final String RECIPE_STEP_KEY = "recipeStep";

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        //getting Recipe form Intent
        Intent recipeIntent = getIntent();

        mRecipe = recipeIntent.getParcelableExtra(RECIPE_KEY);

        if (mRecipe == null) {
            throw new NullPointerException("No Recipe was passed to RecipeActivity");
        }

        //setting Title
        setTitle(mRecipe.getName());

        //setting up RecipeInfoFragment
        RecipeInfoFragment recipeInfoFragment = new RecipeInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_KEY, mRecipe);
        recipeInfoFragment.setArguments(bundle);

        //replacing FrameLayout with Fragment
        getFragmentManager().beginTransaction()
                .add(R.id.recipe_info_container, recipeInfoFragment)
                .commit();
    }
}
