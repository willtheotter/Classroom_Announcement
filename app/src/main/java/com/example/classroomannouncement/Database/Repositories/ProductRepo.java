package com.example.classroomannouncement.Database.Repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.classroomannouncement.Database.AppDatabase;
import com.example.classroomannouncement.Database.Daos.ProductDao;
import com.example.classroomannouncement.Database.Entities.Product;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepo {
    private final ProductDao productDao;
    private final LiveData<List<Product>> allProducts;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ProductRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<Product> getProductById(int productId) {
        return productDao.getProductById(productId);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(Product product) {
        executor.execute(() -> productDao.insert(product));
    }

    public void update(Product product) {
        executor.execute(() -> productDao.update(product));
    }

    public void delete(int productId) {
        executor.execute(() -> productDao.delete(productId));
    }
}