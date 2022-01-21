package com.java.store;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GeneratorCartId implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        String prefix = "TTMobile";
        Serializable result = null;
        Connection connection = sharedSessionContractImplementor.connection();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select count(id) as total from store.carts");
            if(resultSet.next())
            {
                int id=resultSet.getInt(1)+1;
                String suffix = String.format("%04d", id);
                result = prefix.concat(suffix);
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
