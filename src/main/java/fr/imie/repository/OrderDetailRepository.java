package fr.imie.repository;

import fr.imie.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tlemaillet on 6/30/16.
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    OrderDetail findById(Integer id);
}
