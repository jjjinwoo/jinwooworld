<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
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
		<h1 align = "center">회원정보 수정</h1>
	</div>
	<br/>
	<div class = "container">
				<div class="row">
					<nav class="col-sm-2">
						<ul class="nav nav-pills nav-stacked">
							<li><a href="${contextPath}/member/mypageForm.do">회원 정보 보기</a></li>
							<li class="active"><a href="${contextPath}/member/memberUp.do">회원 정보 수정</a></li>
							<li><a href="${contextPath}/member/memberDelete.do">회원 탈퇴</a></li>
							<li><a href="${contextPath}/member/productBuy.do">주문 내역</a></li>
						</ul>
					</nav>
					<div class="col-sm-offset-1 col-sm-9">
					<form class = "form-horizontal" id = "frm">
						<div class = "form-group">
							<label for ="userID" class="col-sm-2 control-label">아이디</label>
							<div class = "col-sm-4">
								<input type = "text" class = "form-control" id="userID" name = "userID" maxlength = "200" value = "${member.userID }" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="repwd" class="col-sm-2 control-label">비밀번호 입력</label>
							<div class="col-sm-4">
								<input type="password" class="form-control" id="pwd" name="pwd" maxlength="20"> 
								<button type = "button" class = "btn btn-light mypageBtn" onclick = "checkPassword()">비밀번호 확인</button>	
							</div>
						</div>
						
					</form>
				</div>
				<div class = "form-group" id="myPageBtn">
					<button type = "button" class = "btn btn-light mypageBtn" onclick="location.href='${contextPath}/member/main.do'">Home</button>		
					<button type = "button" id = "unregisterBtn" class = "btn btn-light" onclick="location.href='${contextPath}/member/memberUpdate.do'" disabled>
						Update
					</button>
				</div>
			</div>
		</div>
</body>
<script>
function checkPassword() {
    var inputPwd = document.getElementById("pwd").value;
    var userID = document.getElementById("userID").value;
    $.ajax({
        type: "POST",
        url: "${contextPath}/member/checkPassword.do",
        data: {pwd: inputPwd, userID: userID},
        success: function(data) {
            if (data.trim() == "success") {
                alert("비밀번호가 일치합니다.");
                document.getElementById("unregisterBtn").disabled = false;
            } else {
                alert("비밀번호가 일치하지 않습니다.");
                document.getElementById("unregisterBtn").disabled = true;
            }
        }
    });
}


</script>
</html>