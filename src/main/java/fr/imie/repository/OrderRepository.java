package fr.imie.repository;

import fr.imie.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tlemaillet on 6/29/16.
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findById(Integer id);
    List<Order> findByRef(String ref);
}
