package com.ecom2.customer.service.impl;

import com.ecom2.cloudinary.CloudinaryService;
import com.ecom2.customer.dto.CustomerDTO;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private CloudinaryService cloudinaryService;

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
    public Customer findByUserName(String userName) {
        return customerRepository.findByUserName(userName);
    }

    @Override
    public void editCustomer(Long customerId, CustomerDTO customerDTO) throws ParseException, IOException{
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        System.out.println(customerDTO.getBirthdate());
        User existingUser = existingCustomer.getUser();

        if (customerDTO.getUserName() != null) existingUser.setUserName(customerDTO.getUserName());
        if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        }
        if (customerDTO.getEmail() != null) existingUser.setEmail(customerDTO.getEmail());

        User savedUser = userService.saveOrUpdate(existingUser);

        if (customerDTO.getName() != null) existingCustomer.setName(customerDTO.getName());
        if (customerDTO.getPhone() != null) existingCustomer.setPhone(customerDTO.getPhone());
        if (customerDTO.getGender() != null) existingCustomer.setGender(customerDTO.getGender());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (customerDTO.getBirthdate() != null) existingCustomer.setBirthdate(dateFormat.parse(customerDTO.getBirthdate()));
        if (customerDTO.getImageUrl() != null && !customerDTO.getImageUrl().isEmpty()) {
            String image = cloudinaryService.uploadFile(customerDTO.getImageUrl());
            existingCustomer.setImageUrl(image);
            System.out.println(image);
        }

        existingCustomer.setUser(savedUser);

        Customer savedCustomer = customerRepository.save(existingCustomer);

        CustomerAddress existingAddress = customerAddressRepository.findByCustomer(savedCustomer).orElseThrow();

        if (customerDTO.getAddress() != null) existingAddress.setAddress(customerDTO.getAddress());
        existingAddress.setCountry("Việt Nam");
        if (customerDTO.getCity() != null) existingAddress.setCity(customerDTO.getCity());
        if (customerDTO.getDistrict() != null) existingAddress.setDistrict(customerDTO.getDistrict());
        if (customerDTO.getCommune() != null) existingAddress.setCommune(customerDTO.getCommune());

        customerAddressRepository.save(existingAddress);
    }


//    @Override
//    @Transactional
//    public void signUp(CustomerDTO customerSignup) {
//
//        // Create a new User entity
//        User newUser = new User();
//        newUser.setUserName(customerSignup.getUserName());
//        newUser.setPassword(passwordEncoder.encode(customerSignup.getPassword()));
//        newUser.setEmail(customerSignup.getEmail());
//        newUser.setCreated(new Date());
//        Set<Role> listRoles = new HashSet<>();
//        Role userRole = roleService.findByRoleName(ERole.ROLE_USER)
//                .orElseThrow(() -> new RuntimeException("Error: role is not found"));
//        listRoles.add(userRole);
//        newUser.setListRoles(listRoles);
//
//        // Save the new User entity
//        User savedUser = userService.saveOrUpdate(newUser);
//
//        // Create a new Customer entity
//        Customer newCustomer = new Customer();
//        newCustomer.setName(customerSignup.getName());
//        newCustomer.setPhone(customerSignup.getPhone());
//        newCustomer.setGender(customerSignup.getGender());
//        newCustomer.setBirthdate(customerSignup.getBirthdate());
//        newCustomer.setImageUrl(customerSignup.getImageUrl());
//        newCustomer.setUser(savedUser); // Associate user with customer
//
//        // Save the new Customer entity
//        Customer savedCustomer = customerRepository.save(newCustomer);
//
//        // Create a new CustomerAddress entity
//        CustomerAddress customerAddress = new CustomerAddress();
//        customerAddress.setAddress(customerSignup.getAddress());
//        customerAddress.setCountry(customerSignup.getCountry());
//        customerAddress.setCity(customerSignup.getCity());
//        customerAddress.setDistrict(customerSignup.getDistrict());
//        customerAddress.setCommune(customerSignup.getCommune());
//        customerAddress.setCustomer(savedCustomer); // Associate address with customer
//
//        // Save the new CustomerAddress entity
//        customerAddressService.saveCustomer(customerAddress);
//
//    }



}
