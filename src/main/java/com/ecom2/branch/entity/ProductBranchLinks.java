package com.ecom2.branch.entity;

import com.ecom2.product.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_branch_links")
@Data
public class ProductBranchLinks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "transfer_id", referencedColumnName = "transferId")
    private Transfer transfer;

}

