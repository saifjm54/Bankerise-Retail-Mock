package com.bankerisemock.product;

import com.bankerisemock.product.model.Product;
import com.bankerisemock.product.repository.ProductRowMapper;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        ProductRowMapper productRowMapper = new ProductRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("code")).thenReturn("EPARLOG");
        when(resultSet.getString("name")).thenReturn("Epargne Logement");
        Product actual = productRowMapper.mapRow(resultSet,1);
        Product expected = new Product(
                1,
                "EPARLOG",
                "Epargne Logement"

        );
        assertThat(actual).isEqualTo(expected);
    }
}
