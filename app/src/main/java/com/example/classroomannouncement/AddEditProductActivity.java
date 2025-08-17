package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.classroomannouncement.Database.Entities.Product;
import com.example.classroomannouncement.R;
import com.example.classroomannouncement.viewmodels.ProductViewModel;

public class AddEditProductActivity extends AppCompatActivity {
    private EditText editName, editDescription, editPrice, editImageUrl, editStockQuantity, editCategory;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        initViews();
        setupSaveButton();
    }

    private void initViews() {
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        editImageUrl = findViewById(R.id.editImageUrl);
        editStockQuantity = findViewById(R.id.editStockQuantity);
        editCategory = findViewById(R.id.editCategory);
    }

    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(view -> {
            if (validateInputs()) {
                saveProduct();
                finish();
            }
        });
    }

    private boolean validateInputs() {
        if (editName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editPrice.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Price is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveProduct() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        double price = Double.parseDouble(editPrice.getText().toString().trim());
        String imageUrl = editImageUrl.getText().toString().trim();
        int stockQuantity = editStockQuantity.getText().toString().isEmpty() ?
                0 : Integer.parseInt(editStockQuantity.getText().toString().trim());
        String category = editCategory.getText().toString().trim();

        Product product = new Product(name, description, price, imageUrl, stockQuantity, category);
        productViewModel.insert(product);
    }
}