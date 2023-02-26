package com.example.wagbaapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishesViewHolder> {

    private final LayoutInflater inflater;

    ArrayList<DishesModel> dishesModelsInternal;
    ClickListener listener;

    public DishesAdapter(Context context, ArrayList<DishesModel> dishes, ClickListener listener) {
        inflater = LayoutInflater.from(context);
        dishesModelsInternal = dishes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.rv_item_dish,parent,false);
        return new DishesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishesViewHolder holder, int position) {
        holder.name.setText(dishesModelsInternal.get(position).getName());
        holder.price.setText(String.format("%.2f", dishesModelsInternal.get(position).getPrice()));

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listener.click(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishesModelsInternal.size();
    }

    public class DishesViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView price;
        Button addToCart;


        public DishesViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textViewDishName);
            price = itemView.findViewById(R.id.textViewPrice);
            addToCart = itemView.findViewById(R.id.addToCartBtn);
        }
    }

}

