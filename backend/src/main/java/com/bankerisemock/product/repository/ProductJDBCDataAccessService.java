package com.bankerisemock.product.repository;

import com.bankerisemock.product.dao.ProductDao;
import com.bankerisemock.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
@RequiredArgsConstructor
public class ProductJDBCDataAccessService implements ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final ProductRowMapper productRowMapper;



    public List<Product> selectAllProducts() {
        var sql = """
                  SELECT id,code,name
                  FROM product
                  LIMIT 500
                """;
        return jdbcTemplate.query(sql,productRowMapper);
    }

    @Override
    public Optional<Product> selectProductById(Integer productId) {
        var sql = """
                  SELECT id,code,name
                  FROM product
                  WHERE id = ?
                """;
        return jdbcTemplate.query(sql,productRowMapper,productId).stream().findFirst();
    }

    @Override
    public void insertProduct(Product product) {
        var sql = """
                  INSERT INTO  product(code,name) VALUES (?,?) ;
                """;
            jdbcTemplate.update(sql,product.getCode(),product.getName());

    }

    @Override
    public boolean existsProductById(Integer productId) {
        var sql = """
                   SELECT count(id)
                   FROM product 
                   WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,productId);
        return count != null && count >0;

    }

    @Override
    public void deleteProductById(Integer productId) {
        var sql = """
                   DELETE 
                   FROM product 
                   WHERE id = ?
                """;
        jdbcTemplate.update(sql,productId);

    }

    @Override
    public void updateProduct(Product newProduct) {
        if(newProduct.getCode() != null){
            String sql = """
                     UPDATE product  
                     SET code = ? 
                     WHERE id = ?
                    """;
            jdbcTemplate.update(sql,newProduct.getCode(),newProduct.getId());
        }
        if(newProduct.getName() != null){
            String sql = """
                     UPDATE product  
                     SET name = ? 
                     WHERE id = ?
                    """;
            jdbcTemplate.update(sql,newProduct.getName(),newProduct.getId());
        }


    }

    @Override
    public boolean existsProductByCode(String code) {
        return false;
    }

    @Override
    public boolean existsProductByName(String name) {
        return false;
    }
}
