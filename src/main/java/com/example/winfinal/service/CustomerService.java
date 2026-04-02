package com.example.winfinal.service;

import com.example.winfinal.dao.CustomerDAO;
import com.example.winfinal.dto.CustomerDTO;
import com.example.winfinal.entity.Customer;
import com.example.winfinal.mapper.CustomerMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    private final CustomerDAO customerDAO;
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Integer id) {
        return customerDAO.findById(id)
                .map(customerMapper::toDTO)
                .orElse(null);
    }

    public void addCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customerDAO.save(customer);
    }

    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customerDAO.update(customer);
    }

    public void deleteCustomer(Integer id) {
        customerDAO.deleteById(id);
    }
}
