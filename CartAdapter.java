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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final LayoutInflater inflater;

    ArrayList<CartModel> cartModelsInternal;
    ClickListener listener;

    public CartAdapter(Context context, ArrayList<CartModel> cart, ClickListener listener) {
        inflater = LayoutInflater.from(context);
        cartModelsInternal = cart;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.rv_item_cart,parent,false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.name.setText(cartModelsInternal.get(position).getName());
        holder.quantity.setText(String.valueOf(cartModelsInternal.get(position).getQuantity()));

        holder.rmvFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listener.click(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelsInternal.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView quantity;
        Button rmvFromCart;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textViewDishName);
            quantity = itemView.findViewById(R.id.textViewQuantity);
            rmvFromCart = itemView.findViewById(R.id.rmvFromCartBtn);
        }
    }

}

