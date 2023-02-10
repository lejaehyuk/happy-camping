<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/happy/fragments/head.jsp" flush="false"/>
<style>
    #notice-content p {
        margin: 0;
    }
</style>
<body>
<jsp:include page="/happy/fragments/header.jsp" flush="false"/>
<div class="container">
    <div class="mt-5">
        <div class="mb-3">
            <span class="h1">
                자유게시판
            </span>
        </div>
        <table class="table table-bordered">
            <tbody>
            <tr>
                <th width="20%" class="p-3 bg-light">제목</th>
                <td width="80%" class="p-3">${board.title}</td>
            </tr>
            <tr>
                <th width="20%" class="p-3 bg-light">작성자</th>
                <td width="80%" class="p-3">${board.mid}</td>
            </tr>
            <tr>
                <th width="20%" class="p-3 bg-light">작성일</th>
                <td width="80%" class="p-3">${board.regDate}</td>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="p-2 m-auto" id="board-content">
                        ${board.content}
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="mt-2 d-flex justify-content-start">
 
            <c:if test="${sessionScope.mid != board.mid}">
              <div class="me-2">
                <a role="button" class="btn btn-secondary" href="/board/list.do">목록</a>
              </div>
            </c:if>
            <c:if test="${sessionScope.mid == board.mid}">
                 <div class="me-2">
                    <a role="button" class="btn btn-secondary" href="/board/list.do">목록</a>
                    <a role="button" class="btn btn-primary" href="/board/update.do?no=${board.bno}">수정</a>
                 </div>
                 <div>
                    <c:set var="no" value="${board.bno}"/>
                    <button role="button" class="btn btn-danger" id="board-delete-btn">삭제</button>
                 </div>
             </c:if>
        </div>
        <!-- 댓글 -->
        <div style="height: 5px"></div>
  <div class="content three_quarter first m-5">
  <h3 class="sectiontitle">댓글(${count})</h3>
  <c:if test="${count==0 }">
   <table class="table">
     <tr>
      <td class="text-center">댓글이 없습니다</td>
     </tr>
   </table>
  </c:if>
  <c:if test="${count>0 }">
    <table class="table">
     <tr>
      <td>
        <c:forEach var="board" items="${list }">
          <table class="table">
            <tr>
              <td class="text-left">
                <c:if test="${board.group_tab>0 }">
                  <img src="image/re_icon.png">
                </c:if>
              ◑<span style="color:black">${board.mid }</span>&nbsp;(${board.dbday })</td>
              <td class="text-right">
                <c:if test="${sessionScope.mid!=null }">
                 <c:if test="${sessionScope.mid==board.mid }">
                  <div class= "d-flex justify-content-end">
                   <c:set var="brno" value="${board.brno }"/>
                   <c:set var="bno" value="${board.bno }"/>
                    <a href="../board/replyDelete.do?brno=${board.brno }&bno=${board.bno}" class="btn btn-outline-dark btn-sm">x</a>
                    </div>
                 </c:if>
                </c:if>
              </td>
            </tr>
            
            <c:if test="${board.content!='관리자가 삭제한 댓글입니다' }">
            <tr>
              <td colspan="2">
               <pre style="white-space: pre-wrap;background-color: white;border: none">${board.content }</pre>
              </td>
            </tr>
            </c:if>
            <c:if test="${rvo.content=='관리자가 삭제한 댓글입니다' }">
            <tr>
              <td colspan="2">
               <pre style="white-space: pre-wrap;background-color: white;border: none;color:gray">${board.content }</pre>
              </td>
            </tr>
            </c:if>
          </table>
        </c:forEach>
      </td>
     </tr>
    </table>
  </c:if>
  <c:if test="${sessionScope.role == '일반회원' }">
    <table class="table">
     <tr>
       <td>
        <form method="post" action="../board/replywrite.do">
         <input type=hidden name="bno" value="${board.bno }">
         <textarea rows="3" cols="120" name="content" style="float: left"></textarea>&nbsp;
         <input type=submit value="댓글쓰기" class="btn btn-sm btn-danger" style="height: 65px">
        </form>
       </td>
     </tr>
    </table>
  </c:if>
  </div>
    </div>
</div>
<jsp:include page="/happy/fragments/footer.jsp" flush="false"/>
<jsp:include page="/happy/fragments/common-script.jsp" flush="false"/>
<script>
    $(function () {
        $('#board-delete-btn').click(function () {
            var isConfirm = confirm('게시글을 삭제하시겠습니까?');
            var no = ${no};
            if (isConfirm) {
                location.href="/boardDelete.do?no=" + no;
            }
        });

</script>
</body>
</html>