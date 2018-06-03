package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.simonoppowa.bakingapp.fragments.RecipeInfoFragment;
import com.github.simonoppowa.bakingapp.fragments.RecipeVideoFragment;
import com.github.simonoppowa.bakingapp.model.Recipe;
import com.github.simonoppowa.bakingapp.model.RecipeStep;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.simonoppowa.bakingapp.fragments.RecipeInfoFragment.CLICKED_RECIPE_STEP_KEY;

public class RecipeActivity extends AppCompatActivity implements RecipeStepAdapter.RecipeItemClickListener{

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

        //replacing FrameLayoutContainer with RecipeInfoFragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_info_container, recipeInfoFragment)
                .commit();

        //checking if Tablet layout
        if(findViewById(R.id.recipe_video_step_container) != null) {
            //checking for rotation
            if(savedInstanceState == null) {
                //getting default RecipeStep
                RecipeStep defaultRecipeStep = mRecipe.getRecipeSteps()[0];

                //setting up RecipeVideoFragment
                RecipeVideoFragment recipeVideoFragment = new RecipeVideoFragment();
                bundle = new Bundle();
                bundle.putParcelable(RECIPE_STEP_KEY, defaultRecipeStep);
                recipeVideoFragment.setArguments(bundle);

                //replacing FrameLayoutContainer with RecipeVideoFragment
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_video_step_container, recipeVideoFragment)
                        .commit();
            } else {
                //replacing the container with restored RecipeVideoFragment
                RecipeVideoFragment recipeVideoFragment = (RecipeVideoFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_video_step_container);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_video_step_container, recipeVideoFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onListItemClick(int clickedPosition) {

        RecipeStep[] clickedRecipeSteps = mRecipe.getRecipeSteps();

        //checking if Tablet layout
        if(findViewById(R.id.recipe_video_step_container) != null) {

            //setting up new RecipeVideoFragment
            RecipeVideoFragment recipeVideoFragment = new RecipeVideoFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE_STEP_KEY, clickedRecipeSteps[clickedPosition]);
            recipeVideoFragment.setArguments(bundle);

            //replacing existing RecipeVideoFragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipe_video_step_container, recipeVideoFragment)
                    .commit();
        } else {
            //launching new RecipeStepActivity
            Intent recipeStepIntent = new Intent(this, RecipeStepActivity.class);
            recipeStepIntent.putParcelableArrayListExtra(RECIPE_STEP_KEY, new ArrayList<>(Arrays.asList(clickedRecipeSteps)));
            recipeStepIntent.putExtra(CLICKED_RECIPE_STEP_KEY, clickedPosition);

            startActivity(recipeStepIntent);
        }

    }
}
