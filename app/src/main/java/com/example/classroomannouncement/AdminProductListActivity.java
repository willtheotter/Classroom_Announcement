package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classroomannouncement.Database.Entities.Product;
import com.example.classroomannouncement.adapters.ProductAdapter;
import com.example.classroomannouncement.viewmodels.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminProductListActivity extends AppCompatActivity {
    private ProductViewModel productViewModel;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);

        // Initialize ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(product -> {
            // Handle item click
            openProductDetailActivity(product);
        });
        recyclerView.setAdapter(adapter);

        // Observe products from ViewModel
        productViewModel.getAllProducts().observe(this, products -> {
            adapter.setProducts(products);
        });

        // Setup Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            openAddProductActivity();
        });
    }

    private void openAddProductActivity() {
        Intent intent = new Intent(this, AddEditProductActivity.class);
        startActivity(intent);
    }

    private void openProductDetailActivity(Product product) {
        Intent intent = new Intent(this, AdminProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }
}