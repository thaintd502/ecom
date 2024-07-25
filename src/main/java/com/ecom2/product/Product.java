package com.ecom2.product;

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
    private int price;

    @Column
    private int stockQuantity;

    @Column
    private int discount;

    @ManyToMany
    @JoinTable(
            name = "product_category_links",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

}
