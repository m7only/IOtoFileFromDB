package org.m7.dao;

import java.util.List;

/**
 * Контракт на общие методы (по сути CRUD) для сущностей
 *
 * @param <T> Сущность, для которой создается DAO
 */
public interface Dao<T> {

    /**
     * Сохранение сущности в БД
     *
     * @param t сохраняемая сущность
     * @return сохраненная сущность
     */
    long create(T t);

    /**
     * Чтение сущности по идентификатору
     *
     * @param id идентификатор искомой сущности
     * @return Найденная сущность
     */
    T readById(long id);

    /**
     * Получение всех сущностей из БД
     *
     * @return список всех сущностей
     */
    List<T> readAll();

    /**
     * Изменение сущности в БД
     *
     * @param t изменяемая сущность
     */
    void updateByObject(T t);

    /**
     * Удаление сущности из бд
     *
     * @param t удаляемая сущность
     */
    void deleteByObject(T t);
}
