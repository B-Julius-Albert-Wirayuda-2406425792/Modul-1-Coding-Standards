package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository extends BaseRepository<Product> {
    public Product update(String id, Product updatedProduct) {
        if (updatedProduct == null || updatedProduct.getId() == null) {
            throw new IllegalArgumentException("Product and Product Id must not be null.");
        }

        Product currentProduct = findById(id);

        if (!currentProduct.getId().equals(id)) {
            throw new IllegalArgumentException("Product and Id must match");
        }

        currentProduct.setProductName(updatedProduct.getProductName());
        currentProduct.setProductQuantity(updatedProduct.getProductQuantity());

        return currentProduct;
    }
}
