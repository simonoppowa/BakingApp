package com.github.simonoppowa.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
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

    private SparseBooleanArray mCheckedItems;

    public IngredientAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.mIngredientList = ingredientList;
        this.mCheckedItems = new SparseBooleanArray();
    }

    public boolean[] getCheckedIngredients() {
        boolean[] checkedItems = new boolean[mIngredientList.size()];

        for(int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = mCheckedItems.get(i);
        }
        return checkedItems;
    }

    public void setCheckedIngredients(boolean[] checkedIngredientsArray) {
        for(int i = 0; i < checkedIngredientsArray.length; i++) {
            mCheckedItems.put(i, checkedIngredientsArray[i]);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_card_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.ingredientNameTextView.setText(mIngredientList.get(position).getIngredientName());
        holder.ingredientQuantity.setText(mIngredientList.get(position).getQuantity());
        holder.ingredientMeasureTextView.setText(mIngredientList.get(position).getMeasure());

        boolean isChecked = mCheckedItems.get(position, false);
        final View selectedView = holder.ingredientItemCardView;

        if(isChecked) {
            selectedView.setBackgroundColor(context.getResources().getColor(R.color.cardItemChecked));
        } else {
            selectedView.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
        }

        //set onClickListener to CardView
        selectedView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isChecked = mCheckedItems.get(position);
                if(isChecked) {
                    selectedView.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                } else {
                    selectedView.setBackgroundColor(context.getResources().getColor(R.color.cardItemChecked));
                }
                mCheckedItems.put(position, !mCheckedItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_card_item)
        CardView ingredientItemCardView;

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
