package org.m7.dao;

import org.hibernate.Session;
import org.m7.dao.impl.DaoImpl;
import org.m7.factory.HibernateSessionFactoryUtil;
import org.m7.model.entity.Customer;

import java.util.List;

/**
 * DAO для покупателя
 */
public class CustomerDao extends DaoImpl<Customer> {
    public CustomerDao() {
        super(Customer.class);
    }

    public List<Customer> findByLastName(String lastName) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("From Customer where lastName='" + lastName + "'", Customer.class)
                    .list();
        }
    }

    public List<Customer> findByProductTimes(String productName, Integer minTimes) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("from Customer as c " +
                                    "inner join Purchase as p on c.id = p.customer.id " +
                                    "inner join Product as pr on pr.id = p.product.id " +
                                    "where pr.productName='" + productName + "' " +
                                    "group by c.id " +
                                    "having count(pr.id) >= " + minTimes,
                            Customer.class)
                    .list();
        }
    }

    public List<Customer> findBadCustomers(Integer badCustomers) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("from Customer as c " +
                                    "left join Purchase as p on c.id = p.customer.id " +
                                    "group by c.id " +
                                    "order by count(*) asc",
                            Customer.class)
                    .setMaxResults(badCustomers)
                    .list();
        }
    }

    public List<Customer> findByMinMaxExpenses(Integer minExpenses, Integer maxExpenses) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("from Customer as c " +
                                    "left join Purchase as p on c.id = p.customer.id " +
                                    "group by c.id " +
                                    "having sum(p.product.price) >= " + minExpenses + " and sum(p.product.price) <=" + maxExpenses,
                            Customer.class)
                    .list();
        }
    }
}
