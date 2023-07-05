package com.bankerisemock.product.repository;

import com.bankerisemock.product.dao.ProductDao;
import com.bankerisemock.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("jpa")
public class ProductJPADataAccessService implements ProductDao {
    private final ProductRepository productRepository;

    public ProductJPADataAccessService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> selectAllProducts() {
        Page<Product> page = productRepository.findAll(Pageable.ofSize(500));
        return page.getContent();
    }

    @Override
    public Optional<Product> selectProductById(Integer productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void insertProduct(Product product) {
        productRepository.save(product);

    }

    @Override
    public boolean existsProductById(Integer productId) {
        return productRepository.existsById(productId);
    }




    @Override
    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);

    }

    @Override
    public void updateProduct(Product newProduct) {
        productRepository.save(newProduct);

    }

    @Override
    public boolean existsProductByCode(String code) {
        return productRepository.existsProductByCode(code);
    }

    @Override
    public boolean existsProductByName(String name) {
        return productRepository.existsProductByName(name);
    }
}
