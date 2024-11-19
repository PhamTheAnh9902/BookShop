
        // $(".header__nav-menu-wrap").mouseover(function(){
        //     $(".menu__nav").fadeIn();
        // });

        // $(".menu__nav").mouseleave(function(){
        //     $(".menu__nav").fadeOut();
        // });

        $(".header__nav-menu-wrap").click(function(){
            $(".menu__nav").toggle();
        });

        $(".modal__overlay").click(function(){
            $(".main__modal").hide();
        });

        $(".sale-off__close").click(function(){
            $(".main__modal").hide();
        });

        $(".product__panel-item").click(function(){
            $(location).attr('href','product.html');
        });

        // $(".product__main-img-list img").click(function(){
        //     $("product__main-img-primary img").attr('src','product.html');
        // });

        // $(".header__nav-menu-wrap").click(function(){
        //     $(".menu__nav").hide();
        // });
        //Get the button
var mybutton = document.getElementById("myBtn-scroll");

// When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function() {scrollFunction()};

function scrollFunction() {
  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
    mybutton.style.display = "block";
  } else {
    mybutton.style.display = "none";
  }
}

// When the user clicks on the button, scroll to the top of the document
function topFunction() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

// Tăng giảm số lượng cập nhật đơn giá
        // Hàm định dạng số thành dạng
        function formatCurrency(amount) {
            return amount.toLocaleString('vi-VN') + ' VNĐ';
        }

        // Hàm tính toán và cập nhật giá trị
        function updateCart() {
            let total = 0;

            // Duyệt qua tất cả các sản phẩm trong giỏ hàng
            document.querySelectorAll('.cart__body').forEach((item) => {
                const priceElement = item.querySelector('.book-price');
                const quantityInput = item.querySelector('.cart__body-quantity-total');
                const totalPriceElement = item.querySelector('.price');
                const spanElement = item.querySelector(".book-price");

                if (priceElement && quantityInput && totalPriceElement) {
                    const price = parseFloat(priceElement.textContent.replace(/[^\d.-]/g, ''));  // Loại bỏ ký tự không phải số
                    const quantity = parseInt(quantityInput.value);
                    const totalPrice = price * quantity;


                    const index = spanElement.getAttribute('data-cart-detail-index');
                    console.log(index);
                    const quantityUpdate = document.getElementById(index);
                    quantityUpdate.value = quantity;

                    totalPriceElement.textContent = formatCurrency(totalPrice);
                    total += totalPrice;

                }
            });

            const totalPriceSpan = document.querySelector('.total-price');
            if (totalPriceSpan) {
                totalPriceSpan.textContent = formatCurrency(total);
            }


        }

        // Sự kiện thay đổi số lượng sản phẩm
        function attachEventListeners() {
            const quantityInputs = document.querySelectorAll('.cart__body-quantity-total');
            const plusButtons = document.querySelectorAll('.cart__body-quantity-plus');
            const minusButtons = document.querySelectorAll('.cart__body-quantity-minus');

            quantityInputs.forEach(input => {
                input.addEventListener('change', updateCart);
            });

            plusButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const input = this.previousElementSibling;
                    input.value = parseInt(input.value) + 1;
                    updateCart();
                });
            });

            minusButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const input = this.nextElementSibling;
                    if (parseInt(input.value) > 1) {
                        input.value = parseInt(input.value) - 1;
                        updateCart();
                    }
                });
            });
        }

        // Cập nhật giỏ hàng khi trang tải xong
        document.addEventListener('DOMContentLoaded', function() {
            updateCart();
            attachEventListeners();
        });



