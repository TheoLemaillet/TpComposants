package fr.imie.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by tlemaillet on 6/23/16.
 */

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ref")
    private String ref;

    @Column(name = "dateInvoice", nullable = false)
    private Date dateInvoice;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invoice")
    private List<Delivery> deliveries;

    public Invoice() {}

    public Invoice(Date dateInvoice, List<Delivery> deliveries) {
        this.dateInvoice = dateInvoice;
        this.deliveries = deliveries;
    }

    public Integer getId() {
        return id;
    }

    @PostPersist
    public void initRef() {
        this.ref = "Invoice_" + id.toString();
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Date getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(Date dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    @Override
    public String toString() {
        return this.ref;
    }
}
