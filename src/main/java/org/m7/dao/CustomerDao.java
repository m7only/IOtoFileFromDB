package org.m7.dao;

import org.m7.dao.impl.DaoImpl;
import org.m7.entity.Customer;

/**
 * DAO для покупателя
 */
public class CustomerDao extends DaoImpl<Customer> {
    public CustomerDao() {
        super(Customer.class);
    }
}
