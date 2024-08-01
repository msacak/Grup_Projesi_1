package Repositories;

import java.util.List;

public interface ICRUD<T> {

    T save(T t);
    List<T> saveAll(List<T> t);
    T update(T t);
    List<T> getAll();
    T findByID(int id);
}