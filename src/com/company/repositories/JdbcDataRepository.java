package com.company.repositories;

import com.company.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcDataRepository<T> implements DataRepository<T> {
    private final Connection connection;

    public JdbcDataRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<T> getAll() throws SQLException {
        String queryString = "SELECT * FROM " + this.getTableName();
        PreparedStatement query = connection.prepareStatement(queryString);
        ResultSet resultSet = query.executeQuery();

        return toList(resultSet);
    }

    protected abstract String getTableName();

    private List<T> toList(ResultSet resultSet) throws SQLException {
        List<T> list = new ArrayList<>();

        while(resultSet.next()){
            T object = this.parseRow(resultSet);
            list.add(object);
        }
        return list;
    }

    protected   abstract T parseRow(ResultSet resultSet) throws SQLException;



    @Override
    public void insert(T object) throws SQLException {
        List<String> values = this.getValues(object);

        StringBuilder params = new StringBuilder();
        for(int i=0; i<values.size(); i++){
            params.append("?, ");

        }

        params.deleteCharAt(params.length() -1);
        params.deleteCharAt(params.length() -1);

        String insert = "INSERT INTO " + this.getTableName() + " (" + this.getColumns(object) +
                ") VALUES (" + params + ")";

        PreparedStatement query = connection.prepareStatement(insert);

        for(int i=0; i< values.size(); i++){
            query.setString(i+1, values.get(i));
        }

        query.executeUpdate();
    }

    protected abstract List<String> getValues(T object);

    protected abstract String getColumns(T object);
}
