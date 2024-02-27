import createToast, {toastComponent} from "./toast.js";
import {setTotalCartItemsQuantity} from "./header.js";

// STATIC DATA
const contextPathMetaTag = document.querySelector("meta[name='contextPath']");
const currentUserIdMetaTag = document.querySelector("meta[name='currentUserId']");
const productIdMetaTag = document.querySelector("meta[name='productId']");
const deliveryPriceRootElement = document.querySelector("#delivery-price");
const quantityInput = document.querySelector("#quantity-product");
const productTitleElement = document.querySelector(".title");
const priceProduct = document.querySelector(".price.h4");
const deliveryMethodRadioElements = [...document.querySelectorAll("input[name='delivery-method']")];
const tempPriceRootElement = document.querySelector("#temp-price");
const totalPriceRootElement = document.querySelector("#total-price");
const checkoutNowElement = document.querySelector("#checkout-now")
// MESSAGES
const REQUIRED_SIGNIN_MESSAGE = "Vui lòng đăng nhập để thực hiện thao tác!";
const SUCCESS_BUY_MESSAGE = (quantity, productTitle) =>
    `Đặt thành công ${quantity} sản phẩm ${productTitle} thành công!`;
const FAILED_BUY_ITEM_MESSAGE = "Đã có lỗi truy vấn!";
const deliveryMethodInputValues = {
    "1": {
        deliveryMethod: 1,
        deliveryPrice: 15000,
    },
    "2": {
        deliveryMethod: 2,
        deliveryPrice: 50000,
    },
};
const initialOrder = {
    deliveryMethod: 1,
    deliveryPrice: 15000,
};
const initProduct = {
    id: productIdMetaTag.content,
    quantity: quantityInput.value
}

async function _fetchPostCheckOutNow() {
    const orderProductRequest = {
        productId: Number(productIdMetaTag.content),
        tempPrice: state.getTempPrice(),
        deliveryMethod: state.order.deliveryMethod,
        deliveryPrice: state.order.deliveryPrice,
        quantity: Number(document.getElementById("quantity-product").innerText),
    }
    const response = await fetch(contextPathMetaTag.content + "/checkout", {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-type": "application/json",
        },
        body: JSON.stringify(orderProductRequest)
    })
    return  [response.status, await response.json()]
}

const state = {
    order: {...initialOrder},
    initState: async => {
        render();
        attachEventHandlersForNoneRerenderElements();
    },
    checkoutProduct: async () => {
        if (confirm("Bạn đã chắn với đơn hàng này chưa?")) {
            const [status] = await _fetchPostCheckOutNow();
            if (status === 200){
                createToast(toastComponent(SUCCESS_BUY_MESSAGE(
                    Number(document.getElementById("quantity-product").innerText),
                    productTitleElement.innerHTML), "success"));
                window.location.href = contextPathMetaTag.content + "/"
            }
        }
    },
    changeDeliveryMethod: (deliveryMethodValue) => {
        if (state.order.deliveryMethod !== Number(deliveryMethodValue)) {
            state.order.deliveryMethod = deliveryMethodInputValues[deliveryMethodValue].deliveryMethod;
            state.order.deliveryPrice = deliveryMethodInputValues[deliveryMethodValue].deliveryPrice;
            render();
        }
    },
    getTempPrice: () => {
        const tmpPrice = priceProduct.innerText.replace(".", "")
        const quantity = document.getElementById("quantity-product").innerText;
        return Number(tmpPrice) * Number(quantity);
    },
    getDeliveryPrice: () => state.order.deliveryPrice,
    getTotalPrice: () => state.getTempPrice() + state.getDeliveryPrice(),
}

function _formatPrice(price) {
    return new Intl.NumberFormat('vi-VN').format(price.toFixed());
}

function render() {
    // Render tempPriceRootElement, deliveryPriceRootElement, totalPriceRootElement
    tempPriceRootElement.innerHTML = _formatPrice(state.getTempPrice());
    deliveryPriceRootElement.innerHTML = _formatPrice(state.getDeliveryPrice());
    totalPriceRootElement.innerHTML = _formatPrice(state.getTotalPrice());

    // Render deliveryMethodRadioElements
    deliveryMethodRadioElements.forEach((radio) => {
        radio.checked = radio.value === String(state.order.deliveryMethod);
    })

    checkoutNowElement.addEventListener("click", state.checkoutProduct);
}

function attachEventHandlersForNoneRerenderElements() {
    // Attach event handlers for delivery method radios
    deliveryMethodRadioElements.forEach((radio) => {
        radio.disabled = false;
        radio.addEventListener("click", () => state.changeDeliveryMethod(radio.value));
    });
}

if (currentUserIdMetaTag) {
    void state.initState();
}