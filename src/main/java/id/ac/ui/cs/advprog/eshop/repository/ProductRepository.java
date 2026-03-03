package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository extends BaseRepository<Product> {
    public Product update(String id, Product updatedProduct) {
        if (updatedProduct == null || updatedProduct.getId() == null) {
            throw new IllegalArgumentException("Product and Product Id must not be null.");
        }

        if (!updatedProduct.getId().equals(id)) {
            throw new IllegalArgumentException("Product and Id must match");
        }

        Product currentProduct = findById(id);

        currentProduct.setProductName(updatedProduct.getProductName());
        currentProduct.setProductQuantity(updatedProduct.getProductQuantity());

        return currentProduct;
    }
}
