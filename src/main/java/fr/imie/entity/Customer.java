package fr.imie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by tlemaillet on 6/23/16.
 */

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ref")
    private String ref;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "postalCode", nullable = false)
    private String postalCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
    @JsonIgnore
    private List<Order> orders;

    public Customer() {}

    public Customer(String name, String address, String postalCode,
                    String city, String email, List<Order> orders, String telephone) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.email = email;
        this.orders = orders;
        this.telephone = telephone;
    }

    public Customer(String name, String address, String postalCode,
                    String city, String email, List<Order> orders) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.email = email;
        this.orders = orders;
    }

    public Integer getId() {
        return id;
    }

    public String getRef() {
        return ref;
    }

    @PostPersist
    public void initRef() {
        this.ref = "Customer_" + id.toString();
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
