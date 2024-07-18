package com.ecom2.customer;

import com.ecom2.auth.payload.response.MessageResponse;
import com.ecom2.cloudinary.CloudinaryService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired CloudinaryService cloudinaryService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/signup-customer")
    public ResponseEntity<?> signupCustomer(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String birthdate,
            @RequestParam(required = false) MultipartFile imageUrl,
            @RequestParam(required = false) String address,
//            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String commune) throws IOException, ParseException {
        if(userService.existsByUserName(userName)) return new ResponseEntity<>(new MessageResponse("Username already exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(email)) return new ResponseEntity<>(new MessageResponse("Email already exists"), HttpStatus.BAD_REQUEST);

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
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
        newCustomer.setName(name);
        newCustomer.setPhone(phone);
        newCustomer.setGender(gender);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        newCustomer.setBirthdate(dateFormat.parse(birthdate));
        newCustomer.setImageUrl(cloudinaryService.uploadFile(imageUrl));
        newCustomer.setUser(savedUser); // Associate user with customer


        // Save the new Customer entity
        Customer savedCustomer = customerService.saveCustomer(newCustomer);

        // Create a new CustomerAddress entity
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setAddress(address);
        customerAddress.setCountry("Việt Nam");
        customerAddress.setCity(city);
        customerAddress.setDistrict(district);
        customerAddress.setCommune(commune);
        customerAddress.setCustomer(savedCustomer); // Associate address with customer

        // Save the new CustomerAddress entity
        customerAddressService.saveCustomer(customerAddress);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PutMapping("/edit-customer/{customerId}")
    public ResponseEntity<?> editCustomer(
            @PathVariable Long customerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String birthdate,
            @RequestParam(required = false) MultipartFile imageUrl,
            @RequestParam(required = false) String address,
//            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String commune) throws IOException, ParseException {

//        if(userService.existsByUserName(userName)) return new ResponseEntity<>(new MessageResponse("Username already exists"), HttpStatus.BAD_REQUEST);
//        if(userService.existsByEmail(email)) return new ResponseEntity<>(new MessageResponse("Email already exists"), HttpStatus.BAD_REQUEST);

        Customer existingCustomer = customerService.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        System.out.println(birthdate);
        User existingUser = existingCustomer.getUser();

        if (userName != null) existingUser.setUserName(userName);
        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(password));
        }
        if (email != null) existingUser.setEmail(email);

        User savedUser = userService.saveOrUpdate(existingUser);

        if (name != null) existingCustomer.setName(name);
        if (phone != null) existingCustomer.setPhone(phone);
        if (gender != null) existingCustomer.setGender(gender);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (birthdate != null) existingCustomer.setBirthdate(dateFormat.parse(birthdate));
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String image = cloudinaryService.uploadFile(imageUrl);
            existingCustomer.setImageUrl(image);
            System.out.println(image);
        }

        existingCustomer.setUser(savedUser);

        Customer savedCustomer = customerService.saveCustomer(existingCustomer);

        CustomerAddress existingAddress = customerAddressService.findByCustomer(savedCustomer);

        if (address != null) existingAddress.setAddress(address);
        existingAddress.setCountry("Việt Nam");
        if (city != null) existingAddress.setCity(city);
        if (district != null) existingAddress.setDistrict(district);
        if (commune != null) existingAddress.setCommune(commune);

        customerAddressService.saveCustomer(existingAddress);

        return ResponseEntity.ok("Customer updated successfully!");
    }

// Edit customer use CustomerSignup but not edit image yet
//    @PutMapping("/edit-customer/{customerId}")
//    public ResponseEntity<?> editCustomer(@PathVariable Long customerId, @RequestBody CustomerSignup customerSignup) {
//        // Tìm customer theo ID
//        Customer existingCustomer = customerService.findById(customerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        // Tìm user liên quan đến customer
//        User existingUser = existingCustomer.getUser();
//
//        // Cập nhật thông tin user
//        existingUser.setUserName(customerSignup.getUserName());
//        if (!customerSignup.getPassword().isEmpty()) {
//            existingUser.setPassword(passwordEncoder.encode(customerSignup.getPassword()));
//        }
//        existingUser.setEmail(customerSignup.getEmail());
//
//        // Lưu user đã cập nhật
//        User savedUser = userService.saveOrUpdate(existingUser);
//
//        // Cập nhật thông tin customer
//        existingCustomer.setName(customerSignup.getName());
//        existingCustomer.setPhone(customerSignup.getPhone());
//        existingCustomer.setGender(customerSignup.getGender());
//        existingCustomer.setBirthdate(customerSignup.getBirthdate());
//        existingCustomer.setImageUrl(customerSignup.getImageUrl());
//        existingCustomer.setUser(savedUser); // Associate user with customer
//
//        // Lưu customer đã cập nhật
//        Customer savedCustomer = customerService.saveCustomer(existingCustomer);
//
//        // Tìm địa chỉ liên quan đến customer
//        CustomerAddress existingAddress = customerAddressService.findByCustomer(savedCustomer);
//
//        // Cập nhật thông tin địa chỉ
//        existingAddress.setAddress(customerSignup.getAddress());
//        existingAddress.setCountry(customerSignup.getCountry());
//        existingAddress.setCity(customerSignup.getCity());
//        existingAddress.setDistrict(customerSignup.getDistrict());
//        existingAddress.setCommune(customerSignup.getCommune());
//
//        // Lưu địa chỉ đã cập nhật
//        customerAddressService.saveCustomer(existingAddress);
//
//        return ResponseEntity.ok("Customer updated successfully!");
//    }
}

