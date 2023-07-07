<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
String result = (String)request.getAttribute("result");
if(result != null && result.equals("sessionExpired")) {
%>
<script>
alert("세션이 만료되었습니다. 다시 로그인해주세요.");
</script>
<%
}
%>
<div class = "navbar" >
		<div class="container-fluid" id="navbar1" style="display: flex; justify-content: center; align-items: center;">
			<a href="<%=request.getContextPath()%>/member/main.do"><img src="${pageContext.request.contextPath}/images/logo.webp" alt="로고" width="90px" height="90px"></a>
			<span style="margin-left: 10px; font-size: 60px;"><strong>Chelsea FC</strong></span>
		</div>
		
		<!-- 로그인 -->
    <div class="form-group" style = "position : absolute; top: 0; right: 40px;" >
        <c:choose>
            <c:when test="${empty sessionScope.member}">
                <a href="<%=request.getContextPath()%>/member/loginForm.do" style = "color: black;  text-decoration: none;">&nbsp;&nbsp;Login&nbsp;&nbsp;</a>
                <a href="<%=request.getContextPath()%>/member/memberForm.do" style = "color: black;  text-decoration: none;">&nbsp;&nbsp;Join&nbsp;&nbsp;</a>
            </c:when>
            <c:otherwise>
                <span>${sessionScope.member.name}님 환영합니다.</span>&nbsp;&nbsp;
                <a href="<%=request.getContextPath()%>/member/mypageForm.do" style = "color: black;  text-decoration: none;">마이페이지</a>&nbsp;&nbsp;
            	<a href="<%=request.getContextPath()%>/member/mycartForm.do" style = "color: black;  text-decoration: none;">장바구니</a>&nbsp;&nbsp;
                <a href="<%=request.getContextPath()%>/member/logout.do" style = "color: black;  text-decoration: none;">Logout</a>&nbsp;&nbsp;
            	<c:if test="${sessionScope.member.grade == 7}">
                    <a href="<%=request.getContextPath()%>/member/listMember.do" style = "color: black;  text-decoration: none;">회원목록</a>&nbsp;&nbsp;
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</div>
		

		<!-- 로그인
		<div class="form-group" style = "position : absolute; top: 0; right: 40px;" >
			<a href="<%=request.getContextPath()%>/member/loginForm.do" style = "color: black;  text-decoration: none;">&nbsp;&nbsp;Login&nbsp;&nbsp;</a>
			<a href="<%=request.getContextPath()%>/member/memberForm.do" style = "color: black;  text-decoration: none;">&nbsp;&nbsp;Join&nbsp;&nbsp;</a>
		</div>
		 -->

</div>