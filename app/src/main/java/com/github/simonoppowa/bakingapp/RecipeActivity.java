package com.github.simonoppowa.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.github.simonoppowa.bakingapp.fragments.RecipeInfoFragment;
import com.github.simonoppowa.bakingapp.fragments.RecipeVideoFragment;
import com.github.simonoppowa.bakingapp.model.Recipe;
import com.github.simonoppowa.bakingapp.model.RecipeStep;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static java.util.Arrays.asList;

public class RecipeActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe";
    public static final String RECIPE_STEP_KEY = "recipeStep";

    private int mClickedRecipeStep;
    private List<RecipeStep> mRecipeSteps;

    @BindView(R.id.recipe_step_viewPager)
    ViewPager mRecipeStepViewPager;

    private RecipeVideoFragment mCurrentRecipeVideoFragment;

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


        mRecipeSteps = Arrays.asList(mRecipe.getRecipeSteps()[0]);


        //ViewPager
        mRecipeStepViewPager.setAdapter(new RecipeStepPagerAdapter(getSupportFragmentManager()));
        mRecipeStepViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(mCurrentRecipeVideoFragment != null) {
                    mCurrentRecipeVideoFragment.pausePlayer();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public class RecipeStepPagerAdapter extends FragmentPagerAdapter {

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

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            //setting new active fragment
            if (mCurrentRecipeVideoFragment != object) {
                mCurrentRecipeVideoFragment = (RecipeVideoFragment) object;
            }
        }
    }

}
