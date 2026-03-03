package id.ac.ui.cs.advprog.eshop.service.base;

import java.util.List;

public interface ReadService<T> {
    List<T> findAll();
    T findById(String id);
}
