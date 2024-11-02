package com.ecom2.order.entity;

import com.ecom2.branch.entity.ProductBranchLinks;
import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;
import com.ecom2.payment.Payment;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`order`")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private CustomerAddress address;

    @Column
    private Date orderDate;

    @Column
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "product_branch_link_id", referencedColumnName = "id")
    private ProductBranchLinks productBranchLink;

    public double getTotalAmount() {

        double sum = 0;

        for (OrderItem x : orderItems){
            sum += x.getPrice();
        }

        return sum;
    }

    public double getShippingFee() {
        double sum = 0;

        return sum;
    }

}

