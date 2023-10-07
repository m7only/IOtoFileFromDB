package org.m7.dao;

import org.m7.dao.impl.DaoImpl;
import org.m7.entity.Purchase;

/**
 * DAO для покупки
 */
public class PurchaseDao extends DaoImpl<Purchase> {
    public PurchaseDao() {
        super(Purchase.class);
    }
}
