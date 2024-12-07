package com.shop.bookshop.services;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.Dto.BookStockReport;
import com.shop.bookshop.domain.Order;
import com.shop.bookshop.domain.OrderDetail;
import com.shop.bookshop.domain.Promotion;
import com.shop.bookshop.repository.BookRespository;
import com.shop.bookshop.repository.OrderDetailRepository;
import com.shop.bookshop.repository.OrderRepository;
import com.shop.bookshop.repository.PromotionRepository;
import com.shop.bookshop.util.constant.StatusEnum;
import org.aspectj.runtime.internal.Conversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    BookRespository bookRespository;


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

    public Order updateOrder(Long id, Order order) {
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

    public Order updateStatus(long id) {
        Order order = this.getOrderById(id);
        order.setStatusPayment("Đã thanh toán");
        order.setStatus(StatusEnum.COMPLETE);
        orderRepository.save(order);
        return order;
    }

    public Map<LocalDate, Double> calculateRevenue(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByCreateDateBetween(start, end);

        Map<LocalDate, Double> revenueMap = new HashMap<>();
        for (Order order : orders) {
            if ("Đã thanh toán".equals(order.getStatusPayment())) {
                LocalDate orderDate = order.getCreateDate().toLocalDate();
                revenueMap.merge(orderDate, order.getTotalPrice(), Double::sum);
            }
        }

        return revenueMap;
    }

    public List<BookStockReport> getStockReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Book> books = bookRespository.findAll();
//        List<Order> orders = orderRepository.findByCreateDateBetween(start, end);
        List<Order> orders = orderRepository.findAll();


        // Lưu trữ số lượng đã bán
        Map<String, Long> salesMap = new HashMap<>();
        for (Order order : orders) {
            for (OrderDetail detail : order.getOrderDetails()) {
                    String bookTitle = detail.getBook().getTitle();
                    Long quantitySold = detail.getQuantity();
                    salesMap.merge(bookTitle, quantitySold, Long::sum);
            }
        }

        List<BookStockReport> stockReport = new ArrayList<>();

        for (Book book : books) {
            String title = book.getTitle();
            int quantityInStock = book.getQuantityInStock();
            long quantitySold = salesMap.getOrDefault(title, 0L);

            stockReport.add(new BookStockReport(title, quantityInStock, quantitySold));
        }

        return stockReport;
    }

}