package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.simonoppowa.bakingapp.model.Recipe;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.simonoppowa.bakingapp.MainActivity.RECIPE_KEY;

public class RecipeActivity extends AppCompatActivity implements RecipeStepAdapter.RecipeItemClickListener{

    public static final String INGREDIENTS_KEY = "ingredients";
    public static final String RECIPE_STEP_KEY = "recipeStep";

    @BindView(R.id.ingredient_card_recyclerView)
    RecyclerView mIngredientsRecyclerView;
    private LinearLayoutManager mIngredientsLinearLayoutManager;
    private IngredientAdapter mIngredientAdapter;

    @BindView(R.id.recipe_step_card_recyclerView)
    RecyclerView mRecipeStepRecyclerView;
    private LinearLayoutManager mRecipeStepsLinearLayoutManager;
    private RecipeStepAdapter mRecipeStepAdapter;

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

        //creating Ingredients RecyclerView
        mIngredientsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mIngredientAdapter = new IngredientAdapter(this, Arrays.asList(mRecipe.getIngredients()));

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLinearLayoutManager);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

        //creating RecipeSteps RecyclerView
        mRecipeStepsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecipeStepAdapter = new RecipeStepAdapter(this, Arrays.asList(mRecipe.getRecipeSteps()), this);

        mRecipeStepRecyclerView.setLayoutManager(mRecipeStepsLinearLayoutManager);
        mRecipeStepRecyclerView.setAdapter(mRecipeStepAdapter);

        //checking for rotation
        if(savedInstanceState != null && savedInstanceState.containsKey(INGREDIENTS_KEY)) {
            mIngredientAdapter.setCheckedIngredients(savedInstanceState.getBooleanArray(INGREDIENTS_KEY));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(INGREDIENTS_KEY, mIngredientAdapter.getCheckedIngredients());
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        Intent recipeStepIntent = new Intent(this, RecipeStepActivity.class);

        recipeStepIntent.putExtra(RECIPE_STEP_KEY, mRecipe.getRecipeSteps()[clickedPosition]);

        startActivity(recipeStepIntent);
    }
}
