package com.shop.bookshop.services;

import com.shop.bookshop.domain.Order;
import com.shop.bookshop.domain.OrderDetail;
import com.shop.bookshop.domain.Promotion;
import com.shop.bookshop.repository.OrderDetailRepository;
import com.shop.bookshop.repository.OrderRepository;
import com.shop.bookshop.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    PromotionRepository promotionRepository;


    public Page<Order> getAllOrderPaging(int pageNum) {
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()){
            return orderOptional.get();
        }
        return null;
    }

    public Order updateUser(Long id, Order order) {
        Order currentOrder = this.getOrderById(id);
        if (currentOrder != null){
            currentOrder.setStatus(order.getStatus());
            currentOrder.setStatusPayment(order.getStatusPayment());
        }
        return orderRepository.save(currentOrder);
    }

    public void deleteOrderById(long id) {
        Order order = this.getOrderById(id);
        if (order != null){
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails){
                orderDetailRepository.deleteById(orderDetail.getOrderDetailId());
            }
        }
        orderRepository.deleteById(id);
    }

    public List<Order> getOrderByUserId(long id) {
        return orderRepository.findOrderByUserId(id);
    }

    public List<OrderDetail> getOrderDetailByOrderId(Long id) {
        return orderDetailRepository.findOrderDetailByOrderOrderId(id);
    }
}