package com.ecom2.customer.entity;

import com.ecom2.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthdate") // Thêm cột ngày sinh
    private Date birthdate;

    @Column(name = "image_url") // Thêm cột đường dẫn hình ảnh
    private String imageUrl;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<CustomerAddress> addresses;


}