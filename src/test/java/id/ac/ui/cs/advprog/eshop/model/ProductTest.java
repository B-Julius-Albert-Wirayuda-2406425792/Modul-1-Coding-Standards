package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {
    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testGetId() {
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", product.getId());
    }

    @Test
    void testGetProductName() {
        assertEquals("Sampo Cap Bambang", product.getProductName());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(100, product.getProductQuantity());
    }
}