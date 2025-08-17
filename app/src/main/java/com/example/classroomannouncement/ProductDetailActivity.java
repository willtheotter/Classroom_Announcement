package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.classroomannouncement.Database.Entities.Product;
import com.example.classroomannouncement.viewmodels.ProductViewModel;

public class ProductDetailActivity extends AppCompatActivity {
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            productViewModel.getProductById(productId).observe(this, this::displayProduct);
        }
    }

    private void displayProduct(Product product) {
        if (product != null) {
            TextView nameView = findViewById(R.id.productName);
            TextView priceView = findViewById(R.id.productPrice);
            TextView descriptionView = findViewById(R.id.productDescription);
            TextView stockView = findViewById(R.id.productStock);
            TextView categoryView = findViewById(R.id.productCategory);

            nameView.setText(product.getName());
            priceView.setText(String.format("Price: $%.2f", product.getPrice()));
            descriptionView.setText(product.getDescription());

            // For primitive int (can't be null)
            stockView.setText(String.format("Stock: %d", product.getStockQuantity()));

            // Only check null for String fields
            if (product.getCategory() != null && !product.getCategory().isEmpty()) {
                categoryView.setText(String.format("Category: %s", product.getCategory()));
            } else {
                categoryView.setText("Category: Not specified");
            }
        }
    }
}