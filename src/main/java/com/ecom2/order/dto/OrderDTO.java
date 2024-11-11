package com.ecom2.order.dto;

import com.ecom2.customer.dto.CustomerAddressDTO;
import com.ecom2.customer.dto.CustomerDTO;
import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;
    private CustomerDTO customer;
    private CustomerAddressDTO address;
    private Date orderDate;
    private String status;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
}
