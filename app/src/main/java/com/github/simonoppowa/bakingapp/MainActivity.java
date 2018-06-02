package com.github.simonoppowa.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.simonoppowa.bakingapp.model.Recipe;
import com.github.simonoppowa.bakingapp.tests.SimpleIdlingResource;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener{

    public static final String recipeURLString = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final String RECIPE_KEY = "recipe";

    public static final int GRID_LAYOUT_COLUMN_COUNT_PORTRAIT = 1;
    public static final int GRID_LAYOUT_COLUMN_COUNT_LANDSCAPE = 3;

    @BindView(R.id.recipe_card_recyclerView)
    RecyclerView mRecipeRecyclerView;
    @BindView(R.id.loading_ProgressBar)
    ProgressBar mLoadingProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private RecipeAdapter mRecipeAdapter;

    public static List<Recipe> mRecipeList;

    @Nullable private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up Libraries
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        //creating mRecipeList
        mRecipeList = new ArrayList<>();

        //creating RecyclerView
        final int columns = chooseLayoutColumns();
        mGridLayoutManager = new GridLayoutManager(this, columns, GridLayoutManager.VERTICAL, false);
        mRecipeAdapter = new RecipeAdapter(this, mRecipeList, this);

        mRecipeRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        //checking for rotation
        if(savedInstanceState != null && savedInstanceState.containsKey(RECIPE_KEY)) {
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPE_KEY);
            mRecipeAdapter.setRecipeList((mRecipeList));
            showContent();
        } else {
            fetchRecipeList();
        }
    }

    /**
     * Fetches Recipe JSON from recipeURLString and creates ArrayList
     */
    private void fetchRecipeList() {
        showProgressBar();
        //using Volley to fetch JSON from Recipe URL
        StringRequest request = new StringRequest(Request.Method.GET, recipeURLString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d(response);

                //using GSON to create Recipe List
                Gson gson = new Gson();
                mRecipeList = new ArrayList<>(Arrays.asList(
                        gson.fromJson(response, Recipe[].class)
                ));

                mRecipeAdapter.setRecipeList(mRecipeList);
                showContent();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //show errorSnackbar
                Snackbar errorSnackbar = Snackbar.make(mRecipeRecyclerView, getString(R.string.no_connection_error_message), Snackbar.LENGTH_INDEFINITE);
                errorSnackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchRecipeList();
                    }
                })
                        .setActionTextColor(Color.RED)
                        .show();
                showContent();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

    }

    private void showContent() {
        //setting idling
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
        mLoadingProgressBar.setVisibility(View.GONE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mRecipeRecyclerView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    private int chooseLayoutColumns() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return GRID_LAYOUT_COLUMN_COUNT_PORTRAIT;
        } else {
            return GRID_LAYOUT_COLUMN_COUNT_LANDSCAPE;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_KEY, (ArrayList<? extends Parcelable>) mRecipeList);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        //checking if Activity was launched as config for IngredientsWidget
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            int widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
            setupWidget(widgetId, mRecipeList.get(clickedItemIndex));
            return;
        }

        //if MainActivity wasn't launched for configuration, start RecipeActivity
        Intent recipeIntent = new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra(RECIPE_KEY, mRecipeList.get(clickedItemIndex));
        startActivity(recipeIntent);
    }

    /**
     * Configure the static Widget with the clicked Recipe
     * @param widgetId Id of the created Widget to configure
     * @param clickedRecipe clicked Recipe
     */
    private void setupWidget(int widgetId, Recipe clickedRecipe) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.ingredients_widget);

            views.setTextViewText(R.id.recipe_widget_title_textView, clickedRecipe.getName());

            views.setTextViewText(R.id.recipe_widget_ingredients_textView, clickedRecipe.getIngredientNamesListString());

            appWidgetManager.updateAppWidget(widgetId, views);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            setResult(RESULT_OK, resultValue);
            finish();
    }

    /**
     * Only for testing, creates and returns new {@link SimpleIdlingResource}
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if(mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
