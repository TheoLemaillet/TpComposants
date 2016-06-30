package fr.imie.repository;

import fr.imie.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by tlemaillet on 6/30/16.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    Delivery findById(Integer id);
}
