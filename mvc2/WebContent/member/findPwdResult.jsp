<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기 결과</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	<h2 align = "center">비밀번호 찾기 결과</h2>
	<div class = "jumbotron" style="display: flex; justify-content: center; align-items: center;">
	    <c:choose>
	        <c:when test="${pwd != null}">
	            <p>비밀번호 : ${pwd}</p>
	        </c:when>
	        <c:otherwise>
	            <p>고객님의 정보가 없습니다.</p>
	        </c:otherwise>
	    </c:choose>
	</div>
	<div class ="form-group" style="display: flex; justify-content: center; align-items: center;">
		<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/main.do'">Home</button>&nbsp;&nbsp;
		<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/memberForm.do'">Join</button>&nbsp;&nbsp;
		<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/findPass.do'" >비밀번호 찾기</button>
	</div>
</body>
</html>