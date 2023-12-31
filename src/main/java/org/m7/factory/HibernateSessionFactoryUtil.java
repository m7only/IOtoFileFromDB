package org.m7.factory;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.m7.model.entity.Customer;
import org.m7.model.entity.Product;
import org.m7.model.entity.Purchase;
import org.m7.service.impl.ApplicationServiceImpl;

/**
 * Класс для создания и хранения сессии подключения к БД
 */
public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {

    }

    /**
     * Создание и конфигурирование сессии
     *
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Customer.class);
                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(Purchase.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
                ApplicationServiceImpl.error("Ошибка подключения к БД");
            }
        }
        return sessionFactory;
    }
}
