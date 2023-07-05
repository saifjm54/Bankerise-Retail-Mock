package com.bankerisemock.product;

import com.bankerisemock.product.model.Product;
import com.bankerisemock.product.repository.ProductJPADataAccessService;
import com.bankerisemock.product.repository.ProductRepository;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ProductJPADataAccessServiceTest {

    private ProductJPADataAccessService underTest;

    private AutoCloseable autoCloseable;

    @Mock private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ProductJPADataAccessService(productRepository);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
    @Test
    void selectAllProducts() {
        // Given
        Page<Product> page = mock(Page.class);
        List<Product> products = List.of(new Product());
        when(page.getContent()).thenReturn(products);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        //When
        List<Product> actual = underTest.selectAllProducts();
        // Then
        assertThat(actual).isEqualTo(products);
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findAll(pageableArgumentCaptor.capture());
        assertThat(pageableArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(500));
    }
    @Test
    void selectProductById() {
        // Given
        int id = 1;

        // When
        underTest.selectProductById(id);

        // then
        verify(productRepository).findById(id);
    }
    @Test
    void insertProduct() {
        // Given
        Product product = new Product(1,"codeProd","ProdName");

        // When
        underTest.insertProduct(product);

        // Then
        verify(productRepository).save(product);
    }

    @Test
    void existsProductById() {
        // Given
        int id = 1;

        // When
        underTest.existsProductById(id);

        // Then
        verify(productRepository).existsById(id);
    }
    @Test
    void deleteProductById() {
        // Given
        int id = 1;

        // When
        underTest.deleteProductById(id);

        // Then
        verify(productRepository).deleteById(id);
    }

    @Test
    void updateProduct() {
        // Given
        Product product = new Product(
                1,
                "codeProd",
                "ProdNameV"
        );

        // When
        underTest.updateProduct(product);

        // Then
        verify(productRepository).save(product);

    }
}
