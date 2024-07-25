package com.ecom2.customer.repository;

import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerAddress ca WHERE ca.customer.customerId = :customerId")
    void deleteByCustomerId(@Param("customerId") Long customerId);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM CustomerAddress ca WHERE ca.customer.customerId IN (SELECT c.customerId FROM Customer c WHERE c.user.userId = :userId)")
//    void deleteByUserId(@Param("userId") int userId);

    Optional<CustomerAddress> findByCustomer_CustomerId(Long customerId);
    Optional<CustomerAddress> findByCustomer(Customer customer);

}