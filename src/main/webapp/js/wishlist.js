import createToast, {toastComponent} from "./toast.js";

// STATIC DATA
const contextPathMetaTag = document.querySelector("meta[name='contextPath']")
const SUCCESS_DELETE_WISHLIST_ITEM_MESSAGE = "Đã xóa sản phẩm khỏi sản phẩm yêu thích thành công!";
const FAILED_DELETE_MESSAGE = "Đã có lỗi truy vấn!";
const REQUIRED_SIGNIN_MESSAGE = "Vui lòng đăng nhập để thực hiện thao tác!";

function noneSignInEvent() {
    createToast(toastComponent(REQUIRED_SIGNIN_MESSAGE));
}

async function _fetchDeleteWishlistItem(itemId) {
    const response = await fetch(contextPathMetaTag.content + `/wishlist?id=${itemId}`, {
        method: "DELETE",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
    });
    return [response.status, await response.json()];
}

async function deleteWishlistItem(itemId) {
    if (confirm("Bạn có muốn xóa sản phẩm yêu thích này?")) {
        const [status] = await _fetchDeleteWishlistItem(itemId);
        if (status === 200) {
            createToast(toastComponent(SUCCESS_DELETE_WISHLIST_ITEM_MESSAGE, "success"));
            window.location.href = contextPathMetaTag.content + "/wishlist"
        } else if (status === 400) {
            createToast(toastComponent(FAILED_DELETE_MESSAGE, "danger"));
        }
    }
}

const currentUserIdMetaTag = document.querySelector("meta[name='currentUserId']");
const deleteWishlistItemBtn = [...document.querySelectorAll("#delete-wishlist")]

if (currentUserIdMetaTag) {
    deleteWishlistItemBtn.forEach((button) => {
        const id = button.getAttribute("content")
        button.addEventListener("click", () => deleteWishlistItem(id))
    })
} else {
    deleteWishlistItemBtn.addEventListener("click", noneSignInEvent)
}