package com.shop.bookshop.services;

import com.shop.bookshop.domain.*;
import com.shop.bookshop.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRespository bookRespository;
    @Autowired
    private PublisherRepository publisherRepository;

    public Page<PurchaseOrder> getAllBookPaging(int pageNum, int pagesize) {
        int pageSize = pagesize;

        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        return purchaseOrderRepository.findAll(pageable);
    }

    public PurchaseOrder addPurchaseOrder(PurchaseOrder purchaseOrder, HttpSession session) {
        LocalDateTime dateTime = LocalDateTime.now();
        long id = (long) session.getAttribute("id");
        User currentUser = userRepository.findById(id);
        int totalQuantity = 0;
        double totalValue = 0;

        purchaseOrder.setImportDate(dateTime);
        purchaseOrder.setStatus("Đang xử lý");
        purchaseOrder.setEnteredBy(currentUser);
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetails()){
            detail.setTotalPrice( detail.getQuantity() * detail.getUnitPrice());
            detail.setPurchaseOrder(savedPurchaseOrder);
            totalQuantity += detail.getQuantity();
            totalValue += detail.getTotalPrice();

            Book book = bookRespository.findBookByBookId(detail.getBook().getBookId());
            int newQuantityInStock = book.getQuantityInStock() + detail.getQuantity();
            book.setQuantityInStock(newQuantityInStock);
            bookRespository.save(book);
            purchaseOrderDetailRepository.save(detail);
        }
        purchaseOrder.setTotalQuantity(totalQuantity);
        purchaseOrder.setTotalValue(totalValue);
        return savedPurchaseOrder;
    }

    public PurchaseOrder findById(long id) {
        return purchaseOrderRepository.findPurchaseOrderByPurchaseOrderId(id);
    }

    @Transactional
    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder purchaseOrder) {
        PurchaseOrder currentPurchaseOrder = purchaseOrderRepository.findPurchaseOrderByPurchaseOrderId(id);

        if (currentPurchaseOrder != null) {
            currentPurchaseOrder.setPublisher(purchaseOrder.getPublisher());
            currentPurchaseOrder.setNotes(purchaseOrder.getNotes());

            // Giam so luong sach cu
            for (PurchaseOrderDetail oldDetail : currentPurchaseOrder.getPurchaseOrderDetails()) {
                Book book = bookRespository.findBookByBookId(oldDetail.getBook().getBookId());
                if (book != null) {
                    int oldQuantityInStock = book.getQuantityInStock() - oldDetail.getQuantity();
                    book.setQuantityInStock(oldQuantityInStock);
                    bookRespository.save(book);
                }
            }

            int totalQuantity = 0;
            double totalValue = 0;

            // Cap nhat chi tiet sach moi
            for (PurchaseOrderDetail newDetail : purchaseOrder.getPurchaseOrderDetails()) {
                // Kiem tra chi tiet sach da ton tai chua
                PurchaseOrderDetail existingDetail = currentPurchaseOrder.getPurchaseOrderDetails().stream()
                        .filter(detail -> detail.getBook().getBookId() == newDetail.getBook().getBookId())
                        .findFirst()
                        .orElse(null);

                if (existingDetail != null) {
                    existingDetail.setQuantity(newDetail.getQuantity());
                    existingDetail.setUnitPrice(newDetail.getUnitPrice());
                    existingDetail.setTotalPrice(newDetail.getQuantity() * newDetail.getUnitPrice());
                    totalValue += existingDetail.getTotalPrice();
                } else {
                    // tao moi
                    newDetail.setTotalPrice(newDetail.getQuantity() * newDetail.getUnitPrice());
                    newDetail.setPurchaseOrder(currentPurchaseOrder);
                    currentPurchaseOrder.getPurchaseOrderDetails().add(newDetail);
                    totalValue += newDetail.getTotalPrice();
                }

                totalQuantity += newDetail.getQuantity();

                // Cap nhat so luong ton
                Book book = bookRespository.findBookByBookId(newDetail.getBook().getBookId());
                if (book != null) {
                    int newQuantityInStock = book.getQuantityInStock() + newDetail.getQuantity();
                    book.setQuantityInStock(newQuantityInStock);
                    bookRespository.save(book);
                }
            }

            currentPurchaseOrder.setTotalQuantity(totalQuantity);
            currentPurchaseOrder.setTotalValue(totalValue);

            return purchaseOrderRepository.save(currentPurchaseOrder);
        }

        return null;
    }

    //DELETE
    @Transactional
    public boolean deletePurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findPurchaseOrderByPurchaseOrderId(id);

        if (purchaseOrder == null) {
            return false;
        }

        // Cap nhat so luong ton cua sach
        for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetails()) {
            Book book = bookRespository.findBookByBookId(detail.getBook().getBookId());
            if (book != null) {
                int updatedQuantityInStock = book.getQuantityInStock() - detail.getQuantity();
                book.setQuantityInStock(updatedQuantityInStock);
                bookRespository.save(book);
            }
        }

        // Xoa purchaseOrderDetail
        purchaseOrderDetailRepository.deleteAll(purchaseOrder.getPurchaseOrderDetails());

        purchaseOrderRepository.delete(purchaseOrder);

        return true;
    }


}
