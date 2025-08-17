package com.example.classroomannouncement.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.classroomannouncement.Database.Entities.Product;
import com.example.classroomannouncement.Database.Repositories.ProductRepo;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final ProductRepo productRepo;
    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepo = new ProductRepo(application);
        allProducts = productRepo.getAllProducts();
    }

    public LiveData<Product> getProductById(int productId) {
        return productRepo.getProductById(productId);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(Product product) {
        productRepo.insert(product);
    }

    public void update(Product product) {
        productRepo.update(product);
    }

    public void delete(int productId) {
        productRepo.delete(productId);
    }
}