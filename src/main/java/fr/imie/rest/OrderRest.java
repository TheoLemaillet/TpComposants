package fr.imie.rest;

import fr.imie.entity.Order;
import fr.imie.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by tlemaillet on 6/29/16.
 */
@RestController
@RequestMapping("/api/order")
public class OrderRest {

    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping(path = "/get", produces = {"application/json"}, method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @RequestMapping(path = "/get/{id}", produces = {"application/json"}, method = RequestMethod.GET)
    @ResponseBody
    public Order getOrderById(@PathVariable int id) {
        return orderRepository.findById(id);
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Order createOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return order;
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void deleteOrder(@PathVariable int id) {
        orderRepository.delete(id);
    }
}
