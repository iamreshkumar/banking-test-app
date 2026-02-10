package com.rhb.bank.repository;

import com.rhb.bank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPanNumber(String panNumber);
    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);

    Optional<Customer> findByPanNumber(String panNumber);

}
