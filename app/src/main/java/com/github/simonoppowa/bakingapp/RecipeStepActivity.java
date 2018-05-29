package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.simonoppowa.bakingapp.fragments.RecipeVideoFragment;
import com.github.simonoppowa.bakingapp.model.RecipeStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.simonoppowa.bakingapp.RecipeActivity.RECIPE_STEP_KEY;

public class RecipeStepActivity extends AppCompatActivity {

    private List<RecipeStep> mRecipeSteps;

    @BindView(R.id.recipe_step_viewPager)
    ViewPager mRecipeStepViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        //getting RecipeStep from Intent
        Intent recipeStepIntent = getIntent();

        mRecipeSteps = recipeStepIntent.getParcelableArrayListExtra(RECIPE_STEP_KEY);

        if (mRecipeSteps == null) {
            throw new NullPointerException("No RecipeStep was passed to RecipeStepActivity");
        }


        mRecipeStepViewPager.setAdapter(new RecipeStepPagerAdapter(getSupportFragmentManager()));

    }




    private class RecipeStepPagerAdapter extends FragmentPagerAdapter {

        public RecipeStepPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return RecipeVideoFragment.newInstance(mRecipeSteps.get(position));
        }

        @Override
        public int getCount() {
            return mRecipeSteps.size();
        }
    }
}
