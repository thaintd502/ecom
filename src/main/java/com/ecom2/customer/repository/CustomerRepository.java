package com.ecom2.customer.repository;

import com.ecom2.customer.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.user.userId = :userId")
    Customer findByUserId(int userId);
    @Query("SELECT c FROM Customer c WHERE c.user.userName = :userName")
    Customer findByUserName(String userName);
}
