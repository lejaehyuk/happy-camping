<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/happy/fragments/head.jsp" flush="false"/>
<style>
    .text-underline-hover {
        text-decoration: none;
    }

    .text-underline-hover:hover {
        text-decoration: underline;
    }
</style>
<body>
<jsp:include page="/happy/fragments/header.jsp" flush="false"/>
<div class="container">
    <div class="mt-1">
        <span class="h1">
            자유게시판
        </span>
        <c:if test="${sessionScope.role == '일반회원'}">
            <a role="button" class="btn btn-sm btn-outline-primary" href="/board/write.do">새글</a>
        </c:if>
        <hr>
    </div>
    <div>
        <table class="table table-bordered">
            <thead>
            <tr class="text-center bg-light">
                <th class="p-3" width="10%" scope="col">번호</th>
                <th class="p-3" width="60%" scope="col">제목</th>
                <th class="p-3" width="10%" scope="col">작성자</th>
                <th class="p-3" width="10%" scope="col">작성일</th>
                <th class="p-3" width="10%" scope="col">조회수</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="board" items="${page.items}">
                <tr class="text-center" >
                    <th class="p-3">${board.bno}</th>
                    <td class="p-3 text-start">
                        <a class="text-dark text-underline-hover" href="/board/detail.do?no=${board.bno}">${board.title}</a>
                    </td>
                    <td class="p-3">${board.mid}</td>
                    <td class="p-3">${board.regDate}</td>
                    <td class="p-3">${board.hit}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div>
        <nav>
            <ul class="pagination justify-content-center">
                <li class="page-item">
                    <a class="page-link <c:if test="${page.first || page.curPage - 10 <= 0}">disabled invisible</c:if>"
                       href="/board/list.do?page=${page.curPage - 10}">&laquo;</a>
                </li>
                <li class="page-item">
                    <a class="page-link <c:if test="${!page.hasPrev}">disabled invisible</c:if>"
                       href="/board/list.do?page=${page.curPage - 1}">◀︎</a>
                </li>
                <c:forEach var="pageNumber" begin="${page.startPage}" end="${page.endPage}">
                    <li class="page-item <c:if test="${pageNumber == page.curPage}">active</c:if>">
                        <a class="page-link"
                           href="/board/list.do?page=${pageNumber}">${pageNumber}</a>
                    </li>
                </c:forEach>
                <li class="page-item">
                    <a class="page-link <c:if test="${!page.hasNext}">disabled invisible</c:if>"
                       href="/board/list.do?page=${page.curPage + 1}">▶︎</a>
                </li>
                <li class="page-item">
                    <a class="page-link <c:if test="${page.last || (page.curPage + 10) > page.totalPage}">disabled invisible</c:if>"
                       href="/board/list.do?page=${page.curPage + 10}">&raquo;</a>
                </li>
            </ul>
        </nav>
    </div>
</div>
<jsp:include page="/happy/fragments/footer.jsp" flush="false"/>
<jsp:include page="/happy/fragments/common-script.jsp" flush="false"/>
</body>
</html>