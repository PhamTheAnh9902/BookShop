
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
        document.addEventListener("DOMContentLoaded", function () {
            const minusButtons = document.querySelectorAll(".cart__body-quantity-minus");
            const plusButtons = document.querySelectorAll(".cart__body-quantity-plus");
            const totalPriceSpan = document.querySelector(".total-price");

            // Xử lý sự kiện khi nhấn nút "-"
            minusButtons.forEach(button => {
                button.addEventListener("click", function () {
                    const quantityInput = this.nextElementSibling;
                    let currentQuantity = parseInt(quantityInput.value);
                    const cartDetailId = quantityInput.getAttribute("data-cart-detail-id");
                    const priceSpan = document.querySelector(`.price[data-cart-detail-id="${cartDetailId}"]`);
                    const bookPrice = parseFloat(quantityInput.getAttribute("data-cart-detail-price"));

                    if (currentQuantity > 1) {
                        quantityInput.value = --currentQuantity;
                        const newPrice = currentQuantity * bookPrice;
                        priceSpan.textContent = formatCurrency(newPrice);
                        updateCart(cartDetailId, currentQuantity);
                        updateTotalPrice();
                    }
                });
            });

            // Xử lý sự kiện khi nhấn nút "+"
            plusButtons.forEach(button => {
                button.addEventListener("click", function () {
                    const quantityInput = this.previousElementSibling;
                    let currentQuantity = parseInt(quantityInput.value);
                    const cartDetailId = quantityInput.getAttribute("data-cart-detail-id");
                    const priceSpan = document.querySelector(`.price[data-cart-detail-id="${cartDetailId}"]`);
                    const bookPrice = parseFloat(quantityInput.getAttribute("data-cart-detail-price"));

                    quantityInput.value = ++currentQuantity;
                    const newPrice = currentQuantity * bookPrice;
                    priceSpan.textContent = formatCurrency(newPrice);
                    updateCart(cartDetailId, currentQuantity);
                    updateTotalPrice();
                });
            });

            // Hàm định dạng tiền tệ theo yêu cầu (4,000 VNĐ)
            function formatCurrency(amount) {
                const formatter = new Intl.NumberFormat('en-US'); // Định dạng 'en-US' sử dụng dấu phẩy
                return formatter.format(amount) + ' VNĐ';
            }

            // Hàm cập nhật giỏ hàng khi thay đổi số lượng
            function updateCart(cartDetailId, quantity) {
                fetch(`/cart/update`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ cartDetailId, quantity })
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            console.log('Cập nhật giỏ hàng thành công');
                        } else {
                            console.error('Cập nhật giỏ hàng thất bại');
                        }
                    })
                    .catch(error => console.error('Lỗi:', error));
            }

            // Hàm tính toán và cập nhật tổng cộng
            function updateTotalPrice() {
                const priceSpans = document.querySelectorAll(".price");
                let total = 0;

                priceSpans.forEach(span => {
                    const priceText = span.textContent.replace(' VNĐ', '').replace(/,/g, '');
                    total += parseFloat(priceText);
                });

                totalPriceSpan.textContent = formatCurrency(total);
            }

            // Khởi tạo giá tổng cộng khi tải trang
            updateTotalPrice();
        });


