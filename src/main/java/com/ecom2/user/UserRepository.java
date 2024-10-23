package com.ecom2.user;

import com.ecom2.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);
    Boolean existsUserByUserName(String username);
    Boolean existsUserByEmail(String email);

    @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.userId = :userId")
    Customer findByUserId(int userId);
//    void deleteUserAndRelatedEntities(int userId);
}
