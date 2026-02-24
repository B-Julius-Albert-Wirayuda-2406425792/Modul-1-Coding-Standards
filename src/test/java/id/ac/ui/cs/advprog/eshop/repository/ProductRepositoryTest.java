package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Bambang Edition");
        updatedProduct.setProductQuantity(200);

        productRepository.update(updatedProduct);

        Product foundProduct = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertEquals("Sampo Cap Bambang Edition", foundProduct.getProductName());
        assertEquals(200, foundProduct.getProductQuantity());
    }

    @Test
    void testEditProductIfNotFound() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        assertThrows(IllegalArgumentException.class, () -> productRepository.update(product));
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductIfNotFound() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("random-id");
        nonExistentProduct.setProductName("Ghost Product");
        nonExistentProduct.setProductQuantity(0);

        productRepository.delete(nonExistentProduct);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
    }

    @Test
    void testCreate_generatesUuidWhenProductIdBlank() {
        Product product = new Product();
        product.setProductId("");
        product.setProductName("Auto ID Product");
        product.setProductQuantity(1);

        Product created = productRepository.create(product);

        assertNotNull(created.getProductId());
        assertFalse(created.getProductId().isBlank());

        assertDoesNotThrow(() -> UUID.fromString(created.getProductId()));
    }

    @Test
    void testFindById_throwsWhenNotFound_andMessageMatches() {
        String missingId = "missing-id";

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.findById(missingId)
        );

        assertEquals("Product with Id + " + missingId + " was not found.", ex.getMessage());
    }

    @Test
    void testUpdate_throwsWhenUpdatedProductNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.update(null)
        );
        assertEquals("Product and Product Id must not be null.", ex.getMessage());
    }

    @Test
    void testUpdate_throwsWhenProductIdNull() {
        Product updated = new Product();
        updated.setProductId(null);
        updated.setProductName("X");
        updated.setProductQuantity(1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.update(updated)
        );
        assertEquals("Product and Product Id must not be null.", ex.getMessage());
    }
}