package com.cloud.kitchen.food.order.emulator.model;

import com.cloud.kitchen.food.order.emulator.dto.TempEnum;
import com.cloud.kitchen.food.order.emulator.dto.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;


public class Shelf {

    private static final Logger logger = LoggerFactory.getLogger(Shelf.class);

    private String type;
    private int capacity;
    private PriorityBlockingQueue<Order> orders;

    public Shelf(String type, int capacity) {
        this.type = type;
        this.capacity = capacity;

        orders = new PriorityBlockingQueue<>();
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public PriorityBlockingQueue<Order> getOrders() {
        return orders;
    }

    public synchronized void add(Order order) {
        if(type.equals(TempEnum.OVERFLOW.toString())) {
            order.setDecayRate(order.getDecayRate() * 2);
        }

        order.setOnShelfTime(System.currentTimeMillis());
        order.setShelf(this);
        orders.put(order);

        logger.info("Add an order {} to {} shelf", order, type);
        display();
    }

    public synchronized boolean remove(Order order) {
        if(orders.remove(order)) {
            logger.info("Remove an order {} from {} shelf", order, type);
            display();
            return true;
        }

        return false;
    }

    public Order getMinValueOrder() {
        return orders.peek();
    }

    public boolean isFull() {
       return orders.size() == capacity;
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public void display() {
        if(!orders.isEmpty()) {
            logger.info("--------------{} Shelf Content Start--------------", type);
            orders.forEach(order -> logger.info("Order is {}, normalized value is {}", order, order.getNormalizedValue()));
            logger.info("--------------{} Shelf Content End----------------", type);
        }else {
            logger.info("{} shelf is empty.", type);
        }
    }
}
