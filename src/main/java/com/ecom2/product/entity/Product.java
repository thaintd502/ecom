package com.ecom2.product.entity;

import com.ecom2.brand.Brand;
import com.ecom2.category.Category;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brandId")
    private Brand brand;

    @Column
    private double price;

    @Column
    private double promotePrice;

    @Column
    private double importPrice;

    @Column
    private int stockQuantity;

    @Column
    private double discount;

    @Column(nullable = false)
    private String productCode;

    @Column
    private int importQuantity;

    @Column
    private int availableStock;

    @Column(length = 1000)
    private String description;

    @Column
    private String imageUrl;

    @Column
    private int sold = 0;

    @ManyToMany
    @JoinTable(
            name = "product_category_links",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();
}
