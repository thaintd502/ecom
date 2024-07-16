package com.ecom2.customer.service;

import com.ecom2.customer.dto.CustomerSignup;
import com.ecom2.customer.entity.Customer;

import java.util.List;
import java.util.Optional;


public interface CustomerService {

//    Boolean existsByUserName(String username);
//    Boolean existsByEmail(String email);

    void signUp(CustomerSignup customerSignup);
    Customer saveCustomer(Customer customer);
    List<Customer> getAllCustomers();
    void deleteCustomer(Long customerId);
    Optional<Customer> findById(Long customerId);
    Customer findByUserId(int userId);
    void deleteUserAndRelatedEntities(int userId);
}
