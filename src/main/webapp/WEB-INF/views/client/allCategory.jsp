<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">

<head>
    <%--    <%@include file="/common/meta.jsp"%>--%>
    <jsp:include page="/common/meta.jsp"/>
    <title>Trang chủ</title>
</head>

<body>
<jsp:include page="/common/client/header.jsp"/>

<section class="section-content mb-2">
    <div class="container">
        <header class="section-heading py-4 d-flex justify-content-between">
            <h3 class="section-title">Danh mục sản phẩm</h3>
        </header> <!-- section-heading.// -->
        <div class="row item-grid">
            <c:forEach var="category" items="${requestScope.categories}">
                <div class="col-lg-3 col-md-6">
                    <div class="card mb-4">
                        <div class="card-body">

                            <a href="<c:url value="/category?id=${category.id}"/>"
                               class="stretched-link">
                                <div class="d-flex align-items-center">
                                    <c:choose>
                                        <c:when test="${empty category.imageName}">
                                            <img width="50" height="50"
                                                 src="<c:url value="/img/50px.png"/>"
                                                 alt="50px.png">
                                        </c:when>
                                        <c:otherwise>
                                            <img width="50" height="50"
                                                 src="<c:url value="/image/${category.imageName}"/>"
                                                 alt="${category.imageName}">
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="category-title ms-3">${category.name}</span>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
                <!-- col.// -->
            </c:forEach>
        </div> <!-- row.// -->
        <c:if test="${requestScope.totalPages != 0}">
            <div class="d-flex justify-content-center">
                <nav class="mt-4">
                    <ul class="pagination">
                        <li class="page-item ${requestScope.page == 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="<c:url value="/?category=all&page=${param.page - 1}"/>">
                                Trang trước
                            </a>
                        </li>

                        <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                            <c:choose>
                                <c:when test="${param.page == i}">
                                    <li class="page-item active">
                                        <a class="page-link">${i}</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="<c:url value="/?category=all&page=${i}"/>">
                                                ${i}
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <li class="page-item ${param.page == requestScope.totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="<c:url value="/?category=all&page=${param.page + 1}"/>">
                                Trang sau
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </c:if>
    </div> <!-- container.// -->
</section> <!-- section-content.// -->

<jsp:include page="/common/client/footer.jsp"/>
</body>

</html>
