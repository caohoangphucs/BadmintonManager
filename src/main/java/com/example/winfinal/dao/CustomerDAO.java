package com.example.winfinal.dao;

import com.example.winfinal.entity.Customer;

public class CustomerDAO extends BaseDAO<Customer, Integer> {
    public CustomerDAO() {
        super(Customer.class);
    }
}
