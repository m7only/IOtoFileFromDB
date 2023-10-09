package org.m7.dao;

import org.m7.dao.impl.DaoImpl;
import org.m7.model.entity.Product;

/**
 * DAO для товара
 */
public class ProductDao extends DaoImpl<Product> {
    public ProductDao() {
        super(Product.class);
    }
}
