package org.m7.dao;

import java.util.List;

public interface Dao<T> {
    long create(T t);

    T readById(long id);

    List<T> readAll();

    void updateByObject(T t);

    void deleteByObject(T t);
}
