package com.github.simonoppowa.bakingapp;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.github.simonoppowa.bakingapp.model.Ingredient;
import com.github.simonoppowa.bakingapp.model.Recipe;
import com.github.simonoppowa.bakingapp.model.RecipeStep;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.simonoppowa.bakingapp.RecipeActivity.RECIPE_KEY;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntentTests {

    private static final int FAKE_RECIPE_ID = 1;
    private static final String FAKE_RECIPE_NAME = "Cake";

    private static final Ingredient[] FAKE_INGREDIENTS = {new Ingredient("1", "TSP", ""),
            new Ingredient("2", "", "")};

    private static final RecipeStep[] FAKE_RECIPE_STEPS = {new RecipeStep(1, "", "", "", "")};

    private Recipe mTestRecipe;

    @Rule
    public IntentsTestRule<RecipeActivity> mActivityRule = new IntentsTestRule<>(RecipeActivity.class, false, false);


    @Before
    public void createFakeRecipe() {
        mTestRecipe = new Recipe(FAKE_RECIPE_ID, FAKE_RECIPE_NAME, FAKE_INGREDIENTS, FAKE_RECIPE_STEPS, 0, "");
    }

    @Test
    public void startRecipeActivity_checkFakeRecipe() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), RecipeActivity.class);
        intent.putExtra(RECIPE_KEY, mTestRecipe);

        mActivityRule.launchActivity(intent);

        onView(withText(mTestRecipe.getName())).check(matches(isDisplayed()));
    }

}
