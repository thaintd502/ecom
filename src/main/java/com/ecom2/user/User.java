package com.ecom2.user;

import com.ecom2.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "User")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String userName;
    @JsonIgnore
    private String password;
    private String email;
    private Date created;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "User_Role",joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> listRoles;

    // Getters and Setters
}
