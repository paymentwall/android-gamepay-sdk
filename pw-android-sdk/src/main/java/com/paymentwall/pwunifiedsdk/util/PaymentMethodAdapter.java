package com.paymentwall.pwunifiedsdk.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.payalto.core.PayAltoCore.PayAltoMethod;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final List<PayAltoMethod> listMethod;
    private final OnItemClickListener listener;
    private final Context context;


    public PaymentMethodAdapter(Context context, List<PayAltoMethod> itemList, OnItemClickListener listener) {
        this.listMethod = itemList;
        this.listener = listener;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icMethod);
            textView = itemView.findViewById(R.id.tvMethodName);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_method, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PayAltoMethod item = listMethod.get(position);
        holder.textView.setText(item.name);
        if (item.imgUrl != null && !item.imgUrl.isEmpty()) {
            Glide.with(context).load(item.imgUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_card_new);
        }
    }

    @Override
    public int getItemCount() {
        return listMethod.size();
    }
}