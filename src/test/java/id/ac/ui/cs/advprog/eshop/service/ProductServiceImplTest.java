package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl service;

    @Test
    void create_delegatesToRepository_andReturnsProduct() {
        Product p = new Product();
        p.setId("id-1");
        p.setProductName("A");
        p.setProductQuantity(10);

        when(productRepository.create(p)).thenReturn(p);

        Product result = service.create(p);

        assertSame(p, result);
        verify(productRepository, times(1)).create(p);
    }

    @Test
    void findAll_collectsIteratorIntoList() {
        Product p1 = new Product(); p1.setId("1");
        Product p2 = new Product(); p2.setId("2");

        Iterator<Product> it = Arrays.asList(p1, p2).iterator();
        when(productRepository.findAll()).thenReturn(it);

        List<Product> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_delegatesToRepository() {
        Product p = new Product();
        p.setId("x");
        when(productRepository.findById("x")).thenReturn(p);

        Product result = service.findById("x");

        assertSame(p, result);
        verify(productRepository, times(1)).findById("x");
    }

    @Test
    void update_delegatesToRepository() {
        Product p = new Product();
        p.setId("x");

        service.update("x", p);

        verify(productRepository, times(1)).update("x", p);
    }

    @Test
    void deleteById_delegatesToRepository() {
        service.deleteById("x");
        verify(productRepository, times(1)).delete("x");
    }
}