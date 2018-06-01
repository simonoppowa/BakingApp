package com.github.simonoppowa.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.simonoppowa.bakingapp.model.Recipe;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final String[] IMAGE_RESOURCE_STRINGS = new String[] {"recipe_image_url1", "recipe_image_url2", "recipe_image_url3", "recipe_image_url4", "recipe_image_ur5"};
    private static final int NUMBER_IMAGE_URLS = IMAGE_RESOURCE_STRINGS.length;

    private final Context context;

    private List<Recipe> mRecipeList;

    private final ListItemClickListener mListItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(Context context, List<Recipe> mRecipeList, ListItemClickListener mListItemClickListener) {
        this.context = context;
        this.mRecipeList = mRecipeList;
        this.mListItemClickListener = mListItemClickListener;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeTitleTextView.setText(mRecipeList.get(position).getName());

        //choosing image url
        int imageNum = position%NUMBER_IMAGE_URLS;
        int resId = context.getResources().getIdentifier(IMAGE_RESOURCE_STRINGS[imageNum], "string", context.getPackageName());

        Picasso.with(context)
                .load(context.getString(resId))
                .noPlaceholder()
                .error(R.drawable.default_recipe_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.recipeImageView);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_imageView)
        public ImageView recipeImageView;

        @BindView(R.id.recipe_name_textView)
        public TextView recipeTitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recipeImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    }
}
