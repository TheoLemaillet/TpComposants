package fr.imie.repository;

import fr.imie.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tlemaillet on 6/30/16.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Invoice findById(Integer id);
    List<Invoice> findByRef(String ref);
}
