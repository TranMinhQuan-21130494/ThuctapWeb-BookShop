<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <jsp:include page="/common/meta.jsp"/>
    <title>Verify email</title>
</head>

<body>
<jsp:include page="/common/client/header.jsp"/>

<section class="section-content" style="margin: 100px 0;">
    <div class="justify-content-between">
        <h2>${sentEmail}</h2>
    </div>
</section> <!-- section-content.// -->

<jsp:include page="/common/client/footer.jsp"/>
</body>

</html>
