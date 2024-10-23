package com.ecom2.user.repository;

import com.ecom2.customer.entity.Customer;
import com.ecom2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);
    Boolean existsUserByUserName(String username);
    Boolean existsUserByEmail(String email);
    Optional<User> findByEmail(String email);


    @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.userId = :userId")
    Customer findByUserId(int userId);
//    void deleteUserAndRelatedEntities(int userId);
}
