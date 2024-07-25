package com.ecom2.branch.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "branch")
@Data
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "name", nullable = false)
    private String name;

}