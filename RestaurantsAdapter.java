package com.example.wagbaapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>  {

    private final LayoutInflater inflater;

    ArrayList<RestaurantsModel> restaurantsModelsInternal;
    ClickListener listener;

    public RestaurantsAdapter(Context context, ArrayList<RestaurantsModel> restaurants, ClickListener listener) {
        inflater = LayoutInflater.from(context);
        restaurantsModelsInternal = restaurants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.rv_item_restaurant,parent,false);
        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        holder.name.setText(restaurantsModelsInternal.get(position).getName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listener.click(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantsModelsInternal.size();
    }

    public class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public RestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewRestaurant);
        }

    }

}