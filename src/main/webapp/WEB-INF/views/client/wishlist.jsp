<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">

<head>
    <jsp:include page="/common/meta.jsp"/>
    <title>Sản phẩm yêu thích</title>

    <!-- Custom Scripts -->
    <script src="${pageContext.request.contextPath}/js/toast.js" type="module"></script>
    <script src="${pageContext.request.contextPath}/js/wishlist.js" type="module"></script>
</head>
<body>
<jsp:include page="/common/client/header.jsp"/>

<section class="section-pagetop bg-light">
    <div class="container">
        <h2 class="title-page">Sản phẩm yêu thích</h2>
    </div> <!-- container.// -->
</section> <!-- section-pagetop.// -->

<section class="section-content padding-y">
    <div class="container">
        <div class="row">
            <jsp:include page="/common/client/navPanel.jsp">
                <jsp:param name="active" value="WISHLIST"/>
            </jsp:include>

            <main class="col-md-9">
                <article class="card">
                    <div class="card-body">
                        <c:choose>
                        <c:when test="${empty sessionScope.currentUser}">
                            <p>
                                Vui lòng <a href="${pageContext.request.contextPath}/signin">đăng nhập</a> để sử
                                dụng chức năng sản
                                phẩm yêu thích.
                            </p>
                        </c:when>
                        <c:when test="${empty requestScope.wishlistItems}">
                            <p>
                                Người dùng không có sản phẩm yêu thích.
                            </p>
                        </c:when>
                        <c:otherwise>
                        <div class="row g-3">
                            <c:forEach var="wishlistItem" items="${requestScope.wishlistItems}">
                                <div class="col-lg-6">
                                    <figure class="d-flex align-items-center m-0">
                                        <div class="aside">
                                            <c:choose>
                                                <c:when test="${empty wishlistItem.product.imageName}">
                                                    <img width="80"
                                                         height="80"
                                                         src="${pageContext.request.contextPath}/img/280px.png"
                                                         alt="280px.png">
                                                </c:when>
                                                <c:otherwise>
                                                    <img width="80"
                                                         height="80"
                                                         src="${pageContext.request.contextPath}/image/${wishlistItem.product.imageName}"
                                                         alt="${wishlistItem.product.imageName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <figcaption class="ps-3">
                                            <a href="${pageContext.request.contextPath}/product?id=${wishlistItem.product.id}"
                                               target="_blank">
                                                    ${wishlistItem.product.name}
                                            </a>
                                            <p class="mb-2">
                                                <c:choose>
                                                    <c:when test="${wishlistItem.product.discount == 0}">
                                                        <span class="price">
                                                          <fmt:formatNumber pattern="#,##0"
                                                                            value="${wishlistItem.product.price}"/>₫
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="price">
                                                          <fmt:formatNumber
                                                                  pattern="#,##0"
                                                                  value="${wishlistItem.product.price * (100 - wishlistItem.product.discount) / 100}"/>₫
                                                        </span>
                                                        <span class="ms-2 text-muted text-decoration-line-through">
                                                          <fmt:formatNumber pattern="#,##0"
                                                                            value="${wishlistItem.product.price}"/>₫
                                                        </span>
                                                        <span class="ms-2 badge bg-info">
                                                          -<fmt:formatNumber pattern="#,##0"
                                                                             value="${wishlistItem.product.discount}"/>%
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                                <button type="button" class="btn btn-danger btn-sm"
                                                        name="btn-delete-wishlist"
                                                        content="${wishlistItem.id}"
                                                        id="delete-wishlist"
                                                        data-toggle="tooltip"
                                                        title="Xóa khỏi danh sách yêu thích">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                        </figcaption>
                                    </figure>
                                </div>
                                <!-- col.// -->
                            </c:forEach>
                        </div>
                    </div>
                    <!-- row .// -->
                    </c:otherwise>
                    </c:choose>
                    <div class="d-flex justify-content-center">
                        <c:if test="${requestScope.totalPage != 0}">
                            <nav class="mt-4">
                                <ul class="pagination">
                                    <li class="page-item ${requestScope.page == 1 ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/wishlist?page=${requestScope.page - 1}">
                                            Trang trước
                                        </a>
                                    </li>

                                    <c:forEach begin="1" end="${requestScope.totalPage}" var="i">
                                        <c:choose>
                                            <c:when test="${requestScope.page == i}">
                                                <li class="page-item active">
                                                    <a class="page-link">${i}</a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="${pageContext.request.contextPath}/wishlist?page=${i}">
                                                            ${i}
                                                    </a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <li class="page-item ${requestScope.page == requestScope.totalPage ? 'disabled' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/wishlist?page=${requestScope.page + 1}">
                                            Trang sau
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </c:if>
                    </div>
        </div> <!-- card-body.// -->

        </article>

        </main> <!-- col.// -->

    </div> <!-- row.// -->
    </div> <!-- container.// -->
</section> <!-- section-content.// -->
<jsp:include page="/common/client/footer.jsp"/>
<div class="toast-container position-fixed bottom-0 start-0 p-3"></div> <!-- toast-container.// -->
</body>

</html>
