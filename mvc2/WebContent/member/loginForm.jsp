<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<c:set var="result" value="${param.result}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- 로그인 여부 체크 -->
<c:choose>
	<c:when test = "${result == 'loginIdEmpty' }">
		<script>
			window.onload = function(){
				alert("\n아이디를 입력하셔야 합니다.");
			}
		</script>
		</c:when>
		<c:when test = "${result == 'loginFailed' }">
		<script>
			window.onload = function(){
				alert("\n아이디를 잘못입력하셨습니다. \n\n 다시 로그인을 해주십시오.");
			}
		</script>
		</c:when>
		<c:when test = "${result == 'PasswordFailed' }">
		<script>
			window.onload = function(){
				alert("\n비밀번호를 잘못입력하셨습니다. \n\n 다시 로그인을 해주십시오.");
			}
		</script>
	</c:when>
</c:choose>
</head>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	<div class="container" id="loginFormBox" style="display: flex; justify-content: center; align-items: center;">
			<form class = "form-horizontal" method = "post" action = "${contextPath}/member/login.do">
				<div class = "form-group">
					<div class ="col-sm-offset-3 col-sm-3">
						<h2 align = "center">Login</h2>
					</div>
				</div>
				<div class = "form-group">
					<label for = "id" class="col-sm-4 control-label">아이디</label>
					<div class = "col-sm-8">
						<input type = "text" class = "form-control" id = "userID" name ="userID" maxlength="20" placeholder="아이디"/>
					</div>
				</div>
				<br/>
				<div class = "form-group">
					<label for = "pwd" class="col-sm-4 control-label">비밀번호</label>
					<div class = "col-sm-8">
						<input type = "password" class = "form-control" id = "pwd" name ="pwd" maxlength="20" placeholder="비밀번호"/>
					</div>
				</div>
				<div class ="form-group">
						<button type = "reset" class = "btn btn-light">Re-enter</button>
						<button type = "submit" class = "btn btn-light" id = "submit">Login</button>
						<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/memberForm.do'">Join</button>
				</div>
				<div class ="form-group">
					<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/findID.do'" >아이디 찾기</button>
					<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/findPass.do'" >비밀번호 찾기</button>
				</div>
			</form>
		</div>	
	
</body>


</html>