package com.ecom2.customer.service.impl;

import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;
import com.ecom2.customer.repository.CustomerAddressRepository;
import com.ecom2.customer.service.CustomerAddressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerAddressServiceImpl implements CustomerAddressService {

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public CustomerAddress saveCustomer(CustomerAddress customerAddress) {
        return customerAddressRepository.save(customerAddress);
    }

    @Override
    @Transactional
    public void deleteByCustomerId(Long customerId) {
        customerAddressRepository.deleteByCustomerId(customerId);
    }

    @Override
    public Optional<CustomerAddress> findByCustomerId(Long customerId) {
        return customerAddressRepository.findByCustomer_CustomerId(customerId);
    }

    public CustomerAddress findByCustomer(Customer customer) {
        return customerAddressRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Address not found for customer"));
    }


}
