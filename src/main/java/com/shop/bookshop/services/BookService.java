package com.shop.bookshop.services;

import com.shop.bookshop.domain.*;
import com.shop.bookshop.domain.Dto.PromotionDTO;
import com.shop.bookshop.repository.*;
import com.shop.bookshop.util.constant.StatusEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRespository bookRespository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserService userService;

    public List<Book> getAllBook(){
        return bookRespository.findAll();
    }
    public Page<Book> getAllBookPaging(int pageNum, int pagesize){
        int pageSize = pagesize;

        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        return bookRespository.findAll(pageable);
    }

    public Book createBook(Book book) {
        return bookRespository.save(book);
    }

    public Book getBookById(long id) {
        Optional<Book> bookOptional = bookRespository.findById(id);
        if (bookOptional.isPresent()) {
            return bookOptional.get();
        }
        return null;
    }

    public Book updateUser(Long id, Book book) {
        Book currentBook = this.getBookById(id);
        if (currentBook != null) {
            currentBook.setTitle(book.getTitle());
            currentBook.setQuantityInStock(book.getQuantityInStock());
            currentBook.setPrice(book.getPrice());
            currentBook.setImg(book.getImg());
            currentBook.setDescription(book.getDescription());
            currentBook.setCategory(book.getCategory());
            currentBook.setPublisher(book.getPublisher());
            currentBook.setAuthors(book.getAuthors());
        }
        return bookRespository.save(currentBook);
    }

    public boolean deleteUser(long id) {
        try {
            Book book = bookRespository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Book not found"));
            book.getAuthors().clear();
            bookRespository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Book> searchBookByName(String query) {
        return bookRespository.findBookByTitleContainingIgnoreCase(query);
    }

    //Sort
    public List<Book> getAllBooksSorted(Sort sort) {
        return bookRespository.findAll(sort);
    }

    //CART
    public void addBookToCart(String email, long bookId, HttpSession session){

        User user = userService.getUserByEmail(email);
        if (user != null){
            // check user da co cart ?
            Cart cart = cartRepository.findByUser(user);
            if (cart == null){
                //tao moi
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);

                cart = cartRepository.save(otherCart);
            }

            //save cart_detail
            // find book by id
            Optional<Book> book = bookRespository.findById(bookId);
            if (book.isPresent()){
                Book currentBook = book.get();

                //check san pham da tung duoc them
                CartDetail  oldDetail = cartDetailRepository.findByCartAndBook(cart, currentBook);

                if(oldDetail == null){
                    CartDetail cartDetail = new CartDetail();
                    cartDetail.setCart(cart);
                    cartDetail.setBook(currentBook);
                    cartDetail.setPrice(currentBook.getPrice());
                    cartDetail.setQuantity(1);
                    cartDetailRepository.save(cartDetail);

                    //update sum
                    int s = cart.getSum() + 1;
                    cart.setSum(s);
                    cartRepository.save(cart);
                    session.setAttribute("sum",s);
                }
                else {
                    oldDetail.setQuantity(oldDetail.getQuantity()+1);
                    cartDetailRepository.save(oldDetail);
                }

            }
        }
    }

    public Cart findByUser(User currentUser) {
        return cartRepository.findByUser(currentUser);
    }

    public void removeCartDetail(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetailOptional = cartDetailRepository.findById(cartDetailId);
        if (cartDetailOptional != null){
            CartDetail cartDetail = cartDetailOptional.get();

            Cart currentCart = cartDetail.getCart();

            //delete cart-detail
            cartDetailRepository.deleteById(cartDetailId);

            //update cart
            if (currentCart.getSum() > 1){
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);
                session.setAttribute("sum", s);
                cartRepository.save(currentCart);
            } else {
                // delete cart (sum =1)
                cartRepository.deleteById(currentCart.getCartId());
                session.setAttribute("sum", 0);
            }
        }
    }


    //ORDER
    public Order placeOrder(User user, HttpSession session,
                           String receiverName, String receiverAdress,
                           String receiverEmail, String receiverPhone,
                           Promotion promotion){
        //create orderDetail
        Cart cart = cartRepository.findByUser(user);
        LocalDateTime localDateTime = LocalDateTime.now();
        Order order = new Order();
        if(cart != null){
            List<CartDetail> cartDetails = cart.getCartDetails();

            if(cartDetails != null){
                //create order
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAddress(receiverAdress);
                order.setReceiverPhone(receiverPhone);
                order.setReceiverEmail(receiverEmail);
                order.setStatus(StatusEnum.PENDING);
                order.setDiscountValue(0);
                order.setStatusPayment("Chưa thanh toán");
                order.setCreateDate(localDateTime);
                double sum = 0;
                for (CartDetail cartDetail : cartDetails){
                    sum += (cartDetail.getPrice() * cartDetail.getQuantity());
                }
                if (promotion != null) {
                    System.out.println("Promotion found: " + promotion.getCode());

                    boolean isFirstOrder = orderRepository.countByUser(user) == 0;
                    boolean isOrderTotalAboveThreshold = sum > 100_000;
                    if (promotion.getCode().equals("FIRST_ORDER") && isFirstOrder) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    } else if (promotion.getCode().equals("OVER_100K") && isOrderTotalAboveThreshold) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    } else if (promotion.getCode().equals("DISCOUNT10")) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    }
                    else {
                        return null;
                    }
                    order.setDiscountValue(promotion.getDiscountRate() * 100);
                }
                order.setTotalPrice(sum);
                orderRepository.save(order);

                for (CartDetail cd : cartDetails ){
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setBook(cd.getBook());
                    orderDetail.setPrice(cd.getPrice());
                    orderDetail.setQuantity(cd.getQuantity());
                    orderDetailRepository.save(orderDetail);

                    //Update QuantityStock
                    Book book = cd.getBook();
                    book.setQuantityInStock((int) (book.getQuantityInStock() - cd.getQuantity()));
                    bookRespository.save(book);
                }
            }

            //delete cart_detail and cart
            for (CartDetail cd : cartDetails){
                cartDetailRepository.deleteById(cd.getCartDetailId());
            }

            cartRepository.deleteById(cart.getCartId());

            // update session
            session.setAttribute("sum", 0);
        }
        return order;
    }

    public Order placeOrderVnPay(User user, HttpSession session,
                                 String receiverName, String receiverAdress,
                                 String receiverEmail, String receiverPhone,
                                 PromotionDTO promotion){
        //create orderDetail
        Cart cart = cartRepository.findByUser(user);
        LocalDateTime localDateTime = LocalDateTime.now();
        Order order = new Order();
        if(cart != null){
            List<CartDetail> cartDetails = cart.getCartDetails();

            if(cartDetails != null){
                //create order
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAddress(receiverAdress);
                order.setReceiverPhone(receiverPhone);
                order.setReceiverEmail(receiverEmail);
                order.setStatus(StatusEnum.PENDING);
                order.setDiscountValue(0);
                order.setStatusPayment("Đã thanh toán");
                order.setCreateDate(localDateTime);
                double sum = 0;
                for (CartDetail cartDetail : cartDetails){
                    sum += (cartDetail.getPrice() * cartDetail.getQuantity());
                }
                if (promotion != null) {
                    System.out.println("Promotion found: " + promotion.getCode());

                    boolean isFirstOrder = orderRepository.countByUser(user) == 0;
                    boolean isOrderTotalAboveThreshold = sum > 100_000;
                    if (promotion.getCode().equals("FIRST_ORDER") && isFirstOrder) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    } else if (promotion.getCode().equals("OVER_100K") && isOrderTotalAboveThreshold) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    } else if (promotion.getCode().equals("DISCOUNT10")) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    }
                    else {
                        return null;
                    }
                    order.setDiscountValue(promotion.getDiscountRate() * 100);
                }
                order.setTotalPrice(sum);
                orderRepository.save(order);

                for (CartDetail cd : cartDetails ){
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setBook(cd.getBook());
                    orderDetail.setPrice(cd.getPrice());
                    orderDetail.setQuantity(cd.getQuantity());
                    orderDetailRepository.save(orderDetail);

                    //Update QuantityStock
                    Book book = cd.getBook();
                    book.setQuantityInStock((int) (book.getQuantityInStock() - cd.getQuantity()));
                    bookRespository.save(book);
                }
            }

            //delete cart_detail and cart
            for (CartDetail cd : cartDetails){
                cartDetailRepository.deleteById(cd.getCartDetailId());
            }

            cartRepository.deleteById(cart.getCartId());

            // update session
            session.setAttribute("sum", 0);
        }
        return order;
    }


    public List<Book> findByCategoryId(long id) {
        return bookRespository.findBookByCategoryCategoryId(id);
    }

    public void updateCartBeforeOrder(List<CartDetail> cartDetails) {

        for(CartDetail cartdetail : cartDetails){
            Optional<CartDetail> cartDetailOptional = cartDetailRepository.findById(cartdetail.getCartDetailId());
            if (cartDetailOptional.isPresent()){
                CartDetail currentCartDetail = cartDetailOptional.get();
                currentCartDetail.setQuantity(cartdetail.getQuantity());
                cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    public double calculateOrderAmount(User currentUser, Promotion promotion) {
        Cart cart = cartRepository.findByUser(currentUser);
        if (cart != null){
            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails != null){
                double sum = 0;
                for (CartDetail cartDetail : cartDetails){
                    sum += (cartDetail.getPrice() * cartDetail.getQuantity());
                }
                if (promotion != null) {
                    System.out.println("Promotion found: " + promotion.getCode());

                    boolean isFirstOrder = orderRepository.countByUser(currentUser) == 0;
                    boolean isOrderTotalAboveThreshold = sum > 100_000;
                    if (promotion.getCode().equals("FIRST_ORDER") && isFirstOrder) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    } else if (promotion.getCode().equals("OVER_100K") && isOrderTotalAboveThreshold) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    } else if (promotion.getCode().equals("DISCOUNT10")) {
                        double discountRate = promotion.getDiscountRate() / 100.0;
                        sum *= (1 - discountRate);
                    }
                }
                return sum;
            }
        }
        return 0;
    }
}
