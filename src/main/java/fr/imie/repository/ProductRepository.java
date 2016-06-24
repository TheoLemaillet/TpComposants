package fr.imie.repository;

import fr.imie.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tlemaillet on 6/24/16.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameStartsWithIgnoreCase(String name);

}
