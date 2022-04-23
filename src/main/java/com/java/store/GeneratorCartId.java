package com.java.store;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class GeneratorCartId implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        String prefix = "TTMobile";
        String t = String.valueOf(System.currentTimeMillis());
        String id = new StringBuilder(t).substring(0, t.length() - 3);
        Serializable result = prefix.concat(id);
        return result;
    }
}
