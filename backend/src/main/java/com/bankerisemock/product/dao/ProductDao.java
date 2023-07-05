package com.bankerisemock.product.dao;

import com.bankerisemock.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    public List<Product> selectAllProducts();
    public Optional<Product> selectProductById(Integer productId);
    public void insertProduct(Product product);

    public boolean existsProductById(Integer productId);
    public void deleteProductById(Integer productId);
    public void updateProduct(Product newProduct);

    public boolean existsProductByCode(String code);
    public boolean existsProductByName(String name);


}
