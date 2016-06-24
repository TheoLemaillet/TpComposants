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

    @Column(name = "ref", nullable = false)
    private String ref;

    @Column(name = "dateInvoice", nullable = false)
    private Date dateInvoice;

    @OneToMany(mappedBy = "invoice")
    private List<Delivery> deliveries;

    public Invoice() {}

    public Invoice(String ref, Date dateInvoice, List<Delivery> deliveries) {
        this.ref = ref;
        this.dateInvoice = dateInvoice;
        this.deliveries = deliveries;
    }

    public String getRef() {
        return ref;
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
}
