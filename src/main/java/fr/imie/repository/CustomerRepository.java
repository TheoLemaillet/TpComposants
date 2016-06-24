package fr.imie.repository;

import fr.imie.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tlemaillet on 6/23/16.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByNameStartsWithIgnoreCase(String name);
}
