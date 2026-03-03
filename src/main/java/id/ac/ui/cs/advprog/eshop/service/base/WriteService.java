package id.ac.ui.cs.advprog.eshop.service.base;

public interface WriteService<T> {
    T create(T entity);
    void update(String id, T entity);
    void deleteById(String id);
}
