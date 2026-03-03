package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.model.IdHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class BaseRepository<T extends IdHolder> {
    protected List<T> data = new ArrayList<>();

    public T create(T entity) {
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        data.add(entity);
        return entity;
    }

    public Iterator<T> findAll(String id) {
        return data.iterator();
    }

    public T findById(String id) {
        for (T entity: data) {
            if (entity.getId().equals(id)) return entity;
        }
        throw new IllegalArgumentException("Entity with Id " + id + " was not found.");
    }

    public void delete(String id) {
        data.removeIf(entity -> entity.getId().equals(id));
    }

    public abstract T update(String id, T entity);
}
