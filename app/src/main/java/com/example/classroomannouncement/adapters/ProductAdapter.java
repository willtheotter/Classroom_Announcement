package com.example.classroomannouncement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classroomannouncement.Database.Entities.Product;
import com.example.classroomannouncement.R;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productPrice;
        public TextView productStock;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productStock = itemView.findViewById(R.id.productStock);
        }

        public void bind(final Product product, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(product));
        }
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (products != null) {
            Product current = products.get(position);
            holder.productName.setText(current.getName());
            holder.productPrice.setText(String.format("$%.2f", current.getPrice()));
            holder.productStock.setText(String.format("Stock: %d", current.getStockQuantity()));
            holder.bind(current, listener);
        }
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }
}