package org.m7.dao;

import org.m7.dao.impl.DaoImpl;
import org.m7.entity.Product;

public class ProductDao extends DaoImpl<Product> {
    public ProductDao() {
        super(Product.class);
    }
}
