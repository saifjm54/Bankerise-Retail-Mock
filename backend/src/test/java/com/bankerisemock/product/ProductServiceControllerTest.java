package com.bankerisemock.product;
import com.bankerisemock.product.dto.ProductRequest;
import com.bankerisemock.product.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class ProductServiceControllerTest extends AbstractTest{

    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void getProducts() throws Exception {
        String uri = "/api/v1/products";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        String content = mvcResult.getResponse().getContentAsString();
        Product[] products = super.mapFromJson(content,Product[].class);
        assertTrue(products.length > 0);
    }
    @Test
    public void addProduct() throws  Exception {
        String uri = "/api/v1/products";
        ProductRequest productRequest = new ProductRequest("EPARLOG","Epargne Logement");
        String inputjson = super.mapToJson(productRequest);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputjson)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201,status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content,"Product is created successfully");

    }
    @Test
    public void getProduct() throws Exception{
        String uri = "/api/v1/products/7";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        String content = mvcResult.getResponse().getContentAsString();
        Product product = super.mapFromJson(content,Product.class);
        assertTrue(product != null);
    }

    @Test
    public void updateProduct() throws Exception {
        String uri = "/api/v1/products/9";
        ProductRequest productRequest = new ProductRequest("EPARLOG","Eparagne");
        String inputjson = super.mapToJson(productRequest);
        MvcResult mvcResult =
                mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputjson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Product is updated successsfully");
    }
    @Test
    public void deleteProduct() throws Exception {
        String uri = "/api/v1/products/10";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(204,status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content,"Product is deleted successsfully");
    }
}
