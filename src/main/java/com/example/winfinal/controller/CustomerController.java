package com.example.winfinal.controller;

import com.example.winfinal.dto.CustomerDTO;
import com.example.winfinal.service.CustomerService;

import java.util.List;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController() {
        this.customerService = new CustomerService();
    }

    public List<CustomerDTO> getAll() {
        return customerService.getAllCustomers();
    }

    public CustomerDTO getById(Integer id) {
        return customerService.getCustomerById(id);
    }

    public void create(CustomerDTO dto) {
        customerService.addCustomer(dto);
    }

    public void update(CustomerDTO dto) {
        customerService.updateCustomer(dto);
    }

    public void delete(Integer id) {
        customerService.deleteCustomer(id);
    }
}
