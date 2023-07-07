<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<style>
    #myPageBtn {
        text-align: center;
    }
</style>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	<div id="mainTitle">
		<h1 align = "center">회원 정보</h1>
	</div>
	<br/>
	<div class = "container">
				<div class="row">
					<nav class="col-sm-2">
						<ul class="nav nav-pills nav-stacked">
							<li class="active"><a href="${contextPath}/member/mypageForm.do">회원 정보 보기</a></li>
							<li><a href="${contextPath}/member/memberUp.do">회원 정보 수정</a></li>
							<li><a href="${contextPath}/member/memberDelete.do">회원 탈퇴</a></li>
							<li><a href="${contextPath}/member/productBuy.do">주문 내역</a></li>
						</ul>
					</nav>
					<div class="col-sm-offset-1 col-sm-9">
						<form class = "form-horizontal" id = "frm">
							<div class = "form-group">
								<label for = "userID" class = "col-sm-2 control-label">아이디</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "userID" name = "userID" maxlength = "200" value = "${member.userID }" readonly>
								</div>
							</div>
							<div class = "form-group">
								<label for = "name" class = "col-sm-2 control-label">이  름</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "name" name = "name" maxlength = "200" value = "${member.name }" readonly>
								</div>
							</div>
							<div class = "form-group">
								<label for = "postnum" class = "col-sm-2 control-label">우편번호</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "postnum" name = "postnum" maxlength = "200" value = "${member.postnum }" readonly>
								</div>
							</div>
							<div class = "form-group">
								<label for = "address1" class = "col-sm-2 control-label">주  소</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "address1" name = "address1" maxlength = "200" value = "${member.address1 }" readonly>
								</div>
							</div>
							<div class = "form-group">
								<label for = "address2" class = "col-sm-2 control-label">상세주소</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "address2" name = "address2" maxlength = "200" value = "${member.address2 }" readonly>
								</div>
							</div>
							<div class = "form-group">
								<label for = "phone" class = "col-sm-2 control-label">phone</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "phone" name = "phone" maxlength = "200" value = "${member.phone }" readonly>
								</div>
							</div>
							<br/>
							<br/>
						</form>
					</div>
					<div class = "form-group" id="myPageBtn">
						<button type = "button" class = "btn btn-light mypageBtn" onclick="location.href='${contextPath}/member/main.do'">Home</button>
					</div>
				</div>
			</div>
</body>
</html>