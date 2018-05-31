package com.github.simonoppowa.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.simonoppowa.bakingapp.IngredientAdapter;
import com.github.simonoppowa.bakingapp.R;
import com.github.simonoppowa.bakingapp.RecipeStepAdapter;
import com.github.simonoppowa.bakingapp.model.Recipe;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.simonoppowa.bakingapp.RecipeActivity.RECIPE_KEY;

public class RecipeInfoFragment extends Fragment {

    public static final String INGREDIENTS_KEY = "ingredients";
    public static final String CLICKED_RECIPE_STEP_KEY = "clickedRecipeStep";

    @BindView(R.id.ingredient_card_recyclerView)
    RecyclerView mIngredientsRecyclerView;
    private LinearLayoutManager mIngredientsLinearLayoutManager;
    private IngredientAdapter mIngredientAdapter;

    @BindView(R.id.recipe_step_card_recyclerView)
    RecyclerView mRecipeStepRecyclerView;
    private LinearLayoutManager mRecipeStepsLinearLayoutManager;
    private RecipeStepAdapter mRecipeStepAdapter;

    private Recipe mRecipe;

    private Context mContext;

    public RecipeInfoFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_info, container, false);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getting passed Recipe
        Bundle bundle = getArguments();
        if(bundle != null) {
            mRecipe = bundle.getParcelable(RECIPE_KEY);
        } else {
            throw new NullPointerException("No Recipe was passed to RecipeInfoFragment");
        }

        //creating Ingredients RecyclerView
        mIngredientsLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mIngredientAdapter = new IngredientAdapter(mContext, Arrays.asList(mRecipe.getIngredients()));

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLinearLayoutManager);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

        //creating RecipeSteps RecyclerView
        mRecipeStepsLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecipeStepAdapter = new RecipeStepAdapter(mContext, Arrays.asList(mRecipe.getRecipeSteps()), (RecipeStepAdapter.RecipeItemClickListener) getActivity());

        mRecipeStepRecyclerView.setLayoutManager(mRecipeStepsLinearLayoutManager);
        mRecipeStepRecyclerView.setAdapter(mRecipeStepAdapter);

        //checking for rotation
        if(savedInstanceState != null && savedInstanceState.containsKey(INGREDIENTS_KEY)) {
            mIngredientAdapter.setCheckedIngredients(savedInstanceState.getBooleanArray(INGREDIENTS_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(INGREDIENTS_KEY, mIngredientAdapter.getCheckedIngredients());
    }

}
