package org.m7.dao;

import org.hibernate.Session;
import org.m7.dao.impl.DaoImpl;
import org.m7.factory.HibernateSessionFactoryUtil;
import org.m7.model.entity.Customer;
import org.m7.model.entity.Purchase;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO для покупки
 */
public class PurchaseDao extends DaoImpl<Purchase> {
    public PurchaseDao() {
        super(Purchase.class);
    }

    public List<Customer> getStat(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("select new Customer(c.firstName, c.lastName, new list(new Purchase(new Product(pr.productName, sum(pr.price)))))" +
                                    "from Customer as c " +
                                    "left join fetch Purchase as p on c.id = p.customer.id " +
                                    "inner join fetch Product as pr on pr.id = p.product.id " +
                                    "where p.purchaseDate >='" + startDate.atStartOfDay() + "' and p.purchaseDate <= '" + endDate.plusDays(1).atStartOfDay() + "' " +
                                    "group by c.id, pr.id order by sum(pr.price) desc",
                            Customer.class)
                    .list();
        }
    }
}
