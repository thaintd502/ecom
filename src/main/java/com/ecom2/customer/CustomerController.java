package com.ecom2.customer;

import com.ecom2.auth.payload.response.MessageResponse;
import com.ecom2.customer.dto.CustomerAddressDTO;
import com.ecom2.customer.dto.CustomerSignup;
import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;
import com.ecom2.customer.service.CustomerAddressService;
import com.ecom2.customer.service.CustomerService;
import com.ecom2.role.ERole;
import com.ecom2.role.Role;
import com.ecom2.role.RoleService;
import com.ecom2.user.User;
import com.ecom2.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup-customer")
    public ResponseEntity<?> signupCustomer(@RequestBody CustomerSignup customerSignup) {
        if(userService.existsByUserName(customerSignup.getUserName())) return new ResponseEntity<>(new MessageResponse("Username already exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(customerSignup.getEmail())) return new ResponseEntity<>(new MessageResponse("Email already exists"), HttpStatus.BAD_REQUEST);
        customerService.signUp(customerSignup);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }


    @GetMapping("/get-all-customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Xóa khách hàng bằng ID
    @DeleteMapping("/delete-customer/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>("Customer deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete customer. Reason: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customer-address/{customerId}")
    public ResponseEntity<CustomerAddressDTO> getCustomerAddressByCustomerId(@PathVariable Long customerId) {
        return customerAddressService.findByCustomerId(customerId)
                .map(address -> {
                    CustomerAddressDTO dto = new CustomerAddressDTO();
                    dto.setAddressId(address.getAddressId());
                    dto.setAddress(address.getAddress());
                    dto.setCommune(address.getCommune());
                    dto.setDistrict(address.getDistrict());
                    dto.setCity(address.getCity());
                    dto.setCountry(address.getCountry());
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        customerService.deleteUserAndRelatedEntities(id);
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/edit-customer/{customerId}")
    public ResponseEntity<?> editCustomer(@PathVariable Long customerId, @RequestBody CustomerSignup customerSignup) {
        // Tìm customer theo ID
        Customer existingCustomer = customerService.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Tìm user liên quan đến customer
        User existingUser = existingCustomer.getUser();

        // Cập nhật thông tin user
        existingUser.setUserName(customerSignup.getUserName());
        if (!customerSignup.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(customerSignup.getPassword()));
        }
        existingUser.setEmail(customerSignup.getEmail());

        // Lưu user đã cập nhật
        User savedUser = userService.saveOrUpdate(existingUser);

        // Cập nhật thông tin customer
        existingCustomer.setName(customerSignup.getName());
        existingCustomer.setPhone(customerSignup.getPhone());
        existingCustomer.setGender(customerSignup.getGender());
        existingCustomer.setBirthdate(customerSignup.getBirthdate());
        existingCustomer.setImageUrl(customerSignup.getImageUrl());
        existingCustomer.setUser(savedUser); // Associate user with customer

        // Lưu customer đã cập nhật
        Customer savedCustomer = customerService.saveCustomer(existingCustomer);

        // Tìm địa chỉ liên quan đến customer
        CustomerAddress existingAddress = customerAddressService.findByCustomer(savedCustomer);

        // Cập nhật thông tin địa chỉ
        existingAddress.setAddress(customerSignup.getAddress());
        existingAddress.setCountry(customerSignup.getCountry());
        existingAddress.setCity(customerSignup.getCity());
        existingAddress.setDistrict(customerSignup.getDistrict());
        existingAddress.setCommune(customerSignup.getCommune());

        // Lưu địa chỉ đã cập nhật
        customerAddressService.saveCustomer(existingAddress);

        return ResponseEntity.ok("Customer updated successfully!");
    }
}
