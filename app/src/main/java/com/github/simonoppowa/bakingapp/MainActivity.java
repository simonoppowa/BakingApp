package com.github.simonoppowa.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.simonoppowa.bakingapp.model.Recipe;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener{

    @BindView(R.id.recipe_card_recyclerView)
    RecyclerView mRecipeRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecipeAdapter mRecipeAdapter;

    public static final String recipeURLString = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static List<Recipe> mRecipeList;

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
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        mRecipeAdapter = new RecipeAdapter(this, mRecipeList, this);

        mRecipeRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        fetchRecipeList();

    }

    /**
     * Fetches Recipe JSON from recipeURLString and creates ArrayList
     */
    private void fetchRecipeList() {
        //using Volley to fetch JSON from Recipe URL
        StringRequest request = new StringRequest(Request.Method.GET, recipeURLString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //using GSON to create Recipe List
                Gson gson = new Gson();
                mRecipeList = Arrays.asList(
                        gson.fromJson(response, Recipe[].class)
                );
                Timber.d(String.valueOf(mRecipeList.size()));

                mRecipeAdapter.setRecipeList(mRecipeList);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO handle error case
                Timber.d(error.getMessage());
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
