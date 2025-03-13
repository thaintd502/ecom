package com.ecom2.customer.service.impl;

import com.ecom2.customer.dto.CustomerAddressDTO;
import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;
import com.ecom2.customer.repository.CustomerAddressRepository;
import com.ecom2.customer.repository.CustomerRepository;
import com.ecom2.customer.service.CustomerAddressService;
import com.ecom2.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerAddressServiceImpl implements CustomerAddressService {

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

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

    @Override
    public CustomerAddressDTO editCustomerAddress(CustomerAddressDTO customerAddressDTO) {

        CustomerAddress customerAddress = customerAddressRepository.findById(customerAddressDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("CustomerAddress", "addressId", customerAddressDTO.getAddressId()));

        System.out.println(customerAddressDTO.getAddress());
        customerAddress.setAddress(customerAddressDTO.getAddress());
        customerAddress.setCommune(customerAddressDTO.getCommune());
        customerAddress.setDistrict(customerAddressDTO.getDistrict());
        customerAddress.setCity(customerAddressDTO.getCity());

        customerAddressRepository.save(customerAddress);
        return customerAddressDTO;
    }

    @Override
    public CustomerAddress findById(Long addressId) {
        CustomerAddress customerAddress = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerAddress", "addressId", addressId));
        return customerAddress;
    }


}
