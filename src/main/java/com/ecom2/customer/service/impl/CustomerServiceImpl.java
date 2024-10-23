package com.ecom2.customer.service.impl;

import com.ecom2.customer.dto.CustomerSignup;
import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;
import com.ecom2.customer.repository.CustomerAddressRepository;
import com.ecom2.customer.repository.CustomerRepository;
import com.ecom2.customer.service.CustomerAddressService;
import com.ecom2.customer.service.CustomerService;
import com.ecom2.role.ERole;
import com.ecom2.role.Role;
import com.ecom2.role.RoleService;
import com.ecom2.user.entity.User;
import com.ecom2.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public Customer saveCustomer(Customer customer) {
//        return customerRepository.save(customer);
//    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {
        // Xóa các địa chỉ của customer trước
        customerAddressService.deleteByCustomerId(customerId);

        // Xóa customer
        customerRepository.deleteById(customerId);
    }

    @Override
    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Customer findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    public void deleteUserAndRelatedEntities(int userId) {
        // Tìm và xóa khách hàng dựa trên user_id
        Customer customer = customerRepository.findByUserId(userId);
        if (customer != null) {
            // Xóa địa chỉ của khách hàng
            customerAddressRepository.deleteByCustomerId(customer.getCustomerId());
            // Xóa khách hàng
            customerRepository.delete(customer);
        }
    }


    @Override
    @Transactional
    public void signUp(CustomerSignup customerSignup) {

        // Create a new User entity
        User newUser = new User();
        newUser.setUserName(customerSignup.getUserName());
        newUser.setPassword(passwordEncoder.encode(customerSignup.getPassword()));
        newUser.setEmail(customerSignup.getEmail());
        newUser.setCreated(new Date());
        Set<Role> listRoles = new HashSet<>();
        Role userRole = roleService.findByRoleName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: role is not found"));
        listRoles.add(userRole);
        newUser.setListRoles(listRoles);

        // Save the new User entity
        User savedUser = userService.saveOrUpdate(newUser);

        // Create a new Customer entity
        Customer newCustomer = new Customer();
        newCustomer.setName(customerSignup.getName());
        newCustomer.setPhone(customerSignup.getPhone());
        newCustomer.setGender(customerSignup.getGender());
        newCustomer.setBirthdate(customerSignup.getBirthdate());
        newCustomer.setImageUrl(customerSignup.getImageUrl());
        newCustomer.setUser(savedUser); // Associate user with customer

        // Save the new Customer entity
        Customer savedCustomer = customerRepository.save(newCustomer);

        // Create a new CustomerAddress entity
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setAddress(customerSignup.getAddress());
        customerAddress.setCountry(customerSignup.getCountry());
        customerAddress.setCity(customerSignup.getCity());
        customerAddress.setDistrict(customerSignup.getDistrict());
        customerAddress.setCommune(customerSignup.getCommune());
        customerAddress.setCustomer(savedCustomer); // Associate address with customer

        // Save the new CustomerAddress entity
        customerAddressService.saveCustomer(customerAddress);

    }



}
