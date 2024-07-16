package com.ecom2.customer.service;

import com.ecom2.customer.dto.CustomerSignup;
import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressService {

    CustomerAddress saveCustomer(CustomerAddress customerAddress);
    void deleteByCustomerId(Long customerId);
    Optional<CustomerAddress> findByCustomerId(Long customerId);
    CustomerAddress findByCustomer(Customer customer);
}

