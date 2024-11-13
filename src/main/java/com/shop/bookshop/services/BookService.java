package com.shop.bookshop.services;

import com.shop.bookshop.domain.*;
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
    public Page<Book> getAllBookPaging(int pageNum){
        int pageSize = 2;

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
    public void placeOrder(User user, HttpSession session,
                           String receiverName, String receiverAdress,
                           String receiverEmail, String receiverPhone){
        //create orderDetail
        Cart cart = cartRepository.findByUser(user);
        if(cart != null){
            List<CartDetail> cartDetails = cart.getCartDetails();

            if(cartDetails != null){
                //create order
                Order order = new Order();
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAddress(receiverAdress);
                order.setReceiverPhone(receiverPhone);
                order.setReceiverEmail(receiverEmail);
                order.setStatus(StatusEnum.PENDING);
                double sum = 0;
                for (CartDetail cartDetail : cartDetails){
                    sum += cartDetail.getPrice();
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


    }


}
