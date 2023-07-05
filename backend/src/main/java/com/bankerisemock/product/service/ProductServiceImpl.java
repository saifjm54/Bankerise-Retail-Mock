package com.bankerisemock.product.service;

import com.bankerisemock.product.dao.ProductDao;
import com.bankerisemock.product.dto.ProductDTO;
import com.bankerisemock.product.dto.ProductDTOMapper;
import com.bankerisemock.product.dto.ProductRequest;
import com.bankerisemock.product.exception.RequestException;
import com.bankerisemock.product.exception.ResourceNotFoundException;
import com.bankerisemock.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductDao repository;
    private final ProductDTOMapper productDTOMapper;

    public ProductServiceImpl(@Qualifier("jdbc") ProductDao repository,ProductDTOMapper productDTOMapper) {
        this.repository = repository;
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return repository.selectAllProducts().stream().map(productDTOMapper).collect(Collectors.toList());
    }
    @Override
    public void addProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setCode(productRequest.code());
        product.setName(productRequest.name());
        repository.insertProduct(product);
    }

    @Override
    public Optional<ProductDTO> getProduct(Integer productId) {
        return Optional.ofNullable(repository.selectProductById(productId).map(productDTOMapper).orElseThrow(() -> new ResourceNotFoundException(
                "product with id [%s] not found".formatted(productId)
        )));
    }

    @Override
    public void deleteProduct(Integer productId) {
        checkIfProductExistsOrThrow(productId);
        repository.deleteProductById(productId);
    }

    @Override
    public void checkIfProductExistsOrThrow(Integer productId) {
          if(!repository.existsProductById(productId)){
              throw new ResourceNotFoundException("product with id [%s] not found".formatted(productId));
          }
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest update) {
        Product product = repository.selectProductById(productId).orElseThrow(() -> new ResourceNotFoundException("product with id [%s] not found".formatted(productId)));
        boolean changes = false;
        if (update.name() !=null && !update.name().equals(product.getName())){
            product.setName(update.name());
            changes = true;
        }
        if(update.code() != null && !update.code().equals(product.getCode())){
            product.setCode(update.code());
            changes = true;
        }
        if(!changes){
            throw new RequestException("no data changes found");
        }
        repository.updateProduct(product);
    }
}
