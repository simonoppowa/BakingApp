package com.github.simonoppowa.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityTests {

    private static final String TEST_RECIPE_NAME = "Nutella Pie";
    private static final int TEST_RECIPE_POSITION = 0;

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();

        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkRecipeNameMatches() {
      onView(withText(TEST_RECIPE_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void selectRecipeCardView_click_launchesRecipeActivity() {

        onView(withId(R.id.recipe_card_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(TEST_RECIPE_POSITION, click()));

        onView(withId(R.id.ingredients_title_TextView)).check(matches(withText(mActivityTestRule.getActivity().getString(R.string.ingredients_title))));
    }

    @Test
    public void selectRecipeCardView_click_selectsCorrectRecipe() {

        onView(withId(R.id.recipe_card_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(TEST_RECIPE_POSITION, click()));

        onView(withText(TEST_RECIPE_NAME)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if(mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
