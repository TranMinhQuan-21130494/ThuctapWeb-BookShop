<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <jsp:include page="/common/meta.jsp"/>
    <title>${requestScope.product.name}</title>

    <!-- Custom Scripts -->
    <script src="${pageContext.request.contextPath}/js/toast.js" type="module"></script>
    <script src="${pageContext.request.contextPath}/js/checkout.js" type="module"></script>

</head>

<body>
<jsp:include page="/common/client/header.jsp"/>
<section class="section-pagetop-2 bg-light">
    <div class="container">
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item" aria-current="page">
                    <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                </li>
                <li class="breadcrumb-item" aria-current="page">
                    <a href="${pageContext.request.contextPath}/category?id=${requestScope.category.id}">${requestScope.category.name}</a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">${requestScope.product.name}</li>
            </ol>
        </nav>
    </div> <!-- container.// -->
</section> <!-- section-pagetop-2.// -->
<section class="section-content padding-y">
    <c:choose>
        <c:when test="${empty sessionScope.currentUser}">
            <div class="container">
                <div class="row">
                    <p>
                        Vui lòng <a href="${pageContext.request.contextPath}/signin">đăng nhập</a> để sử dụng trang này.
                    </p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="container d-flex justify-content-between">
                <div class="row col-8">
                    <aside class="col-md-5 mb-md-0 mb-4 d-flex justify-content-center align-items-center">
                        <c:choose>
                            <c:when test="${empty requestScope.product.imageName}">
                                <img width="280"
                                     height="280"
                                     class="img-fluid"
                                     src="${pageContext.request.contextPath}/img/280px.png"
                                     alt="280px.png">
                            </c:when>
                            <c:otherwise>
                                <img width="280"
                                     height="280"
                                     class="img-fluid"
                                     src="${pageContext.request.contextPath}/image/${requestScope.product.imageName}"
                                     alt="${requestScope.product.imageName}">
                            </c:otherwise>
                        </c:choose>
                    </aside>

                    <main class="col-md-7">

                        <h2 class="title">${requestScope.product.name}</h2>

                        <div class="rating-wrap my-3">
          <span class="rating-stars me-2">
            <c:forEach begin="1" end="5" step="1" var="i">
                <i class="bi bi-star-fill ${i <= requestScope.averageRatingScore ? 'active' : ''}"></i>
            </c:forEach>
          </span>
                            <small class="label-rating text-muted me-2">${requestScope.totalProductReviews} đánh giá</small>
                            <small class="label-rating text-success">
                                <i class="bi bi-bag-check-fill"></i> ${requestScope.product.totalBuy} đã mua
                            </small>
                        </div>

                        <div class="mb-4">
                            <c:choose>
                                <c:when test="${requestScope.product.discount == 0}">
              <span class="price h4">
                <fmt:formatNumber pattern="#,##0" value="${requestScope.product.price}"/>
              </span>
                                </c:when>
                                <c:otherwise>
              <span class="price h4"><fmt:formatNumber
                      pattern="#,##0"
                      value="${requestScope.product.price * (100 - requestScope.product.discount) / 100}"/></span>
                                    <span class="ms-2 text-muted text-decoration-line-through">
                <fmt:formatNumber pattern="#,##0" value="${requestScope.product.price}"/>
              </span>
                                    <span class="ms-2 badge bg-info">
                -<fmt:formatNumber pattern="#,##0" value="${requestScope.product.discount}"/>%
              </span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <dl class="row mb-4">
                            <dt class="col-xl-4 col-sm-5 col-6">Tác giả</dt>
                            <dd class="col-xl-8 col-sm-7 col-6">${requestScope.product.author}</dd>

                            <dt class="col-xl-4 col-sm-5 col-6">Số trang</dt>
                            <dd class="col-xl-8 col-sm-7 col-6">${requestScope.product.pages}</dd>

                            <dt class="col-xl-4 col-sm-5 col-6">Nhà xuất bản</dt>
                            <dd class="col-xl-8 col-sm-7 col-6">${requestScope.product.publisher}</dd>

                            <dt class="col-xl-4 col-sm-5 col-6">Năm xuất bản</dt>
                            <dd class="col-xl-8 col-sm-7 col-6">${requestScope.product.yearPublishing}</dd>

                            <dt class="col-xl-4 col-sm-5 col-6">Số lượng</dt>
                            <dd class="col-xl-8 col-sm-7 col-6" id="quantity-product">${requestScope.quantity}</dd>
                        </dl>

                    </main>
                </div>
                <div class="row col-4">
                    <aside>


                        <div class="card">
                            <div class="card-body">
                                <dl class="row mb-4">
                                    <div class="card-title">Người nhận:
                                        <span class="badge bg-info" style="font-size: 15px;">
                                                ${sessionScope.currentUser.fullName}</span>
                                    </div>
                                    <div class="card-title">Số điện thoại:
                                        <span class="badge bg-info" style="font-size: 15px;">
                                                ${sessionScope.currentUser.phoneNumber}</span>
                                    </div>
                                    <div class="card-title">Địa chỉ:
                                            ${sessionScope.currentUser.address}
                                    </div>
                                    <p class="card-title">Hình thức giao hàng</p>
                                    <form>
                                        <div class="form-check mb-2">
                                            <input class="form-check-input" type="radio" name="delivery-method"
                                                   id="delivery-method-1" value="1" disabled>
                                            <label class="form-check-label" for="delivery-method-1">Giao tiêu chuẩn</label>
                                        </div>
                                        <div class="form-check mb-2">
                                            <input class="form-check-input" type="radio" name="delivery-method"
                                                   id="delivery-method-2" value="2" disabled>
                                            <label class="form-check-label" for="delivery-method-2">Giao nhanh</label>
                                        </div>
                                    </form>
                                    <dt class="col-xxl-6 col-lg-5 col-6">Tạm tính:</dt>
                                    <dd class="col-xxl-6 col-lg-7 col-6 text-end mb-2"><span id="temp-price">0</span>₫
                                    </dd>
                                    <dt class="col-xxl-6 col-lg-6 col-6">Phí vận chuyển:</dt>
                                    <dd class="col-xxl-6 col-lg-6 col-6 text-end mb-2"><span
                                            id="delivery-price">0</span>₫
                                    </dd>
                                    <dt class="col-xxl-6 col-lg-5 col-6">Tổng cộng:</dt>
                                    <dd class="col-xxl-6 col-lg-7 col-6 text-end mb-2"><strong><span
                                            id="total-price">0</span>₫</strong></dd>
                                </dl>
                                <div class="d-flex justify-content-center">
                                    <button type="button" class="btn btn-primary ms-lg-3" id="checkout-now">Đặt hàng</button>
                                </div>
                            </div> <!-- card-body.// -->
                        </div> <!-- card.// -->

                    </aside>
                </div>
            </div> <!-- container.// -->
        </c:otherwise>
    </c:choose>

</section> <!-- section-content.// -->
<jsp:include page="/common/client/footer.jsp"/>

<div class="toast-container position-fixed bottom-0 start-0 p-3"></div> <!-- toast-container.// -->
</body>
</html>
