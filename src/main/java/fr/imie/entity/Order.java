package fr.imie.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by tlemaillet on 6/23/16.
 */
@Entity
@Table(name = "order_res")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ref")
    private String ref;

    @Column(name = "dateCreated", nullable = false)
    private Date dateCreated;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order")
    private List<Delivery> deliveries;

    @ManyToOne
    private Customer customer;

    public Order() {}

    public Order(Date dateCreated, List<OrderDetail> orderDetails,
                 List<Delivery> deliveries, Customer customer) {
        this.dateCreated = dateCreated;
        this.orderDetails = orderDetails;
        this.deliveries = deliveries;
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }

    @PostPersist
    public void initRef() {
        this.ref = "Order_" + id.toString();
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
