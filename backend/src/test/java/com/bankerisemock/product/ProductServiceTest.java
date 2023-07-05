package com.bankerisemock.product;

import com.bankerisemock.product.dao.ProductDao;
import com.bankerisemock.product.dto.ProductDTO;
import com.bankerisemock.product.dto.ProductDTOMapper;
import com.bankerisemock.product.dto.ProductRequest;
import com.bankerisemock.product.exception.RequestException;
import com.bankerisemock.product.exception.ResourceNotFoundException;
import com.bankerisemock.product.model.Product;
import com.bankerisemock.product.service.ProductService;
import com.bankerisemock.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService underTest;
    private final ProductDTOMapper productDTOMapper = new ProductDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new ProductServiceImpl(productDao,productDTOMapper);
    }

    @Test
    void getAllProducts() {
        // When
        underTest.getAllProducts();

        // Then
        verify(productDao).selectAllProducts();
    }

    @Test
    void canGetProduct() {
        // Given
        int id = 10;
        Product product = new Product(id,"EPARLOG","Epargne Logement");
        when(productDao.selectProductById(id)).thenReturn(Optional.of(product));
       Optional<ProductDTO> expected = Optional.ofNullable(productDTOMapper.apply(product));

        // When
       Optional<ProductDTO> actual = underTest.getProduct(id);
       assertThat(actual).isEqualTo(expected);
    }
    @Test
    void willThrowWhenGetProductReturnEmptyOptional() {
        // Given
        int id = 10;
        when(productDao.selectProductById(id)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> underTest.getProduct(id)).isInstanceOf(ResourceNotFoundException.class).hasMessage("product with id [%s] not found".formatted(id));
    }
    @Test
    void addProduct() {
        // Given
        ProductRequest request = new ProductRequest("EPARLOG","Epargne Logement");

        // When
        underTest.addProduct(request);

        // Then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productDao).insertProduct(productArgumentCaptor.capture());
        Product actual = productArgumentCaptor.getValue();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getCode()).isEqualTo(request.code());
        assertThat(actual.getName()).isEqualTo(request.name());
    }
    @Test
    void deleteProductById() {
        // Given
        int id = 10;
        when(productDao.existsProductById(id)).thenReturn(true);

        // When
        underTest.deleteProduct(id);

        // Then
        verify(productDao).deleteProductById(id);
    }
    @Test
    void willThrowDeleteProductByIdNotExists() {
        // Given
        int id = 10;

        when(productDao.existsProductById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteProduct(id)).isInstanceOf(ResourceNotFoundException.class).hasMessage("product with id [%s] not found".formatted(id));

        // Then
        verify(productDao, never()).deleteProductById(id);
    }
    @Test
    void canUpdateAllProductProperties() {
        // Given
        int id = 10;
        Product product = new Product(id,"EPARLOG","Epargne Logement");
        when(productDao.selectProductById(id)).thenReturn(Optional.of(product));
        ProductRequest request = new ProductRequest("EPAR","Epargne");

        // When
        underTest.updateProduct(id,request);

        // Then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).updateProduct(productArgumentCaptor.capture());
        Product actual = productArgumentCaptor.getValue();
        assertThat(actual.getCode()).isEqualTo(request.code());
        assertThat(actual.getName()).isEqualTo(request.name());
    }
    @Test
    void canUpdateOnlyProductCode() {
        // Given
        int id = 10;
        Product product = new Product(id,"EPARLOG","Epargne Logement");
        when(productDao.selectProductById(id)).thenReturn(Optional.of(product));
        ProductRequest request = new ProductRequest("EPAR",null);
        // When
        underTest.updateProduct(id,request);
        // Then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).updateProduct(productArgumentCaptor.capture());
        Product actual = productArgumentCaptor.getValue();
        assertThat(actual.getCode()).isEqualTo(request.code());
        assertThat(actual.getName()).isEqualTo(product.getName());


    }
    @Test
    void canUpdateOnlyProductName() {
        // Given
        int id = 10;
        Product product = new Product(id,"EPARLOG","Epargne Logement");
        when(productDao.selectProductById(id)).thenReturn(Optional.of(product));
        ProductRequest request = new ProductRequest(null,"Epargne");
        // When
        underTest.updateProduct(id,request);
        // Then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).updateProduct(productArgumentCaptor.capture());
        Product actual = productArgumentCaptor.getValue();
        assertThat(actual.getCode()).isEqualTo(product.getCode());
        assertThat(actual.getName()).isEqualTo(request.name());
    }
    @Test
    void willThrowWhenProductUpdateHasNoChanges() {
        // Given
        int id = 10;
        Product product = new Product(id,"EPARLOG","Epargne Logement");
        when(productDao.selectProductById(id)).thenReturn(Optional.of(product));
        ProductRequest request = new ProductRequest(product.getCode(),product.getName());
        assertThatThrownBy(
                () -> underTest.updateProduct(id,request)
        ).isInstanceOf(RequestException.class).hasMessage("no data changes found");

        // Then
        verify(productDao,never()).updateProduct(any());

    }

}
