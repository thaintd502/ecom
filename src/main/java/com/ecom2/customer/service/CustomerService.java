package com.ecom2.customer.service;

import com.ecom2.customer.dto.CustomerDTO;
import com.ecom2.customer.entity.Customer;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;


public interface CustomerService {

//    Boolean existsByUserName(String username);
//    Boolean existsByEmail(String email);

//    void signUp(CustomerDTO customerSignup);
    Customer saveCustomer(Customer customer);
    List<Customer> getAllCustomers();
    void deleteCustomer(Long customerId);
    Optional<Customer> findById(Long customerId);
    Customer findByUserId(int userId);
    void deleteUserAndRelatedEntities(int userId);
    Customer findByUserName(String userName);
    void editCustomer(Long customerId, CustomerDTO customerDTO) throws ParseException, IOException;
}
