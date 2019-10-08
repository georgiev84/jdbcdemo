package com.company;


import com.company.repositories.DataRepository;
import com.company.repositories.EmployeesDataRepository;
import com.company.repositories.JdbcDataRepository;
import org.w3c.dom.ls.LSOutput;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        String connectionString ="jdbc:mysql://localhost:3306/soft_uni_simple";

        Connection connection = DriverManager.getConnection(connectionString, "any", "12345");


        DataRepository<Employee> repository = new EmployeesDataRepository(connection);
        repository.getAll().stream().map(employee -> employee.getFirstName())
                .forEach(System.out::println);



        System.out.println("------------------------------------");

        repository.insert(new Employee("Ivan", "Ivanov"));
        repository.getAll().stream().map(employee -> employee.getFirstName())
                .forEach(System.out::println);
        connection.close();

    }
}

