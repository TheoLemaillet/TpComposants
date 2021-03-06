package fr.imie.entity;

import javax.persistence.*;

/**
 * Created by tlemaillet on 6/23/16.
 */

@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "qte", nullable = false)
    private Integer qte;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    public OrderDetail() {}

    public OrderDetail(Integer qte, Product product, Order order) {
        this.qte = qte;
        this.product = product;
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public Integer getQte() {
        return qte;
    }

    public void setQte(Integer qte) {
        this.qte = qte;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return this.order + "_" + this.qte + "x" + this.product;
    }
}
