package com.github.simonoppowa.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.simonoppowa.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final Context context;

    private List<Ingredient> mIngredientList;

    public IngredientAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.mIngredientList = ingredientList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_card_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ingredientNameTextView.setText(mIngredientList.get(position).getIngredientName());
        holder.ingredientQuantity.setText(mIngredientList.get(position).getQuantity());
        holder.ingredientMeasureTextView.setText(mIngredientList.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name_TextView)
        TextView ingredientNameTextView;

        @BindView(R.id.ingredient_quantity_TextView)
        TextView ingredientQuantity;

        @BindView(R.id.ingredient_measure_TextView)
        TextView ingredientMeasureTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
