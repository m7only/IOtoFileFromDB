package org.m7.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.m7.dao.Dao;
import org.m7.factory.HibernateSessionFactoryUtil;

import java.util.List;

public abstract class DaoImpl<T> implements Dao<T> {
    private final Class<T> type;

    public DaoImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public long create(T t) {
        long result;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            result = (long) session.save(t);
            transaction.commit();
        }
        return result;
    }

    @Override
    public T readById(long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(type, id);
        }
    }

    @Override
    public List<T> readAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("From " + type.getName(), type)
                    .list();
        }
    }

    @Override
    public void updateByObject(T t) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                session.update(t);
                transaction.commit();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteByObject(T t) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                session.delete(t);
                transaction.commit();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
