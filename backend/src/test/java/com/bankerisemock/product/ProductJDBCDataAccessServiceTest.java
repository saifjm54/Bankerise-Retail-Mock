package com.bankerisemock.product;

import com.bankerisemock.product.model.Product;
import com.bankerisemock.product.repository.ProductJDBCDataAccessService;
import com.bankerisemock.product.repository.ProductRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

public class ProductJDBCDataAccessServiceTest extends AbstractTest{

    private ProductJDBCDataAccessService underTest;

    private final ProductRowMapper productRowMapper = new ProductRowMapper();

    @BeforeEach
    protected void setUp() {
        underTest = new ProductJDBCDataAccessService(
                getJdbcTemplate(),
                productRowMapper
        );
    }

    @Test
    void selectAllProducts() {
        // Given
        Product product = new Product();
        product.setName("Epargne Logement");
        product.setCode("EPARLOG");
        underTest.insertProduct(product);

        // When
        List<Product> actual = underTest.selectAllProducts();

        // Then
        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectProductById() {
        // Given
        Product product = new Product();
        product.setName("Epargne Logement");
        product.setCode("EPARLOG");
        underTest.insertProduct(product);
        int id = underTest.selectAllProducts().stream().filter(p -> p.getCode().equals("EPARLOG")).map(Product::getId).findFirst().orElseThrow();

        // When
        Optional<Product> actaul = underTest.selectProductById(id);

        // Then
        assertThat(actaul).isPresent().hasValueSatisfying(
                p -> {
                    assertThat(p.getId()).isEqualTo(id);
                    assertThat(p.getCode()).isEqualTo(product.getCode());
                    assertThat(p.getName()).isEqualTo(product.getName());
                }
        );

    }
    @Test
    void willReturnEmptyWhenSelectProductById() {
        // Given
        int id = 0;

        // When
        var actual = underTest.selectProductById(id);

        // Then
        assertThat(actual).isEmpty();
    }
    @Test
    void existsProductById() {
        // Given
        Product product = new Product();
        product.setName("Epargne Logement");
        product.setCode("EPARLOG");
        underTest.insertProduct(product);
        int id = underTest.selectAllProducts().stream().filter(p -> p.getCode().equals("EPARLOG")).map(Product::getId).findFirst().orElseThrow();

        // When
        var actual = underTest.existsProductById(id);

        // Then
        assertThat(actual).isTrue();
    }
    @Test
    void existsProductWithIdWillFalseWhenIdNotPresent() {
        // Given
        int id = -1;

        // When
        var actual = underTest.existsProductById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteProductById() {
        // Given
        Product product = new Product();
        product.setName("Epargne Logement");
        product.setCode("EPARLOG");
        underTest.insertProduct(product);
        int id = underTest.selectAllProducts().stream().filter(p -> p.getCode().equals("EPARLOG")).map(Product::getId).findFirst().orElseThrow();

        // When
        underTest.deleteProductById(id);

        // Then
        Optional<Product> actual = underTest.selectProductById(id);
        assertThat(actual).isNotPresent();

    }

    @Test
    void updateProductCode() {
        // Given
        Product product = new Product();
        product.setName("Epargne Logement");
        product.setCode("EPARLOG");
        underTest.insertProduct(product);
        int id = underTest.selectAllProducts().stream().filter(p -> p.getCode().equals("EPARLOG")).map(Product::getId).findFirst().orElseThrow();

        var newCode = "EPAR";

        // When
        Product update = new Product();
        product.setId(id);
        product.setCode(newCode);
        underTest.updateProduct(update);

        // Then
        Optional<Product> actaul = underTest.selectProductById(id);

        assertThat(actaul).isPresent().hasValueSatisfying(
                p -> {
                    assertThat(p.getId()).isEqualTo(id);
                    assertThat(p.getCode()).isEqualTo(newCode);
                    assertThat(p.getName()).isEqualTo(product.getName());
                }
        );
    }
    @Test
    void updateProductName() {
        // Given
        Product product = new Product();
        product.setName("Epargne Logement");
        product.setCode("EPARLOG");
        underTest.insertProduct(product);
        int id = underTest.selectAllProducts().stream().filter(p -> p.getCode().equals("EPARLOG")).map(Product::getId).findFirst().orElseThrow();

        var newName = "Epargne";

        // When
        Product update = new Product();
        product.setId(id);
        product.setCode(newName);
        underTest.updateProduct(update);

        // Then
        Optional<Product> actaul = underTest.selectProductById(id);

        assertThat(actaul).isPresent().hasValueSatisfying(
                p -> {
                    assertThat(p.getId()).isEqualTo(id);
                    assertThat(p.getCode()).isEqualTo(product.getCode());
                    assertThat(p.getName()).isEqualTo(newName);
                }
        );

    }
    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        Product product = new Product();
        product.setName("Epargne Logement");
        product.setCode("EPARLOG");
        underTest.insertProduct(product);
        int id = underTest.selectAllProducts().stream().filter(p -> p.getCode().equals("EPARLOG")).map(Product::getId).findFirst().orElseThrow();

        // When update without no changes
        Product update = new Product();
        product.setId(id);
        underTest.updateProduct(update);

        // Then
        Optional<Product> actual = underTest.selectProductById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                p -> {
                    assertThat(p.getId()).isEqualTo(id);
                    assertThat(p.getCode()).isEqualTo(product.getCode());
                    assertThat(p.getName()).isEqualTo(product.getName());
                }
        );

    }

}
