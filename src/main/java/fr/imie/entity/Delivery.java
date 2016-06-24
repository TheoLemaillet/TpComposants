package fr.imie.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tlemaillet on 6/23/16.
 */

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ref")
    private String ref;

    @Column(name = "deliveryDate", nullable = false)
    private Date deliveryDate;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Invoice invoice;

    public Delivery() {}

    public Delivery(Date deliveryDate, Order order, Invoice invoice) {
        this.deliveryDate = deliveryDate;
        this.order = order;
        this.invoice = invoice;
    }

    public Integer getId() {
        return id;
    }

    public String getRef() {
        return ref;
    }

    @PostPersist
    public void initRef() {
        this.ref = "Delivery_" + id.toString();
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
