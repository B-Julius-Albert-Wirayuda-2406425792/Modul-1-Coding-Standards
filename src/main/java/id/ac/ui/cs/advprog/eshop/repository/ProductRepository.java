package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product){
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String id) {
        for (Product product: productData) {
            if (product.getProductId().equals(id)) return product;
        }
        throw new IllegalArgumentException("Product with Id + " + id + " was not found.");
    }

    public Product update(Product updatedProduct) {
        if (updatedProduct == null || updatedProduct.getProductId() == null) {
            throw new IllegalArgumentException("Product and Product Id must not be null.");
        }

        Product currentProduct = findById(updatedProduct.getProductId());

        currentProduct.setProductName(updatedProduct.getProductName());
        currentProduct.setProductQuantity(updatedProduct.getProductQuantity());

        return currentProduct;
    }

    public void delete(Product product) {
        productData.remove(product);
    }
}
