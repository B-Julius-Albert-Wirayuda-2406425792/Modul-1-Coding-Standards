package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.base.ReadService;
import id.ac.ui.cs.advprog.eshop.service.base.WriteService;

import java.util.List;

public interface ProductService extends ReadService<Product>, WriteService<Product>  {
    public Product create(Product product);
    public List<Product> findAll();
    public Product findById(String id);
    public void update(String productId, Product product);
    public void deleteById(String productId);
}
