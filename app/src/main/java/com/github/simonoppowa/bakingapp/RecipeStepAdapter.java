package com.github.simonoppowa.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.simonoppowa.bakingapp.model.RecipeStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private final Context context;

    private List<RecipeStep> mRecipeStepList;

    public RecipeStepAdapter(Context context, List<RecipeStep> recipeSteps) {
        this.context = context;
        this.mRecipeStepList = recipeSteps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_card_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeStepTitleTextView.setText(mRecipeStepList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mRecipeStepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_step_thumbnail_imageView)
        ImageView recipeStepImageView;

        @BindView(R.id.recipe_step_title_textView)
        TextView recipeStepTitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
