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
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

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

        //TODO change default image
        Picasso.with(context)
                .load("https://images.pexels.com/photos/952724/pexels-photo-952724.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
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
