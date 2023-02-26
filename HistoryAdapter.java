package com.example.wagbaapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final LayoutInflater inflater;

    ArrayList<OrderHistoryModel> ordersModelsInternal;

    public HistoryAdapter(Context context, ArrayList<OrderHistoryModel> orders) {
        inflater = LayoutInflater.from(context);
        ordersModelsInternal = orders;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.rv_item_order,parent,false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        holder.orderNumber.setText("#" + ordersModelsInternal.get(position).getOrderNumber());
        holder.status.setText(ordersModelsInternal.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return ordersModelsInternal.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView orderNumber;
        TextView status;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            orderNumber = itemView.findViewById(R.id.orderNumber);
            status = itemView.findViewById(R.id.status);
        }
    }

}

