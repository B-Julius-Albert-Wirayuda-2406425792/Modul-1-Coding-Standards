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
    void setUp() { }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setId("id-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals("id-1", savedProduct.getId());
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
        Product p1 = new Product(); p1.setId("id-1"); p1.setProductName("A"); p1.setProductQuantity(1);
        Product p2 = new Product(); p2.setId("id-2"); p2.setProductName("B"); p2.setProductQuantity(2);
        productRepository.create(p1);
        productRepository.create(p2);

        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        assertEquals("id-1", it.next().getId());
        assertEquals("id-2", it.next().getId());
        assertFalse(it.hasNext());
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        product.setId("id-1");
        product.setProductName("Old");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updated = new Product();
        updated.setId("id-1");
        updated.setProductName("New");
        updated.setProductQuantity(200);

        Product result = productRepository.update("id-1", updated);

        assertEquals("New", result.getProductName());
        assertEquals(200, result.getProductQuantity());
    }

    @Test
    void testEditProductIfNotFound() {
        Product updated = new Product();
        updated.setId("missing");
        updated.setProductName("X");
        updated.setProductQuantity(1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.update("missing", updated)
        );
        assertEquals("Entity with Id missing was not found.", ex.getMessage());
    }

    @Test
    void testUpdate_throwsWhenUpdatedProductNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.update("id-1", null)
        );
        assertEquals("Product and Product Id must not be null.", ex.getMessage());
    }

    @Test
    void testUpdate_throwsWhenProductIdNull() {
        Product updated = new Product();
        updated.setId(null);
        updated.setProductName("X");
        updated.setProductQuantity(1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.update("id-1", updated)
        );
        assertEquals("Product and Product Id must not be null.", ex.getMessage());
    }

    @Test
    void testUpdate_throwsWhenProductIdMismatch() {
        Product existing = new Product();
        existing.setId("id-1");
        existing.setProductName("A");
        existing.setProductQuantity(1);
        productRepository.create(existing);

        Product updated = new Product();
        updated.setId("id-2"); // intentionally different from path id
        updated.setProductName("B");
        updated.setProductQuantity(2);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.update("id-1", updated)
        );

        assertEquals("Product and Id must match", ex.getMessage());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setId("id-1");
        product.setProductName("A");
        product.setProductQuantity(1);
        productRepository.create(product);

        productRepository.delete("id-1");

        assertFalse(productRepository.findAll().hasNext());
    }

    @Test
    void testDeleteProductIfNotFound() {
        Product product = new Product();
        product.setId("id-1");
        productRepository.create(product);

        productRepository.delete("missing");

        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        assertEquals("id-1", it.next().getId());
    }

    @Test
    void testCreate_generatesUuidWhenProductIdBlank() {
        Product product = new Product();
        product.setId("");
        product.setProductName("Auto ID Product");
        product.setProductQuantity(1);

        Product created = productRepository.create(product);

        assertNotNull(created.getId());
        assertFalse(created.getId().isBlank());
        assertDoesNotThrow(() -> UUID.fromString(created.getId()));
    }

    @Test
    void testFindById_throwsWhenNotFound_andMessageMatches() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productRepository.findById("missing-id")
        );
        assertEquals("Entity with Id missing-id was not found.", ex.getMessage());
    }
}